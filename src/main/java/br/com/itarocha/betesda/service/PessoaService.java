package br.com.itarocha.betesda.service;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.itarocha.betesda.model.Endereco;
import br.com.itarocha.betesda.model.Pessoa;
import br.com.itarocha.betesda.model.UnidadeFederacao;
import br.com.itarocha.betesda.repository.EnderecoRepository;
import br.com.itarocha.betesda.repository.PessoaRepository;

//import static org.springframework.data.jpa.domain.Specifications.where;

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

	public List<Pessoa> findByFieldNameAndValue(String campo, String valor){
		// bookRepository.findAll(where(hasAuthor(author)).and(titleContains(title)));
		//return repositorio.findAll(campoContem(campo, valor));
		return repositorio.findAll(campoQueContenha(campo, valor));
	}
	
	public List<Pessoa> findAll() {
		return em.createQuery("SELECT model FROM Pessoa model ORDER BY model.nome", Pessoa.class).getResultList();
	}

	public List<Pessoa> consultar(String texto) {
		return em.createQuery("SELECT model FROM Pessoa model WHERE lower(model.nome) LIKE :texto ORDER BY model.nome", Pessoa.class)
				.setParameter("texto", "%"+texto.toLowerCase()+"%")
				.getResultList();
	}
	
	static Specification<Pessoa> campoIgual(String campo, String valor) {
	    return (pessoa, cq, cb) -> cb.equal(pessoa.get(campo), valor);
	}
	 
	static Specification<Pessoa> endereco(String campo, String valor) {
	    return (pessoa, cq, cb) -> {
	    	Path<Endereco> endereco = pessoa.<Endereco> get("endereco");
	    	
	    	return cb.equal(endereco.get(campo).as(String.class),valor);
	    };
	}

	static Specification<Pessoa> campoContem(String campo, String valor) {
	    //return (pessoa, cq, cb) -> cb.like(pessoa.get(campo), "%" + valor + "%");
	    return (pessoa, cq, cb) -> cb.like(cb.lower(pessoa.get(campo)), "%" + valor.toLowerCase() + "%");
	}
	
	public static Specification<Pessoa> campoQueContenha(String campo, String conteudo) {
		// https://leaks.wanari.com/2018/01/23/awesome-spring-specification/
		return (root, query, cb) -> {
			query.distinct(true);
			query.orderBy(cb.asc(root.get("nome")));
			//Path<Endereco> endereco = root.<Endereco> get("endereco");
            
            //Predicate endfilter = cb.equal(endereco.get("uf").as(String.class),"MG");
			//return Specification.where(campoContem(campo, conteudo)).or(endereco("uf","MA")).toPredicate(root, query, cb);
			
			return Specification.where(campoContem(campo, conteudo)).toPredicate(root, query, cb);
		};
		
		
		/*
        return new Specification<Pessoa>() {
			private static final long serialVersionUID = 575273514861865441L;

			@Override
            public Predicate toPredicate(Root<Pessoa> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //Join<Pessoa, Endereco> endereco = root.join("endereco");
                //
                //criteriaQuery.equal(endereco.get("uf"),"MG");
                
                Path<Endereco> endereco = root.<Endereco> get("endereco");
                
                Predicate endfilter = cb.equal(endereco.get("uf").as(String.class),"MG");
                
                //cb.and(endfilter);
                cb.like(root.get(campo), conteudo);
                query.orderBy(cb.asc(root.get("nome")));
                
                //return cb.and(endfilter);
            }
        };
        */
    }	
}
