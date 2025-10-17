package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.DialogKeyword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DialogKeywordRepository extends JpaRepository<DialogKeyword, Integer> {
    List<DialogKeyword> findByDialogId(Integer dialogId);
    List<DialogKeyword> findByDestaque(Boolean destaque);
}
