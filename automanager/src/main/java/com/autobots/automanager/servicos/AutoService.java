package com.autobots.automanager.servicos;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.autobots.automanager.entidades.Auto;
import com.autobots.automanager.excecoes.ResourceNotFoundException;
import com.autobots.automanager.repositorios.AutoRepositorio;

@Service
public class AutoService {
	
	@Autowired
	private AutoRepositorio repositorio;
	
	public List<Auto> obterTodos() {
		return repositorio.findAll();
	}
	
	public Optional<Auto> obterPorId(Long id) {
		return repositorio.findById(id);
	}
	
	public Auto criar(Auto auto) {
		return repositorio.save(auto);
	}
	
	public Auto atualizar(Long id, Auto autoAtualizado) {
		Optional<Auto> autoExistente = repositorio.findById(id);
		if (autoExistente.isPresent()) {
			Auto auto = autoExistente.get();
			auto.setModelo(autoAtualizado.getModelo());
			auto.setPlaca(autoAtualizado.getPlaca());
			auto.setTipo(autoAtualizado.getTipo());
			auto.setAno(autoAtualizado.getAno());
			auto.setProprietario(autoAtualizado.getProprietario());
			return repositorio.save(auto);
		}
		throw new ResourceNotFoundException("Auto", id);
	}
	
	public void deletar(Long id) {
		if (!repositorio.existsById(id)) {
			throw new ResourceNotFoundException("Auto", id);
		}
		repositorio.deleteById(id);
	}
}

