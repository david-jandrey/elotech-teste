package com.elotech.main.services;


import com.elotech.main.repositories.ListaContatoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class LIstaContatoServiceTest {

    @InjectMocks
    private LIstaContatoService listaContatoService;

    @Mock
    private ListaContatoRepository listaContatoRepository;

    @Test
    void testDeletarListaContato() {

        UUID idListaContato = UUID.randomUUID();

        listaContatoService.deletarListaContato(idListaContato);

        verify(listaContatoRepository, times(1)).deleteById(idListaContato);
    }
}
