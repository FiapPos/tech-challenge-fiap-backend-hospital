package com.fiap.techchallenge.historico_service.core.repository;

import com.fiap.techchallenge.historico_service.core.entity.HistoricoMedico;
import com.fiap.techchallenge.historico_service.core.enums.EStatusAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface HistoricoMedicoRepository extends JpaRepository<HistoricoMedico, Long> {

    List<HistoricoMedico> findByPacienteIdOrderByDataHoraAgendamentoDesc(Long pacienteId);

    List<HistoricoMedico> findByMedicoIdOrderByDataHoraAgendamentoDesc(Long medicoId);

    List<HistoricoMedico> findByHospitalIdOrderByDataHoraAgendamentoDesc(Long hospitalId);

    @Query("SELECT h FROM HistoricoMedico h WHERE h.pacienteId = :pacienteId AND h.dataHoraAgendamento >= :dataInicio ORDER BY h.dataHoraAgendamento DESC")
    List<HistoricoMedico> findConsultasFuturasPorPaciente(@Param("pacienteId") Long pacienteId, @Param("dataInicio") LocalDateTime dataInicio);

    @Query("SELECT h FROM HistoricoMedico h WHERE h.pacienteId = :pacienteId AND h.dataHoraAgendamento < :dataAtual ORDER BY h.dataHoraAgendamento DESC")
    List<HistoricoMedico> findConsultasPassadasPorPaciente(@Param("pacienteId") Long pacienteId, @Param("dataAtual") LocalDateTime dataAtual);

    List<HistoricoMedico> findByPacienteIdAndStatusAgendamentoOrderByDataHoraAgendamentoDesc(Long pacienteId, EStatusAgendamento status);

    @Query("SELECT h FROM HistoricoMedico h WHERE h.pacienteId = :pacienteId AND h.dataHoraAgendamento BETWEEN :dataInicio AND :dataFim ORDER BY h.dataHoraAgendamento DESC")
    List<HistoricoMedico> findConsultasPorPeriodo(@Param("pacienteId") Long pacienteId, @Param("dataInicio") LocalDateTime dataInicio, @Param("dataFim") LocalDateTime dataFim);

    @Query("SELECT h FROM HistoricoMedico h WHERE h.medicoId = :medicoId AND h.dataHoraAgendamento >= :dataInicio ORDER BY h.dataHoraAgendamento ASC")
    List<HistoricoMedico> findConsultasFuturasPorMedico(@Param("medicoId") Long medicoId, @Param("dataInicio") LocalDateTime dataInicio);
}
