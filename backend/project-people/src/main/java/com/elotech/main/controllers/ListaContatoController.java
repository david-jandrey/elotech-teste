package com.elotech.main.controllers;

import com.elotech.main.services.LIstaContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
