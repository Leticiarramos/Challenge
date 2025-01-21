package br.com.literalura.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "livros")
public class Livro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    private String autor;
    private String idiomas;
    private Integer downloads;

    public Livro () {}

    public Livro (DadosLivro dadosLivro) {
        this.titulo = dadosLivro.titulo();
        this.autor = pegaAutor(dadosLivro).getNome();
        this.idiomas = idiomaMod(dadosLivro.idiomas());
        this.downloads = dadosLivro.downloads();
    }

    private String idiomaMod(List<String> idiomas) {
        if (idiomas == null || idiomas.isEmpty()) {
            return "desconhecido";
        } else {
            return idiomas.get(0);
        }
    }

    public Autor pegaAutor(DadosLivro dadosLivro) {
        DadosAutor dadosAutor = dadosLivro.autor().get(0);
        return new Autor(dadosAutor);
    }


    @Override
    public String toString() {
        return "\n\t**** info livro ****" +
                "\n\tTitulo: " + titulo +
                "\n\tAutor: " + autor +
                "\n\tIdiomas: " + idiomas +
                "\n\tDownloads: " + downloads +
                "\n\t*******************";
    }
}
