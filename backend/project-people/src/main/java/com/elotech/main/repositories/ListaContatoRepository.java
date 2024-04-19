package com.elotech.main.repositories;

import com.elotech.main.entities.ListaContatoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ListaContatoRepository extends JpaRepository<ListaContatoEntity, UUID> {

}
