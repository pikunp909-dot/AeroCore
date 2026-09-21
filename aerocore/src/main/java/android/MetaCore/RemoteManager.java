package android.MetaCore;

import com.aerocore.AeroCore;
import com.aerocore.util.AeroLog;

@Deprecated
public final class RemoteManager {
    public static volatile boolean sEnableDaemonService = true;
    public static volatile boolean sHideRoot = true;
    public static volatile boolean sHideXposed = true;

    private static final RemoteManager INSTANCE = new RemoteManager();

    public static RemoteManager getInstance() {
        AeroLog.w("[SHIM] android.MetaCore.RemoteManager.getInstance called");
        return INSTANCE;
    }

    public void activateSdk(String token) {
        AeroLog.w("[SHIM] RemoteManager.activateSdk called");
        if (AeroCore.getInstance().isReady()) {
            AeroCore.getInstance().remote().activateSdk(token, null);
        }
    }

    public boolean getActivatedSdk() {
        return AeroCore.getInstance().isReady() && AeroCore.getInstance().license().validate();
    }

    public String getServerMessage() {
        return "AeroCore Shim Active";
    }

    public boolean getNetwork() {
        return true;
    }
}
