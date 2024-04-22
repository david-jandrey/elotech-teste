package com.elotech.main.controllers;

import com.elotech.main.dto.ListaContatoDTO;
import com.elotech.main.dto.PessoaDTO;
import com.elotech.main.entities.ListaContatoEntity;
import com.elotech.main.entities.PessoaEntity;
import com.elotech.main.services.LIstaContatoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/lista-contato")
public class ListaContatoController {

    @Autowired
    private LIstaContatoService lIstaContatoService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deletarContato(@PathVariable UUID id){
        try{
            lIstaContatoService.deletarListaContato(id);
            return ResponseEntity.noContent().build();
        }catch(Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> atualizarListaContato(@PathVariable UUID id, @Valid @RequestBody ListaContatoDTO listaContatoDTO) {
        try {
            ListaContatoEntity listaContatoAtualizada = lIstaContatoService.atualizarListaContato(id, listaContatoDTO);
            return ResponseEntity.ok().body(listaContatoAtualizada);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("pessoa/{id}")
    public ResponseEntity<Object> criaContato(@PathVariable UUID id, @Valid @RequestBody ListaContatoDTO listaContatoDTO) {
        try {
            List<ListaContatoEntity> listaContato = lIstaContatoService.criaContato(id, listaContatoDTO);
            return ResponseEntity.ok().body(listaContato);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
