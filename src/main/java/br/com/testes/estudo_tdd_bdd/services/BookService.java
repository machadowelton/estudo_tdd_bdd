package br.com.testes.estudo_tdd_bdd.services;

import br.com.testes.estudo_tdd_bdd.model.entity.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BookService {

	public Book save(Book book);

	public Optional<Book> getById(Long id);
	
	public void delete(Book book);

	public Book update(Book book);

	Page<Book> find(Book book, Pageable pageable);

}
