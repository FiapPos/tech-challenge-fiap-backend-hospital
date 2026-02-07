package com.fiap.techchallenge.agendamento_service.core.repository;

import com.fiap.techchallenge.agendamento_service.core.entity.FilaEspera;
import com.fiap.techchallenge.agendamento_service.core.enums.EStatusFilaEspera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FilaEsperaRepository extends JpaRepository<FilaEspera, Long> {

    @Query("SELECT f FROM FilaEspera f " +
           "WHERE f.status = :status " +
           "AND f.especialidadeId = :especialidadeId " +
           "AND f.hospitalId = :hospitalId " +
           "AND f.dataHoraDesejadaInicio <= :dataHora " +
           "AND f.dataHoraDesejadaFim >= :dataHora " +
           "ORDER BY f.pesoPrioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findPacientesAguardandoPorPrioridade(
            @Param("status") EStatusFilaEspera status,
            @Param("especialidadeId") Long especialidadeId,
            @Param("hospitalId") Long hospitalId,
            @Param("dataHora") LocalDateTime dataHora);

    @Query("SELECT f FROM FilaEspera f " +
           "WHERE f.status = :status " +
           "AND f.especialidadeId = :especialidadeId " +
           "AND f.hospitalId = :hospitalId " +
           "AND (f.medicoId = :medicoId OR f.medicoId IS NULL) " +
           "AND f.dataHoraDesejadaInicio <= :dataHora " +
           "AND f.dataHoraDesejadaFim >= :dataHora " +
           "ORDER BY f.pesoPrioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findPacientesAguardandoPorPrioridadeComMedico(
            @Param("status") EStatusFilaEspera status,
            @Param("especialidadeId") Long especialidadeId,
            @Param("hospitalId") Long hospitalId,
            @Param("medicoId") Long medicoId,
            @Param("dataHora") LocalDateTime dataHora);

    Optional<FilaEspera> findByPacienteIdAndEspecialidadeIdAndHospitalIdAndStatus(
            Long pacienteId, Long especialidadeId, Long hospitalId, EStatusFilaEspera status);

    boolean existsByPacienteIdAndEspecialidadeIdAndHospitalIdAndStatus(
            Long pacienteId, Long especialidadeId, Long hospitalId, EStatusFilaEspera status);

    List<FilaEspera> findByPacienteIdAndStatus(Long pacienteId, EStatusFilaEspera status);

    @Query("SELECT f FROM FilaEspera f " +
           "WHERE f.status = :statusNotificado " +
           "AND f.notificadoEm < :limite")
    List<FilaEspera> findNotificadosExpirados(
            @Param("statusNotificado") EStatusFilaEspera statusNotificado,
            @Param("limite") LocalDateTime limite);

    @Query("SELECT f FROM FilaEspera f " +
           "WHERE f.status = :status " +
           "AND f.especialidadeId = :especialidadeId " +
           "AND f.hospitalId = :hospitalId " +
           "AND f.dataHoraDesejadaInicio <= :dataHora " +
           "AND f.dataHoraDesejadaFim >= :dataHora " +
           "ORDER BY f.pesoPrioridade DESC, f.criadoEm ASC " +
           "LIMIT 1")
    Optional<FilaEspera> findProximoPacientePrioritario(
            @Param("status") EStatusFilaEspera status,
            @Param("especialidadeId") Long especialidadeId,
            @Param("hospitalId") Long hospitalId,
            @Param("dataHora") LocalDateTime dataHora);

    @Query("SELECT f FROM FilaEspera f " +
           "WHERE f.status = :statusAguardando " +
           "ORDER BY f.pesoPrioridade DESC, f.criadoEm ASC")
    List<FilaEspera> findAllAguardandoOrdenadoPorPrioridade(
            @Param("statusAguardando") EStatusFilaEspera statusAguardando);
}
