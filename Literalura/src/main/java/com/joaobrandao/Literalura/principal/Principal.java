package com.joaobrandao.Literalura.principal;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.joaobrandao.Literalura.model.*;
import com.joaobrandao.Literalura.repository.AutorRepository;
import com.joaobrandao.Literalura.repository.LivroRepository;
import com.joaobrandao.Literalura.service.ConsumoApi;
import com.joaobrandao.Literalura.service.ConverteDados;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;


@Component
public class Principal {
    private final LivroRepository livroRepository;
    private final AutorRepository autorRepository;


    Scanner scanner = new Scanner(System.in);
    ConsumoApi consumoApi = new ConsumoApi();
    ConverteDados converteDados = new ConverteDados();


    public void Menu(){
        var opcao=-1;
        while(opcao!=0){

            System.out.println("""
                Escolha o número de sua opção
                
                1 - Buscar livro pelo título
                2 - Listar livros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos em um determinado ano
                5 - Listar livros em um determinado idioma
                
                0 - Sair
                
                """);
            try {
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer
            } catch (InputMismatchException e) {
                opcao = -1;
                System.out.println("Entrada inválida. Somente números são aceitos.");
                scanner.nextLine(); // Limpa o buffer
            }
            opcao = verificaOpcaoDesejada(opcao);
            System.out.println("Você selecionou a opção: " + opcao);


            switch (opcao) {
                case (1):
                    buscaPorAutor();
                    break;
                case(2):
                    listarLivrosSalvos();
                    break;
                case(3):
                    listarAutoresRegistardos();
                    break;
                case(4):
                    autoresVivosEmDeterminadoAno();
                    break;
                case(5):
                    listarPorIdioma();
                    break;
                case(0):
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida, programa encerrado! ");
            }

        }

    }

    private int verificaOpcaoDesejada(int opcao) {

        try {
            while (opcao < 0 || opcao > 5) {
                System.out.println("Informe uma opção válida (0 a 5): ");
                opcao = scanner.nextInt();
                scanner.nextLine(); // Limpa o buffer
            }
        } catch (InputMismatchException e) {
            System.out.println("Somente números são aceitos");
            scanner.nextLine(); // Limpa o buffer
            opcao = verificaOpcaoDesejada(opcao); // Chama o método novamente
        }

        return opcao;
    }

    private void listarPorIdioma() {
        var idiomaEscolhido = opcoesIdioma();

        livroRepository.findAll().stream()
                .filter(livro -> livro.getIdioma() != null && livro.getIdioma().contains(idiomaEscolhido))
                .forEach(livro -> {
                    String nomesAutores = livro.getAutores().stream()
                            .map(Autor::getNome)
                            .collect(Collectors.joining(", "));
                    System.out.println("O livro " + livro.getTitulo() + " de " + nomesAutores);
                });
    }


    public String opcoesIdioma(){
        System.out.println("""
                Informe o idioma que deseja pesquisar:
                es - Espanhol
                en - Inglês
                fr - Francês
                pt - Português
                """);


        var idiomaEscolhido = scanner.nextLine().toLowerCase();

        verificaIdioma(idiomaEscolhido);


        return idiomaEscolhido;
    }

    private String verificaIdioma(String idiomaEscolhido) {
        while((!idiomaEscolhido.equalsIgnoreCase("es")) &&
                (!idiomaEscolhido.equalsIgnoreCase("pt")) &&
                (!idiomaEscolhido.equalsIgnoreCase("fr")) &&
                (!idiomaEscolhido.equalsIgnoreCase("en"))
        ){
            System.out.println("Informe um idioma válido.");
            idiomaEscolhido = scanner.nextLine().toLowerCase();
        }
        return idiomaEscolhido;
    }

    private void autoresVivosEmDeterminadoAno() {
        System.out.println("Informe um ano para verificar quais eram os escritores vivos: ");
        var ano = scanner.nextInt();

        verificacaoAno(ano);
    }

    private void verificacaoAno(int ano) {

        autorRepository.findAll().stream().forEach(autor -> {
            var nascimento =  autor.getAnoDeNascimento();
            var falecimento = autor.getAnoDeFalecimento();

            if ((ano>nascimento)&&(ano<falecimento)){
                System.out.println("Estava vivo " + autor.getNome());
            }
        });
    }

    private void listarAutoresRegistardos() {
    List<Autor> autoresEncontrados = autorRepository.findAll();
    autoresEncontrados.stream().forEach(System.out::println);

    }

    private void listarLivrosSalvos() {

        List<Livro> livros = livroRepository.findAll();

        if (livros.isEmpty()) {
            System.out.println("Nenhum livro encontrado.");
            return;
        }

        livros.forEach(livro -> {
            System.out.println(String.format("Autor: %s | Título: %s", livro.getAutor(), livro.getTitulo()));
        });
    }


    @Autowired
    public Principal(LivroRepository livroRepository, AutorRepository autorRepository) {
        this.livroRepository = livroRepository;
        this.autorRepository = autorRepository;
    }

    private void buscaPorAutor() {
        try{
            System.out.println("Informe o título que deseja pesquisar: ");
            var tituloDaObra = scanner.nextLine();

            tituloDaObra = formatarTituloObra(tituloDaObra);

            var endereco = "https://gutendex.com/books/?search="+tituloDaObra;
            var json = consumoApi.obterDados(endereco);

            DadosResultado resultado = converteDados.obterDados(json, DadosResultado.class);
            exibirInformacaoAutor(resultado);

            salvarLivro(resultado);
        }
        catch (DataIntegrityViolationException e){
            System.out.println("Um dos resultados apresentou um nome muito longo! ou algum registro está nulo");
        }

    }

    private void salvarLivro(DadosResultado resultado) {
        resultado.livro().stream().forEach(m -> {
            m.autores().forEach(autor -> {
                var nomeAutor = autor.nome();

                boolean livroExiste = livroRepository.existsByTitulo(m.titulo());
                boolean autorExiste = autorRepository.existsByNome(autor.nome());

                if (!livroExiste) {
                    List<Autor> novosAutores = new ArrayList<>();
                    Autor novoAutor = new Autor(autor.nome(), autor.anoNascimento(), autor.anoFalecimento());
                    novosAutores.add(novoAutor);

                    Livro novoLivro = new Livro(m.titulo(), nomeAutor, m.idioma());
                    novoLivro.setAutores(novosAutores);
                    livroRepository.save(novoLivro);

                    if (!autorExiste) {
                        autorRepository.save(novoAutor);
                    }
                } else if (!autorExiste) {
                    Autor novoAutor = new Autor(autor.nome(), autor.anoNascimento(), autor.anoFalecimento());
                    autorRepository.save(novoAutor);
                }


            });
        });
    }


    private void exibirInformacaoAutor(DadosResultado resultado) {
        resultado.livro().stream().forEach(m -> {
            m.autores().forEach(autor -> {
                System.out.println("O nome do livro é " + m.titulo() + " e o autor é "
                        + autor.nome());
            });
        });
    }

    private String formatarTituloObra(String tituloDaObra) {
        return tituloDaObra.trim().replace(" ","%20").toLowerCase();
    }
}
