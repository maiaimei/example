package cn.maiaimei.example.dto;

import cn.maiaimei.framework.validation.constraints.Birthday;
import cn.maiaimei.framework.validation.constraints.Email;
import cn.maiaimei.framework.validation.constraints.IdNumber;
import cn.maiaimei.framework.validation.constraints.Mobile;
import cn.maiaimei.framework.validation.group.ValidationGroup;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class PersonRequest {

    /**
     * ID
     */
    @NotNull(groups = {ValidationGroup.Update.class})
    private Long id;

    /**
     * 姓名
     */
    @NotBlank(groups = {ValidationGroup.OrderA.class})
    private String name;

    /**
     * 名
     */
    @NotBlank(groups = {ValidationGroup.OrderA.class})
    private String firstname;

    /**
     * 姓
     */
    @NotBlank(groups = {ValidationGroup.OrderA.class})
    private String lastname;

    /**
     * 男 - M,Male
     * 女 - F,Female
     */
    @NotBlank(groups = {ValidationGroup.OrderA.class})
    @Pattern(regexp = "M|F", groups = ValidationGroup.OrderB.class)
    private String sex;

    /**
     * 出生日期
     */
    @NotBlank(groups = {ValidationGroup.OrderA.class})
    @Birthday(groups = ValidationGroup.OrderB.class)
    private String birthday;

    /**
     * 身份证号
     */
    @NotBlank(groups = {ValidationGroup.OrderA.class})
    @IdNumber(groups = ValidationGroup.OrderB.class)
    private String idNumber;

    /**
     * 手机号码
     */
    @Mobile(groups = {ValidationGroup.OrderA.class})
    private String mobile;

    /**
     * 电子邮箱
     */
    @Email(groups = {ValidationGroup.OrderA.class})
    private String email;

}
