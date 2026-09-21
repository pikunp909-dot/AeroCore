import java.security.*;

public class KeyGen {
    public static void main(String[] args) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("Ed25519");
        KeyPair kp = kpg.generateKeyPair();
        
        byte[] pub = kp.getPublic().getEncoded();
        byte[] priv = kp.getPrivate().getEncoded();
        
        // Ed25519 public key is the last 32 bytes
        byte[] rawPub = new byte[32];
        System.arraycopy(pub, pub.length - 32, rawPub, 0, 32);
        
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
        System.out.println("=== PRIVATE KEY (SAVE OFFLINE — for signing) ===");
        System.out.println(java.util.Base64.getEncoder().encodeToString(priv));
    }
}
