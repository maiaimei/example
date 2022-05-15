package cn.maiaimei.example.model;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_file")
public class FileModel {
    @TableId
    private Long id;
    private String originalFilename;
    private String type;
    private Long size;
    private String groupName;
    private String path;
    private LocalDateTime gmtCreate;
}
