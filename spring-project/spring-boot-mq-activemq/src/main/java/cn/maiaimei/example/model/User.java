package cn.maiaimei.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements Serializable {
    private static final long serialVersionUID = 8606218602085637811L;
    String nickname;
    String username;
}