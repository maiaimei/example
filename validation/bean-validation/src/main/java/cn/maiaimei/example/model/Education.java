package cn.maiaimei.example.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class Education {
    @NotBlank
    private String from;
    @NotBlank
    private String to;
    @NotBlank
    private String school;
}
