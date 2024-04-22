import { Component } from '@angular/core';
import { CommonModule  } from '@angular/common';
import { FormControl, FormGroup, FormArray, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';

interface PessoaForm {
  nome: FormControl,
  cpf: FormControl,
  dataNascimento: FormControl,
}

interface ContatoForm {
  nome: FormControl,
  telefone: FormControl,
  email: FormControl,
}

@Component({
  selector: 'app-cadastro-pessoa',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './cadastro-pessoa.component.html',
  
  styleUrls: ['./cadastro-pessoa.component.scss']
})

export class CadastroPessoaComponent {
  pessoaForm!: FormGroup<PessoaForm>;
  contatoForm!: FormGroup<ContatoForm>;
  listaContatos: ContatoForm[] = [];

  constructor(
    
    private http: HttpClient,
    private toastService: ToastrService
  ) {
    this.pessoaForm = new FormGroup({
      nome: new FormControl('', [Validators.required]),
      cpf: new FormControl('', [Validators.required]),
      dataNascimento: new FormControl('', [Validators.required])
    });
    this.contatoForm = new FormGroup({
      nome: new FormControl('', [Validators.required]),
      telefone: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required])
    });
  }

  adicionarContato() {
    if (this.contatoForm.valid) {
      const novoContato: ContatoForm = {
        nome: this.contatoForm.value.nome,
        telefone: this.contatoForm.value.telefone,
        email: this.contatoForm.value.email
      };
      this.listaContatos.push(novoContato);
      this.contatoForm.reset();
    }
  }

  removerContato(index: number) {
    this.listaContatos.splice(index, 1);
  }

  formatarCPF(cpf: string): string {
    // Remove caracteres não numéricos
    cpf = cpf.replace(/\D/g, '');
    
    // Verifica se o CPF tem 11 dígitos
    if (cpf.length !== 11) {
        throw new Error('CPF inválido');
    }

    return cpf;
}

  submitCadastro() {
    if (this.listaContatos.length <= 0){
      this.toastService.error("Necessário informar ao menos um contato!");
      return
    }
    if (!this.pessoaForm.valid) {
      this.toastService.error("Preencha todos os campos por favor!");
      return
    }

    const dadosCadastro = {
      nome: this.pessoaForm.value.nome,
      cpf: this.formatarCPF(this.pessoaForm.value.cpf),
      dataNascimento: this.pessoaForm.value.dataNascimento,
      listaContatos: this.listaContatos 
    };
    console.log(dadosCadastro)
  
    this.http.post<any>('http://localhost:8080/pessoa', dadosCadastro)
      .subscribe(
        (response) => {
          this.toastService.success("Cadastro feito com sucesso!")
        },
        (error) => {
          console.error('Erro durante a solicitação HTTP:', error);
          this.toastService.error("Erro ao cadastrar. verifique se os dados estão corretos!");
        }
      );
  }
  

}
