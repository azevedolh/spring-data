package br.com.alura.spring.data.service;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import br.com.alura.spring.data.model.Unidade;
import br.com.alura.spring.data.model.Unidade;
import br.com.alura.spring.data.repository.UnidadeRepository;

@Service
public class UnidadeService {
	private final UnidadeRepository unidadeRepository;
	private Boolean system;

	public UnidadeService(UnidadeRepository unidadeRepository) {
		this.unidadeRepository = unidadeRepository;
	}

	public void inicial(Scanner scanner) {
		system = true;

		while (system) {
			System.out.println("Selecione a operação para a entidade UNIDADE:");
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
					this.atualizar(scanner);
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

	public List<Unidade> recuperarUnidades() {
		List<Unidade> unidadeLista = this.unidadeRepository.findAll();
		return unidadeLista;
	}

	private void deletar(Scanner scanner) {
		System.out.println("Digite o id do Unidade que deseja deletar: ");
		String idString = scanner.nextLine();
		try {
			Integer id = Integer.valueOf(idString);
			this.unidadeRepository.deleteById(id);

		} catch (NumberFormatException e) {
			System.out.println("Erro: Id inválido.");
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Erro: Unidade não encontrado");
		}
	}

	private void listarTodos(Scanner scanner) {
		System.out.println("Unidades: ");

		List<Unidade> unidadeLista = recuperarUnidades();

		if (unidadeLista.isEmpty()) {
			System.out.println("Nenhum Registro Encontrado!");
		} else {
			unidadeLista.forEach(c -> System.out.println(c));
		}

		System.out.println(" ");
		System.out.println("Pressione ENTER para voltar ao menu");

		scanner.nextLine();
	}

	private void atualizar(Scanner scanner) {
		Boolean isVoltar = false;

		System.out.println("Digite o id da Unidade que deseja atualizar: ");
		String idString = scanner.nextLine();
		Unidade unidade;

		try {
			Integer id = Integer.valueOf(idString);
			Optional<Unidade> unidadeOptional = this.unidadeRepository.findById(id);

			if (!unidadeOptional.isPresent()) {
				System.out.println("Erro: Unidade não encontrado. Operação Cancelada.");
				return;
			}

			unidade = unidadeOptional.get();
		} catch (NumberFormatException e) {
			System.out.println("Erro: Id inválido. Operação cancelada.");
			return;
		}

		while (!isVoltar) {
			System.out.println(unidade.toString());
			System.out.println("Selecione o campo que deseja atualizar: ");
			System.out.println("0 - Voltar");
			System.out.println("1 - Descrição");
			System.out.println("2 - Endereço");

			String opcaoString = scanner.nextLine();
			try {
				Integer opcao = Integer.valueOf(opcaoString);

				switch (opcao) {
				case 0:
					isVoltar = true;
					break;
				case 1:
					this.atualizarDescricao(unidade, scanner);
					break;
				case 2:
					this.atualizarEndereco(unidade, scanner);
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

	private void atualizarEndereco(Unidade unidade, Scanner scanner) {
		System.out.println("Digite o novo endereço para o funcionário: ");
		String inputUsuario = scanner.nextLine();

		if (inputUsuario.isEmpty()) {
			System.out.println("Erro: endereço não informado. Operação cancelada.");
			return;
		}
		unidade.setEndereco(inputUsuario);
		this.unidadeRepository.save(unidade);
	}

	private void atualizarDescricao(Unidade unidade, Scanner scanner) {
		System.out.println("Digite a nova descrição para o funcionário: ");
		String inputUsuario = scanner.nextLine();

		if (inputUsuario.isEmpty()) {
			System.out.println("Erro: descricao não informada. Operação cancelada.");
			return;
		}
		unidade.setDescricao(inputUsuario);
		this.unidadeRepository.save(unidade);
	}

	private void salvar(Scanner scanner) {
		System.out.println("Informar unidade a ser inserida:");

		String unidadeDescricao;

		unidadeDescricao = scanner.nextLine();

		if (unidadeDescricao.isEmpty()) {
			System.out.println("Erro: Operação não executada, unidade não informada.");
			return;
		}
		
		System.out.println("Informar endereço da unidade a ser inserida:");

		String unidadeEndereco;

		unidadeEndereco = scanner.nextLine();

		if (unidadeEndereco.isEmpty()) {
			System.out.println("Erro: Operação não executada, endereço da unidade não informada.");
			return;
		}

		Unidade unidade = new Unidade(unidadeDescricao, unidadeEndereco);
		this.unidadeRepository.save(unidade);
	}

}
