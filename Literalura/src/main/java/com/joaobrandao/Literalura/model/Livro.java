package com.joaobrandao.Literalura.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Livro {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(name = "titulo", nullable = false)
        private String titulo;
        @Column(name = "autor", nullable = false)
        private String autor;
        @Transient
        private List<String> idioma;
        @OneToMany(mappedBy = "livro", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
        private List<Autor> autores;


        public Livro() {
        }



        public Livro(String titulo, String autor) {
                this.titulo =titulo;
                this.autor=autor;
        }

        public Livro(String titulo, String nome, List<String> idioma) {
                this.titulo=titulo;
                this.autor=nome;
                this.idioma=idioma;
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
                autores.forEach(autor->autor.setLivro(this));
                this.autores = autores;
        }

        public String getAutor() {
                return autor;
        }

        public void setAutor(String autor) {
                this.autor = autor;
        }

        public List<String> getIdioma() {
                return idioma;
        }

        public void setIdioma(List<String> idioma) {
                this.idioma = idioma;
        }

}
