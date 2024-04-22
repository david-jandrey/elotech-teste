package com.elotech.main.services;

import com.elotech.main.dto.ListaContatoDTO;
import com.elotech.main.dto.PessoaDTO;
import com.elotech.main.entities.ListaContatoEntity;
import com.elotech.main.entities.PessoaEntity;
import com.elotech.main.repositories.ListaContatoRepository;
import com.elotech.main.repositories.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LIstaContatoService {

    @Autowired
    private ListaContatoRepository listaContatoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public void deletarListaContato(UUID id) {
        listaContatoRepository.deleteById(id);
    }

    public ListaContatoEntity atualizarListaContato(UUID id, ListaContatoDTO listaContatoDTO) {


        ListaContatoEntity listaContatoExistente = listaContatoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Lista de Contato não encontrada com o ID: " + id));

        listaContatoExistente.setNome(listaContatoDTO.getNome());
        listaContatoExistente.setTelefone(listaContatoDTO.getTelefone());
        listaContatoExistente.setEmail(listaContatoDTO.getEmail());

        return listaContatoRepository.save(listaContatoExistente);
    }

    public List<ListaContatoEntity> criaContato(UUID pessoaId, ListaContatoDTO listaContatoDTO) {

        PessoaEntity pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com o ID: " + pessoaId));

        ListaContatoEntity novoContato = ListaContatoEntity.builder()
                .nome(listaContatoDTO.getNome())
                .telefone(listaContatoDTO.getTelefone())
                .email(listaContatoDTO.getEmail())
                .pessoaEntity(pessoa)
                .build();

        listaContatoRepository.save(novoContato);

        return   listaContatoRepository.findByPessoaEntityId(pessoaId);
    }
}
