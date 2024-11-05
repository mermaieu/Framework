package com.descomplica.FrameBlog.controllers;

import com.descomplica.FrameBlog.request.AuthRequest;
import com.descomplica.FrameBlog.response.AuthResponse;
import com.descomplica.FrameBlog.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

/*  A função do AuthenticationController é receber a requisição de login, validar as credenciais do usuário
    por meio do AuthenticationManager e, uma vez autenticado com sucesso, acionar o AuthenticationService
    para gerar um token. Esse token é então enviado na resposta e permitirá que o usuário seja identificado
    em futuras requisições sem precisar se autenticar novamente.
 */

@RestController
public class AuthenticationController {  // Explicada em: https://chatgpt.com/share/6729f849-3df4-8011-a116-bdc0aba99391

    @Autowired
    private AuthenticationManager authenticationManager;  // from org.springframework.security.authentication
    // Componente do Spring Security que gerencia o processo de autenticação,
    // verificando se as credenciais fornecidas são válidas.

    @Autowired
    private AuthenticationService authenticationService;  // from com.descomplica.FrameBlog.services.

    @PostMapping(path="/login")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public AuthResponse login(@RequestBody final AuthRequest auth) {

        UsernamePasswordAuthenticationToken userAuthenticationToken = new UsernamePasswordAuthenticationToken(auth.getUsername(), auth.getPassword());
    //  Objeto criado usando o username e password do auth.
    //  Esse token é a representação das credenciais do usuário.

        authenticationManager.authenticate(userAuthenticationToken);
    //  Realiza a autenticação usando o token gerado. Caso as credenciais
    //  sejam válidas, o processo segue; caso contrário, será lançada
    //  uma exceção de autenticação (AuthenticationException).

        return new AuthResponse(authenticationService.getToken(auth));
    //  retorna uma intância de AuthResponse, contendo o token (JASON Web Token)
    //  gerado pelo authenticationService.getToken(auth). Esse token serve para
    //  identificar o usuário nas próximas requisições, sem a necessidade
    //  de reautenticação.
    }
}