package br.com.alura.spring.specification;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import br.com.alura.spring.data.model.Funcionario;

public class FuncionarioSpecification {

	public static Specification<Funcionario> nome(String nome){
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(criteriaBuilder.upper(root.get("nome")), "%" + nome.toUpperCase() + "%"); 
	}
	
	public static Specification<Funcionario> cpf(String cpf){
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get("cpf"), cpf); 
	}
	
	public static Specification<Funcionario> dataContratacao(LocalDate dataContratacao){
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("dataContratacao"), dataContratacao); 
	}
	
	public static Specification<Funcionario> salario(BigDecimal salario){
		return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get("salario"), salario); 
	}
}
