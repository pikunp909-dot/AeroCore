package com.aerocore.license;

import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import com.aerocore.util.AeroLog;

public class Ed25519Verifier {
    // Production Ed25519 public key (matches private key in KeyGenFull)
    private static final byte[] PRODUCTION_PUBLIC_KEY = new byte[] {
        (byte)0xb2, (byte)0x05, (byte)0xb9, (byte)0xa7, (byte)0x84, (byte)0xae, (byte)0xa2, (byte)0x1c,
        (byte)0xe9, (byte)0x05, (byte)0xd9, (byte)0x7e, (byte)0x19, (byte)0x77, (byte)0xad, (byte)0x15,
        (byte)0x9f, (byte)0x29, (byte)0x74, (byte)0xbb, (byte)0x8d, (byte)0x70, (byte)0x94, (byte)0x40,
        (byte)0x99, (byte)0xc6, (byte)0x69, (byte)0x52, (byte)0x34, (byte)0x82, (byte)0x3e, (byte)0x1d
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
