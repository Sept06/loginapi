package com.demo.security;

import com.demo.entity.UserEntity;
import com.demo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if (userEntity == null){
            throw new UsernameNotFoundException(username);
        }

        CustomUserDetails userDetails = new CustomUserDetails(userEntity);
        Set< GrantedAuthority> grantedAuthorities = new HashSet<>();
        if (userEntity.getRoleEntities().size() > 0){
            grantedAuthorities = userEntity.getRoleEntities().stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
        }else {
            grantedAuthorities.add(new SimpleGrantedAuthority("Admin"));
        }
        userDetails.setGrantedAuthorities(grantedAuthorities);
        return userDetails;
    }
}
