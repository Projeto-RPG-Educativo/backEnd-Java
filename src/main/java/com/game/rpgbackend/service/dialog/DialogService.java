package com.game.rpgbackend.service.dialog;

import com.game.rpgbackend.domain.Dialog;
import com.game.rpgbackend.exception.NotFoundException;
import com.game.rpgbackend.repository.DialogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela gestão de diálogos educacionais.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DialogService {

    private final DialogRepository dialogRepository;

    /**
     * Busca diálogos por nível do personagem e conteúdo.
     */
    public List<DialogResponse> getDialogsByLevel(Integer characterLevel, Integer contentId) {
        List<Dialog> dialogs = dialogRepository.findByContentIdAndLevelMinimoLessThanEqual(contentId, characterLevel);

        return dialogs.stream()
            .map(this::mapToDialogResponse)
            .collect(Collectors.toList());
    }

    /**
     * Busca um diálogo específico por ID.
     */
    public DialogResponse getDialogById(Integer dialogId) {
        Dialog dialog = dialogRepository.findById(dialogId)
            .orElseThrow(() -> new NotFoundException("Diálogo não encontrado"));

        return mapToDialogResponse(dialog);
    }

    /**
     * Calcula a proporção de inglês/português baseado no nível.
     * Se o personagem está no nível mínimo, mostra 20% em inglês.
     * A cada nível acima do mínimo, aumenta 20% de inglês.
     */
    public double calculateLanguageRatio(Integer characterLevel, Integer dialogMinLevel) {
        if (characterLevel.equals(dialogMinLevel)) {
            return 0.2;
        }

        double ratio = 0.2 + ((characterLevel - dialogMinLevel) * 0.2);
        return Math.min(ratio, 1.0); // Limita o máximo a 100% em inglês
    }

    /**
     * Mapeia Dialog para DialogResponse.
     */
    private DialogResponse mapToDialogResponse(Dialog dialog) {
        DialogResponse response = new DialogResponse();
        response.setId(dialog.getId());
        response.setPortugues(dialog.getPtDialogue());
        response.setIngles(dialog.getEnDialogue());

        if (dialog.getKeywords() != null) {
            List<KeywordResponse> keywords = dialog.getKeywords().stream()
                .map(k -> new KeywordResponse(k.getLanguageWord(), k.getTranslate(), k.getHighlight()))
                .collect(Collectors.toList());
            response.setKeywords(keywords);
        }

        return response;
    }

    // Classes internas para Response
    public static class DialogResponse {
        private Integer id;
        private String portugues;
        private String ingles;
        private List<KeywordResponse> keywords;

        public Integer getId() { return id; }
        public void setId(Integer id) { this.id = id; }

        public String getPortugues() { return portugues; }
        public void setPortugues(String portugues) { this.portugues = portugues; }

        public String getIngles() { return ingles; }
        public void setIngles(String ingles) { this.ingles = ingles; }

        public List<KeywordResponse> getKeywords() { return keywords; }
        public void setKeywords(List<KeywordResponse> keywords) { this.keywords = keywords; }
    }

    public static class KeywordResponse {
        private String palavra;
        private String traducao;
        private Boolean destaque;

        public KeywordResponse(String palavra, String traducao, Boolean destaque) {
            this.palavra = palavra;
            this.traducao = traducao;
            this.destaque = destaque;
        }

        public String getPalavra() { return palavra; }
        public void setPalavra(String palavra) { this.palavra = palavra; }

        public String getTraducao() { return traducao; }
        public void setTraducao(String traducao) { this.traducao = traducao; }

        public Boolean getDestaque() { return destaque; }
        public void setDestaque(Boolean destaque) { this.destaque = destaque; }
    }
}

