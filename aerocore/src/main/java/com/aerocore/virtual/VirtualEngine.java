package com.aerocore.virtual;

import android.content.Context;
import android.content.Intent;

import com.aerocore.util.AeroLog;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.fake.frameworks.BActivityManager;
import top.niunaijun.blackbox.fake.frameworks.BPackageManager;
import top.niunaijun.blackbox.fake.frameworks.BUserManager;
import top.niunaijun.blackbox.entity.pm.InstallOption;
import top.niunaijun.blackbox.entity.pm.InstallResult;
import top.niunaijun.blackbox.entity.pm.InstalledPackage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VirtualEngine {
    private final Context mContext;

    public VirtualEngine(Context context) {
        this.mContext = context;
    }

    /**
     * Install an APK into the virtual space.
     * Uses installPackageAsUser with InstallOption.installByStorage().
     */
    public boolean installPackage(File apkFile) {
        if (apkFile == null || !apkFile.exists()) {
            AeroLog.e("APK file does not exist");
            return false;
        }
        try {
            int userId = getUserId();
            InstallResult result = BPackageManager.get()
                    .installPackageAsUser(
                            apkFile.getAbsolutePath(),
                            InstallOption.installByStorage(),
                            userId);
            AeroLog.i("Installed package: " + (result != null && result.success));
            return result != null && result.success;
        } catch (Throwable t) {
            AeroLog.e("Failed to install package via BlackBox", t);
            return false;
        }
    }

    /**
     * Uninstall a virtual package.
     * API: uninstallPackage(String) - no userId needed.
     */
    public boolean uninstallPackage(String packageName) {
        if (packageName == null) return false;
        try {
            BPackageManager.get().uninstallPackage(packageName);
            AeroLog.i("Uninstalled package: " + packageName);
            return true;
        } catch (Throwable t) {
            AeroLog.e("Failed to uninstall package via BlackBox", t);
            return false;
        }
    }

    /**
     * Launch a virtual app.
     * API: no launchApp() on BActivityManager. Use startActivity with
     * the package's launch Intent built via BPackageManager.getPackageInfo().
     */
    public boolean launchApp(String packageName, int userId) {
        if (packageName == null) return false;
        try {
            // Get launch intent for the package in virtual space
            Intent launchIntent = BlackBoxCore.getContext()
                    .getPackageManager()
                    .getLaunchIntentForPackage(packageName);
            if (launchIntent == null) {
                AeroLog.e("No launch intent for: " + packageName);
                return false;
            }
            BActivityManager.get().startActivity(launchIntent, userId);
            AeroLog.i("Launched app: " + packageName);
            return true;
        } catch (Throwable t) {
            AeroLog.e("Failed to launch app via BlackBox", t);
            return false;
        }
    }

    /**
     * List installed virtual packages.
     * API: getInstalledPackagesAsUser(int userId) returns List<InstalledPackage>.
     */
    public List<String> listInstalled() {
        List<String> list = new ArrayList<>();
        try {
            List<InstalledPackage> packages = BPackageManager.get()
                    .getInstalledPackagesAsUser(getUserId());
            if (packages != null) {
                for (InstalledPackage pkg : packages) {
                    if (pkg != null && pkg.packageName != null) {
                        list.add(pkg.packageName);
                    }
                }
            }
        } catch (Throwable t) {
            AeroLog.e("Failed to list installed packages", t);
        }
        return list;
    }

    /**
     * Get package info from virtual space.
     */
    public Object getApp(String packageName, int userId) {
        if (packageName == null) return null;
        try {
            return BPackageManager.get().getPackageInfo(packageName, 0, userId);
        } catch (Throwable t) {
            AeroLog.e("Failed to get app info", t);
            return null;
        }
    }

    /**
     * Get current virtual user id.
     * API: BlackBoxCore.getUserId() is static.
     */
    public int getUserId() {
        try {
            return BlackBoxCore.getUserId();
        } catch (Throwable t) {
            return 0;
        }
    }

    /**
     * Set current virtual user id.
     * Note: BUserManager doesn't have setUserId(). User id is per-process.
     * Use BlackBoxCore.getUserId() to read; to switch, call launchApp with userId.
     */
    public void setUserId(int userId) {
        // No direct setter in this BlackBox version.
        // User id is passed to per-call APIs (installPackageAsUser, startActivity, etc.)
        AeroLog.w("setUserId(" + userId + ") - no-op in this BlackBox version; " +
                "pass userId to individual API calls instead");
    }
}
