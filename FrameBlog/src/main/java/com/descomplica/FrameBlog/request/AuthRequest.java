package com.descomplica.FrameBlog.request;

// A classe AuthRquest é um DTO
public class AuthRequest {
    final String username;
    final String password;

    // Construtor para criar uma requisição com as informações username e password
    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
