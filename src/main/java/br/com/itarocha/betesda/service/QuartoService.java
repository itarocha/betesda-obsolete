package br.com.itarocha.betesda.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.itarocha.betesda.model.DestinacaoHospedagem;
import br.com.itarocha.betesda.model.EditLeitoVO;
import br.com.itarocha.betesda.model.EditQuartoVO;
import br.com.itarocha.betesda.model.Leito;
import br.com.itarocha.betesda.model.Logico;
import br.com.itarocha.betesda.model.NovoQuartoVO;
import br.com.itarocha.betesda.model.Quarto;
import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.model.SituacaoLeito;
import br.com.itarocha.betesda.model.TipoLeito;
import br.com.itarocha.betesda.repository.LeitoRepository;
import br.com.itarocha.betesda.repository.QuartoRepository;

@Service
@Transactional
//https://www.devmedia.com.br/conheca-o-spring-transactional-annotations/32472
public class QuartoService {

	@Autowired
	private EntityManager em;
	
	@Autowired
	private QuartoRepository repositorio; 
	
	@Autowired
	private LeitoRepository leitoRepositorio;

	public Quarto create(Quarto model) {
		try{
			return repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public Quarto create(NovoQuartoVO model) throws Exception{
		Quarto q = new Quarto();
		try {
			DestinacaoHospedagem dest = em.find(DestinacaoHospedagem.class, model.getDestinacaoHospedagem());
			TipoLeito tipoLeito = em.find(TipoLeito.class, model.getTipoLeito());
			SituacaoLeito situacao = em.find(SituacaoLeito.class, model.getSituacao());
			
			q.setNumero(model.getNumero());
			q.setDescricao(model.getDescricao());
			q.setDestinacaoHospedagem(dest);
			q.setAtivo(Logico.S);
			em.persist(q);
			
			for (int i = 1; i <= model.getQuantidadeLeitos(); i++) {
				Leito leito = new Leito();
				leito.setQuarto(q);
				leito.setNumero(i);
				leito.setTipoLeito(tipoLeito);
				leito.setSituacao(situacao);
				em.persist(leito);
			}
		} catch (Exception e) {
			throw e;
		}
		return q;
	}
	
	public Leito saveLeito(EditLeitoVO model) throws Exception{
		Leito leito;
		boolean isNovo = model.getId() == null; 
		if (isNovo) {
			leito = new Leito();
		} else {
			leito = em.find(Leito.class, model.getId());
			if (leito == null) {
				throw new Exception("Leito inexistente: "+model.getId());
			}
		}
		
		try {
			TipoLeito tipoLeito = em.find(TipoLeito.class, model.getTipoLeito());
			SituacaoLeito situacao = em.find(SituacaoLeito.class, model.getSituacao());
			leito.setNumero(model.getNumero());
			leito.setTipoLeito(tipoLeito);
			leito.setSituacao(situacao);
			
			if (isNovo) {
				Quarto quarto = em.find(Quarto.class, model.getQuartoId());
				leito.setQuarto(quarto);
			}
			leito = leitoRepositorio.save(leito);
		} catch (Exception e) {
			throw e;
		}
		return leito;
	}
	
	public void remove(Long id) {
		Quarto model = find(id);
		if (model != null) {
			int deletedCount = em.createQuery("DELETE FROM Leito o WHERE o.quarto = :quarto").setParameter("quarto", model).executeUpdate();
			em.remove(model);
		}
	}

	public void removeLeito(Long id) {
		leitoRepositorio.deleteById(id);
	}

	public Quarto update(EditQuartoVO model) {
		Quarto obj = em.find(Quarto.class, model.getId());
		if (obj != null) {
			obj.setDescricao(model.getDescricao());
			DestinacaoHospedagem dest = em.find(DestinacaoHospedagem.class, model.getDestinacaoHospedagem());
			obj.setDestinacaoHospedagem(dest);
			obj.setNumero(model.getNumero());
			em.merge(obj);
		}
		return obj;
	}

	public Quarto find(Long id) {
		return em.find(Quarto.class, id);
	}

	public Leito findLeito(Long id) {
		return em.find(Leito.class, id);
	}

	public List<Quarto> findAll() {
		List<Quarto> lista;
		//TODO Mover expressão SQL para repositório
		TypedQuery query = em.createQuery("SELECT q FROM Quarto q FETCH ALL PROPERTIES ORDER BY q.numero", Quarto.class);
		lista = query.getResultList();
		return lista;
	}

	public List<Quarto> findAllByDestinacaoHospedagem(Long id) {
		List<Quarto> lista = new ArrayList<Quarto>();
		DestinacaoHospedagem obj = em.find(DestinacaoHospedagem.class, id);
		if (obj != null) {
			//TODO Mover expressão SQL para repositório
			TypedQuery query = em.createQuery("SELECT q FROM Quarto q FETCH ALL PROPERTIES WHERE q.destinacaoHospedagem = :obj ORDER BY q.numero", Quarto.class);
			lista = query.setParameter("obj", obj).getResultList();
		}
		return lista;
	}

	public List<Leito> findLeitosByQuarto(Long quartoId) {
		Quarto q = em.find(Quarto.class, quartoId);
		//TODO Mover expressão SQL para repositório
		TypedQuery _query = em.createQuery("SELECT e FROM Leito e WHERE e.quarto = :quarto", Leito.class);
		List<Leito> lst = _query.setParameter("quarto", q).getResultList();
		return lst;
	}

	public List<Leito> findLeitosDisponiveis() {
		TypedQuery _query = em.createQuery("SELECT e FROM Leito e WHERE e.situacao.disponivel = :disponivel ORDER BY e.quarto.numero, e.numero", Leito.class);
		List<Leito> lst = _query.setParameter("disponivel", Logico.S).getResultList();
		return lst;
	}

	public List<SelectValueVO> listTipoLeito() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		//TODO Mover expressão SQL para repositório
		em.createQuery("SELECT o FROM TipoLeito o ORDER BY o.descricao",TipoLeito.class)
						.getResultList()
						.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}

	public boolean existeOutroLeitoComEsseNumero(Long leito_id, Long quartoId, Integer numero) {
		//TODO Mover expressão SQL para repositório
		TypedQuery _query = em.createQuery("SELECT o "
										+ "FROM Leito o "
										+ "WHERE (o.quarto.id = :quartoId) "
										+ "AND (o.id <> :id) "
										+ "AND (o.numero = :numero)", Leito.class);
		List<Leito> lst = _query.setParameter("id", leito_id)
								.setParameter("quartoId", quartoId)
								.setParameter("numero", numero)
								.getResultList();
		
		return lst.size() > 0;
	}

	public boolean existeOutroLeitoComEsseNumero(Long quartoId, Integer numero) {
		//TODO Mover expressão SQL para repositório
		TypedQuery _query = em.createQuery("SELECT o "
										+ "FROM Leito o "
										+ "WHERE (o.quarto.id = :quartoId) "
										+ "AND (o.numero = :numero)", Leito.class);
		List<Leito> lst = _query.setParameter("quartoId", quartoId)
								.setParameter("numero", numero)
								.getResultList();
		
		return lst.size() > 0;
	}

	public boolean existeOutroQuartoComEsseNumero(Long id, Integer numero) {
		//TODO Mover expressão SQL para repositório
		TypedQuery _query = em.createQuery("SELECT o FROM Quarto o WHERE (o.id <> :id) AND (o.numero = :numero)", Quarto.class);
		List<Quarto> lst = _query.setParameter("id", id)
								.setParameter("numero", numero)
								.getResultList();
		
		return lst.size() > 0;
	}
	
	public boolean existeOutroQuartoComEsseNumero(Integer numero) {
		//TODO Mover expressão SQL para repositório
		TypedQuery _query = em.createQuery("SELECT o FROM Quarto o WHERE (o.numero = :numero)", Quarto.class);
		List<Quarto> lst = _query.setParameter("numero", numero)
								.getResultList();
		
		return lst.size() > 0;
	}
}