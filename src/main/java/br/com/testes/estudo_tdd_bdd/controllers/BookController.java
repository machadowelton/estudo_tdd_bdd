package br.com.testes.estudo_tdd_bdd.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.testes.estudo_tdd_bdd.dto.BookDTO;
import br.com.testes.estudo_tdd_bdd.model.entity.Book;
import br.com.testes.estudo_tdd_bdd.services.BookService;

@RestController
@RequestMapping("/books")
public class BookController {
	
	private final BookService bookService;
	private final ModelMapper modelMapper;
	
	public BookController(final BookService bookService, final ModelMapper mapper) {
		this.bookService = bookService;
		this.modelMapper = mapper;
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO create(@RequestBody BookDTO dto) {
		//Book book = bookService.save(Book.builder().author(dto.getAuthor()).title(dto.getTitle()).isbn(dto.getIsbn()).build());
		Book entity = modelMapper.map(dto, Book.class);
		entity = bookService.save(entity);
		
		//return BookDTO.builder().id(entity.getId()).author(entity.getAuthor()).title(entity.getTitle()).isbn(entity.getIsbn()).build();
		return modelMapper.map(entity, BookDTO.class);
	}
	
}