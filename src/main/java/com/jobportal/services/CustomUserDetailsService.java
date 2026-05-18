package com.jobportal.services;

import com.jobportal.entity.Users;
import com.jobportal.repository.UsersRepository;
import com.jobportal.util.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final HttpServletRequest request;

    @Autowired
    public CustomUserDetailsService(UsersRepository usersRepository, HttpServletRequest request) {
        this.usersRepository = usersRepository;
        this.request = request;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users user = usersRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("Could not found user"));

        if (RequestContextHolder.getRequestAttributes() != null) {
            String accountType = request.getParameter("accountType");
            if (accountType != null && !accountType.isEmpty()) {
                try {
                    int typeId = Integer.parseInt(accountType);
                    if (user.getUserTypeId().getUserTypeId() != typeId) {
                        throw new UsernameNotFoundException("User not found with selected account type");
                    }
                } catch (NumberFormatException e) {
                    throw new UsernameNotFoundException("Invalid account type");
                }
            }
        }

        return new CustomUserDetails(user);
    }
}
