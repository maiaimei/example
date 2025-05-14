package org.example.controller;

import org.example.model.ApiRequest;
import org.example.model.SuccessResponse;
import org.example.model.request.LoginRequest;
import org.example.model.response.JWTResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/security")
public class SecurityController {

  @PostMapping("/login")
  public ResponseEntity<SuccessResponse<JWTResponse>> login(@RequestBody ApiRequest<LoginRequest> request) {
    final SuccessResponse<JWTResponse> successResponse = new SuccessResponse<>();
    successResponse.setData(new JWTResponse());
    return ResponseEntity.status(HttpStatus.OK).body(successResponse);
  }
}
