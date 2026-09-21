package com.aerocore.hook.internal;

public final class NativeHook {
    static {
        try {
            System.loadLibrary("Aerocore");
        } catch (Throwable t) {
            // fallback
        }
    }

    public static native boolean nativeHookMethod(String className, String methodName, String methodSig, long targetFuncPtr);
    public static native boolean nativeUnhookMethod(String className, String methodName, String methodSig);
    public static native void nativeEnableAntiDebug(boolean enable);
    public static native void nativeEnableAntiTamper(boolean enable);
}
