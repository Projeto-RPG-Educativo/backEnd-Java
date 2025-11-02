package com.game.rpgbackend.dto.response.hub;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para itens disponíveis no Sebo da Linguística.
 * <p>
 * Representa um item à venda em uma loja do Hub, incluindo informações
 * essenciais para exibição ao jogador e processamento de compra.
 * </p>
 * <p>
 * Informações incluídas:
 * - Identificação única do item
 * - Nome e descrição para exibição
 * - Preço em ouro
 * </p>
 * <p>
 * Usado no frontend para:
 * - Montar catálogo de itens da loja
 * - Exibir informações ao passar mouse sobre item
 * - Processar compras quando jogador clicar
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemStoreDto {

    /**
     * Identificador único do item.
     */
    private Integer id;

    /**
     * Nome do item.
     */
    private String name;

    /**
     * Descrição detalhada do item e seus efeitos.
     */
    private String description;

    /**
     * Preço do item em ouro.
     */
    private Integer price;
}
