package com.aerocore.license;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;
import com.aerocore.util.AeroLog;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LicenseManagerImpl implements LicenseManager {
    private static final String PREF_NAME = "aerocore_license_secure";
    private static final String KEY_LICENSE = "license_key";
    private static final String KEY_LAST_SEEN = "last_seen_time";

    private final Context mContext;
    private final Ed25519Verifier mVerifier;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private SharedPreferences mPrefs;
    private String mCurrentKey;
    private LicenseBlob mParsedBlob;

    public LicenseManagerImpl(Context context, String initialKey) {
        this.mContext = context;
        this.mVerifier = new Ed25519Verifier();
        initPrefs();
        if (initialKey != null && !initialKey.isEmpty()) {
            setLicenseKey(initialKey);
        } else {
            setLicenseKey(mPrefs.getString(KEY_LICENSE, ""));
        }
    }

    private void initPrefs() {
        try {
            MasterKey masterKey = new MasterKey.Builder(mContext)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();
            mPrefs = EncryptedSharedPreferences.create(
                    mContext,
                    PREF_NAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Throwable t) {
            AeroLog.w("EncryptedSharedPreferences failed, falling back to standard prefs: " + t.getMessage());
            mPrefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    @Override
    public synchronized void setLicenseKey(String key) {
        this.mCurrentKey = key;
        mPrefs.edit().putString(KEY_LICENSE, key).apply();
        parseAndVerifyKey();
    }

    private void parseAndVerifyKey() {
        if (mCurrentKey == null || mCurrentKey.isEmpty()) {
            mParsedBlob = null;
            return;
        }
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(mCurrentKey);
            if (decoded.length < 136) { // 4+1+8+8+16+32+4+8 + 64 sig
                AeroLog.e("Invalid license blob length");
                mParsedBlob = null;
                return;
            }

            ByteBuffer bb = ByteBuffer.wrap(decoded);
            byte[] magic = new byte[4];
            bb.get(magic);
            if (!LicenseBlob.MAGIC.equals(new String(magic))) {
                AeroLog.e("Invalid license magic");
                mParsedBlob = null;
                return;
            }
            byte ver = bb.get();
            long issuedAt = bb.getLong();
            long expiresAt = bb.getLong();
            long mostSig = bb.getLong();
            long leastSig = bb.getLong();
            UUID licenseId = new UUID(mostSig, leastSig);
            byte[] deviceHash = new byte[32];
            bb.get(deviceHash);
            int flags = bb.getInt();
            long reserved = bb.getLong();

            byte[] signature = new byte[64];
            bb.get(signature);

            // Verify signature over message part (everything except signature)
            int msgLen = decoded.length - 64;
            byte[] msg = new byte[msgLen];
            System.arraycopy(decoded, 0, msg, 0, msgLen);

            if (!mVerifier.verify(msg, signature)) {
                AeroLog.e("License Ed25519 signature verification failed");
                mParsedBlob = null;
                return;
            }

            mParsedBlob = new LicenseBlob(issuedAt, expiresAt, licenseId, deviceHash, flags, signature);
        } catch (Throwable t) {
            AeroLog.e("Failed to parse license blob", t);
            mParsedBlob = null;
        }
    }

    @Override
    public synchronized boolean validate() {
        if (mParsedBlob == null) return false;

        long currentTime = System.currentTimeMillis();
        long lastSeen = mPrefs.getLong(KEY_LAST_SEEN, 0);

        // Clock rollback defense: if system time < lastSeen - 5 minutes
        if (currentTime < lastSeen - 300000L) {
            AeroLog.e("Clock rollback detected!");
            return false;
        }
        mPrefs.edit().putLong(KEY_LAST_SEEN, Math.max(currentTime, lastSeen)).apply();

        // Expiry check (60 days max or explicit expiresAt)
        if (currentTime > mParsedBlob.expiresAt) {
            AeroLog.e("License expired");
            return false;
        }

        // Device hash verification
        if (!verifyDeviceHash(mParsedBlob.deviceHash)) {
            AeroLog.e("Device hash mismatch");
            return false;
        }

        return true;
    }

    private boolean verifyDeviceHash(byte[] expectedHash) {
        try {
            String androidId = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            String pkg = mContext.getPackageName();
            String raw = androidId + "_" + pkg;
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] computed = digest.digest(raw.getBytes());
            if (computed.length != expectedHash.length) return false;
            for (int i = 0; i < computed.length; i++) {
                if (computed[i] != expectedHash[i]) return false;
            }
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    @Override
    public boolean validateAsync(Callback callback) {
        mExecutor.execute(() -> {
            boolean res = validate();
            if (callback != null) {
                callback.onResult(res);
            }
        });
        return true;
    }

    @Override
    public synchronized long daysRemaining() {
        if (mParsedBlob == null) return 0;
        long diff = mParsedBlob.expiresAt - System.currentTimeMillis();
        if (diff <= 0) return 0;
        return diff / (1000L * 60L * 60L * 24L);
    }

    @Override
    public synchronized boolean isExpired() {
        if (mParsedBlob == null) return true;
        return System.currentTimeMillis() > mParsedBlob.expiresAt || !validate();
    }

    @Override
    public synchronized String licenseId() {
        if (mParsedBlob == null) return "";
        return mParsedBlob.licenseId.toString();
    }

    @Override
    public synchronized void clear() {
        mCurrentKey = null;
        mParsedBlob = null;
        mPrefs.edit().clear().apply();
    }
}
