import { Component } from '@angular/core';
import { CommonModule  } from '@angular/common';
import { FormControl, FormGroup, FormArray, ReactiveFormsModule, Validators } from '@angular/forms';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

interface PessoaForm {
  id: FormControl,
  nome: FormControl,
  cpf: FormControl,
  dataNascimento: FormControl,
}

@Component({
  selector: 'app-consulta-pessoa',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    CommonModule
  ],
  templateUrl: './consulta-pessoa.component.html',
  
  styleUrls: ['./consulta-pessoa.component.scss']
})

export class ConsultaPessoaComponent {
  pessoaForm!: FormGroup<PessoaForm>;
  listaPessoas: PessoaForm[] = [];
  responseData: any;
  totalPaginas = 0;
  paginaAtual = 1;


  constructor(
    private router: Router,
    private http: HttpClient,
    private toastService: ToastrService
  ) {
    this.pessoaForm = new FormGroup({
      id: new FormControl('', [Validators.required]),
      nome: new FormControl('', [Validators.required]),
      cpf: new FormControl('', [Validators.required]),
      dataNascimento: new FormControl('', [Validators.required])
    });
  }

  ngOnInit(): void {
   this.buscarPessoas();
  }

  formatarCPF(cpf: string): string {
    if(this.pessoaForm.value.cpf.trim() === ''){
      return cpf
    }
    cpf = cpf.replace(/\D/g, '');
    
    // Verifica se o CPF tem 11 dígitos
    if (cpf.length !== 11) {
        throw new Error('CPF inválido');
    }

    return cpf;
  }
  buscarPessoas() {
    const nome = this.pessoaForm.value.nome;
    const cpf = this.formatarCPF(this.pessoaForm.value.cpf);

    let url = `http://localhost:8080/pessoa/page?`;

    if (nome) {
      url += `nome=${nome}&`;
    }

    if (cpf) {
      url += `cpf=${cpf}&`;
    }

    this.http.get<any[]>(url)
      .subscribe(
        (response) => {
          this.responseData = response;
          this.listaPessoas = this.responseData.content;
          this.totalPaginas = this.responseData.totalPages;
          this.formataData()
          console.log(this.listaPessoas )
        },
        (error) => {
          console.error('Erro durante a solicitação HTTP:', error);
          this.toastService.error("Erro ao buscar pessoas. Tente novamente mais tarde.");
        }
      );
  }

  proximaPagina() {
    if (this.paginaAtual < this.totalPaginas) {
      this.paginaAtual++;
      this.buscarPessoas();
    }
  }

  paginaAnterior() {
    if (this.paginaAtual > 1) {
      this.paginaAtual--;
      this.buscarPessoas();
    }
  }

  removerPessoa(id: any) {
    if (confirm('Tem certeza que deseja deletar esta pessoa?')) {
      this.http.delete(`http://localhost:8080/pessoa/${id}`)
        .subscribe(
          () => {
            this.listaPessoas = this.listaPessoas.filter(pessoa => pessoa.id !== id);
            this.toastService.success('Pessoa removida com sucesso!');
          },
          (error) => {
            this.toastService.error('Erro ao deletar pessoa. Tente novamente mais tarde.');
            console.error('Erro ao deletar pessoa:', error);
          }
        );
    }
  }

  formataData(){
    this.listaPessoas.forEach((pessoa: any) => {
      pessoa.dataNascimento = pessoa.dataNascimento.substring(0, 10);
    });
  }

  editarPessoa(id: any) {
    localStorage.setItem('pessoaId', id);
    this.router.navigate(['/edicao']);
  }

  

}
