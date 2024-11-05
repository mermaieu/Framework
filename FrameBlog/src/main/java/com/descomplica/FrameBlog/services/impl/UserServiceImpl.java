package com.descomplica.FrameBlog.services.impl;

import com.descomplica.FrameBlog.models.User;
import com.descomplica.FrameBlog.repositories.UserRepository;
import com.descomplica.FrameBlog.services.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    // PasswordEncoder is provided by org.springframework.security.crypto.password.PasswordEncoder

    @Override
    @CircuitBreaker(name = "circuitBreaker")  // Explicação: https://chatgpt.com/share/6729d8a9-46a0-8011-bbd5-e77db0700d92
    public User save(final User user) {
        User existingUser = userRepository.findByUsername(user.getUsername());

        if(Objects.nonNull(existingUser)){
            throw new RuntimeException("Existing User");
        }
        String passwordHash = passwordEncoder.encode(user.getPassword());
        // transforma a senha em uma sequência de caracteres embaralhados (hash)
        // para que seja armazenada nesse formato, e não como texto puro.

        User entity = new User(user.getUserId(), user.getName(), user.getEmail(), user.getUsername(), passwordHash, user.getRole()); // Emerson

        User newUser = userRepository.save(entity);
        // save da JPA (JpaRepository ou CrudRepository) retorna o próprio objeto salvo no banco de dados,
        // já com todas as informações atualizadas, incluindo o ID gerado (caso seja uma inserção de um
        // novo registro).

        return newUser;  // Suggested by Emerson
        // return new User(newUser.getUserId(), newUser.getName(), newUser.getEmail(), newUser.getUsername(), newUser.getPassword(),newUser.getRole()); // Emerson
    }

    @Override
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Override
    public User get(final Long id){
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found")
        );
    }

    @Override
    public User update(final Long id, final User user){
        User userUpdate = userRepository.findById(id).orElse(null);
        if(Objects.nonNull(userUpdate)){
            String passwordHash = passwordEncoder.encode(user.getPassword());
            userUpdate.setName(user.getName());
            userUpdate.setUsername(user.getUsername());
            userUpdate.setEmail(user.getEmail());
            userUpdate.setRole(user.getRole());
            userUpdate.setPassword(passwordHash);
            return userRepository.save(userUpdate);
        }
        return null;
    }

    @Override
    public void delete(final Long id){
        userRepository.deleteById(id);
    }
}