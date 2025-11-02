package com.game.rpgbackend.dto.response.hub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para lojas do Sebo da Linguística.
 * <p>
 * Representa um estabelecimento comercial no Hub onde jogadores podem
 * comprar itens usando ouro. Cada loja pode ter um inventário especializado
 * (armas, armaduras, consumíveis, livros, etc.).
 * </p>
 * <p>
 * Informações básicas da loja:
 * - Identificação única
 * - Nome da loja
 * - Descrição do tipo de mercadoria vendida
 * </p>
 * <p>
 * Usado no frontend para:
 * - Listar lojas disponíveis no Hub
 * - Exibir informações ao selecionar uma loja
 * - Permitir navegação entre diferentes lojas
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {

    /**
     * Identificador único da loja.
     */
    private Integer id;

    /**
     * Nome da loja.
     */
    private String name;

    /**
     * Descrição da loja e tipos de itens vendidos.
     */
    private String description;
}
