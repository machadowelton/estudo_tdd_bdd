package br.com.testes.estudo_tdd_bdd.services.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.testes.estudo_tdd_bdd.exceptions.BusinessException;
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
		if(repository.existsByIsbn(""))
			throw new BusinessException("Isbn ja cadastrado");
		return repository.save(book);
	}

	@Override
	public Optional<Book> getById(Long id) {
		return repository.findById(id);
	}

	@Override
	public void delete(Book book) {
		if(book == null || !Optional.of(book.getId()).isPresent())
			throw new IllegalArgumentException("Livro não pode ser nulo e deve conter um id");
		repository.delete(book);
	}

	@Override
	public Book update(Book book) {
		if(book == null || !Optional.of(book.getId()).isPresent())
			throw new IllegalArgumentException("Livro não pode ser nulo e deve conter um id");
		return repository.save(book);
	}

}
