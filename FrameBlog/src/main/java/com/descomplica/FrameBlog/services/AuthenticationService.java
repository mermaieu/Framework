package com.descomplica.FrameBlog.services;

import com.descomplica.FrameBlog.request.AuthRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthenticationService extends UserDetailsService{

    String getToken(AuthRequest auth);
    // Implementado em AuthenticationServiceImpl.
    // Usado em AuthenticationController

    String validateJwtToken(String token);
    // Implementado em AuthenticationServiceImpl.
    // Usado em SecurityFilter
}
