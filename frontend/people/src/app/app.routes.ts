import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CadastroPessoaComponent } from './cadastro-pessoa/cadastro-pessoa.component';
import { ConsultaPessoaComponent } from './consulta-pessoa/consulta-pessoa.component';
import { EdicaoPessoaComponent } from './edicao-pessoa/edicao-pessoa.component';

export const routes: Routes = [
  { path: '', redirectTo: '/cadastro', pathMatch: 'full' },
  { path: 'cadastro', component: CadastroPessoaComponent },
  { path: 'consulta', component: ConsultaPessoaComponent },
  { path: 'edicao', component: EdicaoPessoaComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }