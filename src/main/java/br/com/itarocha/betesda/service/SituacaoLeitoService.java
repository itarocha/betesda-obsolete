package br.com.itarocha.betesda.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.itarocha.betesda.repository.SituacaoLeitoRepository;
import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.model.SituacaoLeito;

@Service
public class SituacaoLeitoService {

	@Autowired
	private EntityManager em;
	
	@Autowired
	private SituacaoLeitoRepository repositorio;

	public SituacaoLeito create(SituacaoLeito model) {
		try{
			return repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}		
	}

	public void remove(Long id) {
		SituacaoLeito model = find(id);
		if (model != null) {
			repositorio.delete(model);
		}		
	}

	/*
	public SituacaoLeito update(SituacaoLeito model) {
		SituacaoLeito obj = find(model.getId());
		if (obj != null) {
			obj = em.merge(model);
		}
		return obj;
	}
	*/
	
	public SituacaoLeito find(Long id) {
		return em.find(SituacaoLeito.class, id);
	}

	public List<SituacaoLeito> findAll() {
		TypedQuery query = em.createQuery("SELECT e FROM SituacaoLeito e ORDER BY e.descricao", SituacaoLeito.class);
		return query.getResultList();
	}
	
	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT o FROM SituacaoLeito o ORDER BY o.descricao",SituacaoLeito.class)
			.getResultList()
			.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}

/*	
	
	public DestinacaoHospedagem create(DestinacaoHospedagem model) {
		try{
			return repositorio.save(model);
		}catch(Exception e){
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	public void remove(Long id) {
		DestinacaoHospedagem model = find(id);
		if (model != null) {
			repositorio.delete(model);
		}
	}
	
	public DestinacaoHospedagem find(Long id) {
		return em.find(DestinacaoHospedagem.class, id);
	}

	public List<DestinacaoHospedagem> findAll() {
		TypedQuery query = em.createQuery("SELECT model FROM DestinacaoHospedagem model ORDER BY model.descricao", DestinacaoHospedagem.class);
		return query.getResultList();
	}

	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT model FROM DestinacaoHospedagem model ORDER BY model.descricao", DestinacaoHospedagem.class)
		.getResultList()
		.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}

*/	
}
