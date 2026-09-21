package android.MetaCore.Service;

import com.aerocore.remote.AeroRemoteService;
import com.aerocore.util.AeroLog;

@Deprecated
public class RemoteManagerService extends AeroRemoteService {
    public RemoteManagerService() {
        super();
        AeroLog.w("[SHIM] android.MetaCore.Service.RemoteManagerService instantiated");
    }
}
