package com.game.rpgbackend.controller;

import com.game.rpgbackend.dto.request.LoginUserDto;
import com.game.rpgbackend.dto.request.RegisterUserDto;
import com.game.rpgbackend.dto.response.LoginResponseDto;
import com.game.rpgbackend.dto.response.UserDto;
import com.game.rpgbackend.domain.User;
import com.game.rpgbackend.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller responsável pela autenticação de usuários.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * Endpoint para registro de novo usuário.
     */
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegisterUserDto request) {
        UserService.RegisterRequest registerRequest = new UserService.RegisterRequest();
        registerRequest.setNomeUsuario(request.getNomeUsuario());
        registerRequest.setEmail(request.getEmail());
        registerRequest.setSenha(request.getSenha());

        User user = userService.registerUser(registerRequest);

        UserDto userDto = new UserDto(user.getId(), user.getNomeUsuario(), user.getEmail(), user.getCriadoEm());
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    /**
     * Endpoint para login de usuário.
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginUserDto request) {
        LoginResponseDto response = userService.loginUser(request.getNomeUsuario(), request.getSenha());
        return ResponseEntity.ok(response);
    }
}
