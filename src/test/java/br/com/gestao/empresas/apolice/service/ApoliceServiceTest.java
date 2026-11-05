package br.com.gestao.empresas.apolice.service;

import br.com.gestao.empresas.apolice.dto.ApoliceFormDTO;
import br.com.gestao.empresas.apolice.model.Apolice;
import br.com.gestao.empresas.apolice.repository.ApoliceRepository;
import br.com.gestao.empresas.local.model.Local;
import br.com.gestao.empresas.local.repository.LocalRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApoliceServiceTest {

    @Mock
    private ApoliceRepository apoliceRepository;

    @Mock
    private LocalRepository localRepository;

    @InjectMocks
    private ApoliceService apoliceService;

    private ApoliceFormDTO criarFormulario(Long localId) {
        ApoliceFormDTO dados = new ApoliceFormDTO();

        dados.setDescricao("Seguro automóvel");
        dados.setAnoModelo("2024");
        dados.setIdentificador("ABC1234");
        dados.setDataInicio(LocalDate.of(2026, 1, 1));
        dados.setDataVencimento(LocalDate.of(2027, 1, 1));
        dados.setNumeroApolice("AP-123");
        dados.setSeguradora("Seguradora Teste");
        dados.setCorretor("Corretor Teste");
        dados.setValorPremio(new BigDecimal("1500.00"));
        dados.setValorFranquia(new BigDecimal("800.00"));
        dados.setLmi(new BigDecimal("50000.00"));
        dados.setParcelas(10);
        dados.setValorParcela(new BigDecimal("150.00"));
        dados.setComissao(new BigDecimal("200.00"));
        dados.setLocalId(localId);

        return dados;
    }

    @Test
    void deveCadastrarApoliceComLocalEPreencherDados() {
        Local local = mock(Local.class);

        when(localRepository.findById(1L)).thenReturn(Optional.of(local));
        when(apoliceRepository.save(any(Apolice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Apolice resultado = apoliceService.cadastrar(criarFormulario(1L));

        assertEquals("Seguro automóvel", resultado.getDescricao());
        assertEquals("2024", resultado.getAnoModelo());
        assertEquals("ABC1234", resultado.getIdentificador());
        assertEquals("AP-123", resultado.getNumeroApolice());
        assertEquals("Seguradora Teste", resultado.getSeguradora());
        assertEquals("Corretor Teste", resultado.getCorretor());
        assertEquals(new BigDecimal("1500.00"), resultado.getValorPremio());
        assertEquals(new BigDecimal("800.00"), resultado.getValorFranquia());
        assertEquals(new BigDecimal("50000.00"), resultado.getLmi());
        assertEquals(10, resultado.getParcelas());
        assertEquals(new BigDecimal("150.00"), resultado.getValorParcela());
        assertEquals(new BigDecimal("200.00"), resultado.getComissao());
        assertSame(local, resultado.getLocal());

        verify(apoliceRepository).save(any(Apolice.class));
        verify(localRepository).findById(1L);
    }

    @Test
    void deveCadastrarApoliceSemLocal() {
        when(apoliceRepository.save(any(Apolice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Apolice resultado = apoliceService.cadastrar(criarFormulario(null));

        assertNull(resultado.getLocal());

        verify(apoliceRepository).save(any(Apolice.class));
        verifyNoInteractions(localRepository);
    }

    @Test
    void deveAtualizarApoliceExistente() {
        Apolice existente = new Apolice();
        existente.setDescricao("Descrição antiga");

        when(apoliceRepository.findById(1L))
                .thenReturn(Optional.of(existente));
        when(apoliceRepository.save(any(Apolice.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Apolice resultado = apoliceService.atualizar(1L, criarFormulario(null));

        assertEquals("Seguro automóvel", resultado.getDescricao());
        assertEquals("AP-123", resultado.getNumeroApolice());
        assertNull(resultado.getLocal());

        verify(apoliceRepository).findById(1L);
        verify(apoliceRepository).save(existente);
    }

    @Test
    void deveListarTodasAsApolices() {
        List<Apolice> apolices = List.of(new Apolice(), new Apolice());

        when(apoliceRepository.findAll()).thenReturn(apolices);

        List<Apolice> resultado = apoliceService.listarTodas();

        assertEquals(2, resultado.size());
        assertSame(apolices, resultado);

        verify(apoliceRepository).findAll();
    }

    @Test
    void deveListarApolicesPorLocal() {
        List<Apolice> apolices = List.of(new Apolice());

        when(apoliceRepository.findByLocalId(1L)).thenReturn(apolices);

        List<Apolice> resultado = apoliceService.listarPorLocal(1L);

        assertEquals(1, resultado.size());
        assertSame(apolices, resultado);

        verify(apoliceRepository).findByLocalId(1L);
    }

    @Test
    void deveBuscarApolicePorId() {
        Apolice apolice = new Apolice();
        apolice.setDescricao("Seguro automóvel");

        when(apoliceRepository.findById(1L))
                .thenReturn(Optional.of(apolice));

        Apolice resultado = apoliceService.buscarPorId(1L);

        assertEquals("Seguro automóvel", resultado.getDescricao());

        verify(apoliceRepository).findById(1L);
    }

    @Test
    void deveLancarErroQuandoApoliceNaoExistir() {
        when(apoliceRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> apoliceService.buscarPorId(99L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
    }

    @Test
    void deveLancarErroQuandoLocalNaoExistir() {
        when(localRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(
                ResponseStatusException.class,
                () -> apoliceService.cadastrar(criarFormulario(99L)));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        verify(apoliceRepository, never()).save(any(Apolice.class));
    }

    @Test
    void deveExcluirApoliceExistente() {
        Apolice apolice = new Apolice();

        when(apoliceRepository.findById(1L))
                .thenReturn(Optional.of(apolice));

        apoliceService.excluir(1L);

        verify(apoliceRepository).findById(1L);
        verify(apoliceRepository).delete(apolice);
    }

    @Test
    void naoDeveAtualizarApoliceInexistente() {
        when(apoliceRepository.findById(99L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResponseStatusException.class,
                () -> apoliceService.atualizar(99L, criarFormulario(null)));

        verify(apoliceRepository, never()).save(any(Apolice.class));
    }
}
