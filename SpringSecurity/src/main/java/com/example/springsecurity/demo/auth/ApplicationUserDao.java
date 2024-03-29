package com.example.springsecurity.demo.auth;

import java.util.Optional;

public interface ApplicationUserDao {

    public Optional<ApplicationUser> selectApplicationUser(String uname);

}
