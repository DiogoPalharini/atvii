package com.autobots.automanager.controles;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.autobots.automanager.assemblers.AutoModelAssembler;
import com.autobots.automanager.entidades.Auto;
import com.autobots.automanager.servicos.AutoService;

@RestController
@RequestMapping("/autos")
public class AutoControle {
	
	@Autowired
	private AutoService service;
	
	@Autowired
	private AutoModelAssembler assembler;
	
	@GetMapping
	public ResponseEntity<CollectionModel<EntityModel<Auto>>> obterTodosAutos() {
		List<Auto> autos = service.obterTodos();
		return ResponseEntity.ok(assembler.toCollectionModel(autos));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<Auto>> obterAuto(@PathVariable Long id) {
		return service.obterPorId(id)
				.map(auto -> ResponseEntity.ok(assembler.toModel(auto)))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@PostMapping
	public ResponseEntity<EntityModel<Auto>> criarAuto(@Valid @RequestBody Auto auto) {
		Auto autoCriado = service.criar(auto);
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(autoCriado.getId())
				.toUri();
		EntityModel<Auto> entityModel = assembler.toModel(autoCriado);
		return ResponseEntity.created(location).body(entityModel);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EntityModel<Auto>> atualizarAuto(@PathVariable Long id, @Valid @RequestBody Auto auto) {
		try {
			Auto autoAtualizado = service.atualizar(id, auto);
			return ResponseEntity.ok(assembler.toModel(autoAtualizado));
		} catch (com.autobots.automanager.excecoes.ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarAuto(@PathVariable Long id) {
		try {
			service.deletar(id);
			return ResponseEntity.noContent().build();
		} catch (com.autobots.automanager.excecoes.ResourceNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}

