package br.com.gestao.empresas.apolice.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ApoliceFormDTO {

    @NotBlank(message = "A descrição da apólice é obrigatória.")
    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    private String descricao;

    @Size(max = 50, message = "O ano/modelo deve ter no máximo 50 caracteres.")
    private String anoModelo;

    @Size(max = 100, message = "O identificador deve ter no máximo 100 caracteres.")
    private String identificador;

    private LocalDate dataInicio;
    private LocalDate dataVencimento;

    @Size(max = 100, message = "O número da apólice deve ter no máximo 100 caracteres.")
    private String numeroApolice;

    @Size(max = 150, message = "O nome da seguradora deve ter no máximo 150 caracteres.")
    private String seguradora;

    @Size(max = 150, message = "O nome do corretor deve ter no máximo 150 caracteres.")
    private String corretor;

    @DecimalMin(value = "0.00", message = "O prêmio não pode ser negativo.")
    private BigDecimal valorPremio;

    @DecimalMin(value = "0.00", message = "A franquia não pode ser negativa.")
    private BigDecimal valorFranquia;

    @DecimalMin(value = "0.00", message = "O LMI não pode ser negativo.")
    private BigDecimal lmi;

    private Integer parcelas;

    @DecimalMin(value = "0.00", message = "O valor da parcela não pode ser negativo.")
    private BigDecimal valorParcela;

    @DecimalMin(value = "0.00", message = "A comissão não pode ser negativa.")
    private BigDecimal comissao;

    private Long localId;

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

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }
}
