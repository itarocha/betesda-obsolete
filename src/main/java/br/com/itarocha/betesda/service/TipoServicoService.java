package br.com.itarocha.betesda.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.itarocha.betesda.repository.TipoServicoRepository;
import br.com.itarocha.betesda.model.SelectValueVO;
import br.com.itarocha.betesda.model.TipoServico;

@Service
public class TipoServicoService {

	@Autowired
	private EntityManager em;
	
	@Autowired
	private TipoServicoRepository repositorio;

	public TipoServico create(TipoServico model) {
		repositorio.save(model);
		return model;
	}

	public void remove(Long id) {
		TipoServico emp = find(id);
		if (emp != null) {
			repositorio.delete(emp);
		}
	}

	public TipoServico update(TipoServico model) {
		TipoServico obj = find(model.getId());
		if (obj != null) {
			obj = em.merge(model);
		}
		return obj;
	}

	public TipoServico find(Long id) {
		return em.find(TipoServico.class, id);
	}

	public List<TipoServico> findAll() {
		return em.createQuery("SELECT e FROM TipoServico e ORDER BY e.descricao", TipoServico.class).getResultList();
	}
	
	public List<SelectValueVO> listSelect() {
		List<SelectValueVO> retorno = new ArrayList<SelectValueVO>();
		em.createQuery("SELECT o FROM TipoServico o WHERE o.ativo = 'S' ORDER BY o.descricao",TipoServico.class)
			.getResultList()
			.forEach(x -> retorno.add(new SelectValueVO(x.getId(), x.getDescricao())));
		return retorno;
	}
}
