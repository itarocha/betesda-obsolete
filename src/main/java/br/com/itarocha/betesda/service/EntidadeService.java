package br.com.itarocha.betesda.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.itarocha.betesda.model.Entidade;
import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.model.TipoServico;
import br.com.itarocha.betesda.repository.EnderecoRepository;
import br.com.itarocha.betesda.repository.EntidadeRepository;

@Service
public class EntidadeService {

	@Autowired
	private EntityManager em;

	@Autowired
	private EntidadeRepository repositorio;

	@Autowired
	private EnderecoRepository enderecoRepo;

	public EntidadeService() {
	}

	public Entidade create(Entidade model) {
		try{
			enderecoRepo.save(model.getEndereco());
			repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		return model;
	}

	public void remove(Long id) {
		Optional<Entidade> model = find(id);
		if (model.isPresent()) {
			repositorio.delete(model.get());
		}
	}

	public Entidade update(Entidade model) {
		Optional<Entidade> obj = find(model.getId());
		if (obj.isPresent()) {
			return repositorio.save(model);
		}
		return model;
	}

	public Optional<Entidade> find(Long id) {
		return repositorio.findById(id);
	}

	public List<Entidade> findAll() {
		return em.createQuery("SELECT model FROM Entidade model ORDER BY model.nome", Entidade.class).getResultList();
	}

	public List<Entidade> consultar(String texto) {
		return em.createQuery("SELECT model FROM Entidade model WHERE lower(model.nome) LIKE :texto ORDER BY model.nome", Entidade.class)
				.setParameter("texto", "%"+texto.toLowerCase()+"%")
				.getResultList();
	}

	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT e FROM Entidade e ORDER BY e.nome",Entidade.class)
			.getResultList()
			.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getNome())));
		return retorno;
	}
	
}
