package com.elotech.main.services;

import com.elotech.main.dto.PessoaDTO;
import com.elotech.main.entities.ListaContatoEntity;
import com.elotech.main.entities.PessoaEntity;
import com.elotech.main.repositories.PessoaRepository;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PessoaService {

    @Autowired
    private  PessoaRepository pessoaRepository;

    public PessoaEntity criarPessoa(PessoaDTO pessoaDTO) {

        this.ValidaListaContato(pessoaDTO);
        this.ValidaCPF(pessoaDTO.getCpf());
        this.validaDataNascimento(pessoaDTO.getDataNascimento());

        PessoaEntity pessoa = PessoaEntity.builder()
                .nome(pessoaDTO.getNome())
                .cpf(pessoaDTO.getCpf())
                .dataNascimento(pessoaDTO.getDataNascimento())
                .build();

        List<ListaContatoEntity> listaContatos = pessoaDTO.getListaContatos().stream()
                .map(contatoDTO -> {
                    ListaContatoEntity contato = ListaContatoEntity.builder()
                            .nome(contatoDTO.getNome())
                            .telefone(contatoDTO.getTelefone())
                            .email(contatoDTO.getEmail())
                            .pessoaEntity(pessoa)
                            .build();
                    return contato;
                })
                .collect(Collectors.toList());

        pessoa.setListaContatos(listaContatos);



        return this.pessoaRepository.save(pessoa);
    }

    public Optional<PessoaEntity> buscarPessoaPorId(UUID id) {

        Optional<PessoaEntity> pessoaOptional = pessoaRepository.findById(id);

        if (pessoaOptional.isPresent()) {
            return Optional.of(pessoaOptional.get());
        } else {
            throw new RuntimeException("Pessoa não encontrada com o ID fornecido");
        }
    }

    public Page<PessoaEntity> buscarPessoasPaginadas(int pagina, int tamanhoPagina, String nome, String cpf) {
        Pageable pageable = PageRequest.of(pagina, tamanhoPagina);
        if (nome != null && cpf != null) {
            return pessoaRepository.findByNomeContainingAndCpfContaining(nome, cpf, pageable);
        } else if (nome != null) {
            return pessoaRepository.findByNomeContaining(nome, pageable);
        } else if (cpf != null) {
            return pessoaRepository.findByCpfContaining(cpf, pageable);
        } else {
            return pessoaRepository.findAll(pageable);
        }
    }

    public void deletarPessoa(UUID id) {
        pessoaRepository.deleteById(id);
    }

    public PessoaEntity atualizarPessoa(UUID id, PessoaDTO pessoaDTO) {

        this.ValidaCPF(pessoaDTO.getCpf());
        this.validaDataNascimento(pessoaDTO.getDataNascimento());

        PessoaEntity pessoaExistente = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com o ID: " + id));

        pessoaExistente.setNome(pessoaDTO.getNome());
        pessoaExistente.setCpf(pessoaDTO.getCpf());
        pessoaExistente.setDataNascimento(pessoaDTO.getDataNascimento());

        return pessoaRepository.save(pessoaExistente);
    }

    public  void ValidaListaContato(PessoaDTO pessoaDTO) {

        if (pessoaDTO.getListaContatos() == null || pessoaDTO.getListaContatos().isEmpty()) {
            throw new RuntimeException("A pessoa deve ter pelo menos um contato.");
        }
    }
    public  void ValidaCPF(String cpf) {

        CPFValidator cpfValidator = new CPFValidator();
        cpfValidator.initialize(null);

        if (!cpfValidator.isValid(cpf, null)) {
            throw new RuntimeException("CPF inválido!");
        }
    }

    public static void validaDataNascimento(Date dataNascimento) {
        LocalDate hoje = LocalDate.now();
        LocalDate dataNascimentoLocalDate = dataNascimento.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        if (dataNascimentoLocalDate.isAfter(hoje)) {
            throw new RuntimeException("A data de nascimento não pode ser uma data futura.");
        }
    }
}