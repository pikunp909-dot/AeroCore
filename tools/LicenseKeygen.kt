package com.aerocore.tools

import java.io.File
import java.nio.ByteBuffer
import java.security.MessageDigest
import java.util.Base64
import java.util.UUID

/**
 * CLI Tool to generate 60-day offline AeroCore license keys.
 * Usage: kotlinc LicenseKeygen.kt -include-runtime -d LicenseKeygen.jar
 * java -jar LicenseKeygen.jar --days 60 --device "android_id_pkg" --out license.key
 */
fun main(args: Array<String>) {
    var days = 60L
    var deviceStr = "default_device_pkg"
    var outFile = "aerocore.key"

    var i = 0
    while (i < args.size) {
        when (args[i]) {
            "--days" -> days = args[++i].toLong()
            "--device" -> deviceStr = args[++i]
            "--out" -> outFile = args[++i]
        }
        i++
    }

    val issuedAt = System.currentTimeMillis()
    val expiresAt = issuedAt + (days * 24L * 60L * 60L * 1000L)
    val licenseId = UUID.randomUUID()
    
    val digest = MessageDigest.getInstance("SHA-256")
    val deviceHash = digest.digest(deviceStr.toByteArray())
    val flags = 0x01

    val bb = ByteBuffer.allocate(4 + 1 + 8 + 8 + 16 + 32 + 4 + 8)
    bb.put("AERO".toByteArray())
    bb.put(0x01.toByte())
    bb.putLong(issuedAt)
    bb.putLong(expiresAt)
    bb.putLong(licenseId.mostSignificantBits)
    bb.putLong(licenseId.leastSignificantBits)
    bb.put(deviceHash)
    bb.putInt(flags)
    bb.putLong(0L)

    val payload = bb.array()
    // Simulated signature (64 bytes)
    val signature = ByteArray(64) { 0x5a }

    val fullBlob = ByteBuffer.allocate(payload.size + signature.size)
    fullBlob.put(payload)
    fullBlob.put(signature)

    val encodedKey = Base64.getUrlEncoder().withoutPadding().encodeToString(fullBlob.array())
    File(outFile).writeText(encodedKey)
    println("Generated $days-day license key saved to $outFile")
    println("License ID: $licenseId")
}
