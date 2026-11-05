package br.com.gestao.empresas.apolice.repository;

import br.com.gestao.empresas.apolice.model.Apolice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ApoliceRepository extends JpaRepository<Apolice, Long> {

        List<Apolice> findByLocalId(Long localId);

        boolean existsByLocalId(Long localId);

        boolean existsByNumeroApoliceIgnoreCaseAndSeguradoraIgnoreCase(
                        String numeroApolice,
                        String seguradora);

        boolean existsByIdentificadorIgnoreCaseAndSeguradoraIgnoreCaseAndDataInicioAndDataVencimento(
                        String identificador,
                        String seguradora,
                        LocalDate dataInicio,
                        LocalDate dataVencimento);
}
