package cn.maiaimei.example.dto;

import cn.maiaimei.framework.validation.constraints.Password;
import cn.maiaimei.framework.validation.group.ValidationGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class UserRequest {
    @NotBlank(groups = {ValidationGroup.Update.class})
    private Long id;

    @NotBlank(groups = {ValidationGroup.OrderA.class})
    private String username;

    @NotBlank(groups = {ValidationGroup.OrderA.class})
    @Password
    private String password;
}
