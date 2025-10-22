package com.fiap.techchallenge.feedback.core.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "feedbacks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "aula_id", nullable = false)
    private Long aulaId;

    @Column(name = "curso_id", nullable = false)
    private Long cursoId;

    @Column(name = "estudante_id", nullable = false)
    private Long estudanteId;

    @Column(name = "professor_id")
    private Long professorId;

    @Column(nullable = false)
    private Integer nota;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String comentario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusFeedback status;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private CategoriaFeedback categoria;

    @Column(name = "anonimo")
    private Boolean anonimo;

    @Column(name = "data_criacao", nullable = false)
    private LocalDateTime dataCriacao;

    @Column(name = "data_atualizacao")
    private LocalDateTime dataAtualizacao;

    @Column(name = "respondido_por")
    private Long respondidoPor;

    @Column(name = "resposta", columnDefinition = "TEXT")
    private String resposta;

    @Column(name = "data_resposta")
    private LocalDateTime dataResposta;
}
