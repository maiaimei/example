package cn.maiaimei.example.model;

import lombok.Data;

@Data
public class UserPagingQueryRequest {
    Long current;
    Long size;
}
