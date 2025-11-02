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
 * Serviço responsável pela gestão de diálogos educacionais multilíngues.
 * <p>
 * Gerencia diálogos em português e inglês usados para ensinar idiomas aos jogadores.
 * Calcula dinamicamente a proporção de cada idioma mostrada com base no nível
 * do personagem, implementando aprendizado progressivo.
 * </p>
 * <p>
 * Sistema de progressão de idioma:
 * - Nível mínimo: 20% inglês / 80% português
 * - Cada nível acima: +20% inglês
 * - Máximo: 100% inglês
 * </p>
 *
 * @author MURILO FURTADO
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DialogService {

    private final DialogRepository dialogRepository;

    /**
     * Busca diálogos educacionais acessíveis para um nível específico.
     * <p>
     * Filtra diálogos por conteúdo educacional e nível mínimo necessário,
     * retornando apenas aqueles que o personagem tem permissão para ver.
     * </p>
     *
     * @param characterLevel nível atual do personagem
     * @param contentId identificador do conteúdo educacional
     * @return lista de diálogos adaptados ao nível do personagem
     */
    public List<DialogResponse> getDialogsByLevel(Integer characterLevel, Integer contentId) {
        List<Dialog> dialogs = dialogRepository.findByContentIdAndMinLevelLessThanEqual(contentId, characterLevel);

        return dialogs.stream()
            .map(this::mapToDialogResponse)
            .collect(Collectors.toList());
    }

    /**
     * Busca um diálogo educacional específico por ID.
     * <p>
     * Retorna o diálogo completo em ambos os idiomas (português e inglês)
     * junto com as palavras-chave destacadas.
     * </p>
     *
     * @param dialogId identificador único do diálogo
     * @return diálogo completo com textos e palavras-chave
     * @throws NotFoundException se o diálogo não for encontrado
     */
    public DialogResponse getDialogById(Integer dialogId) {
        Dialog dialog = dialogRepository.findById(dialogId)
            .orElseThrow(() -> new NotFoundException("Diálogo não encontrado"));

        return mapToDialogResponse(dialog);
    }

    /**
     * Calcula a proporção dinâmica de inglês/português para um diálogo.
     * <p>
     * A proporção aumenta progressivamente com o nível do personagem:
     * - No nível mínimo do diálogo: 20% inglês
     * - Cada nível acima adiciona 20% de inglês
     * - Máximo de 100% inglês (totalmente em inglês)
     * </p>
     * <p>
     * Exemplo: Se dialogMinLevel = 5 e characterLevel = 7,
     * ratio = 0.2 + (2 * 0.2) = 0.6 (60% inglês, 40% português)
     * </p>
     *
     * @param characterLevel nível atual do personagem
     * @param dialogMinLevel nível mínimo do diálogo
     * @return valor entre 0.2 e 1.0 representando a proporção de inglês
     */
    public double calculateLanguageRatio(Integer characterLevel, Integer dialogMinLevel) {
        if (characterLevel.equals(dialogMinLevel)) {
            return 0.2;
        }

        double ratio = 0.2 + ((characterLevel - dialogMinLevel) * 0.2);
        return Math.min(ratio, 1.0); // Limita o máximo a 100% em inglês
    }

    /**
     * Converte entidade Dialog para DTO de resposta.
     * <p>
     * Mapeia todas as informações do diálogo incluindo textos em ambos
     * os idiomas e palavras-chave destacadas com traduções.
     * </p>
     *
     * @param dialog entidade de diálogo do banco de dados
     * @return DTO formatado para resposta ao cliente
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
