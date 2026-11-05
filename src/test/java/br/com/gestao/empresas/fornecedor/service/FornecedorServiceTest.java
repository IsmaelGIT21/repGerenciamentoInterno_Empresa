package br.com.gestao.empresas.fornecedor.service;

import br.com.gestao.empresas.fornecedor.dto.FornecedorFormDTO;
import br.com.gestao.empresas.fornecedor.model.Fornecedor;
import br.com.gestao.empresas.fornecedor.repository.FornecedorRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FornecedorServiceTest {

    @Mock
    private FornecedorRepository fornecedorRepository;

    @InjectMocks
    private FornecedorService fornecedorService;

    @Test
    void deveCadastrarFornecedorComNomeNormalizado() {
        FornecedorFormDTO dto = new FornecedorFormDTO();
        dto.setNome("  Fornecedor Teste  ");

        when(fornecedorRepository.existsByNomeIgnoreCase("Fornecedor Teste"))
                .thenReturn(false);

        when(fornecedorRepository.save(any(Fornecedor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Fornecedor resultado = fornecedorService.cadastrar(dto);

        assertEquals("Fornecedor Teste", resultado.getNome());
        assertTrue(resultado.isAtivo());

        verify(fornecedorRepository).save(any(Fornecedor.class));
    }

    @Test
    void naoDeveCadastrarFornecedorComNomeVazio() {
        FornecedorFormDTO dto = new FornecedorFormDTO();
        dto.setNome("   ");

        assertThrows(
                IllegalArgumentException.class,
                () -> fornecedorService.cadastrar(dto));

        verify(fornecedorRepository, never()).save(any());
    }

    @Test
    void naoDeveCadastrarFornecedorComNomeMaiorQue150Caracteres() {
        FornecedorFormDTO dto = new FornecedorFormDTO();
        dto.setNome("A".repeat(151));

        assertThrows(
                IllegalArgumentException.class,
                () -> fornecedorService.cadastrar(dto));

        verify(fornecedorRepository, never()).save(any());
    }

    @Test
    void naoDeveCadastrarFornecedorDuplicado() {
        FornecedorFormDTO dto = new FornecedorFormDTO();
        dto.setNome("Fornecedor Teste");

        when(fornecedorRepository.existsByNomeIgnoreCase("Fornecedor Teste"))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> fornecedorService.cadastrar(dto));

        verify(fornecedorRepository, never()).save(any());
    }

    @Test
    void deveAtualizarFornecedor() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1L);
        fornecedor.setNome("Nome Antigo");

        FornecedorFormDTO dto = new FornecedorFormDTO();
        dto.setNome("Nome Novo");

        when(fornecedorRepository.findById(1L))
                .thenReturn(Optional.of(fornecedor));

        when(fornecedorRepository.existsByNomeIgnoreCaseAndIdNot("Nome Novo", 1L))
                .thenReturn(false);

        when(fornecedorRepository.save(any(Fornecedor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Fornecedor resultado = fornecedorService.atualizar(1L, dto);

        assertEquals("Nome Novo", resultado.getNome());
    }

    @Test
    void naoDeveAtualizarComNomeDeOutroFornecedor() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1L);
        fornecedor.setNome("Nome Antigo");

        FornecedorFormDTO dto = new FornecedorFormDTO();
        dto.setNome("Nome Existente");

        when(fornecedorRepository.findById(1L))
                .thenReturn(Optional.of(fornecedor));

        when(fornecedorRepository.existsByNomeIgnoreCaseAndIdNot("Nome Existente", 1L))
                .thenReturn(true);

        assertThrows(
                IllegalArgumentException.class,
                () -> fornecedorService.atualizar(1L, dto));

        verify(fornecedorRepository, never()).save(any());
    }

    @Test
    void deveAlternarStatusDoFornecedor() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setId(1L);
        fornecedor.setAtivo(true);

        when(fornecedorRepository.findById(1L))
                .thenReturn(Optional.of(fornecedor));

        fornecedorService.alterarStatus(1L);

        assertFalse(fornecedor.isAtivo());

        verify(fornecedorRepository).save(fornecedor);
    }

    @Test
    void deveRetornarErroAoBuscarFornecedorInexistente() {
        when(fornecedorRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> fornecedorService.buscarPorId(1L));

        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());
    }

    @Test
    void deveReutilizarFornecedorExistente() {
        Fornecedor fornecedor = new Fornecedor();
        fornecedor.setNome("Fornecedor Existente");

        when(fornecedorRepository.findByNomeIgnoreCase("Fornecedor Existente"))
                .thenReturn(Optional.of(fornecedor));

        Fornecedor resultado =
                fornecedorService.buscarOuCriar("Fornecedor Existente");

        assertSame(fornecedor, resultado);

        verify(fornecedorRepository, never()).save(any());
    }

    @Test
    void deveCriarFornecedorQuandoNaoExistir() {
        when(fornecedorRepository.findByNomeIgnoreCase("Fornecedor Novo"))
                .thenReturn(Optional.empty());

        when(fornecedorRepository.save(any(Fornecedor.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Fornecedor resultado = fornecedorService.buscarOuCriar("Fornecedor Novo");

        assertEquals("Fornecedor Novo", resultado.getNome());
        assertTrue(resultado.isAtivo());

        verify(fornecedorRepository).save(any(Fornecedor.class));
    }
}
