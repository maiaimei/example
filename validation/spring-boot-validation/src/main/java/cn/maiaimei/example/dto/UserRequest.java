package cn.maiaimei.example.dto;

import cn.maiaimei.framework.validation.constraints.Password;
import cn.maiaimei.framework.validation.group.ValidationGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserRequest {
    @NotNull(groups = {ValidationGroup.Update.class})
    private Long id;

    @NotBlank(groups = {ValidationGroup.OrderA.class})
    private String username;

    @NotBlank(groups = {ValidationGroup.OrderA.class})
    @Password
    private String password;
}
