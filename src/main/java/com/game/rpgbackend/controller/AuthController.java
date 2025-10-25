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
 * Controller REST responsável pela autenticação e registro de usuários.
 * <p>
 * Fornece endpoints para registro de novos usuários e login no sistema,
 * gerando tokens JWT para autenticação nas requisições subsequentes.
 * </p>
 *
 * @author D0UGH5
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * Registra um novo usuário no sistema.
     * <p>
     * Cria uma nova conta de usuário com as credenciais fornecidas.
     * A senha será criptografada antes de ser armazenada.
     * </p>
     *
     * @param request dados de registro contendo nome de usuário, email e senha
     * @return DTO com informações básicas do usuário criado
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
     * Realiza o login de um usuário no sistema.
     * <p>
     * Autentica as credenciais fornecidas e retorna um token JWT
     * que deve ser usado nas próximas requisições protegidas.
     * </p>
     *
     * @param request credenciais de login (nome de usuário e senha)
     * @return resposta contendo token JWT e dados do usuário autenticado
     */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginUserDto request) {
        LoginResponseDto response = userService.loginUser(request.getNomeUsuario(), request.getSenha());
        return ResponseEntity.ok(response);
    }
}
