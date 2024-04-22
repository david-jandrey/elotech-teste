package com.elotech.main.controllers;

import com.elotech.main.dto.PessoaDTO;
import com.elotech.main.entities.PessoaEntity;
import com.elotech.main.services.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("")
    public ResponseEntity<Object> criarPessoa(@Valid @RequestBody PessoaDTO pessoaDTO){
        try{
            PessoaEntity pessoaSalva = pessoaService.criarPessoa(pessoaDTO);
            return ResponseEntity.ok().body(pessoaSalva);
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{id}")
    public ResponseEntity<Object> buscarPessoaPorId(@PathVariable UUID id) {
        try {
            Optional<PessoaEntity> pessoa = pessoaService.buscarPessoaPorId(id);
            return ResponseEntity.ok().body(pessoa);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/page")
    public ResponseEntity<Page<PessoaEntity>> buscarPessoasPaginadas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String cpf) {
        try {
            Page<PessoaEntity> pessoasPage = pessoaService.buscarPessoasPaginadas(page, size, nome, cpf);
            return ResponseEntity.ok().body(pessoasPage);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarPessoa(@PathVariable UUID id){
        try{
            pessoaService.deletarPessoa(id);
            return ResponseEntity.noContent().build();
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarPessoa(@PathVariable UUID id, @Valid @RequestBody PessoaDTO pessoaDTO) {
        try {
            PessoaEntity pessoaAtualizada = pessoaService.atualizarPessoa(id, pessoaDTO);
            return ResponseEntity.ok().body(pessoaAtualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
