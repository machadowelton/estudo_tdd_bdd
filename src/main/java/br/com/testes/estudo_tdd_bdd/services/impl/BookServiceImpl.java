package br.com.testes.estudo_tdd_bdd.services.impl;

import org.springframework.stereotype.Service;

import br.com.testes.estudo_tdd_bdd.model.entity.Book;
import br.com.testes.estudo_tdd_bdd.services.BookService;
import br.com.testes.estudo_tdd_bdd.services.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {

	private final BookRepository repository;
	
	public BookServiceImpl(final BookRepository repository ) {
		this.repository = repository;
	}

	@Override
	public Book save(Book book) {
		return repository.save(book);
	}

}
