package com.aerocore.license;

import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import com.aerocore.util.AeroLog;

public class Ed25519Verifier {
    // Production Ed25519 public key (generated offline)
    private static final byte[] PRODUCTION_PUBLIC_KEY = new byte[] {
        (byte)0x9b, (byte)0x4c, (byte)0x4b, (byte)0x2f, (byte)0x2c, (byte)0xfa, (byte)0xfe, (byte)0x17,
        (byte)0xc4, (byte)0xce, (byte)0x86, (byte)0x85, (byte)0xd1, (byte)0x41, (byte)0x57, (byte)0x6a,
        (byte)0x37, (byte)0x01, (byte)0x0b, (byte)0x0f, (byte)0xfa, (byte)0xd3, (byte)0xd4, (byte)0x00,
        (byte)0xa0, (byte)0x0e, (byte)0x66, (byte)0xc2, (byte)0x47, (byte)0x39, (byte)0xf6, (byte)0x19
    };

    private final byte[] publicKeyBytes;

    public Ed25519Verifier() {
        this.publicKeyBytes = PRODUCTION_PUBLIC_KEY;
    }

    public Ed25519Verifier(byte[] customPublicKeyBytes) {
        this.publicKeyBytes = customPublicKeyBytes != null 
            ? customPublicKeyBytes 
            : PRODUCTION_PUBLIC_KEY;
    }

    public boolean verify(byte[] message, byte[] signature) {
        if (message == null || signature == null || signature.length != 64) {
            return false;
        }
        try {
            Ed25519PublicKeyParameters pubKey = 
                new Ed25519PublicKeyParameters(publicKeyBytes, 0);
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
