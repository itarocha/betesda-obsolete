package br.com.itarocha.betesda.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.itarocha.betesda.model.DestinacaoHospedagem;
import br.com.itarocha.betesda.service.DestinacaoHospedagemService;
import br.com.itarocha.betesda.util.validation.ItaValidator;

@RestController
@RequestMapping("/api/app/destinacao_hospedagem")
public class DestinacaoHospedagemController {

	@Autowired
	private DestinacaoHospedagemService service;
	
	@RequestMapping
	@PreAuthorize("hasAnyRole('USER','ADMIN','ROOT')")
	public ResponseEntity<?> listar() {
		List<DestinacaoHospedagem> lista = service.findAll();
	    return new ResponseEntity<List<DestinacaoHospedagem>>(lista, HttpStatus.OK);
	}

	@RequestMapping(value="{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> getById(@PathVariable("id") Long id) {
		try {
			DestinacaoHospedagem model = service.find(id);
			if (model != null) {
				return new ResponseEntity<>(model, HttpStatus.OK);
			} else {
				return new ResponseEntity<>("Destinação de Hospedagem não existe", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
	
	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> gravar(@RequestBody DestinacaoHospedagem model) {
		ItaValidator<DestinacaoHospedagem> v = new ItaValidator<DestinacaoHospedagem>(model);
		v.validate();
		if (!v.hasErrors() ) {
			return new ResponseEntity<>(v.getErrors(), HttpStatus.BAD_REQUEST);
		}
		
		try {
			DestinacaoHospedagem saved = null;
			saved = service.create(model);
		    return new ResponseEntity<DestinacaoHospedagem>(saved, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value="{id}", method = RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN','ROOT')")
	public ResponseEntity<?> excluir(@PathVariable("id") Long id) {
		try {
			service.remove(id);
		    return new ResponseEntity<String>("sucesso", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	 }
}
