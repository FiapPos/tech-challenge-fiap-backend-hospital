package com.fiap.techchallenge.hospital_service.core.controller;

import com.fiap.techchallenge.hospital_service.core.dto.HospitalRequest;
import com.fiap.techchallenge.hospital_service.core.dto.HospitalResponse;
import com.fiap.techchallenge.hospital_service.core.service.HospitalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hospitais")
@RequiredArgsConstructor
public class HospitalController {

    private final HospitalService hospitalService;

    @PostMapping
    public ResponseEntity<HospitalResponse> criarHospital(@Valid @RequestBody HospitalRequest request) {
        HospitalResponse response = hospitalService.criarHospital(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<HospitalResponse>> listarHospitais() {
        List<HospitalResponse> hospitais = hospitalService.listarTodosHospitais();
        return ResponseEntity.ok(hospitais);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HospitalResponse> buscarHospitalPorId(@PathVariable Long id) {
        HospitalResponse hospital = hospitalService.buscarPorId(id);
        return ResponseEntity.ok(hospital);
    }

    @PutMapping("/{id}")
    public ResponseEntity<HospitalResponse> atualizarHospital(@PathVariable Long id,
                                                             @Valid @RequestBody HospitalRequest request) {
        HospitalResponse response = hospitalService.atualizarHospital(id, request);
        return ResponseEntity.ok(response);
    }

}
