package br.com.gestao.empresas.conta.service;

import br.com.gestao.empresas.conta.dto.ContaFormDTO;
import br.com.gestao.empresas.conta.model.Conta;
import br.com.gestao.empresas.conta.repository.ContaRepository;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.repository.LocalRepository;
import br.com.gestao.empresas.fornecedor.model.Fornecedor;
import br.com.gestao.empresas.fornecedor.repository.FornecedorRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private LocalRepository localRepository;

    @Mock
    private FornecedorRepository fornecedorRepository;

    @InjectMocks
    private ContaService contaService;

    private ContaFormDTO criarDTO() {
        ContaFormDTO dto = new ContaFormDTO();
        dto.setDescricao("Conta de energia");
        dto.setValor(new BigDecimal("250.00"));
        dto.setDataVencimento(LocalDate.of(2026, 12, 10));
        dto.setStatus("PENDENTE");
        dto.setObservacoes("Conta de teste");
        dto.setLocalId(1L);
        dto.setFornecedorId(2L);
        return dto;
    }

    private Local criarLocal() {
        Local local = new Local();
        local.setId(1L);
        local.setNome("Escritório");
        return local;
    }

    private Fornecedor criarFornecedor() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(2L);
        fornecedor.setNome("Fornecedor Teste");
        return fornecedor;
    }

    @Test
    void deveCadastrarContaPendente() {
        ContaFormDTO dto = criarDTO();

        when(localRepository.findById(1L))
                .thenReturn(Optional.of(criarLocal()));

        when(fornecedorRepository.findById(2L))
                .thenReturn(Optional.of(criarFornecedor()));

        when(contaRepository.save(any(Conta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Conta resultado = contaService.cadastrar(dto);

        assertEquals("Conta de energia", resultado.getDescricao());
        assertEquals(new BigDecimal("250.00"), resultado.getValor());
        assertEquals("PENDENTE", resultado.getStatus());
        assertNull(resultado.getDataPagamento());
        assertEquals("Escritório", resultado.getLocal().getNome());
        assertEquals("Fornecedor Teste", resultado.getFornecedor().getNome());
    }

    @Test
    void deveDefinirStatusPendenteQuandoNaoInformado() {
        ContaFormDTO dto = criarDTO();
        dto.setStatus(null);

        when(localRepository.findById(1L))
                .thenReturn(Optional.of(criarLocal()));

        when(fornecedorRepository.findById(2L))
                .thenReturn(Optional.of(criarFornecedor()));

        when(contaRepository.save(any(Conta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Conta resultado = contaService.cadastrar(dto);

        assertEquals("PENDENTE", resultado.getStatus());
    }

    @Test
    void deveDefinirDataPagamentoAoCadastrarContaPaga() {
        ContaFormDTO dto = criarDTO();
        dto.setStatus("PAGA");

        when(localRepository.findById(1L))
                .thenReturn(Optional.of(criarLocal()));

        when(fornecedorRepository.findById(2L))
                .thenReturn(Optional.of(criarFornecedor()));

        when(contaRepository.save(any(Conta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Conta resultado = contaService.cadastrar(dto);

        assertEquals("PAGA", resultado.getStatus());
        assertEquals(LocalDate.now(), resultado.getDataPagamento());
    }

    @Test
    void deveLimparDataPagamentoAoCadastrarContaPendente() {
        ContaFormDTO dto = criarDTO();
        dto.setDataPagamento(LocalDate.of(2026, 11, 1));

        when(localRepository.findById(1L))
                .thenReturn(Optional.of(criarLocal()));

        when(fornecedorRepository.findById(2L))
                .thenReturn(Optional.of(criarFornecedor()));

        when(contaRepository.save(any(Conta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Conta resultado = contaService.cadastrar(dto);

        assertNull(resultado.getDataPagamento());
    }

    @Test
    void naoDeveCadastrarContaSemLocal() {
        ContaFormDTO dto = criarDTO();
        dto.setLocalId(null);

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> contaService.cadastrar(dto));

        assertEquals(HttpStatus.BAD_REQUEST, excecao.getStatusCode());

        verify(contaRepository, never()).save(any());
    }

    @Test
    void devePermitirCadastroSemFornecedor() {
        ContaFormDTO dto = criarDTO();
        dto.setFornecedorId(null);

        when(localRepository.findById(1L))
                .thenReturn(Optional.of(criarLocal()));

        when(contaRepository.save(any(Conta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Conta resultado = contaService.cadastrar(dto);

        assertNull(resultado.getFornecedor());
        verify(contaRepository).save(any(Conta.class));
    }

    @Test
    void deveAtualizarContaParaPendenteLimpandoDataPagamento() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setStatus("PAGA");
        conta.setDataPagamento(LocalDate.of(2026, 10, 20));

        ContaFormDTO dto = criarDTO();
        dto.setStatus("PENDENTE");

        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(conta));

        when(localRepository.findById(1L))
                .thenReturn(Optional.of(criarLocal()));

        when(fornecedorRepository.findById(2L))
                .thenReturn(Optional.of(criarFornecedor()));

        when(contaRepository.save(any(Conta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Conta resultado = contaService.atualizar(1L, dto);

        assertEquals("PENDENTE", resultado.getStatus());
        assertNull(resultado.getDataPagamento());
    }

    @Test
    void devePreservarDataPagamentoAoAtualizarContaJaPaga() {
        LocalDate dataOriginal = LocalDate.of(2026, 10, 20);

        Conta conta = new Conta();
        conta.setId(1L);
        conta.setStatus("PAGA");
        conta.setDataPagamento(dataOriginal);

        ContaFormDTO dto = criarDTO();
        dto.setStatus("PAGA");
        dto.setDataPagamento(null);

        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(conta));

        when(localRepository.findById(1L))
                .thenReturn(Optional.of(criarLocal()));

        when(fornecedorRepository.findById(2L))
                .thenReturn(Optional.of(criarFornecedor()));

        when(contaRepository.save(any(Conta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Conta resultado = contaService.atualizar(1L, dto);

        assertEquals("PAGA", resultado.getStatus());
        assertEquals(dataOriginal, resultado.getDataPagamento());
    }

    @Test
    void deveMarcarContaComoPaga() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setStatus("PENDENTE");

        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(conta));

        when(contaRepository.save(any(Conta.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Conta resultado = contaService.marcarComoPaga(1L);

        assertEquals("PAGA", resultado.getStatus());
        assertEquals(LocalDate.now(), resultado.getDataPagamento());

        verify(contaRepository).save(conta);
    }

    @Test
    void naoDeveSalvarNovamenteContaJaPaga() {
        Conta conta = new Conta();
        conta.setId(1L);
        conta.setStatus("PAGA");

        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(conta));

        Conta resultado = contaService.marcarComoPaga(1L);

        assertSame(conta, resultado);

        verify(contaRepository, never()).save(any());
    }

    @Test
    void deveRetornarErroAoBuscarContaInexistente() {
        when(contaRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> contaService.buscarPorId(1L));

        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());
    }

    @Test
    void deveExcluirConta() {
        Conta conta = new Conta();
        conta.setId(1L);

        when(contaRepository.findById(1L))
                .thenReturn(Optional.of(conta));

        contaService.excluir(1L);

        verify(contaRepository).delete(conta);
    }
}
