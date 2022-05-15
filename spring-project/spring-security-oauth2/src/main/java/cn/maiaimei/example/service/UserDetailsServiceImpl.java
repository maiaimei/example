package cn.maiaimei.example.service;

import cn.maiaimei.example.model.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = passwordEncoder.encode("12345");
        String authorityString = map.get(username);
        return new CustomUserDetails(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList(authorityString));
    }

    private static HashMap<String, String> map = new HashMap<>();

    static {
        map.put("admin", "index,uuap,wfap,ROLE_admin,user:create,user:update,,user:delete,,user:view");
        map.put("maiaimei", "index,ROLE_user");
    }
}
