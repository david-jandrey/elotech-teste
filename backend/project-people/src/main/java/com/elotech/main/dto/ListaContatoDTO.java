package com.elotech.main.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListaContatoDTO {

    private UUID id;

    @NotBlank(message = "O nome  é obrigatório")
    private String nome;

    @NotBlank(message = "O telefone  é obrigatório")
    private String telefone;

    @NotBlank(message = "O email é obrigatório")
    @Email(message = "O email: [email] não é válido")
    private String email;
}
