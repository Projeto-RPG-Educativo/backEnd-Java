package com.game.rpgbackend.controller.auth;

import com.game.rpgbackend.dto.request.auth.LoginUserDto;
import com.game.rpgbackend.dto.request.auth.RegisterUserDto;
import com.game.rpgbackend.dto.response.auth.LoginResponseDto;
import com.game.rpgbackend.dto.response.auth.UserDto;
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
 * @author MURILO FURTADO
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
        registerRequest.setNomeUsuario(request.getUsername());
        registerRequest.setEmail(request.getEmail());
        registerRequest.setSenha(request.getPassword());

        User user = userService.registerUser(registerRequest);

        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
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
        LoginResponseDto response = userService.loginUser(request.getUsername(), request.getPassword());
        return ResponseEntity.ok(response);
    }
}
