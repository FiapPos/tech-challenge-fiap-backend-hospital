package com.fiap.techchallenge.hospital_service.core.repository;

import com.fiap.techchallenge.hospital_service.core.entity.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    @Query("SELECT h FROM Hospital h WHERE h.ativo = true")
    List<Hospital> findAllAtivos();

    @Query("SELECT h FROM Hospital h WHERE h.id = :id AND h.ativo = true")
    Optional<Hospital> findByIdAndAtivo(@Param("id") Long id);

    @Query("SELECT h FROM Hospital h WHERE h.nome LIKE %:nome% AND h.ativo = true")
    List<Hospital> findByNomeContainingIgnoreCaseAndAtivo(@Param("nome") String nome);

    @Query("SELECT h FROM Hospital h WHERE h.especialidades LIKE %:especialidade% AND h.ativo = true")
    List<Hospital> findByEspecialidadesContainingIgnoreCaseAndAtivo(@Param("especialidade") String especialidade);
}
