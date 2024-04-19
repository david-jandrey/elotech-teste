package com.elotech.main.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "pessoa")
public class PessoaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private  String nome;
    private  String cpf;
    private Date dataNascimento;

    @OneToMany(mappedBy = "pessoaEntity", cascade = CascadeType.ALL)
    private List<ListaContatoEntity> listaContatos = new ArrayList<>();

}
