package com.game.rpgbackend.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemLojaId implements Serializable {
    private Integer lojaId;
    private Integer itemId;
}
