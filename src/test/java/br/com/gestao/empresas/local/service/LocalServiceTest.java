package br.com.gestao.empresas.local.service;

import br.com.gestao.empresas.local.dto.LocalFormDTO;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.repository.LocalRepository;
import br.com.gestao.empresas.conta.repository.ContaRepository;
import br.com.gestao.empresas.apolice.repository.ApoliceRepository;

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
class LocalServiceTest {

    @Mock
    private LocalRepository localRepository;

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private ApoliceRepository apoliceRepository;

    @InjectMocks
    private LocalService localService;

    @Test
    void deveCadastrarLocal() {
        LocalFormDTO dto = new LocalFormDTO();
        dto.setNome("Escritório");
        dto.setTipo("Comercial");
        dto.setEndereco("Centro");
        dto.setAtivo(true);

        when(localRepository.save(any(Local.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Local resultado = localService.cadastrar(dto);

        assertEquals("Escritório", resultado.getNome());
        assertEquals("Comercial", resultado.getTipo());
        assertEquals("Centro", resultado.getEndereco());
        assertTrue(resultado.getAtivo());

        verify(localRepository).save(any(Local.class));
    }

    @Test
    void deveDefinirLocalComoAtivoQuandoStatusNaoInformado() {
        LocalFormDTO dto = new LocalFormDTO();
        dto.setNome("Casa de Praia");

        when(localRepository.save(any(Local.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Local resultado = localService.cadastrar(dto);

        assertTrue(resultado.getAtivo());
    }

    @Test
    void deveBuscarLocalPorId() {
        Local local = new Local();
        local.setId(1L);
        local.setNome("Escritório");

        when(localRepository.findById(1L))
                .thenReturn(Optional.of(local));

        Local resultado = localService.buscarPorId(1L);

        assertSame(local, resultado);
    }

    @Test
    void deveRetornarErroQuandoLocalNaoExistir() {
        when(localRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResponseStatusException excecao = assertThrows(
                ResponseStatusException.class,
                () -> localService.buscarPorId(1L));

        assertEquals(HttpStatus.NOT_FOUND, excecao.getStatusCode());
    }

    @Test
    void deveAtualizarDadosSemAlterarStatus() {
        Local local = new Local();
        local.setId(1L);
        local.setNome("Nome Antigo");
        local.setAtivo(false);

        LocalFormDTO dto = new LocalFormDTO();
        dto.setNome("Nome Novo");
        dto.setTipo("Residencial");
        dto.setEndereco("Praia");

        when(localRepository.findById(1L))
                .thenReturn(Optional.of(local));

        when(localRepository.save(any(Local.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Local resultado = localService.atualizar(1L, dto);

        assertEquals("Nome Novo", resultado.getNome());
        assertEquals("Residencial", resultado.getTipo());
        assertEquals("Praia", resultado.getEndereco());
        assertFalse(resultado.getAtivo());
    }

    @Test
    void deveExcluirLocalSemContasOuApolices() {
        Local local = new Local();
        local.setId(1L);

        when(localRepository.findById(1L))
                .thenReturn(Optional.of(local));

        when(contaRepository.existsByLocalId(1L))
                .thenReturn(false);

        when(apoliceRepository.existsByLocalId(1L))
                .thenReturn(false);

        boolean resultado = localService.excluir(1L);

        assertTrue(resultado);

        verify(localRepository).delete(local);
    }

    @Test
    void naoDeveExcluirLocalComContasVinculadas() {
        Local local = new Local();
        local.setId(1L);

        when(localRepository.findById(1L))
                .thenReturn(Optional.of(local));

        when(contaRepository.existsByLocalId(1L))
                .thenReturn(true);

        when(apoliceRepository.existsByLocalId(1L))
                .thenReturn(false);

        boolean resultado = localService.excluir(1L);

        assertFalse(resultado);

        verify(localRepository, never()).delete(any());
    }

    @Test
    void naoDeveExcluirLocalComApolicesVinculadas() {
        Local local = new Local();
        local.setId(1L);

        when(localRepository.findById(1L))
                .thenReturn(Optional.of(local));

        when(contaRepository.existsByLocalId(1L))
                .thenReturn(false);

        when(apoliceRepository.existsByLocalId(1L))
                .thenReturn(true);

        boolean resultado = localService.excluir(1L);

        assertFalse(resultado);

        verify(localRepository, never()).delete(any());
    }
}
