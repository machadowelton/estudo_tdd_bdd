package br.com.testes.estudo_tdd_bdd.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.testes.estudo_tdd_bdd.exceptions.BusinessException;
import br.com.testes.estudo_tdd_bdd.model.entity.Book;
import br.com.testes.estudo_tdd_bdd.services.BookService;
import br.com.testes.estudo_tdd_bdd.services.impl.BookServiceImpl;
import br.com.testes.estudo_tdd_bdd.services.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class BookServiceTest {
	
	
	public BookService bookService;
	
	@MockBean
	public BookRepository bookRepository;
	
	@BeforeEach
	public void setUp() {
		this.bookService = new BookServiceImpl(bookRepository);
	}
	
	@Test
	@DisplayName("Deve salvar um livro")
	public void saveBookTest() {
		Book book = createBook();
		when(bookRepository.save(book)).thenReturn(Book.builder().id(1l).isbn("123").title("As aventuras").author("Fulano").build());
		Book savedBook = bookService.save(book);
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
		assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
		assertThat(savedBook.getIsbn()).isEqualTo("123");
	}
	
	private Book createBook() {
		return Book.builder().isbn("123").author("Fulano").title("As aventuras").build();
	}
	
	@Test
	@DisplayName("Deve lançar um erro de negocio ao usar isbn existente")
	public void saveBookFailureOnIsbExists() {
		Book book = createBook();
		when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(Boolean.TRUE);
		Throwable ex = Assertions.catchThrowable(() -> {
			bookService.save(book);
		});		
		assertThat(ex)
			.isInstanceOf(BusinessException.class)
			.hasMessage("Isbn ja cadastrado");
		verify(bookRepository, Mockito.never()).save(book);
	}
	
	@Test
	@DisplayName("Deve retornar um livro pelo id")
	public void getBookByIdTest() {
		Long id = 1l;
		Book book = createBook();
		book.setId(id);
		when(bookRepository.findById(id)).thenReturn(Optional.of(book));
		
		Optional<Book> opFoundBook = bookService.getById(id);
		
		
		assertThat(opFoundBook.isPresent()).isTrue();
		Book bookFound = opFoundBook.get();
		assertThat(book).isEqualTo(bookFound);
	}
	
	@Test
	@DisplayName("Deve retornar um erro quando nao encontrar o livro pelo id")
	public void getBookFailById() {
		when(bookRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
		Optional<Book> book = bookService.getById(1l);
		assertThat(book.isPresent()).isFalse();
	}
	
	@Test
	@DisplayName("Deve deletar um livro")
	public void deleteBookTest() {
		Book book = Book.builder().id(1l).build();
		
		assertDoesNotThrow(() -> {
			bookService.delete(book);
		});
				
		verify(bookRepository, times(1)).delete(book);
	}
	
	@Test
	@DisplayName("Deve ocorrer um erro ao deletar um livro que não existe")
	public void deleteInvalidBookTest() {
		//Book book = Book.builder().id(1l).build();
		Book book = null;
		Throwable thr = catchThrowable(() -> {
			bookService.delete(book);
		});
		assertThat(thr)
			.isInstanceOf(IllegalArgumentException.class);
		verify(bookRepository, Mockito.never()).delete(book);
	}
	
	@Test
	@DisplayName("Deve ocorrer um erro ao atualizar um livro que não existe")
	public void updateInvalidBookTest() {
		//Book book = Book.builder().id(1l).build();
		Book book = null;
		Throwable thr = catchThrowable(() -> {
			bookService.update(book);
		});
		assertThat(thr)
			.isInstanceOf(IllegalArgumentException.class);
		verify(bookRepository, Mockito.never()).save(book);
	}
	
	@Test
	@DisplayName("Deve atualizar um livro")
	public void updateBookTest() {
		final Long id = 1l;
		final Book updatingBook = Book.builder().id(id).build();
		
		final Book updatedBook = createBook();
		updatedBook.setId(id);
		
		when(bookRepository.save(updatingBook)).thenReturn(updatedBook);
		
		final Book book = bookService.update(updatingBook);		
		assertThat(book).isEqualTo(updatedBook);		
	}

	@Test
	@DisplayName("Deve filtrar livros pelas propriedades")
	public void findBookTest() {
		Book book = createBook();
		PageRequest pageRequest = PageRequest.of(0, 10);
		List<Book> bookList = Arrays.asList(book);
		Page<Book> pageBookMock =
				new PageImpl<Book>(bookList, pageRequest, 1);
		when(bookRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(pageBookMock);
		Page<Book> books = bookService.find(book, pageRequest);
		assertThat(books.getTotalElements()).isEqualTo(1);
		assertThat(books.getContent()).isEqualTo(bookList);
		assertThat(books.getPageable().getPageNumber()).isEqualTo(0);
		assertThat(books.getPageable().getPageSize()).isEqualTo(10);
	}
	
}
