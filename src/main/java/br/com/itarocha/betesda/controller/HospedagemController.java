package br.com.itarocha.betesda.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import br.com.itarocha.betesda.model.HospedeLeitoVO;
import br.com.itarocha.betesda.model.Pessoa;
import br.com.itarocha.betesda.model.hospedagem.Dia;
import br.com.itarocha.betesda.model.hospedagem.MapaHospedagem;
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
		    return new ResponseEntity<HospedagemVO>(model, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="/mapa", method = RequestMethod.POST)
	public MapaHospedagem mapa(@RequestBody MapaHospedagemRequest model)
	{
		System.out.println("Showing mapa de hospedagem. Data = " + model.data);
		
		//List<HospedeLeitoVO> listagem = new ArrayList<HospedeLeitoVO>();
		 
		//Map<String, Dia[]> listagem = new HashMap<String, Dia[]>();
		MapaHospedagem listagem = new MapaHospedagem();
		try {
			listagem  = service.getHospedagens(model.data);
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
		
		//return new ResponseEntity<List<HospedeLeitoVO>>(listagem, HttpStatus.OK);
		return listagem;
	}
	
	private class Mapa {
		public int leito;
		public String[] semana;
		
		public Mapa(int leito, String[] semana) {
			this.leito = leito;
			this.semana = semana;
		}
	}
	
	private static class MapaHospedagemRequest{
		public LocalDate data;
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
