package cn.maiaimei.example.multiple.jpa.ds1.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "sys_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FirstUser {
    @Id
    private Long id;

    @Column
    private String nickname;

    @Column
    private String username;

    @Column
    private String password;

    @Column
    private Boolean is_enabled;

    @Column
    private Boolean is_deleted;

    @CreationTimestamp
    @Column(name = "gmt_create")
    private LocalDateTime gmtCreate;

    @UpdateTimestamp
    @Column(name = "gmt_modified")
    private LocalDateTime gmtModified;
}
