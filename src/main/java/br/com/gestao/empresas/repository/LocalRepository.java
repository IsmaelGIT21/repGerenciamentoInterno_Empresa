
package br.com.gestao.empresas.repository;

import br.com.gestao.empresas.model.Local;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalRepository extends JpaRepository<Local, Long> {
    
}
