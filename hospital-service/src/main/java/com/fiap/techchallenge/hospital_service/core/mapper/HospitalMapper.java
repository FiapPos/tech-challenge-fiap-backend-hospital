package com.fiap.techchallenge.hospital_service.core.mapper;

import com.fiap.techchallenge.hospital_service.core.dto.HospitalRequest;
import com.fiap.techchallenge.hospital_service.core.dto.HospitalResponse;
import com.fiap.techchallenge.hospital_service.core.entity.Hospital;
import org.springframework.stereotype.Component;

@Component
public class HospitalMapper {

    public Hospital toEntity(HospitalRequest request) {
        Hospital hospital = new Hospital();
        hospital.setNome(request.getNome());
        hospital.setEndereco(request.getEndereco());
        hospital.setTelefone(request.getTelefone());
        hospital.setEmail(request.getEmail());
        hospital.setEspecialidades(request.getEspecialidades());
        hospital.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);
        return hospital;
    }

    public HospitalResponse toResponse(Hospital hospital) {
        HospitalResponse response = new HospitalResponse();
        response.setId(hospital.getId());
        response.setNome(hospital.getNome());
        response.setEndereco(hospital.getEndereco());
        response.setTelefone(hospital.getTelefone());
        response.setEmail(hospital.getEmail());
        response.setEspecialidades(hospital.getEspecialidades());
        response.setAtivo(hospital.getAtivo());
        response.setCriadoEm(hospital.getCriadoEm());
        response.setAtualizadoEm(hospital.getAtualizadoEm());
        return response;
    }

    public void updateEntity(Hospital hospital, HospitalRequest request) {
        hospital.setNome(request.getNome());
        hospital.setEndereco(request.getEndereco());
        hospital.setTelefone(request.getTelefone());
        hospital.setEmail(request.getEmail());
        hospital.setEspecialidades(request.getEspecialidades());
        if (request.getAtivo() != null) {
            hospital.setAtivo(request.getAtivo());
        }
    }
}
