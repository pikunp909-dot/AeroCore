package com.aerocore.license.internal;

public final class NativeCrypto {
    static {
        try {
            System.loadLibrary("Aerocore");
        } catch (Throwable t) {
            // fallback
        }
    }

    public static native boolean verifyEd25519(byte[] pubKey, byte[] msg, byte[] sig);
}
