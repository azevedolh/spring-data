package br.com.alura.spring.data.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.alura.spring.data.model.Funcionario;

public interface FuncionarioRepository extends PagingAndSortingRepository<Funcionario, Integer> {
	List<Funcionario> findByNomeContainsIgnoreCase(String nome);
	
	@Query("SELECT f FROM Funcionario f WHERE lower(f.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND f.salario >= :salario AND f.dataContratacao = :dataContratacao")
	List<Funcionario> findNomeSalarioMaiorDataContratacao(String nome, BigDecimal salario, LocalDate dataContratacao);
	
	@Query("SELECT f FROM Funcionario f LEFT JOIN FETCH f.unidades LEFT JOIN FETCH f.cargo")
	List<Funcionario> findAllFuncionarioWithEagerUnidadesAndCargo();
}
