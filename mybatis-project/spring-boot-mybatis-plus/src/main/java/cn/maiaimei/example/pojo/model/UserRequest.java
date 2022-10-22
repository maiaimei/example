package cn.maiaimei.example.pojo.model;

import cn.maiaimei.framework.validation.group.ValidationGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotNull(groups = {ValidationGroup.Update.class})
    @ApiModelProperty(value = "ID", example = "1512076060877955072")
    private Long id;

    @NotBlank(groups = {ValidationGroup.OrderA.class})
    @Length(min = 3, max = 50, groups = {ValidationGroup.OrderB.class})
    @ApiModelProperty(value = "昵称", required = true, example = "Tester")
    private String nickname;

    @NotBlank(groups = {ValidationGroup.OrderA.class})
    @Length(min = 3, max = 20, groups = {ValidationGroup.OrderB.class})
    @Pattern(regexp = "[a-zA-Z0-9]{3,20}", groups = {ValidationGroup.OrderC.class})
    @ApiModelProperty(value = "账号", required = true, example = "Tester")
    private String username;

    @NotBlank(groups = {ValidationGroup.Create.class})
    @Length(min = 3, max = 20, groups = {ValidationGroup.Create.class})
    @Pattern(regexp = "[a-zA-Z0-9\\@\\#\\$]{3,20}", groups = {ValidationGroup.Create.class})
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;

    private Integer version;
}
