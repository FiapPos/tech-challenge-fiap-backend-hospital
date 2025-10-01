package com.fiap.techchallenge.agendamento_service.repository;
import com.fiap.techchallenge.agendamento_service.entity.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}