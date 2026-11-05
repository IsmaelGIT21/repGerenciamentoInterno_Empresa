package br.com.gestao.empresas.conta.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ContaFormDTO {
    @NotNull(message = "Selecione um local.")
    private Long localId;

    private Long fornecedorId;

    @NotBlank(message = "A descrição da conta é obrigatória.")
    @Size(max = 255, message = "A descrição deve ter no máximo 255 caracteres.")
    private String descricao;

    @NotNull(message = "Informe o valor da conta.")
    @DecimalMin(value = "0.01", message = "O valor deve ser maior que zero.")
    private BigDecimal valor;

    @NotNull(message = "Informe a data de vencimento.")
    private LocalDate dataVencimento;

    private LocalDate dataPagamento;

    @NotBlank(message = "Selecione a situação da conta.")
    @Pattern(regexp = "PENDENTE|PAGA", message = "Situação inválida.")
    private String status;

    @Size(max = 255, message = "As observações devem ter no máximo 255 caracteres.")
    private String observacoes;

    public Long getFornecedorId() {
        return fornecedorId;
    }

    public void setFornecedorId(Long fornecedorId) {
        this.fornecedorId = fornecedorId;
    }

    public Long getLocalId() {
        return localId;
    }

    public void setLocalId(Long localId) {
        this.localId = localId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(LocalDate dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public LocalDate getDataPagamento() {
        return dataPagamento;
    }

    public void setDataPagamento(LocalDate dataPagamento) {
        this.dataPagamento = dataPagamento;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}