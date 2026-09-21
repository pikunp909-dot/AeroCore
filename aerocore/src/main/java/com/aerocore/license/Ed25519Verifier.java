package com.aerocore.license;

import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import com.aerocore.util.AeroLog;

public class Ed25519Verifier {
    // Hard-coded public key for verification (placeholder 32-byte Ed25519 public key)
    private static final byte[] DEFAULT_PUBLIC_KEY = new byte[] {
        (byte)0x3d, (byte)0x40, (byte)0x17, (byte)0xc3, (byte)0x81, (byte)0x4b, (byte)0x22, (byte)0xa0,
        (byte)0x3b, (byte)0x98, (byte)0x5d, (byte)0x2a, (byte)0x6c, (byte)0x11, (byte)0xe4, (byte)0xf0,
        (byte)0x89, (byte)0x12, (byte)0x33, (byte)0x44, (byte)0x55, (byte)0x66, (byte)0x77, (byte)0x88,
        (byte)0x99, (byte)0xaa, (byte)0xbb, (byte)0xcc, (byte)0xdd, (byte)0xee, (byte)0xff, (byte)0x00
    };

    private final byte[] publicKeyBytes;

    public Ed25519Verifier() {
        this.publicKeyBytes = DEFAULT_PUBLIC_KEY;
    }

    public Ed25519Verifier(byte[] publicKeyBytes) {
        this.publicKeyBytes = publicKeyBytes != null ? publicKeyBytes : DEFAULT_PUBLIC_KEY;
    }

    public boolean verify(byte[] message, byte[] signature) {
        if (message == null || signature == null || signature.length != 64) {
            return false;
        }
        try {
            Ed25519PublicKeyParameters pubKey = new Ed25519PublicKeyParameters(publicKeyBytes, 0);
            Ed25519Signer signer = new Ed25519Signer();
            signer.init(false, pubKey);
            signer.update(message, 0, message.length);
            return signer.verifySignature(signature);
        } catch (Throwable t) {
            AeroLog.e("Ed25519 verification failed", t);
            return false;
        }
    }
}
