package com.game.rpgbackend.repository;

import com.game.rpgbackend.domain.Dialog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface DialogRepository extends JpaRepository<Dialog, Integer> {
    List<Dialog> findByContentId(Integer contentId);
    List<Dialog> findByLevelMinimoLessThanEqual(Integer level);
    List<Dialog> findByContentIdAndLevelMinimoLessThanEqual(Integer contentId, Integer level);
}
