package com.fiap.techchallenge.hospital_service.core.service;

import com.fiap.techchallenge.hospital_service.core.dto.HospitalRequest;
import com.fiap.techchallenge.hospital_service.core.dto.HospitalResponse;
import com.fiap.techchallenge.hospital_service.core.entity.Hospital;
import com.fiap.techchallenge.hospital_service.core.mapper.HospitalMapper;
import com.fiap.techchallenge.hospital_service.core.repository.HospitalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final HospitalMapper hospitalMapper;

    public HospitalResponse criarHospital(HospitalRequest request) {
        Hospital hospital = hospitalMapper.toEntity(request);
        Hospital hospitalSalvo = hospitalRepository.save(hospital);
        return hospitalMapper.toResponse(hospitalSalvo);
    }

    @Transactional(readOnly = true)
    public List<HospitalResponse> listarTodosHospitais() {
        List<Hospital> hospitais = hospitalRepository.findAllAtivos();
        return hospitais.stream()
                .map(hospitalMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public HospitalResponse buscarPorId(Long id) {
        Hospital hospital = hospitalRepository.findByIdAndAtivo(id)
                .orElseThrow(() -> new RuntimeException("Hospital não encontrado com ID: " + id));
        return hospitalMapper.toResponse(hospital);
    }

    public HospitalResponse atualizarHospital(Long id, HospitalRequest request) {
        Hospital hospital = hospitalRepository.findByIdAndAtivo(id)
                .orElseThrow(() -> new RuntimeException("Hospital não encontrado com ID: " + id));

        hospitalMapper.updateEntity(hospital, request);
        Hospital hospitalAtualizado = hospitalRepository.save(hospital);
        return hospitalMapper.toResponse(hospitalAtualizado);
    }

    public void deletarHospital(Long id) {
        Hospital hospital = hospitalRepository.findByIdAndAtivo(id)
                .orElseThrow(() -> new RuntimeException("Hospital não encontrado com ID: " + id));

        hospital.setAtivo(false);
        hospitalRepository.save(hospital);
    }

    @Transactional(readOnly = true)
    public List<HospitalResponse> buscarPorNome(String nome) {
        List<Hospital> hospitais = hospitalRepository.findByNomeContainingIgnoreCaseAndAtivo(nome);
        return hospitais.stream()
                .map(hospitalMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HospitalResponse> buscarPorEspecialidade(String especialidade) {
        List<Hospital> hospitais = hospitalRepository.findByEspecialidadesContainingIgnoreCaseAndAtivo(especialidade);
        return hospitais.stream()
                .map(hospitalMapper::toResponse)
                .collect(Collectors.toList());
    }
}
