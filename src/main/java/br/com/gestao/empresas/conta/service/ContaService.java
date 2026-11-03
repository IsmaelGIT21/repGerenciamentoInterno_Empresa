package br.com.gestao.empresas.conta.service;

import br.com.gestao.empresas.conta.model.Conta;
import br.com.gestao.empresas.conta.repository.ContaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.stereotype.Service;
import java.util.List;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.repository.LocalRepository;
import br.com.gestao.empresas.fornecedor.model.Fornecedor;
import br.com.gestao.empresas.fornecedor.repository.FornecedorRepository;
import java.time.LocalDate;

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

    public Conta cadastrar(Conta conta) {
        if (conta.getLocal() == null || conta.getLocal().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selecione um local para a conta");
        }

        Long localId = conta.getLocal().getId();

        Local local = localRepository.findById(localId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Local não encontrado"));

        if (conta.getFornecedor() != null
                && conta.getFornecedor().getId() != null) {

            Fornecedor fornecedor = fornecedorRepository
                    .findById(conta.getFornecedor().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Fornecedor não encontrado"));

            conta.setFornecedor(fornecedor);

        } else {
            conta.setFornecedor(null);
        }

        conta.setLocal(local);

        if (conta.getStatus() == null || conta.getStatus().isBlank()) {
            conta.setStatus("PENDENTE");
        }

        if ("PAGA".equals(conta.getStatus())) {
            if (conta.getDataPagamento() == null) {
                conta.setDataPagamento(LocalDate.now());
            }
        } else {
            conta.setDataPagamento(null);
        }

        return contaRepository.save(conta);
    }

    public Conta atualizar(Long id, Conta dadosFormulario) {
        Conta conta = buscarPorId(id);

        if (dadosFormulario.getLocal() == null
                || dadosFormulario.getLocal().getId() == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Selecione um local para a conta");
        }

        Long localId = dadosFormulario.getLocal().getId();

        Local local = localRepository.findById(localId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Local não encontrado"));

        // Atualiza o fornecedor
        if (dadosFormulario.getFornecedor() != null
                && dadosFormulario.getFornecedor().getId() != null) {

            Fornecedor fornecedor = fornecedorRepository
                    .findById(dadosFormulario.getFornecedor().getId())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Fornecedor não encontrado"));

            conta.setFornecedor(fornecedor);

        } else {
            conta.setFornecedor(null);
        }

        conta.setDescricao(dadosFormulario.getDescricao());
        conta.setValor(dadosFormulario.getValor());
        conta.setDataVencimento(dadosFormulario.getDataVencimento());
        conta.setObservacoes(dadosFormulario.getObservacoes());
        conta.setLocal(local);

        String status = dadosFormulario.getStatus();

        if (status == null || status.isBlank()) {
            status = "PENDENTE";
        }

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
}
