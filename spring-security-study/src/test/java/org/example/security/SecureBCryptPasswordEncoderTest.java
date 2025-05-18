package org.example.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.BCryptVersion;

@Slf4j
public class SecureBCryptPasswordEncoderTest {

  private SecureBCryptPasswordEncoder encoder;

  @BeforeEach
  void setUp() {
    encoder = new SecureBCryptPasswordEncoder();
  }

  @Test
  void testBasicEncode() {
    String rawPassword = "TestPassword123!";
    String encodedPassword = encoder.encode(rawPassword);
    log.info("encodedPassword: {}", encodedPassword);
    assertNotNull(encodedPassword);
    assertTrue(encoder.matches(rawPassword, encodedPassword));
  }

  @Test
  void testBasicMatches() {
    String rawPassword = "TestPassword123!";
    String encodedPassword = "JDJiJDE0JHFDZVlIWDBaRWY2LjR3Q09CZWt2R09oTGdlZ1NOMTQ2TzM0T0dDMVphYWhvVDJ3dXZZVTcu";
    assertTrue(encoder.matches(rawPassword, encodedPassword));
  }

  @Test
  public void testGenerateSaltWithCTRDRBGSecureRandom() {
    final CTRDRBGSecureRandomV3 random = new CTRDRBGSecureRandomV3();
    for (int i = 0; i < 10000; i++) {
      System.out.println(BCrypt.gensalt(BCryptVersion.$2B.getVersion(), 14, random));
    }
  }
}
