# AeroCore ProGuard Rules
-keep public class com.aerocore.** {
    public *;
}

-keep public class android.MetaCore.** {
    public *;
}

-keepclassmembers class * {
    @androidx.annotation.Keep <fields>;
    @androidx.annotation.Keep <methods>;
}

-dontwarn org.bouncycastle.**
-keep class org.bouncycastle.** { *; }

-keepclassmembers class * extends android.app.Service {
    <init>();
}
