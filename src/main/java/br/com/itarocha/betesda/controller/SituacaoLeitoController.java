package br.com.itarocha.betesda.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.itarocha.betesda.model.SituacaoLeito;
import br.com.itarocha.betesda.service.SituacaoLeitoService;
import br.com.itarocha.betesda.util.validation.ItaValidator;

@CrossOrigin
@RestController
@RequestMapping("/api/app/situacao_leito")
public class SituacaoLeitoController {

	@Autowired
	private SituacaoLeitoService service;
	
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
}
