package br.com.gestao.empresas.importacao.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ApoliceImportacaoDTO {

    private int linhaExcel;

    private String descricao;
    private String anoModelo;
    private String identificador;

    private LocalDate dataInicio;
    private LocalDate dataVencimento;

    private String numeroApolice;
    private String seguradora;
    private String corretor;

    private BigDecimal valorPremio;
    private BigDecimal valorFranquia;
    private BigDecimal lmi;

    private Integer parcelas;
    private BigDecimal valorParcela;
    private BigDecimal comissao;

    private List<String> erros = new ArrayList<>();

    private boolean possivelDuplicidade;

    public int getLinhaExcel() {
        return linhaExcel;
    }

    public void setLinhaExcel(int linhaExcel) {
        this.linhaExcel = linhaExcel;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAnoModelo() {
        return anoModelo;
    }

    public void setAnoModelo(String anoModelo) {
        this.anoModelo = anoModelo;
    }

    public String getIdentificador() {
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getNumeroApolice() {
        return numeroApolice;
    }

    public void setNumeroApolice(String numeroApolice) {
        this.numeroApolice = numeroApolice;
    }

    public String getSeguradora() {
        return seguradora;
    }

    public void setSeguradora(String seguradora) {
        this.seguradora = seguradora;
    }

    public String getCorretor() {
        return corretor;
    }

    public void setCorretor(String corretor) {
        this.corretor = corretor;
    }

    public BigDecimal getValorPremio() {
        return valorPremio;
    }

    public void setValorPremio(BigDecimal valorPremio) {
        this.valorPremio = valorPremio;
    }

    public BigDecimal getValorFranquia() {
        return valorFranquia;
    }

    public void setValorFranquia(BigDecimal valorFranquia) {
        this.valorFranquia = valorFranquia;
    }

    public BigDecimal getLmi() {
        return lmi;
    }

    public void setLmi(BigDecimal lmi) {
        this.lmi = lmi;
    }

    public Integer getParcelas() {
        return parcelas;
    }

    public void setParcelas(Integer parcelas) {
        this.parcelas = parcelas;
    }

    public BigDecimal getValorParcela() {
        return valorParcela;
    }

    public void setValorParcela(BigDecimal valorParcela) {
        this.valorParcela = valorParcela;
    }

    public BigDecimal getComissao() {
        return comissao;
    }

    public void setComissao(BigDecimal comissao) {
        this.comissao = comissao;
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
