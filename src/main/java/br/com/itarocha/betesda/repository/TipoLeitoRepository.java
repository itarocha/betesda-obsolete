package br.com.itarocha.betesda.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.itarocha.betesda.model.TipoLeito;

public interface TipoLeitoRepository extends JpaRepository<TipoLeito, Long> {

	
	@Query("SELECT o FROM TipoLeito o ORDER BY o.descricao")
	List<TipoLeito> findAllOrderByDescricao();

}
