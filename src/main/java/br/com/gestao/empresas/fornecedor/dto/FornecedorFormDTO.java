package br.com.gestao.empresas.fornecedor.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FornecedorFormDTO {

    @NotBlank(message = "O nome do fornecedor é obrigatório.")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres.")
    private String nome;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}