    package com.joaobrandao.Literalura.model;

    import jakarta.persistence.*;

    @Entity
    public class Autor {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String nome;
        private int anoDeNascimento;
        private int anoDeFalecimento;
        @ManyToOne
        @JoinColumn(name = "livro_id")
        private Livro livro;


        public Autor() {}

        public Autor(String nome, Integer anoDeNascimento, Integer anoDeFalecimento){
            this.nome=nome;
            this.anoDeFalecimento=anoDeFalecimento;
            this.anoDeNascimento=anoDeNascimento;
        }



        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public int getAnoDeNascimento() {
            return anoDeNascimento;
        }

        public void setAnoDeNascimento(int anoDeNascimento) {
            this.anoDeNascimento = anoDeNascimento;
        }

        public int getAnoDeFalecimento() {
            return anoDeFalecimento;
        }

        public void setAnoDeFalecimento(int anoDeFalecimento) {
            this.anoDeFalecimento = anoDeFalecimento;
        }

        public Livro getLivro() {
            return livro;
        }

        public void setLivro(Livro livro) {
            this.livro = livro;
        }

        @Override
        public String toString() {
            return String.format("Autor: %s (Nascimento: %d, Falecimento: %d)\nLivro: %s",
                    nome, anoDeNascimento, anoDeFalecimento, livro != null ? livro.getTitulo() : "Sem livro associado");
        }


    }
