package org.example.controller;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.example.constants.ResponseCode;
import org.example.exception.BusinessException;
import org.example.model.request.ApiRequest;
import org.example.model.request.dto.UserDTO;
import org.example.utils.IdGenerator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

  private static final List<UserDTO> users = new ArrayList<>();

  @GetMapping("/{id}")
  public UserDTO get(BigDecimal id){
    return users.stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
  }

  @PostMapping
  public UserDTO create(@RequestBody @Valid ApiRequest<UserDTO> apiRequest){
    final UserDTO userDTO = apiRequest.getData();
    userDTO.setId(IdGenerator.nextId());
    users.add(userDTO);
    return userDTO;
  }

  @PutMapping
  public UserDTO update(@RequestBody ApiRequest<UserDTO> apiRequest){
    final UserDTO userDTO = apiRequest.getData();
    final UserDTO existingUserDTO = users.stream().filter(user -> user.getId().equals(userDTO.getId())).findFirst().orElse(null);
    if(Objects.isNull(existingUserDTO)){
      throw new BusinessException(ResponseCode.BUSINESS_ERROR, "Incorrect request");
    }
    existingUserDTO.setName(userDTO.getName());
    existingUserDTO.setPhone(userDTO.getPhone());
    return existingUserDTO;
  }

  @PatchMapping
  public UserDTO partialUpdate(@RequestBody ApiRequest<UserDTO> apiRequest){
    final UserDTO userDTO = apiRequest.getData();
    final UserDTO existingUserDTO = users.stream().filter(user -> user.getId().equals(userDTO.getId())).findFirst().orElse(null);
    if(Objects.isNull(existingUserDTO)){
      throw new BusinessException(ResponseCode.BUSINESS_ERROR, "Incorrect request");
    }
    existingUserDTO.setPhone(userDTO.getPhone());
    return existingUserDTO;
  }

  @DeleteMapping("/{id}")
  public String delete(BigDecimal id){
    return "删除成功";
  }

}
