package br.com.literalura;


import br.com.literalura.repository.iAutorRepository;
import br.com.literalura.repository.iLivrosRepository;
import br.com.literalura.model.Autor;
import br.com.literalura.model.DadosLivro;
import br.com.literalura.model.Livro;
import br.com.literalura.model.Results;
import br.com.literalura.service.ConsumoAPI;
import br.com.literalura.service.ConverteDados;

import java.util.*;

public class Principal {
    private Scanner scan = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();
    private iLivrosRepository livrosRepositorio;
    private iAutorRepository autorRepositorio;
    private static String API_URL = "https://gutendex.com/books/?search=";

    List<Livro> livros;
    List<Autor> autor;

    public Principal(iLivrosRepository livrosRepositorio, iAutorRepository autorRepositorio) {
        this.livrosRepositorio = livrosRepositorio;
        this.autorRepositorio = autorRepositorio;
    }

    public void exibeMenu() {
        var opcao = -1;
        while (opcao != 0) {
            var menu = """
                    Bem vindo ao LiterAlura!
                    
                    Escolha uma opção:
                    
                    1 - Buscar livro por nome
                    2 - Listar livros salvos
                    3 - Listar autores salvos
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros por idioma
                    
                    0 - sair
                    

                    
                    """;
            try {
                System.out.println(menu);
                opcao = scan.nextInt();
                scan.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("número inválido");
                scan.nextLine();
                continue;
            }

            switch (opcao) {
                case 1:
                    buscarLivro();
                    break;
                case 2:
                    listarLivrosSalvos();
                    break;
                case 3:
                    listarAutoresSalvos();
                    break;
                case 4:
                    listarAutoreVivosEmUmAno();
                    break;
                case 5:
                    listarLivrosPorIdioma();
                    break;
                case 0:
                    System.out.println("encerrando!");
                    ;
                    break;
                default:
                    System.out.println("opção incorreta, tente novamente!");

                    exibeMenu();
                    break;
            }
        }
    }

    private void listarLivrosPorIdioma() {
        System.out.println("lista de livros por idioma\n------------");
        System.out.println("""
                \n\t---- escolha o idioma ----
                \ten - inglês
                \tes - espanhol
                \tfr - francês
                \tpt - português
                """);
        String idioma = scan.nextLine();
        livros = livrosRepositorio.findByIdiomasContains(idioma);
        if (livros.isEmpty()) {
            System.out.println("livros pelo idioma escolhido não encontrado");
            listarLivrosPorIdioma();
        } else {
            livros.stream()
                    .sorted(Comparator.comparing(Livro::getTitulo))
                    .forEach(System.out::println);
        }
    }

    private void listarAutoreVivosEmUmAno() {
        System.out.println("liste os autores vivos em um determinado ano... por favor, insira o ano");
        Integer ano = Integer.valueOf(scan.nextLine());
        autor = autorRepositorio
                .findByAnoNascimentoLessThanEqualAndAnoFalecimentoGreaterThanEqual(ano, ano);
        if (autor.isEmpty()) {
            System.out.println("autores vivos não encontrados");
        } else {
            autor.stream()
                    .sorted(Comparator.comparing(Autor::getNome))
                    .forEach(System.out::println);
        }
    }

    private void listarAutoresSalvos() {
        System.out.println("lista de autores no banco de dados\n------------");
        autor = autorRepositorio.findAll();
        autor.stream()
                .sorted(Comparator.comparing(Autor::getNome))
                .forEach(System.out::println);
    }

    private void listarLivrosSalvos() {
        System.out.println("lista de livros no banco de dados\n------------");
        livros = livrosRepositorio.findAll();
        livros.stream()
                .sorted(Comparator.comparing(Livro::getTitulo))
                .forEach(System.out::println);
    }

    private void buscarLivro() {
        System.out.println("qual livro deseja encontrar? ");
        var nomeLivro = scan.nextLine().toLowerCase();
        var json = consumo.obterDados(API_URL + nomeLivro.replace(" ", "%20").trim());
        var dados = conversor.obterDados(json, Results.class);
        if (dados.results().isEmpty()) {
            System.out.println("livro não encontrado");;
        } else {
            DadosLivro dadosLivro = dados.results().get(0);
            Livro livro = new Livro(dadosLivro);
            Autor autor = new Autor().pegaAutor(dadosLivro);
            salvarDados(livro, autor);
        }
    }

    private void salvarDados(Livro livro, Autor autor) {
        Optional<Livro> livroEncontrado = livrosRepositorio.findByTituloContains(livro.getTitulo());
        if (livroEncontrado.isPresent()) {
            System.out.println("esse livro já existe no banco de dados");
            System.out.println(livro.toString());
        } else {
            try {
                livrosRepositorio.save(livro);
                System.out.println("livro salvo");
                System.out.println(livro);
            } catch (Exception e) {
                System.out.println("erro: " + e.getMessage());
            }
        }

        Optional<Autor> autorEncontrado = autorRepositorio.findByNomeContains(autor.getNome());
        if (autorEncontrado.isPresent()) {
            System.out.println("esse autor já existe no banco de dados");
            System.out.println(autor.toString());
        } else {
            try {
                autorRepositorio.save(autor);
                System.out.println("autor salvo");
                System.out.println(autor);
            } catch (Exception e) {
                System.out.println("erro: " + e.getMessage());
            }
        }
    }

}
