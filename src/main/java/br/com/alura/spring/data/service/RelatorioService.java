package br.com.alura.spring.data.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import javax.transaction.Transactional;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import br.com.alura.spring.data.model.Funcionario;
import br.com.alura.spring.data.model.FuncionarioProjecao;
import br.com.alura.spring.data.repository.FuncionarioRepository;
import br.com.alura.spring.specification.FuncionarioSpecification;

@Service
@Transactional
public class RelatorioService {
	private Boolean system;
	private final FuncionarioRepository funcionarioRepository;
	private final String DATA_PATTERN = "dd/MM/yyyy";

	public RelatorioService(FuncionarioRepository funcionarioRepository) {
		this.funcionarioRepository = funcionarioRepository;
	}

	public void inicial(Scanner scanner) {
		system = true;

		while (system) {
			System.out.println("Selecione o tipo de relatório:");
			System.out.println("0 - Voltar");
			System.out.println("1 - Pesquisar Funcionário pelo nome");
			System.out.println("2 - Pesquisar Funcionário pelo nome, salario e data de contratação:");
			System.out.println("3 - Pesquisar Salarios dos Funcionarios");
			System.out.println("4 - Pesquisa Dinâmica");

			String opcaoString = scanner.nextLine();
			try {
				Integer opcao = Integer.valueOf(opcaoString);

				switch (opcao) {
				case 0:
					system = false;
					break;
				case 1:
					this.buscarFuncionarioPorNome(scanner);
					break;
				case 2:
					this.buscarFuncionarioPorNomeSalarioMaiorDataContratacao(scanner);
					break;
				case 3:
					this.listarSalarios();
					break;
				case 4:
					this.listarFuncionarioPorNome(scanner);
					break;
				default:
					System.out.println("Erro: Opcao inválida.");
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Erro: Opcao inválida.");
			}
		}
	}

	private void listarFuncionarioPorNome(Scanner scanner) {
		Specification<Funcionario> where = Specification.where(null);

		System.out.println("Digite o nome dos funcionários que deseja pesquisar");
		String nome = scanner.nextLine();
		if (!nome.trim().isEmpty()) {
			where = where.and(FuncionarioSpecification.nome(nome));
		}

		System.out.println("Digite o cpf dos funcionários que deseja pesquisar");
		String cpf = scanner.nextLine();
		if (!cpf.trim().isEmpty()) {
			where = where.and(FuncionarioSpecification.cpf(cpf));
		}

		System.out.println("Digite a data de contratação dos funcionários que deseja pesquisar");
		String inputUsuarioData = scanner.nextLine();
		LocalDate dataContratacao = null;

		if (!inputUsuarioData.trim().isEmpty()) {
			dataContratacao = LocalDate.parse(inputUsuarioData, DateTimeFormatter.ofPattern(DATA_PATTERN));
			where = where.and(FuncionarioSpecification.dataContratacao(dataContratacao));
		}

		System.out.println("Digite o salário dos funcionários que deseja pesquisar");
		String inputUsuarioSalario = scanner.nextLine();
		BigDecimal salario = null;

		if (!inputUsuarioSalario.trim().isEmpty()) {
			try {
				salario = new BigDecimal(inputUsuarioSalario);
				where = where.and(FuncionarioSpecification.salario(salario));
			} catch (NumberFormatException e) {
				System.out.println("Erro: Salário inválido. Operação cancelada.");
				return;
			}
		}

		List<Funcionario> funcionarios = funcionarioRepository.findAll(where);

//      Também é possível fazer assim (cada método da Specification retorna uma Specification com aquele método adicionado) (porém assim não fica dinâmico):
//		List<Funcionario> funcionarios = funcionarioRepository
//				.findAll(Specification.where(FuncionarioSpecification.nome(nome)).and(FuncionarioSpecification.cpf(cpf))
//						.and(FuncionarioSpecification.dataContratacao(dataContratacao))
//						.and(FuncionarioSpecification.salario(salario)));

		funcionarios.forEach(System.out::println);
	}

	private void listarSalarios() {
		List<FuncionarioProjecao> funcionarioProjecao = funcionarioRepository.findFuncionarioAndSalario();

		funcionarioProjecao.forEach(f -> System.out
				.println("Id: " + f.getId() + " | Nome: " + f.getNome() + " | Salario: " + f.getSalario()));
	}

	private void buscarFuncionarioPorNomeSalarioMaiorDataContratacao(Scanner scanner) {
		String inputUsuario;

		System.out.println("Digite o nome do funcionário que deseja pesquisar:");
		String nome = scanner.nextLine();

		System.out.println("Digite a partir de qual salário deseja pesquisar:");
		inputUsuario = scanner.nextLine();
		BigDecimal salario = new BigDecimal(inputUsuario);

		System.out.println("Digite a data de contratação que deseja pesquisar no formato DD/MM/AAAA:");
		inputUsuario = scanner.nextLine();
		LocalDate dataContratacao = LocalDate.parse(inputUsuario, DateTimeFormatter.ofPattern(DATA_PATTERN));

		List<Funcionario> funcionarios = funcionarioRepository.findNomeSalarioMaiorDataContratacao(nome, salario,
				dataContratacao);

		funcionarios.forEach(System.out::println);
	}

	private void buscarFuncionarioPorNome(Scanner scanner) {
		System.out.println("Digite o nome do funcionário que deseja pesquisar:");

		String inputUsuario = scanner.nextLine();

		List<Funcionario> funcionarios = funcionarioRepository.findByNomeContainsIgnoreCase(inputUsuario);

		if (funcionarios.isEmpty()) {
			System.out.println("Nenhum Funcionário encontrado com este nome!");
			return;
		}

		funcionarios.forEach(System.out::println);
	}
}
