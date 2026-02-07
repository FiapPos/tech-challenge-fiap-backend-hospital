package com.fiap.techchallenge.agendamento_service.core.repository;

import com.fiap.techchallenge.agendamento_service.core.entity.Consulta;
import com.fiap.techchallenge.agendamento_service.core.enums.EStatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    int DURACAO_CONSULTA_MINUTOS = 15;

    @Query("SELECT c FROM Consulta c WHERE c.pacienteId = :pacienteId " +
           "AND c.dataHora BETWEEN :dataHoraInicio AND :dataHoraFim " +
           "AND c.status NOT IN (:statusExcluidos)")
    List<Consulta> findConflitoPaciente(
            @Param("pacienteId") Long pacienteId,
            @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
            @Param("dataHoraFim") LocalDateTime dataHoraFim,
            @Param("statusExcluidos") List<EStatusAgendamento> statusExcluidos);

    @Query("SELECT c FROM Consulta c WHERE c.medicoId = :medicoId " +
           "AND c.dataHora BETWEEN :dataHoraInicio AND :dataHoraFim " +
           "AND c.status NOT IN (:statusExcluidos)")
    List<Consulta> findConflitoMedico(
            @Param("medicoId") Long medicoId,
            @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
            @Param("dataHoraFim") LocalDateTime dataHoraFim,
            @Param("statusExcluidos") List<EStatusAgendamento> statusExcluidos);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Consulta c " +
           "WHERE c.medicoId = :medicoId " +
           "AND c.dataHora BETWEEN :dataHoraInicio AND :dataHoraFim " +
           "AND c.status <> :status")
    boolean existsByMedicoIdAndDataHoraBetweenAndStatusNot(
            @Param("medicoId") Long medicoId,
            @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
            @Param("dataHoraFim") LocalDateTime dataHoraFim,
            @Param("status") EStatusAgendamento status);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Consulta c " +
           "WHERE c.pacienteId = :pacienteId " +
           "AND c.dataHora BETWEEN :dataHoraInicio AND :dataHoraFim " +
           "AND c.status <> :status")
    boolean existsByPacienteIdAndDataHoraBetweenAndStatusNot(
            @Param("pacienteId") Long pacienteId,
            @Param("dataHoraInicio") LocalDateTime dataHoraInicio,
            @Param("dataHoraFim") LocalDateTime dataHoraFim,
            @Param("status") EStatusAgendamento status);

    List<Consulta> findByPacienteIdAndStatus(Long pacienteId, EStatusAgendamento status);

    List<Consulta> findByMedicoIdAndStatus(Long medicoId, EStatusAgendamento status);

    @Query("SELECT c FROM Consulta c WHERE c.hospitalId = :hospitalId " +
           "AND c.status <> :statusCancelada " +
           "ORDER BY c.dataHora ASC")
    List<Consulta> findByHospitalIdAtivas(
            @Param("hospitalId") Long hospitalId,
            @Param("statusCancelada") EStatusAgendamento statusCancelada);

    @Query("SELECT c FROM Consulta c WHERE c.hospitalId = :hospitalId " +
           "AND c.dataHora BETWEEN :dataInicio AND :dataFim " +
           "AND c.status <> :statusCancelada " +
           "ORDER BY c.dataHora ASC")
    List<Consulta> findByHospitalIdAndData(
            @Param("hospitalId") Long hospitalId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("statusCancelada") EStatusAgendamento statusCancelada);

    @Query("SELECT c FROM Consulta c WHERE c.hospitalId = :hospitalId " +
           "AND c.especialidadeId = :especialidadeId " +
           "AND c.status <> :statusCancelada " +
           "ORDER BY c.dataHora ASC")
    List<Consulta> findByHospitalIdAndEspecialidadeId(
            @Param("hospitalId") Long hospitalId,
            @Param("especialidadeId") Long especialidadeId,
            @Param("statusCancelada") EStatusAgendamento statusCancelada);

    @Query("SELECT c FROM Consulta c WHERE c.medicoId = :medicoId " +
           "AND c.dataHora BETWEEN :dataInicio AND :dataFim " +
           "AND c.status <> :statusCancelada " +
           "ORDER BY c.dataHora ASC")
    List<Consulta> findByMedicoIdAndData(
            @Param("medicoId") Long medicoId,
            @Param("dataInicio") LocalDateTime dataInicio,
            @Param("dataFim") LocalDateTime dataFim,
            @Param("statusCancelada") EStatusAgendamento statusCancelada);
}