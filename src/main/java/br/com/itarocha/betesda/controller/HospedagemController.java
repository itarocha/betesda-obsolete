package br.com.itarocha.betesda.controller;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.itarocha.betesda.model.Hospedagem;
import br.com.itarocha.betesda.model.HospedagemVO;
import br.com.itarocha.betesda.service.HospedagemService;
import br.com.itarocha.betesda.util.validation.ItaValidator;

@CrossOrigin
@RestController
@RequestMapping("/api/hospedagem")
public class HospedagemController {

	@Autowired
	private EntityManager em;

	@Autowired
	private HospedagemService service;
	
	/*
	@GET
	public Response listar() {
		try {
			DestinacaoHospedagemService service = new DestinacaoHospedagemService(em);
			List<DestinacaoHospedagem> lista = service.findAll();
		    return Response.status(200).entity(lista).build();
		} finally {
			em.close();
		}
	    
	}
	*/
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> gravar(@RequestBody HospedagemVO model) {
		ItaValidator<HospedagemVO> v = new ItaValidator<HospedagemVO>(model);
		v.validate();
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try {
			Hospedagem saved;
			saved = service.create(model);
		    // O model retornado vem preenchido com campos adicionais
		    //return Response.status(200).entity(model).build();
		    return new ResponseEntity<HospedagemVO>(model, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/mapa")
	public List<Mapa> mapa()
	{
		try {
			service.getHospedagens();
		} catch(Exception e) {
			throw e;
		} finally {
			em.close();
		}
		
		List<Mapa> lista = new ArrayList<Mapa>();
		lista.add(new Mapa(5, new String[] {"0","I","X","X","F","0","0"}));
		lista.add(new Mapa(7, new String[] {"0","0","I","X","X","X","F"}));
		lista.add(new Mapa(10, new String[] {"X","X","X","X","X","F","0"}));
		lista.add(new Mapa(15, new String[] {"I","X","F","0","0","0","0"}));
		lista.add(new Mapa(15, new String[] {"0","0","0","0","0","I","X"}));
		return lista;
	}
	
	private class Mapa {
		public int leito;
		public String[] semana;
		
		public Mapa(int leito, String[] semana) {
			this.leito = leito;
			this.semana = semana;
		}
	}
/*
	
	@RequestMapping
	public ResponseEntity<?> listar() {
		List<SituacaoLeito> lista = service.findAll();
	    return new ResponseEntity<List<SituacaoLeito>>(lista, HttpStatus.OK);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> gravar(@RequestBody SituacaoLeito model) {
		ItaValidator<SituacaoLeito> v = new ItaValidator<SituacaoLeito>(model);
		v.validate();
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try {
			SituacaoLeito saved = null;
			saved = service.create(model);
		    return new ResponseEntity<SituacaoLeito>(saved, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
		    return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }

	
*/	
}
