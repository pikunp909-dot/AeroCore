package com.aerocore.security;

import android.content.Context;
import android.os.Build;
import com.aerocore.util.AeroLog;

import java.io.File;

public class SecurityManager {
    private final Context mContext;
    private boolean mHideRoot = true;
    private boolean mHideXposed = true;

    public SecurityManager(Context context) {
        this.mContext = context;
    }

    public boolean isRooted() {
        boolean rooted = checkSuBinary() || checkBuildTags();
        return rooted && !mHideRoot;
    }

    public boolean isXposedDetected() {
        boolean xposed = checkXposedClasses();
        return xposed && !mHideXposed;
    }

    public boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.MODEL.contains("google_sdk")
                || Build.HARDWARE.contains("goldfish")
                || Build.PRODUCT.contains("sdk");
    }

    public boolean isDebuggerAttached() {
        return android.os.Debug.isDebuggerConnected();
    }

    public void hideRoot(boolean hide) {
        this.mHideRoot = hide;
        AeroLog.i("Hide root set to: " + hide);
    }

    public void hideXposed(boolean hide) {
        this.mHideXposed = hide;
        AeroLog.i("Hide Xposed set to: " + hide);
    }

    private boolean checkSuBinary() {
        String[] paths = {
            "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su",
            "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su",
            "/system/sd/xbin/su", "/system/bin/failsafe/su", "/data/local/su"
        };
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    private boolean checkBuildTags() {
        String buildTags = Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    private boolean checkXposedClasses() {
        try {
            Class.forName("de.robv.android.xposed.XposedBridge");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }
}
