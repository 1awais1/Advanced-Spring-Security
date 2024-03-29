package com.example.springsecurity.demo.auth;

import com.example.springsecurity.demo.Security.ApplicationUserRole;
import com.example.springsecurity.demo.Student.Student;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository("fake")
public class ApplicationUserDaoService implements ApplicationUserDao{



    private final PasswordEncoder passwordEncoder;



@Autowired
    public ApplicationUserDaoService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
}

    @Override
    public Optional<ApplicationUser> selectApplicationUser(String username) {

        return getApplicationUserList().stream()
                .filter(x -> username.equals(x.getUsername()))
                .findFirst();

    }

    private List<ApplicationUser> getApplicationUserList() {
        List<ApplicationUser> applicationUsers = Lists.newArrayList(
                new ApplicationUser(
                        ApplicationUserRole.ADMIN.getGrantedAuthorities(),
                        passwordEncoder.encode("password"),
                        "MianSaab",
                        true, true, true, true


                ),
                new ApplicationUser(
                        ApplicationUserRole.ADMIN_TRAINEE.getGrantedAuthorities(),
                        passwordEncoder.encode("password"),
                        "Zain",
                        true, true, true, true


                ),
                new ApplicationUser(
                        ApplicationUserRole.STUDENT.getGrantedAuthorities(),
                        passwordEncoder.encode("password"),
                        "mushii",
                        true, true, true, true
                )
        );

        return applicationUsers;
    }
}
