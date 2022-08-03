package br.com.alura.spring.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.alura.spring.data.model.Unidade;

public interface UnidadeRepository extends CrudRepository<Unidade, Integer> {
	List<Unidade> findAll();
}
