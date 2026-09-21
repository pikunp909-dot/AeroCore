package com.aerocore.hook;

import com.aerocore.hook.internal.NativeHook;
import com.aerocore.util.AeroLog;

public class HookBridge {

    public boolean hookMethod(String className, String methodName, String methodSig, long targetFuncPtr) {
        try {
            return NativeHook.nativeHookMethod(className, methodName, methodSig, targetFuncPtr);
        } catch (Throwable t) {
            AeroLog.e("Failed to hook method " + className + "." + methodName, t);
            return false;
        }
    }

    public boolean unhookMethod(String className, String methodName, String methodSig) {
        try {
            return NativeHook.nativeUnhookMethod(className, methodName, methodSig);
        } catch (Throwable t) {
            AeroLog.e("Failed to unhook method " + className + "." + methodName, t);
            return false;
        }
    }

    public void enableAntiDebug(boolean enable) {
        try {
            NativeHook.nativeEnableAntiDebug(enable);
        } catch (Throwable t) {
            AeroLog.e("Failed to set anti-debug", t);
        }
    }

    public void enableAntiTamper(boolean enable) {
        try {
            NativeHook.nativeEnableAntiTamper(enable);
        } catch (Throwable t) {
            AeroLog.e("Failed to set anti-tamper", t);
        }
    }

    public void enableIntegrityCheck(boolean enable) {
        AeroLog.i("Integrity check set to: " + enable);
    }
}
