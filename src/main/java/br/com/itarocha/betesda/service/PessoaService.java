package br.com.itarocha.betesda.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.itarocha.betesda.model.Pessoa;
import br.com.itarocha.betesda.repository.EnderecoRepository;
import br.com.itarocha.betesda.repository.PessoaRepository;

@Service
public class PessoaService {

	@Autowired
	private EntityManager em;

	@Autowired
	private PessoaRepository repositorio;

	@Autowired
	private EnderecoRepository enderecoRepo;

	public PessoaService() {
	}

	public Pessoa create(Pessoa model) {
		try{
			enderecoRepo.save(model.getEndereco());
			repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
		return model;
	}

	public void remove(Long id) {
		Optional<Pessoa> model = find(id);
		if (model.isPresent()) {
			repositorio.delete(model.get());
		}
	}

	public Pessoa update(Pessoa model) {
		Optional<Pessoa> obj = find(model.getId());
		if (obj.isPresent()) {
			return repositorio.save(model);
		}
		return model;
	}

	public Optional<Pessoa> find(Long id) {
		return repositorio.findById(id);
	}

	public List<Pessoa> findAll() {
		return em.createQuery("SELECT model FROM Pessoa model ORDER BY model.nome", Pessoa.class).getResultList();
	}

	public List<Pessoa> consultar(String texto) {
		return em.createQuery("SELECT model FROM Pessoa model WHERE lower(model.nome) LIKE :texto ORDER BY model.nome", Pessoa.class)
				.setParameter("texto", "%"+texto.toLowerCase()+"%")
				.getResultList();
	}
}
