package com.game.rpgbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemStoreId implements Serializable {
    private Integer storeId;
    private Integer itemId;
}
