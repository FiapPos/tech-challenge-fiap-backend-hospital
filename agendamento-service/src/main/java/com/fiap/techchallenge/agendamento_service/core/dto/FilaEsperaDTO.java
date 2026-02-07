package com.fiap.techchallenge.agendamento_service.core.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fiap.techchallenge.agendamento_service.core.entity.FilaEspera;
import com.fiap.techchallenge.agendamento_service.core.enums.EStatusFilaEspera;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilaEsperaDTO implements Serializable {

    private Long id;
    private Long pacienteId;
    private Long especialidadeId;
    private Long hospitalId;
    private Long medicoId;
    private String nomePaciente;
    private String emailPaciente;
    private String telegramId;
    private LocalDateTime dataHoraDesejadaInicio;
    private LocalDateTime dataHoraDesejadaFim;
    private Boolean idoso;
    private Boolean gestante;
    private Boolean pcd;
    private Integer pesoPrioridade;
    private EStatusFilaEspera status;
    private LocalDateTime criadoEm;
    private LocalDateTime notificadoEm;
    private String urlAceitar;
    private String urlRecusar;

    public void setDataHoraPreferida(LocalDateTime dataHoraPreferida) {
        if (dataHoraPreferida != null) {
            this.dataHoraDesejadaInicio = dataHoraPreferida;
            this.dataHoraDesejadaFim = dataHoraPreferida;
        }
    }

    public FilaEsperaDTO(FilaEspera entity) {
        this.id = entity.getId();
        this.pacienteId = entity.getPacienteId();
        this.especialidadeId = entity.getEspecialidadeId();
        this.hospitalId = entity.getHospitalId();
        this.medicoId = entity.getMedicoId();
        this.nomePaciente = entity.getNomePaciente();
        this.emailPaciente = entity.getEmailPaciente();
        this.telegramId = entity.getTelegramId();
        this.dataHoraDesejadaInicio = entity.getDataHoraDesejadaInicio();
        this.dataHoraDesejadaFim = entity.getDataHoraDesejadaFim();
        this.idoso = entity.getIdoso();
        this.gestante = entity.getGestante();
        this.pcd = entity.getPcd();
        this.pesoPrioridade = entity.getPesoPrioridade();
        this.status = entity.getStatus();
        this.criadoEm = entity.getCriadoEm();
        this.notificadoEm = entity.getNotificadoEm();

        if (entity.getStatus() == EStatusFilaEspera.NOTIFICADO && entity.getId() != null) {
            this.urlAceitar = "/api/fila-espera/" + entity.getId() + "/aceitar";
            this.urlRecusar = "/api/fila-espera/" + entity.getId() + "/recusar";
        }
    }

    public FilaEspera toEntity() {
        return FilaEspera.builder()
                .id(this.id)
                .pacienteId(this.pacienteId)
                .especialidadeId(this.especialidadeId)
                .hospitalId(this.hospitalId)
                .medicoId(this.medicoId)
                .nomePaciente(this.nomePaciente)
                .emailPaciente(this.emailPaciente)
                .telegramId(this.telegramId)
                .dataHoraDesejadaInicio(this.dataHoraDesejadaInicio)
                .dataHoraDesejadaFim(this.dataHoraDesejadaFim)
                .idoso(this.idoso != null ? this.idoso : false)
                .gestante(this.gestante != null ? this.gestante : false)
                .pcd(this.pcd != null ? this.pcd : false)
                .status(this.status != null ? this.status : EStatusFilaEspera.AGUARDANDO)
                .build();
    }

    public String getDataHoraInicioFormatada() {
        return dataHoraDesejadaInicio != null
                ? dataHoraDesejadaInicio.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : null;
    }

    public String getDataHoraFimFormatada() {
        return dataHoraDesejadaFim != null
                ? dataHoraDesejadaFim.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
                : null;
    }
}
