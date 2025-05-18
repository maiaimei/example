package org.example.security;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
public class BCryptPasswordEncoderWrapperTest {

  private BCryptPasswordEncoderWrapperV3 encoder;

  @BeforeEach
  void setUp() {
    encoder = new BCryptPasswordEncoderWrapperV3();
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
}
