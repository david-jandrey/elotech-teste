import { Component  } from '@angular/core';
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

interface ContatoFormUpdate {
  id: FormControl,
  nome: FormControl,
  telefone: FormControl,
  email: FormControl,
}

@Component({
  selector: 'app-edicao-pessoa',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './edicao-pessoa.component.html',
  
  styleUrls: ['./edicao-pessoa.component.scss']
})

export class EdicaoPessoaComponent {
  pessoaForm!: FormGroup<PessoaForm>;
  contatoForm!: FormGroup<ContatoForm>;  
  contatoFormUpdate!: FormGroup<ContatoFormUpdate>;
  listaContatos: ContatoFormUpdate[] = [];
  id: String ='';
  pessoaData: any;

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
    this.contatoFormUpdate = new FormGroup({
      id: new FormControl('', [Validators.required]),
      nome: new FormControl('', [Validators.required]),
      telefone: new FormControl('', [Validators.required]),
      email: new FormControl('', [Validators.required])
    });
  }

  ngOnInit() {
    this.id = localStorage.getItem('pessoaId')as string;
    this.carregaDados();
  }
  carregaDados(){

    this.http.get<any>(`http://localhost:8080/pessoa/${this.id}`).subscribe(
      (response) => {
        this.pessoaData = response;

        this.pessoaForm.patchValue({
          nome: this.pessoaData.nome,
          cpf: this.pessoaData.cpf,
          dataNascimento: this.pessoaData.dataNascimento.substring(0, 10)
        });

        this.listaContatos = this.pessoaData.listaContatos

       
      },
      (error) => {
        this.toastService.error("Erro ao buscar informações do usuário!");
      }
    );
  }

  adicionarContato() {
    if (!(this.contatoForm.valid)) {
      this.toastService.error('Favor preencher todos os dados do contato!');
      return
    }
    const novoContato  = {
      nome: this.contatoForm.value.nome,
      telefone: this.contatoForm.value.telefone,
      email: this.contatoForm.value.email
    }

    this.http.post<any>(`http://localhost:8080/lista-contato/pessoa/${this.id}`,novoContato)
        .subscribe(
          (response) => {
            this.listaContatos = response;
            this.toastService.success('Contato criado com sucesso!');
          },
          (error) => {
            this.toastService.error('Erro ao criar contato. Tente novamente mais tarde.');
            console.error('Erro ao criar contato:', error);
          }
        );
    
    this.contatoForm.reset();
  }

  removerContato(id: any) {
    if (this.listaContatos.length === 1) {
      this.toastService.error('Você precisa ter pelo menos um contato!');
      return;
    }

    if (confirm('Tem certeza que deseja deletar este contato?')) {
      this.http.delete(`http://localhost:8080/lista-contato/${id}`)
        .subscribe(
          () => {
            this.listaContatos = this.listaContatos.filter(contato => contato.id !== id);
            this.toastService.success('Contato removida com sucesso!');
          },
          (error) => {
            this.toastService.error('Erro ao deletar contato. Tente novamente mais tarde.');
            console.error('Erro ao remover contato:', error);
          }
        );
    }
  }

  formatarCPF(cpf: string): string {
    cpf = cpf.replace(/\D/g, '');
    
    if (cpf.length !== 11) {
        throw new Error('CPF inválido');
    }

    return cpf;
}

AtualizaPessoa() {
    if (!this.pessoaForm.valid) {
      this.toastService.error("Preencha todos os campos por favor!");
      return
    }

    const dados = {
      nome: this.pessoaForm.value.nome,
      cpf: this.formatarCPF(this.pessoaForm.value.cpf),
      dataNascimento: this.pessoaForm.value.dataNascimento
    };
  
    this.http.put<any>(`http://localhost:8080/pessoa/${this.id}`,dados)
        .subscribe(
        (response) => {
          this.toastService.success("Dados Atualizados com sucesso!")
        },
        (error) => {
          console.error('Erro durante a solicitação HTTP:', error);
          this.toastService.error("Erro ao atualizar cadastro de pessoa. verifique se os dados estão corretos!");
        }
      );
  }

  salvarContato(id: any) {
    const nome = (document.getElementById('nome' + id) as HTMLInputElement).value;
    const telefone = (document.getElementById('telefone' + id) as HTMLInputElement).value;
    const email = (document.getElementById('email' + id) as HTMLInputElement).value;

    const dados = {
      id: id,
      nome: nome,
      telefone: telefone,
      email: email
    };

    this.http.put<any>(`http://localhost:8080/lista-contato/${id}`, dados).subscribe(
        (response) => {
          this.toastService.success("contato atualizado com feito com sucesso!")
        },
        (error) => {
          console.error('Erro durante a solicitação HTTP:', error);
          this.toastService.error("Erro ao atualizar contato. verifique se os dados estão corretos!");
        }
      );
  
    
  
  }

  
  

}
