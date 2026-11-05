package br.com.gestao.empresas.conta.service;

import br.com.gestao.empresas.conta.dto.ContaFormDTO;
import br.com.gestao.empresas.conta.model.Conta;
import br.com.gestao.empresas.conta.repository.ContaRepository;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.repository.LocalRepository;
import br.com.gestao.empresas.fornecedor.model.Fornecedor;
import br.com.gestao.empresas.fornecedor.repository.FornecedorRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final LocalRepository localRepository;
    private final FornecedorRepository fornecedorRepository;

    public ContaService(
            ContaRepository contaRepository,
            LocalRepository localRepository,
            FornecedorRepository fornecedorRepository) {

        this.contaRepository = contaRepository;
        this.localRepository = localRepository;
        this.fornecedorRepository = fornecedorRepository;
    }

    public List<Conta> listarTodas() {
        return contaRepository.findAll();
    }

    public Conta buscarPorId(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Conta não encontrada"));
    }

    public List<Conta> listarPorLocal(Long localId) {
        return contaRepository.findByLocalId(localId);
    }

    public Conta cadastrar(ContaFormDTO dadosFormulario) {

        Local local = buscarLocal(dadosFormulario.getLocalId());
        Fornecedor fornecedor = buscarFornecedor(dadosFormulario.getFornecedorId());

        Conta conta = new Conta();

        conta.setDescricao(dadosFormulario.getDescricao());
        conta.setValor(dadosFormulario.getValor());
        conta.setDataVencimento(dadosFormulario.getDataVencimento());
        conta.setDataPagamento(dadosFormulario.getDataPagamento());
        conta.setStatus(normalizarStatus(dadosFormulario.getStatus()));
        conta.setObservacoes(dadosFormulario.getObservacoes());
        conta.setLocal(local);
        conta.setFornecedor(fornecedor);

        ajustarDataPagamento(conta);

        return contaRepository.save(conta);
    }

    public Conta atualizar(Long id, ContaFormDTO dadosFormulario) {

        Conta conta = buscarPorId(id);

        Local local = buscarLocal(dadosFormulario.getLocalId());
        Fornecedor fornecedor = buscarFornecedor(dadosFormulario.getFornecedorId());

        conta.setDescricao(dadosFormulario.getDescricao());
        conta.setValor(dadosFormulario.getValor());
        conta.setDataVencimento(dadosFormulario.getDataVencimento());
        conta.setObservacoes(dadosFormulario.getObservacoes());
        conta.setLocal(local);
        conta.setFornecedor(fornecedor);

        String status = normalizarStatus(dadosFormulario.getStatus());
        conta.setStatus(status);

        if ("PAGA".equals(status)) {
            if (dadosFormulario.getDataPagamento() != null) {
                conta.setDataPagamento(dadosFormulario.getDataPagamento());
            } else if (conta.getDataPagamento() == null) {
                conta.setDataPagamento(LocalDate.now());
            }
        } else {
            conta.setDataPagamento(null);
        }

        return contaRepository.save(conta);
    }

    public Conta marcarComoPaga(Long id) {

        Conta conta = buscarPorId(id);

        if (!"PAGA".equals(conta.getStatus())) {
            conta.setStatus("PAGA");
            conta.setDataPagamento(LocalDate.now());

            return contaRepository.save(conta);
        }

        return conta;
    }

    public void excluir(Long id) {
        Conta conta = buscarPorId(id);
        contaRepository.delete(conta);
    }

    private Local buscarLocal(Long localId) {

        if (localId == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selecione um local para a conta");
        }

        return localRepository.findById(localId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Local não encontrado"));
    }

    private Fornecedor buscarFornecedor(Long fornecedorId) {

        if (fornecedorId == null) {
            return null;
        }

        return fornecedorRepository.findById(fornecedorId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Fornecedor não encontrado"));
    }

    private String normalizarStatus(String status) {
        return status == null || status.isBlank()
                ? "PENDENTE"
                : status;
    }

    private void ajustarDataPagamento(Conta conta) {

        if ("PAGA".equals(conta.getStatus())) {
            if (conta.getDataPagamento() == null) {
                conta.setDataPagamento(LocalDate.now());
            }
        } else {
            conta.setDataPagamento(null);
        }
    }
}
