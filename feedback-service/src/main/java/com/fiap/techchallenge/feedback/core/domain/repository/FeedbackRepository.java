package com.fiap.techchallenge.feedback.core.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import com.fiap.techchallenge.feedback.core.domain.model.Feedback;
import com.fiap.techchallenge.feedback.core.domain.model.StatusFeedback;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Long> {

    List<Feedback> findByAulaId(Long aulaId);

    List<Feedback> findByCursoId(Long cursoId);

    List<Feedback> findByEstudanteId(Long estudanteId);

    List<Feedback> findByProfessorId(Long professorId);

    List<Feedback> findByStatus(StatusFeedback status);

    @Query("SELECT f FROM Feedback f WHERE f.nota <= :nota AND f.status = 'PENDENTE'")
    List<Feedback> findFeedbacksCriticos(@Param("nota") Integer nota);

    @Query("SELECT f FROM Feedback f WHERE f.dataCriacao BETWEEN :inicio AND :fim")
    List<Feedback> findByPeriodo(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);

    @Query("SELECT AVG(f.nota) FROM Feedback f WHERE f.aulaId = :aulaId")
    Double calcularMediaNotaAula(@Param("aulaId") Long aulaId);

    @Query("SELECT AVG(f.nota) FROM Feedback f WHERE f.cursoId = :cursoId")
    Double calcularMediaNotaCurso(@Param("cursoId") Long cursoId);

    @Query("SELECT AVG(f.nota) FROM Feedback f WHERE f.professorId = :professorId")
    Double calcularMediaNotaProfessor(@Param("professorId") Long professorId);

    @Query("SELECT COUNT(f) FROM Feedback f WHERE f.aulaId = :aulaId AND f.nota <= :nota")
    Long contarFeedbacksCriticosPorAula(@Param("aulaId") Long aulaId, @Param("nota") Integer nota);

    Long countByAulaId(Long aulaId);

    Long countByCursoId(Long cursoId);

    Long countByProfessorId(Long professorId);
}
