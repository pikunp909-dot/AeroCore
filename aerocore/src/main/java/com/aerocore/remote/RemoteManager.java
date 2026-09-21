package com.aerocore.remote;

import android.content.Context;
import android.content.SharedPreferences;
import com.aerocore.remote.config.RemoteConfig;
import com.aerocore.util.AeroLog;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RemoteManager {
    private static final String PREF_REMOTE = "aerocore_remote_prefs";
    private final Context mContext;
    private final String mRemoteUrl;
    private final SharedPreferences mPrefs;
    private final ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    private final RemoteConfig mConfig = new RemoteConfig();

    public interface Callback {
        void onResult(boolean success, String message);
    }

    public RemoteManager(Context context, String remoteUrl) {
        this.mContext = context;
        this.mRemoteUrl = remoteUrl;
        this.mPrefs = context.getSharedPreferences(PREF_REMOTE, Context.MODE_PRIVATE);
        
        mConfig.setDaemonServiceEnabled(mPrefs.getBoolean("daemon", true));
        mConfig.setHideRootEnabled(mPrefs.getBoolean("hide_root", true));
        mConfig.setHideXposedEnabled(mPrefs.getBoolean("hide_xposed", true));
    }

    public void activateSdk(String token, Callback callback) {
        mExecutor.execute(() -> {
            try {
                AeroLog.i("Activating SDK with token: " + token);
                // Simulate network activation call
                Thread.sleep(500);
                if (callback != null) {
                    callback.onResult(true, "Activated successfully");
                }
            } catch (Throwable t) {
                AeroLog.e("Activation failed", t);
                if (callback != null) {
                    callback.onResult(false, t.getMessage());
                }
            }
        });
    }

    public boolean isDaemonServiceEnabled() {
        return mConfig.isDaemonServiceEnabled();
    }

    public void setDaemonServiceEnabled(boolean enabled) {
        mConfig.setDaemonServiceEnabled(enabled);
        mPrefs.edit().putBoolean("daemon", enabled).apply();
    }

    public boolean isHideRootEnabled() {
        return mConfig.isHideRootEnabled();
    }

    public void setHideRootEnabled(boolean enabled) {
        mConfig.setHideRootEnabled(enabled);
        mPrefs.edit().putBoolean("hide_root", enabled).apply();
    }

    public boolean isHideXposedEnabled() {
        return mConfig.isHideXposedEnabled();
    }

    public void setHideXposedEnabled(boolean enabled) {
        mConfig.setHideXposedEnabled(enabled);
        mPrefs.edit().putBoolean("hide_xposed", enabled).apply();
    }

    public void fetchConfig(Callback callback) {
        mExecutor.execute(() -> {
            try {
                if (mRemoteUrl == null || mRemoteUrl.isEmpty()) {
                    if (callback != null) callback.onResult(true, "No remote URL configured");
                    return;
                }
                // Simulate network fetch
                Thread.sleep(300);
                if (callback != null) callback.onResult(true, "Config fetched");
            } catch (Throwable t) {
                if (callback != null) callback.onResult(false, t.getMessage());
            }
        });
    }

    public void shutdown() {
        mExecutor.shutdown();
    }
}
