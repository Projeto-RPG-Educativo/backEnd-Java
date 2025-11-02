package com.game.rpgbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

/**
 * Classe de identificador composto para a entidade ItemStore.
 * <p>
 * Representa a chave primária composta pela combinação de
 * ID da loja e ID do item, usada no relacionamento many-to-many
 * entre Store e Item.
 * </p>
 *
 * @author GABRIEL XAVIER
 * @version 1.0
 * @since 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemStoreId implements Serializable {

    /** Identificador da loja */
    private Integer storeId;

    /** Identificador do item */
    private Integer itemId;
}
