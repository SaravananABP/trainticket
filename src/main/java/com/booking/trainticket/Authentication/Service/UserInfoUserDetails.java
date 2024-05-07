package com.booking.trainticket.Authentication.Service;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.booking.trainticket.Authentication.Pojo.UserDetailsInfo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;





/**
 *Custom implementation of UserDetails for the UserInfo entity.
 *This class provides the necessary details for user authentication and authorization.
 */
public class UserInfoUserDetails implements UserDetails{

    private String name;
    private String password;
    private List<GrantedAuthority> authorityList;

    public UserInfoUserDetails(UserDetailsInfo userDetailsInfo){
        name=userDetailsInfo.getUserName();
        password=userDetailsInfo.getPassword();
        authorityList= Arrays.stream(userDetailsInfo.getRole().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorityList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return name;
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

}

