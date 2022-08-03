package br.com.alura.spring.data.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import br.com.alura.spring.data.model.Funcionario;
import br.com.alura.spring.data.repository.FuncionarioRepository;

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
				default:
					System.out.println("Erro: Opcao inválida.");
					break;
				}
			} catch (NumberFormatException e) {
				System.out.println("Erro: Opcao inválida.");
			}
		}
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
		
		List<Funcionario> funcionarios = funcionarioRepository.findNomeSalarioMaiorDataContratacao(nome, salario, dataContratacao);
		
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
