package com.elotech.main.services;

import com.elotech.main.repositories.ListaContatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LIstaContatoService {

    @Autowired
    private ListaContatoRepository listaContatoRepository;

    public void deletarListaContato(UUID id) {
        listaContatoRepository.deleteById(id);
    }
}
