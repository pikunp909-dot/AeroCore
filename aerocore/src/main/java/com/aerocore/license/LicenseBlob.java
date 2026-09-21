package com.aerocore.license;

import java.nio.ByteBuffer;
import java.util.UUID;

public class LicenseBlob {
    public static final String MAGIC = "AERO";
    public static final byte VERSION = 0x01;
    
    public final long issuedAt;
    public final long expiresAt;
    public final UUID licenseId;
    public final byte[] deviceHash;
    public final int flags;
    public final byte[] signature;

    public LicenseBlob(long issuedAt, long expiresAt, UUID licenseId, byte[] deviceHash, int flags, byte[] signature) {
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.licenseId = licenseId;
        this.deviceHash = deviceHash;
        this.flags = flags;
        this.signature = signature;
    }

    public byte[] toBytes() {
        ByteBuffer bb = ByteBuffer.allocate(4 + 1 + 8 + 8 + 16 + 32 + 4 + 8);
        bb.put(MAGIC.getBytes());
        bb.put(VERSION);
        bb.putLong(issuedAt);
        bb.putLong(expiresAt);
        bb.putLong(licenseId.getMostSignificantBits());
        bb.putLong(licenseId.getLeastSignificantBits());
        bb.put(deviceHash);
        bb.putInt(flags);
        bb.putLong(0L); // reserved
        return bb.array();
    }
}
