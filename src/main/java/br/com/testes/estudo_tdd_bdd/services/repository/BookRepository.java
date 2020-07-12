package br.com.testes.estudo_tdd_bdd.services.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.testes.estudo_tdd_bdd.model.entity.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

	boolean existsByIsbn(String string);

}
