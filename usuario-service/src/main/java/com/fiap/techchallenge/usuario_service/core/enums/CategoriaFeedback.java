package com.fiap.techchallenge.usuario_service.core.enums;

/**
 * Categorias para classificação dos feedbacks
 */
public enum CategoriaFeedback {
    CONTEUDO("Conteúdo", "Qualidade e relevância do conteúdo apresentado"),
    DIDATICA("Didática", "Metodologia e clareza na explicação"),
    MATERIAL("Material", "Qualidade dos materiais de apoio"),
    RECURSOS_TECNICOS("Recursos Técnicos", "Qualidade de áudio, vídeo e plataforma"),
    INTERACAO("Interação", "Engajamento e interação com alunos"),
    ORGANIZACAO("Organização", "Estruturação e organização da aula"),
    OUTRO("Outro", "Outras observações");

    private final String titulo;
    private final String descricao;

    CategoriaFeedback(String titulo, String descricao) {
        this.titulo = titulo;
        this.descricao = descricao;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }
}

