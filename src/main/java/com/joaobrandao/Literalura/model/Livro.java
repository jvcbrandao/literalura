package com.joaobrandao.Literalura.model;

import jakarta.persistence.*;

import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Livro {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String titulo;
        private String autor;
        private String idioma;
        @OneToMany(mappedBy = "livro", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        private List<Autor> autores;

        public Livro() {}

        public Livro(String titulo, String autor) {
                this.titulo =titulo;
                this.autor=autor;
        }

        public Livro(String titulo, String nome, String idioma) {
                this.titulo=titulo;
                this.autor=nome;
                this.idioma=idioma;
        }

        public Livro(String titulo, String nomeAutor, List<String> idioma) {
                this.titulo=titulo;
                this.autor=nomeAutor;
                this.idioma = idioma.stream().collect(Collectors.joining(","));

        }

        // Getters e setters
        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getTitulo() {
                return titulo;
        }

        public void setTitulo(String titulo) {
                this.titulo = titulo;
        }


        public List<Autor> getAutores() {
                return autores;
        }

        public void setAutores(List<Autor> autores) {

                this.autores = autores;

                autores.forEach(a->a.setLivro(this));


//                public void setEpisodios(List<Episodio> episodios) {
//                        this.episodios = episodios;
//                        episodios.forEach(e -> e.setSerie(this));/esse comando e para ter uma interacao
//                        reversa e falando para o episodio a qual serie ele pertence/
//                }
//
//







        }

        public String getAutor() {
                return autor;
        }

        public void setAutor(String autor) {
                this.autor = autor;
        }

        public String getIdioma() {
                return idioma;
        }

        public void setIdioma(String idioma) {
                this.idioma = idioma;
        }
}
