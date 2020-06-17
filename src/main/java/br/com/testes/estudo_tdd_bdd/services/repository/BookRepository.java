package br.com.testes.estudo_tdd_bdd.services.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.testes.estudo_tdd_bdd.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

}
