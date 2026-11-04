package br.com.gestao.empresas.importacao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ContaImportacaoDTO {

    private int linhaExcel;
    private String fornecedor;
    private String descricao;
    private String nomeLocal;
    private LocalDate dataVencimento;
    private BigDecimal valor;
    private List<String> erros = new ArrayList<>();

    private boolean possivelDuplicidade;

    public int getLinhaExcel() {
        return linhaExcel;
    }

    public void setLinhaExcel(int linhaExcel) {
        this.linhaExcel = linhaExcel;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public List<String> getErros() {
        return erros;
    }

    public void adicionarErro(String erro) {
        this.erros.add(erro);
    }

    public boolean isValido() {
        return erros.isEmpty();
    }

    public boolean isPossivelDuplicidade() {
        return possivelDuplicidade;
    }

    public void setPossivelDuplicidade(boolean possivelDuplicidade) {
        this.possivelDuplicidade = possivelDuplicidade;
    }
}