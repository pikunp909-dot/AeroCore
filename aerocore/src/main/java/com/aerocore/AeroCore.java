package com.aerocore;

import android.content.Context;
import com.aerocore.hook.HookBridge;
import com.aerocore.license.LicenseManager;
import com.aerocore.license.LicenseManagerImpl;
import com.aerocore.remote.RemoteManager;
import com.aerocore.security.SecurityManager;
import com.aerocore.util.AeroException;
import com.aerocore.util.AeroLog;
import com.aerocore.virtual.VirtualEngine;

public final class AeroCore {
    private static volatile AeroCore sInstance;
    private boolean mInitialized = false;
    private Context mContext;
    private AeroConfig mConfig;

    private LicenseManager mLicenseManager;
    private RemoteManager mRemoteManager;
    private VirtualEngine mVirtualEngine;
    private HookBridge mHookBridge;
    private SecurityManager mSecurityManager;

    private AeroCore() {}

    public static AeroCore getInstance() {
        if (sInstance == null) {
            synchronized (AeroCore.class) {
                if (sInstance == null) {
                    sInstance = new AeroCore();
                }
            }
        }
        return sInstance;
    }

    public synchronized void init(Context context, AeroConfig config) {
        if (mInitialized) {
            return;
        }
        try {
            mContext = context.getApplicationContext();
            mConfig = config;
            AeroLog.setVerbose(config.isEnableVerboseLogging());
            AeroLog.i("Initializing AeroCore SDK v" + sdkVersion());

            mLicenseManager = new LicenseManagerImpl(mContext, config.getLicenseKey());
            mRemoteManager = new RemoteManager(mContext, config.getRemoteUrl());
            mVirtualEngine = new VirtualEngine(mContext);
            mHookBridge = new HookBridge();
            mSecurityManager = new SecurityManager(mContext);

            if (config.isEnableAntiDebug()) {
                mHookBridge.enableAntiDebug(true);
            }
            if (config.isEnableAntiTamper()) {
                mHookBridge.enableAntiTamper(true);
            }
            if (config.isHideRootByDefault()) {
                mSecurityManager.hideRoot(true);
            }
            if (config.isHideXposedByDefault()) {
                mSecurityManager.hideXposed(true);
            }

            mInitialized = true;
            AeroLog.i("AeroCore initialized successfully.");
        } catch (Throwable t) {
            AeroLog.e("Failed to initialize AeroCore", t);
        }
    }

    public synchronized void shutdown() {
        if (!mInitialized) return;
        try {
            if (mRemoteManager != null) {
                mRemoteManager.shutdown();
            }
            mInitialized = false;
            AeroLog.i("AeroCore shutdown.");
        } catch (Throwable t) {
            AeroLog.e("Error during shutdown", t);
        }
    }

    public boolean isReady() {
        return mInitialized;
    }

    public LicenseManager license() {
        return mLicenseManager;
    }

    public RemoteManager remote() {
        return mRemoteManager;
    }

    public VirtualEngine virtual() {
        return mVirtualEngine;
    }

    public HookBridge hooks() {
        return mHookBridge;
    }

    public SecurityManager security() {
        return mSecurityManager;
    }

    public String sdkVersion() {
        return "1.0.0";
    }

    public int sdkVersionCode() {
        return 10000;
    }
}
