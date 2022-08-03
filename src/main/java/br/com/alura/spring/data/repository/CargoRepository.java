package br.com.alura.spring.data.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.alura.spring.data.model.Cargo;

public interface CargoRepository extends CrudRepository<Cargo, Integer> {
	List<Cargo> findAll();
}
