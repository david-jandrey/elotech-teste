package com.elotech.main.services;

import com.elotech.main.dto.ListaContatoDTO;
import com.elotech.main.dto.PessoaDTO;
import com.elotech.main.entities.ListaContatoEntity;
import com.elotech.main.entities.PessoaEntity;
import com.elotech.main.repositories.PessoaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PessoaServiceTest {

    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    @Test
    void testValidaCPF() {
        String cpfValido = "49761482030";
        assertDoesNotThrow(() -> pessoaService.ValidaCPF(cpfValido));

        String cpfInvalido = "12345678900";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> pessoaService.ValidaCPF(cpfInvalido));
        assertEquals("CPF inválido!", exception.getMessage());
    }

    @Test
    void testValidaDataNascimento() {
        Date dataNascimentoPassado = Date.from(LocalDate.of(2002, 3, 26).atStartOfDay(ZoneId.systemDefault()).toInstant());
        assertDoesNotThrow(() -> pessoaService.validaDataNascimento(dataNascimentoPassado));

        Date dataNascimentoFuturo = Date.from(LocalDate.now().plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> pessoaService.validaDataNascimento(dataNascimentoFuturo));
        assertEquals("A data de nascimento não pode ser uma data futura.", exception.getMessage());
    }

    @Test
    void testValidaListaContato() {
        List<ListaContatoDTO> listaContatosNaoVazia = new ArrayList<>();
        listaContatosNaoVazia.add(new ListaContatoDTO(null, "João", "123456789", "joao@gmail.com"));
        PessoaDTO pessoaDTO1 = new PessoaDTO("david", "49761482030", null, listaContatosNaoVazia);
        assertDoesNotThrow(() -> pessoaService.ValidaListaContato(pessoaDTO1));

        List<ListaContatoDTO> listaContatosVazia = new ArrayList<>();
        PessoaDTO pessoaDTO2 = new PessoaDTO("pedro", "54441522070", null, listaContatosVazia);
        RuntimeException exception = assertThrows(RuntimeException.class, () -> pessoaService.ValidaListaContato(pessoaDTO2));
        assertEquals("A pessoa deve ter pelo menos um contato.", exception.getMessage());
    }

    @Test
    void testCriarPessoaComSucesso() {

        List<ListaContatoDTO> listaContatos = new ArrayList<>();
        listaContatos.add(new ListaContatoDTO(null, "david", "123456789", "david@gmail.com"));
        PessoaDTO pessoaDTO = new PessoaDTO("matheus", "49761482030", new Date(), listaContatos);

        assertDoesNotThrow(() -> pessoaService.criarPessoa(pessoaDTO));

        verify(pessoaRepository, times(1)).save(any());
    }

    @Test
    void testBuscarPessoaPorIdExistente() {
        UUID idExistente = UUID.randomUUID();
        PessoaEntity pessoa = new PessoaEntity();
        pessoa.setId(idExistente);

        when(pessoaRepository.findById(idExistente)).thenReturn(Optional.of(pessoa));

        Optional<PessoaEntity> pessoaEncontrada = pessoaService.buscarPessoaPorId(idExistente);

        assertTrue(pessoaEncontrada.isPresent());
        assertEquals(idExistente, pessoaEncontrada.get().getId());
    }

    @Test
    void testAtualizarPessoaEListaContatoComSucesso() {
        UUID idExistente = UUID.randomUUID();
        PessoaEntity pessoaExistente = new PessoaEntity();
        pessoaExistente.setId(idExistente);
        pessoaExistente.setNome("david jandrey");
        pessoaExistente.setCpf("78475069045");
        pessoaExistente.setDataNascimento(new Date());
        List<ListaContatoEntity> listaContatos = new ArrayList<>();
        ListaContatoEntity contatoExistente = new ListaContatoEntity();
        contatoExistente.setId(UUID.randomUUID());
        contatoExistente.setNome("blue pen ");
        contatoExistente.setTelefone("123456789");
        contatoExistente.setEmail("blue.pen@gmail.com");
        listaContatos.add(contatoExistente);
        pessoaExistente.setListaContatos(listaContatos);

        when(pessoaRepository.findById(idExistente)).thenReturn(Optional.of(pessoaExistente));

        PessoaDTO pessoaDTO = new PessoaDTO("new david jandrey", "78475069045", new Date(), Collections.emptyList());
        List<ListaContatoDTO> listaContatosDTO = new ArrayList<>();
        listaContatosDTO.add(new ListaContatoDTO(contatoExistente.getId(), "Novo Blue pen", "987654321", "blue.pen@gmail.com"));
        pessoaDTO.setListaContatos(listaContatosDTO);

        pessoaService.atualizarPessoaEListaContato(idExistente, pessoaDTO);

        verify(pessoaRepository, times(1)).save(any());
    }

}
