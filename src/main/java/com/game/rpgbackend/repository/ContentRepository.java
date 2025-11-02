package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

/**
 * Repositório para operações de persistência de conteúdos educacionais.
 * <p>
 * Gerencia o acesso aos tópicos educacionais disponíveis na Torre do Conhecimento,
 * permitindo buscar conteúdos por nome e filtrar por nível mínimo requerido.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Repository
public interface ContentRepository extends JpaRepository<Content, Integer> {

    /**
     * Busca um conteúdo educacional pelo nome.
     *
     * @param contentName nome do conteúdo
     * @return Optional contendo o conteúdo se encontrado
     */
    Optional<Content> findByContentName(String contentName);

    /**
     * Busca todos os conteúdos acessíveis para um nível específico.
     * Retorna conteúdos cujo nível mínimo é menor ou igual ao nível fornecido.
     *
     * @param level nível do jogador
     * @return lista de conteúdos disponíveis para o nível
     */
    List<Content> findByMinLevelLessThanEqual(Integer level);
}
