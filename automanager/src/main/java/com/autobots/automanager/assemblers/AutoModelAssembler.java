package com.autobots.automanager.assemblers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.AutoControle;
import com.autobots.automanager.entidades.Auto;

@Component
public class AutoModelAssembler implements RepresentationModelAssembler<Auto, EntityModel<Auto>> {

	@Override
	public EntityModel<Auto> toModel(Auto auto) {
		return EntityModel.of(auto,
				linkTo(methodOn(AutoControle.class).obterAuto(auto.getId())).withSelfRel(),
				linkTo(methodOn(AutoControle.class).obterTodosAutos()).withRel("autos"));
	}

	@Override
	public CollectionModel<EntityModel<Auto>> toCollectionModel(Iterable<? extends Auto> entities) {
		return RepresentationModelAssembler.super.toCollectionModel(entities)
				.add(linkTo(methodOn(AutoControle.class).obterTodosAutos()).withSelfRel());
	}
}

