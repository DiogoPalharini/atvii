package com.autobots.automanager.controles;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.autobots.automanager.assemblers.AutoModelAssembler;
import com.autobots.automanager.entidades.Auto;
import com.autobots.automanager.servicos.AutoService;

@SpringBootTest
@AutoConfigureMockMvc
public class AutoControleTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private AutoService service;
	
	@MockBean
	private AutoModelAssembler assembler;
	
	@Test
	public void deveRetornarListaDeAutos() throws Exception {
		Auto auto1 = new Auto();
		auto1.setId(1L);
		auto1.setModelo("Fusca");
		auto1.setPlaca("ABC1234");
		
		Auto auto2 = new Auto();
		auto2.setId(2L);
		auto2.setModelo("Gol");
		auto2.setPlaca("XYZ5678");
		
		List<Auto> autos = Arrays.asList(auto1, auto2);
		when(service.obterTodos()).thenReturn(autos);
		when(assembler.toCollectionModel(any())).thenReturn(
				CollectionModel.of(Arrays.asList(
						EntityModel.of(auto1),
						EntityModel.of(auto2)
				))
		);
		
		mockMvc.perform(get("/autos"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void deveRetornarAutoPorId() throws Exception {
		Auto auto = new Auto();
		auto.setId(1L);
		auto.setModelo("Fusca");
		auto.setPlaca("ABC1234");
		
		when(service.obterPorId(1L)).thenReturn(Optional.of(auto));
		when(assembler.toModel(any(Auto.class))).thenReturn(EntityModel.of(auto));
		
		mockMvc.perform(get("/autos/1"))
				.andExpect(status().isOk());
	}
	
	@Test
	public void deveRetornar404QuandoAutoNaoEncontrado() throws Exception {
		when(service.obterPorId(999L)).thenReturn(Optional.empty());
		
		mockMvc.perform(get("/autos/999"))
				.andExpect(status().isNotFound());
	}
	
	@Test
	public void deveCriarNovoAuto() throws Exception {
		Auto auto = new Auto();
		auto.setId(1L);
		auto.setModelo("Fusca");
		auto.setPlaca("ABC1234");
		
		when(service.criar(any(Auto.class))).thenReturn(auto);
		when(assembler.toModel(any(Auto.class))).thenReturn(EntityModel.of(auto));
		
		String jsonAuto = "{\"modelo\":\"Fusca\",\"placa\":\"ABC1234\",\"tipo\":\"Sedan\",\"ano\":1980}";
		
		mockMvc.perform(post("/autos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonAuto))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void deveAtualizarAuto() throws Exception {
		Auto auto = new Auto();
		auto.setId(1L);
		auto.setModelo("Fusca");
		auto.setPlaca("ABC1234");
		
		when(service.atualizar(eq(1L), any(Auto.class))).thenReturn(auto);
		when(assembler.toModel(any(Auto.class))).thenReturn(EntityModel.of(auto));
		
		String jsonAuto = "{\"modelo\":\"Fusca Novo\",\"placa\":\"ABC1234\",\"tipo\":\"Sedan\",\"ano\":1980}";
		
		mockMvc.perform(put("/autos/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonAuto))
				.andExpect(status().isOk());
	}
	
	@Test
	public void deveDeletarAuto() throws Exception {
		mockMvc.perform(delete("/autos/1"))
				.andExpect(status().isNoContent());
	}
}

