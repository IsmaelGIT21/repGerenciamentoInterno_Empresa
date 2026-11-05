package br.com.gestao.empresas.local.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LocalFormDTO {

    @NotBlank(message = "O nome do local é obrigatório.")
    @Size(max = 255, message = "O nome deve ter no máximo 255 caracteres.")
    private String nome;

    @NotBlank(message = "Selecione o tipo de local.")
    private String tipo;

    @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres.")
    private String endereco;

    private Boolean ativo = true;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}