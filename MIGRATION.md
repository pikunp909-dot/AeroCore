# Migration Guide: UJJWAL/MetaCore to AeroCore

If you were previously using the expired UJJWAL / MetaCore SDK, migrating to AeroCore is seamless due to built-in compatibility shims.

## 1. Package Namespace Compatibility Shim
AeroCore includes a drop-in shim for `android.MetaCore.*`:
- `android.MetaCore.RemoteManager` -> automatically redirects to `AeroCore.remote()`
- `android.MetaCore.Service.RemoteManagerService` -> automatically redirects to `AeroRemoteService`

## 2. Initialization Changes
Replace your old initialization with:
```java
AeroConfig config = new AeroConfig.Builder()
    .licenseKey("YOUR_60_DAY_OFFLINE_LICENSE_KEY")
    .build();

AeroCore.getInstance().init(context, config);
```

## 3. Disabling the Shim
If you wish to disable the legacy MetaCore shim entirely, compile with `-DdisableShim=true` or remove the shim classes from your ProGuard configuration.
