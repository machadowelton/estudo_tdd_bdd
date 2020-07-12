package br.com.testes.estudo_tdd_bdd.services;

import java.util.Optional;

import br.com.testes.estudo_tdd_bdd.model.entity.Book;

public interface BookService {

	public Book save(Book book);

	public Optional<Book> getById(Long id);
	
	public void delete(Book book);

	public Book update(Book book);
	
}
