package cn.maiaimei.example.model;

import cn.maiaimei.example.validation.constraints.Sex;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class Person {

    public interface Insert {
    }

    public interface Update {
    }

    @Null(groups = Insert.class) // 分组校验，默认校验组是 javax.validation.groups.Default
    @NotNull(groups = Update.class)
    private Long id;

    @NotBlank
    private String name;

    @Sex(required = true)
    private String sex;

    private LocalDateTime birthday;

    @NotNull
    @Min(value = 1, message = "必须大于{value}") // tomcat-embed-el 用于解析消息模板
    @Max(value = 150, message = "必须小于{value}")
    private Integer age;

    private String idCardNumber;

    private String mobile;

    private String email;

    @NotEmpty
    @Valid // 被引用对象加@Valid注解才能完成级联校验
    private List<Education> education;

}
