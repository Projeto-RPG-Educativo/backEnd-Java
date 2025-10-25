package com.game.rpgbackend.service.user;

import com.game.rpgbackend.domain.PlayerStats;
import com.game.rpgbackend.domain.User;
import com.game.rpgbackend.dto.request.UpdateProfileRequest;
import com.game.rpgbackend.dto.response.LoginResponseDto;
import com.game.rpgbackend.dto.response.UserDto;
import com.game.rpgbackend.dto.response.UserProfileResponse;
import com.game.rpgbackend.dto.response.UserStatsResponse;
import com.game.rpgbackend.exception.BadRequestException;
import com.game.rpgbackend.exception.UnauthorizedException;
import com.game.rpgbackend.repository.PlayerStatsRepository;
import com.game.rpgbackend.repository.UserRepository;
import com.game.rpgbackend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Serviço responsável pela gestão de usuários e autenticação.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final PlayerStatsRepository playerStatsRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    /**
     * Registra um novo usuário no sistema.
     */
    @Transactional
    public User registerUser(RegisterRequest request) {
        if (request.getNomeUsuario() == null || request.getSenha() == null || request.getEmail() == null) {
            throw new BadRequestException("Nome de usuário, email e senha são obrigatórios.");
        }

        // Verifica se o usuário já existe
        if (userRepository.findByUsername(request.getNomeUsuario()).isPresent()) {
            throw new BadRequestException("Este nome de usuário já está em uso.");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new BadRequestException("Este email já está em uso.");
        }

        // Cria o novo usuário
        User newUser = new User();
        newUser.setUsername(request.getNomeUsuario());
        newUser.setEmail(request.getEmail());
        newUser.setHashedPassword(passwordEncoder.encode(request.getSenha()));
        newUser.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(newUser);

        // Cria as estatísticas iniciais do jogador
        PlayerStats stats = new PlayerStats();
        stats.setUser(savedUser);
        stats.setLevel(1);
        stats.setTotalXpEarned(0);
        stats.setTotalGoldEarned(0);
        stats.setBattlesWon(0);
        stats.setBattlesLost(0);
        stats.setQuestionsRight(0);
        stats.setQuestionsWrong(0);
        stats.setSkillPoints(0);
        playerStatsRepository.save(stats);

        return savedUser;
    }

    /**
     * Autentica um usuário e retorna um token JWT.
     */
    public LoginResponseDto loginUser(String username, String password) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UnauthorizedException("Credenciais inválidas."));

        if (!passwordEncoder.matches(password, user.getHashedPassword())) {
            throw new UnauthorizedException("Credenciais inválidas.");
        }

        // 3. Gera o token JWT
        String token = jwtUtil.generateToken(user.getUsername());

        // 4. Retorna a resposta usando o DTO do pacote dto.response
        UserDto userDto = new UserDto(user.getId(), user.getUsername(), user.getEmail(), user.getCreatedAt());
        return new LoginResponseDto("Login bem-sucedido!", userDto, token);
    }

    /**
     * Busca um usuário por ID.
     */
    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    /**
     * Busca um usuário por nome de usuário.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Busca um usuário por email.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Atualiza um usuário.
     */
    @Transactional
    public User updateUser(Integer id, User userData) {
        User existing = userRepository.findById(id)
            .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));

        if (userData.getUsername() != null) {
            // Verifica se o novo nome já está em uso por outro usuário
            userRepository.findByUsername(userData.getUsername())
                .ifPresent(user -> {
                    if (!user.getId().equals(id)) {
                        throw new BadRequestException("Nome de usuário já está em uso");
                    }
                });
            existing.setUsername(userData.getUsername());
        }

        if (userData.getEmail() != null) {
            // Verifica se o novo email já está em uso por outro usuário
            userRepository.findByEmail(userData.getEmail())
                .ifPresent(user -> {
                    if (!user.getId().equals(id)) {
                        throw new BadRequestException("Email já está em uso");
                    }
                });
            existing.setEmail(userData.getEmail());
        }

        return userRepository.save(existing);
    }

    /**
     * Atualiza a senha de um usuário.
     */
    @Transactional
    public void updatePassword(Integer id, String novaSenha) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));

        user.setHashedPassword(passwordEncoder.encode(novaSenha));
        userRepository.save(user);
    }

    /**
     * Retorna o perfil completo do usuário.
     */
    public UserProfileResponse getUserProfile(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));

        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setNome(user.getUsername());
        response.setEmail(user.getEmail());
        response.setDataCriacao(user.getCreatedAt());
        response.setTotalPersonagens(0); // TODO: Implementar contagem de personagens quando houver o repository necessário

        return response;
    }

    /**
     * Retorna as estatísticas do usuário.
     */
    public UserStatsResponse getUserStats(String username) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));

        PlayerStats stats = playerStatsRepository.findByUserId(user.getId())
            .orElseThrow(() -> new BadRequestException("Estatísticas não encontradas"));

        UserStatsResponse response = new UserStatsResponse();
        response.setLevel(stats.getLevel());
        response.setTotalBatalhasVencidas(stats.getBattlesWon());
        response.setTotalBatalhasPerdidas(stats.getBattlesLost());
        response.setTotalQuestoesCorretas(stats.getQuestionsRight());
        response.setTotalQuestoesErradas(stats.getQuestionsWrong());
        response.setXpTotal(stats.getTotalXpEarned());

        int totalQuestoes = stats.getQuestionsRight() + stats.getQuestionsWrong();
        response.setTaxaAcerto(totalQuestoes > 0 ? (stats.getQuestionsRight() * 100.0) / totalQuestoes : 0.0);

        return response;
    }

    /**
     * Atualiza o perfil do usuário.
     */
    @Transactional
    public UserProfileResponse updateProfile(String username, UpdateProfileRequest request) {
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BadRequestException("Usuário não encontrado"));

        // Atualiza o nome de usuário se fornecido
        if (request.getNome() != null && !request.getNome().equals(user.getUsername())) {
            if (userRepository.findByUsername(request.getNome()).isPresent()) {
                throw new BadRequestException("Nome de usuário já está em uso");
            }
            user.setUsername(request.getNome());
        }

        // Atualiza o email se fornecido
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new BadRequestException("Email já está em uso");
            }
            user.setEmail(request.getEmail());
        }

        // Atualiza a senha se fornecida
        if (request.getNovaSenha() != null) {
            if (request.getSenhaAtual() == null ||
                !passwordEncoder.matches(request.getSenhaAtual(), user.getHashedPassword())) {
                throw new BadRequestException("Senha atual incorreta");
            }
            user.setHashedPassword(passwordEncoder.encode(request.getNovaSenha()));
        }

        userRepository.save(user);
        return getUserProfile(user.getUsername());
    }

    // Classe interna para Request de registro
    public static class RegisterRequest {
        private String nomeUsuario;
        private String email;
        private String senha;

        public String getNomeUsuario() { return nomeUsuario; }
        public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getSenha() { return senha; }
        public void setSenha(String senha) { this.senha = senha; }
    }
}
