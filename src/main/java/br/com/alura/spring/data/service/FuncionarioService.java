package br.com.alura.spring.data.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import br.com.alura.spring.data.model.Cargo;
import br.com.alura.spring.data.model.Funcionario;
import br.com.alura.spring.data.model.Unidade;
import br.com.alura.spring.data.repository.FuncionarioRepository;

@Service
@Transactional
public class FuncionarioService {
	private final FuncionarioRepository funcionarioRepository;
	private final CargoService cargoService;
	private final UnidadeService unidadeService;
	private Boolean system;

	public FuncionarioService(FuncionarioRepository funcionarioRepository, CargoService cargoService,
			UnidadeService unidadeService) {
		this.funcionarioRepository = funcionarioRepository;
		this.cargoService = cargoService;
		this.unidadeService = unidadeService;
	}

	public void inicial(Scanner scanner) {
		system = true;

		while (system) {
			System.out.println("Selecione a operação para a entidade FUNCIONARIO:");
			System.out.println("0 - Voltar");
			System.out.println("1 - Inserir");
			System.out.println("2 - Atualizar");
			System.out.println("3 - Listar Todos");
			System.out.println("4 - Deletar");

			String opcaoString = scanner.nextLine();
			try {
				Integer opcao = Integer.valueOf(opcaoString);

				switch (opcao) {
				case 0:
					system = false;
					break;
				case 1:
					this.salvar(scanner);
					break;
				case 2:
					this.exibirMenuAtualizar(scanner);
					break;
				case 3:
					this.listarTodos(scanner);
					break;
				case 4:
					this.deletar(scanner);
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

	private void exibirMenuAtualizar(Scanner scanner) {
		Boolean isVoltar = false;

		System.out.println("Digite o id do Funcionario que deseja atualizar: ");
		String idString = scanner.nextLine();
		Funcionario funcionario;

		try {
			Integer id = Integer.valueOf(idString);
			Optional<Funcionario> funcionarioOptional = this.funcionarioRepository.findById(id);

			if (!funcionarioOptional.isPresent()) {
				System.out.println("Erro: Funcionario não encontrado. Operação Cancelada.");
				return;
			}

			funcionario = funcionarioOptional.get();
		} catch (NumberFormatException e) {
			System.out.println("Erro: Id inválido. Operação cancelada.");
			return;
		}

		while (!isVoltar) {
			System.out.println(funcionario.toString());
			System.out.println("Selecione o campo que deseja atualizar: ");
			System.out.println("0 - Voltar");
			System.out.println("1 - Nome");
			System.out.println("2 - CPF");
			System.out.println("3 - Salario");
			System.out.println("4 - Data de Contratação");
			System.out.println("5 - cargo");
			System.out.println("6 - Unidades");

			String opcaoString = scanner.nextLine();
			try {
				Integer opcao = Integer.valueOf(opcaoString);

				switch (opcao) {
				case 0:
					isVoltar = true;
					break;
				case 1:
					this.atualizarNome(funcionario, scanner);
					break;
				case 2:
					this.atualizarCpf(funcionario, scanner);
					break;
				case 3:
					this.atualizarSalario(funcionario, scanner);
					break;
				case 4:
					this.atualizarDataDeContratacao(funcionario, scanner);
					break;
				case 5:
					this.atualizarCargo(funcionario, scanner);
					break;
				case 6:
					this.exibirMenuAtualizarUnidades(funcionario, scanner);
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

	private void exibirMenuAtualizarUnidades(Funcionario funcionario, Scanner scanner) {
		Boolean isVoltar = false;

		System.out.println("Unidades associadas ao Funcionário:");

		List<Unidade> unidadesFuncionario = funcionario.getUnidades();

		for (int i = 0; i < unidadesFuncionario.size(); i++) {
			System.out.println((i + 1) + " - " + unidadesFuncionario.get(i).getDescricao());
		}

		System.out.println(funcionario.toString());
		System.out.println("Selecione uma opção abaixo: ");
		System.out.println("0 - voltar");
		System.out.println("1 - Incluir");
		System.out.println("2 - Excluir");

		String opcaoString = scanner.nextLine();
		try {
			Integer opcao = Integer.valueOf(opcaoString);

			switch (opcao) {
			case 0:
				isVoltar = true;
				break;
			case 1:
				this.incluirUnidade(funcionario, scanner);
				break;
			case 2:
				this.excluirUnidade(funcionario, scanner);
				break;
			default:
				System.out.println("Erro: Opcao inválida.");
				break;
			}
		} catch (NumberFormatException e) {
			System.out.println("Erro: Opcao inválida.");
		}
	}

	private void excluirUnidade(Funcionario funcionario, Scanner scanner) {
		Boolean hasNextUnidade = true;
		List<Unidade> unidadesFuncionario = new ArrayList<Unidade>(funcionario.getUnidades());

		while (hasNextUnidade) {
			System.out.println("Selecione uma das Unidades disponíveis para excluir: ");
			System.out.println("0 - Voltar");

			for (int i = 0; i < unidadesFuncionario.size(); i++) {
				System.out.println((i + 1) + " - " + unidadesFuncionario.get(i).getDescricao());
			}
			String inputUsuario = scanner.nextLine();
			Integer inputUsuarioInt;

			try {
				inputUsuarioInt = Integer.valueOf(inputUsuario);
			} catch (NumberFormatException e) {
				System.out.println("Erro: Opção inválida, operação cancelada.");
				return;
			}

			if (inputUsuarioInt.equals(0)) {
				break;
			}

			unidadesFuncionario.remove(unidadesFuncionario.get(inputUsuarioInt - 1));
			funcionario.setUnidades(unidadesFuncionario);
			this.funcionarioRepository.save(funcionario);
		}

	}

	private void incluirUnidade(Funcionario funcionario, Scanner scanner) {
		Boolean hasNextUnidade = true;
		List<Unidade> unidadesFuncionario = new ArrayList<Unidade>(funcionario.getUnidades());

		while (hasNextUnidade) {
			System.out.println("Selecione uma das Unidades disponíveis para inclusão: ");
			System.out.println("0 - Voltar");
			List<Unidade> unidades = this.unidadeService.recuperarUnidades();
			unidades = unidades.stream().filter(u -> !unidadesFuncionario.contains(u)).collect(Collectors.toList());
			for (int i = 0; i < unidades.size(); i++) {
				System.out.println((i + 1) + " - " + unidades.get(i).getDescricao());
			}
			String inputUsuario = scanner.nextLine();
			try {
				Integer inputUsuarioInt = Integer.valueOf(inputUsuario);

				if (inputUsuarioInt.equals(0)) {
					break;
				}

				unidadesFuncionario.add(unidades.get(inputUsuarioInt - 1));
				funcionario.setUnidades(unidadesFuncionario);
				this.funcionarioRepository.save(funcionario);
			} catch (NumberFormatException e) {
				System.out.println("Erro: Opção inválida. Operação cancelada.");
			}

		}

	}

	private void atualizarCargo(Funcionario funcionario, Scanner scanner) {
		System.out.println("Selecione o novo Cargo do funcionário:");
		System.out.println("0 - Cancelar Operação");

		List<Cargo> cargos = cargoService.recuperarCargos();

		for (int i = 0; i < cargos.size(); i++) {
			System.out.println((i + 1) + " - " + cargos.get(i).getDescricao());
		}

		String inputUsuario = scanner.nextLine();

		Cargo funcionarioCargo;

		try {
			Integer inputUsuarioInt = Integer.valueOf(inputUsuario);

			if (inputUsuarioInt.equals(0)) {
				System.out.println("Erro: Cargo não selecionado, operação cancelada.");
				return;
			}

			funcionarioCargo = cargos.get(inputUsuarioInt - 1);
		} catch (NumberFormatException e) {
			System.out.println("Erro: Opção inválida. operação cancelada.");
			return;
		}

		funcionario.setCargo(funcionarioCargo);
		this.funcionarioRepository.save(funcionario);
	}

	private void atualizarDataDeContratacao(Funcionario funcionario, Scanner scanner) {
		System.out.println("Informe a nova data de contratação do funcionario no formato dd/mm/aaaa:");

		String inputUsuario = scanner.nextLine();

		if (inputUsuario.isEmpty()) {
			System.out.println("Erro: Operação não executada, data de contratação do funcionario não informada.");
			return;
		}

		LocalDate funcionarioDataContratacao = LocalDate.parse(inputUsuario, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		funcionario.setDataContratacao(funcionarioDataContratacao);
		this.funcionarioRepository.save(funcionario);
	}

	private void atualizarSalario(Funcionario funcionario, Scanner scanner) {
		System.out.println("Digite o novo Salario para o funcionário: ");
		String inputUsuario = scanner.nextLine();

		try {
			BigDecimal inputUsuarioInt = new BigDecimal(inputUsuario);

			if (inputUsuarioInt.equals(BigDecimal.ZERO)) {
				System.out.println("Erro: Salario não informado. Operação cancelada.");
				return;
			}
			funcionario.setSalario(inputUsuarioInt);
			this.funcionarioRepository.save(funcionario);
		} catch (NumberFormatException e) {
			System.out.println("Erro: Salário inválido. Operação cancelada. ");
		}
	}

	private void atualizarCpf(Funcionario funcionario, Scanner scanner) {
		System.out.println("Digite o novo CPF para o funcionário: ");
		String inputUsuario = scanner.nextLine();

		if (inputUsuario.isEmpty()) {
			System.out.println("Erro: CPF não informado. Operação cancelada.");
			return;
		}
		funcionario.setCpf(inputUsuario);
		this.funcionarioRepository.save(funcionario);
	}

	private void atualizarNome(Funcionario funcionario, Scanner scanner) {
		System.out.println("Digite o novo nome para o funcionário: ");
		String inputUsuario = scanner.nextLine();

		if (inputUsuario.isEmpty()) {
			System.out.println("Erro: Nome não informado. Operação cancelada.");
			return;
		}
		funcionario.setNome(inputUsuario);
		this.funcionarioRepository.save(funcionario);
	}

	private void deletar(Scanner scanner) {
		System.out.println("Digite o id do Funcionario que deseja deletar: ");
		String idString = scanner.nextLine();
		try {
			Integer id = Integer.valueOf(idString);
			this.funcionarioRepository.deleteById(id);

		} catch (NumberFormatException e) {
			System.out.println("Erro: Id inválido.");
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Erro: Funcionario não encontrado");
		}
	}

	private void listarTodos(Scanner scanner) {
		
		System.out.println("Informe a página que deseja consultar: ");
		
		String inputUsuario = scanner.nextLine();
		
		Integer pageNumber;
		
		try {
			pageNumber = Integer.valueOf(inputUsuario);
		} catch (NumberFormatException e) {
			System.out.println("Página informada inválida. Operação cancelada.");
			return;
		}
		
		Integer PageIndex = pageNumber - 1;
		
		Pageable pageable = PageRequest.of(PageIndex, 5, Sort.by(Sort.Direction.ASC, "nome"));
		
		System.out.println("Funcionarios: ");

		Page<Funcionario> funcionarioLista = this.funcionarioRepository.findAll(pageable);
		
		System.out.println("Página atual: " + (funcionarioLista.getNumber() + 1));
		System.out.println("Numero total de elementos: " + funcionarioLista.getTotalElements());
		System.out.println("Numero total de páginas: " + funcionarioLista.getTotalPages());

		if (funcionarioLista.isEmpty()) {
			System.out.println("Nenhum Registro Encontrado!");
		} else {
			funcionarioLista.forEach(c -> System.out.println(c));
		}

		System.out.println(" ");
		System.out.println("Pressione ENTER para voltar ao menu");

		scanner.nextLine();
	}

	private void salvar(Scanner scanner) {
		System.out.println("Informe o nome do funcionario a ser inserido:");

		String inputUsuario;

		inputUsuario = scanner.nextLine();

		if (inputUsuario.isEmpty()) {
			System.out.println("Erro: Operação não executada, nome do funcionario não informado.");
			return;
		}

		String funcionarioNome = inputUsuario;

		System.out.println("Informe o cpf do funcionario a ser inserido:");

		inputUsuario = scanner.nextLine();

		if (inputUsuario.isEmpty()) {
			System.out.println("Erro: Operação não executada, cpf do funcionario não informado.");
			return;
		}

		String funcionarioCpf = inputUsuario;

		System.out.println("Informe o salario do funcionario a ser inserido:");

		inputUsuario = scanner.nextLine();

		if (inputUsuario.isEmpty()) {
			System.out.println("Erro: Operação não executada, salario do funcionario não informado.");
			return;
		}

		BigDecimal funcionarioSalario = new BigDecimal(inputUsuario);

		System.out.println("Informe a data de contratação do funcionario a ser inserido no formato dd/mm/aaaa:");

		inputUsuario = scanner.nextLine();

		if (inputUsuario.isEmpty()) {
			System.out.println("Erro: Operação não executada, salario do funcionario não informado.");
			return;
		}

		LocalDate funcionarioDataContratacao = LocalDate.parse(inputUsuario, DateTimeFormatter.ofPattern("dd/MM/yyyy"));

		System.out.println("Selecione o Cargo do funcionário:");
		System.out.println("0 - Cancelar Operação");

		List<Cargo> cargos = cargoService.recuperarCargos();

		for (int i = 0; i < cargos.size(); i++) {
			System.out.println((i + 1) + " - " + cargos.get(i).getDescricao());
		}

		inputUsuario = scanner.nextLine();

		Cargo funcionarioCargo;

		try {
			Integer inputUsuarioInt = Integer.valueOf(inputUsuario);

			if (inputUsuarioInt.equals(0)) {
				System.out.println("Erro: Cargo não selecionado, operação cancelada.");
				return;
			}

			funcionarioCargo = cargos.get(inputUsuarioInt - 1);
		} catch (NumberFormatException e) {
			System.out.println("Erro: Opção inválida. operação cancelada.");
			return;
		}

		Boolean hasNextUnidade = true;
		List<Unidade> unidadesRecebidas = new ArrayList<Unidade>();
		List<Unidade> unidades = unidadeService.recuperarUnidades();

		while (hasNextUnidade) {
			System.out.println(
					"Selecione a(s) unidade(s) do funcionario a ser inserido, após finalização selecionar a opção CONTINUAR:");
			System.out.println("0 - Continuar");

			unidades = unidades.stream().filter(u -> !unidadesRecebidas.contains(u)).collect(Collectors.toList());

			if (unidades.isEmpty() && unidadesRecebidas.isEmpty()) {
				System.out
						.println("Nenhuma Unidade disponível, cadastre uma unidade antes de cadastrar o funcionário.");
				return;
			}

			for (int i = 0; i < unidades.size(); i++) {
				System.out.println((i + 1) + " - " + unidades.get(i).getDescricao());
			}

			inputUsuario = scanner.nextLine();

			Integer inputUsuarioInt = Integer.valueOf(inputUsuario);

			if (inputUsuarioInt.equals(0)) {
				break;
			}

			unidadesRecebidas.add(unidades.get(inputUsuarioInt - 1));
		}

		if (unidadesRecebidas.isEmpty()) {
			System.out.println("Erro: Nenhuma Unidade selecionada. Operação cancelada.");
		}

		Funcionario funcionario = new Funcionario(funcionarioNome, funcionarioCpf, funcionarioSalario,
				funcionarioDataContratacao, funcionarioCargo, unidadesRecebidas);

		this.funcionarioRepository.save(funcionario);
	}

}
