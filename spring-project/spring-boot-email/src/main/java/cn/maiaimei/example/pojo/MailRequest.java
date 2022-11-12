package cn.maiaimei.example.pojo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@ApiModel
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailRequest {
    private String subject;
    private String text;
    private List<String> to;
    private List<String> cc;
    private List<String> bcc;
}
