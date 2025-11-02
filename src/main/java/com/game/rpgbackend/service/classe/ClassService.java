package com.game.rpgbackend.service.classe;

import com.game.rpgbackend.domain.GameClass;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.ClassRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Serviço responsável pela gestão de classes de personagem.
 * <p>
 * Fornece acesso às classes jogáveis disponíveis no sistema,
 * incluindo seus atributos base, habilidades especiais e características.
 * Classes disponíveis: Lutador, Mago, Bardo, Ladino, Paladino e Tank.
 * </p>
 * <p>
 * As classes definem os atributos iniciais dos personagens criados,
 * incluindo HP, força, inteligência, defesa e energia.
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClassService {

    private final ClassRepository classRepository;

    /**
     * Retorna todas as classes de personagem disponíveis no jogo.
     * <p>
     * Inclui informações completas de cada classe:
     * - Atributos base (HP, força, inteligência, defesa, etc.)
     * - Energia inicial
     * - Descrição da classe
     * </p>
     *
     * @return lista com todas as classes disponíveis
     */
    public List<GameClass> getAllClasses() {
        return classRepository.findAll();
    }

    /**
     * Busca uma classe específica por seu identificador único.
     *
     * @param id identificador único da classe
     * @return classe encontrada com todos os seus atributos
     * @throws NotFoundException se a classe não for encontrada
     */
    public GameClass getClassById(Integer id) {
        return classRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Classe não encontrada"));
    }

    /**
     * Busca uma classe pelo seu nome.
     * <p>
     * Nomes aceitos (case-insensitive): lutador, mago, bardo, ladino, paladino, tank.
     * </p>
     *
     * @param name nome da classe a ser buscada
     * @return classe encontrada com todos os seus atributos
     * @throws NotFoundException se a classe não for encontrada
     */
    public GameClass getClassByName(String name) {
        return classRepository.findByName(name)
            .orElseThrow(() -> new NotFoundException("Classe não encontrada"));
    }
}
