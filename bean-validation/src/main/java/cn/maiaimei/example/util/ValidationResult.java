package cn.maiaimei.example.util;

import lombok.Data;

import java.util.List;

@Data
public class ValidationResult {
    private boolean hasErrors;
    private List<String> errorMessages;
}
