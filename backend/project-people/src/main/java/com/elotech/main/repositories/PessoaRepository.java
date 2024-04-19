package com.elotech.main.repositories;

import com.elotech.main.entities.PessoaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PessoaRepository extends JpaRepository<PessoaEntity, UUID> {

    Optional<PessoaEntity> findById(UUID id);

    Page<PessoaEntity> findByNomeContaining(String nome, Pageable pageable);

    Page<PessoaEntity> findByCpfContaining(String cpf, Pageable pageable);

    Page<PessoaEntity> findByNomeContainingAndCpfContaining(String nome, String cpf, Pageable pageable);
}
