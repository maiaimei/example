package cn.maiaimei.example.entity;

import lombok.Data;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "sys_user")
@ToString
@Data
public class User {
    @Id
    private Long id;
    private String nickname;
    private String username;
    private String password;
}
