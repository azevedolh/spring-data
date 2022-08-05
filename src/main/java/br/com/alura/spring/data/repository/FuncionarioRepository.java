package br.com.alura.spring.data.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import br.com.alura.spring.data.model.Funcionario;
import br.com.alura.spring.data.model.FuncionarioProjecao;

public interface FuncionarioRepository extends PagingAndSortingRepository<Funcionario, Integer>, JpaSpecificationExecutor<Funcionario> {
	List<Funcionario> findByNomeContainsIgnoreCase(String nome);
	
	@Query("SELECT f FROM Funcionario f WHERE lower(f.nome) LIKE LOWER(CONCAT('%', :nome, '%')) AND f.salario >= :salario AND f.dataContratacao = :dataContratacao")
	List<Funcionario> findNomeSalarioMaiorDataContratacao(String nome, BigDecimal salario, LocalDate dataContratacao);
	
	@Query(value = "SELECT f FROM Funcionario f LEFT JOIN FETCH f.unidades LEFT JOIN FETCH f.cargo", countQuery = "SELECT COUNT (f) FROM Funcionario f")
	Page<Funcionario> findAllFuncionarioWithEagerUnidadesAndCargo(Pageable pageable);
	
	@Query(value = "SELECT f.id, f.nome, f.salario FROM funcionarios f", nativeQuery = true)
	List<FuncionarioProjecao> findFuncionarioAndSalario();
}
