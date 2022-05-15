package cn.maiaimei.example.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class UserDetail extends User {
    private String uid;

    private boolean ssoEnabled;

    public String getUid() {
        return uid;
    }

    public boolean isSsoEnabled() {
        return ssoEnabled;
    }

    public UserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities, String uid) {
        super(username, password, authorities);
        this.uid = uid;
    }

    public UserDetail(String username, String password, Collection<? extends GrantedAuthority> authorities, String uid, boolean ssoEnabled) {
        super(username, password, authorities);
        this.uid = uid;
        this.ssoEnabled = ssoEnabled;
    }
}
