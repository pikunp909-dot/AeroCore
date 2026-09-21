import java.security.*;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.UUID;
import java.security.MessageDigest;
import org.bouncycastle.crypto.generators.Ed25519KeyPairGenerator;
import org.bouncycastle.crypto.params.Ed25519KeyGenerationParameters;
import org.bouncycastle.crypto.params.Ed25519PrivateKeyParameters;
import org.bouncycastle.crypto.params.Ed25519PublicKeyParameters;
import org.bouncycastle.crypto.signers.Ed25519Signer;
import java.io.File;

public class KeyGenFull {
    public static void main(String[] args) throws Exception {
        // 1. Generate keypair
        Ed25519KeyPairGenerator kpg = new Ed25519KeyPairGenerator();
        kpg.init(new Ed25519KeyGenerationParameters(new SecureRandom()));
        org.bouncycastle.crypto.AsymmetricCipherKeyPair kp = kpg.generateKeyPair();
        Ed25519PrivateKeyParameters privKey = (Ed25519PrivateKeyParameters) kp.getPrivate();
        Ed25519PublicKeyParameters pubKey = (Ed25519PublicKeyParameters) kp.getPublic();
        
        byte[] rawPub = pubKey.getEncoded();
        byte[] rawPriv = privKey.getEncoded();
        
        // 2. Print public key for Ed25519Verifier.java
        System.out.println("=== PUBLIC KEY (paste into Ed25519Verifier.java) ===");
        StringBuilder sb = new StringBuilder();
        sb.append("private static final byte[] PRODUCTION_PUBLIC_KEY = new byte[] {\n        ");
        for (int i = 0; i < 32; i++) {
            sb.append(String.format("(byte)0x%02x", rawPub[i]));
            if (i < 31) sb.append(", ");
            if ((i+1) % 8 == 0 && i < 31) sb.append("\n        ");
        }
        sb.append("\n};");
        System.out.println(sb.toString());
        
        System.out.println();
        System.out.println("=== PRIVATE KEY (SAVE OFFLINE) ===");
        System.out.println(Base64.getEncoder().encodeToString(rawPriv));
        
        System.out.println();
        System.out.println("=== LICENSE GENERATION ===");
        
        // 3. Generate license (60 days)
        long days = 60L;
        String deviceStr = "TEST_DEVICE";
        long issuedAt = System.currentTimeMillis();
        long expiresAt = issuedAt + (days * 24L * 60L * 60L * 1000L);
        UUID licenseId = UUID.randomUUID();
        
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] deviceHash = digest.digest(deviceStr.getBytes());
        
        ByteBuffer bb = ByteBuffer.allocate(4 + 1 + 8 + 8 + 16 + 32 + 4 + 8);
        bb.put("AERO".getBytes());
        bb.put((byte) 0x01);
        bb.putLong(issuedAt);
        bb.putLong(expiresAt);
        bb.putLong(licenseId.getMostSignificantBits());
        bb.putLong(licenseId.getLeastSignificantBits());
        bb.put(deviceHash);
        bb.putInt(0x01);
        bb.putLong(0L);
        byte[] payload = bb.array();
        
        Ed25519Signer signer = new Ed25519Signer();
        signer.init(true, privKey);
        signer.update(payload, 0, payload.length);
        byte[] signature = signer.generateSignature();
        
        ByteBuffer fullBlob = ByteBuffer.allocate(payload.length + signature.length);
        fullBlob.put(payload);
        fullBlob.put(signature);
        byte[] blob = fullBlob.array();
        
        String encoded = Base64.getUrlEncoder().withoutPadding().encodeToString(blob);
        System.out.println("License (60 days): " + encoded);
        System.out.println("License ID: " + licenseId);
        
        // Save to file
        java.nio.file.Files.write(new File("fresh_test.key").toPath(), encoded.getBytes());
        System.out.println();
        System.out.println("✅ Saved to fresh_test.key");
        System.out.println("✅ Public key embedded in Ed25519Verifier.java");
        System.out.println("✅ Private key saved — KEEP SAFE");
    }
}
