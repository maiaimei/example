package org.example.security;

import java.security.SecureRandom;
import org.bouncycastle.crypto.engines.AESLightEngine;
import org.bouncycastle.crypto.prng.BasicEntropySourceProvider;
import org.bouncycastle.crypto.prng.EntropySource;
import org.bouncycastle.crypto.prng.EntropySourceProvider;
import org.bouncycastle.crypto.prng.drbg.CTRSP800DRBG;

public class CTRDRBGExample {

  public static void main(String[] args) {
    // Create a SecureRandom instance for entropy
    SecureRandom secureRandom = new SecureRandom();

    // Create an entropy source provider
    EntropySourceProvider entropySourceProvider = new BasicEntropySourceProvider(secureRandom, true);

    // Create an entropy source
    EntropySource entropySource = entropySourceProvider.get(256);

    // Initialize CTR_DRBG with AES-256
    CTRSP800DRBG ctrDrbg = new CTRSP800DRBG(
        new AESLightEngine(), // AES engine
        256,                 // Security strength
        256,                 // Block size
        entropySource,       // Entropy source
        null,                // Personalization string
        null                 // Additional input
    );

    // Generate random bytes
    byte[] randomBytes = new byte[32];
    ctrDrbg.generate(randomBytes, null, false);

    // Print the generated random bytes
    System.out.println("Generated random bytes: " + bytesToHex(randomBytes));
  }

  private static String bytesToHex(byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }
}