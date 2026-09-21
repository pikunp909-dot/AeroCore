# Integration Guide: AeroCore SDK

## Gradle Setup
Add `AeroCore-release.aar` to your app's `libs` folder and add dependency:
```kotlin
implementation(files("libs/AeroCore-release.aar"))
implementation("org.bouncycastle:bcprov-jdk18on:1.76")
implementation("androidx.security:security-crypto:1.1.0-alpha06")
```

## Initialization in Application class
```java
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AeroConfig config = new AeroConfig.Builder()
            .licenseKey("YOUR_KEY")
            .enableAntiDebug(true)
            .build();
        AeroCore.getInstance().init(this, config);
    }
}
```
