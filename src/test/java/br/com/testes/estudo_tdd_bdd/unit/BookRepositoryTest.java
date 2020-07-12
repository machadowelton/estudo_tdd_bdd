package br.com.testes.estudo_tdd_bdd.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.testes.estudo_tdd_bdd.model.entity.Book;
import br.com.testes.estudo_tdd_bdd.services.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {
	
	@Autowired
	TestEntityManager testEntityManager;
	
	@Autowired
	BookRepository bookRepositoryTest;
	
	@Test
	@DisplayName("Deve retornar verdadeiro se existir o isbn informado")
	public void existsIsbnTest() {
		String isbn = "123";
		Book book = Book.builder().title("As aventuras").author("fulano").isbn(isbn).build();
		testEntityManager.persist(book);		
		boolean exists = bookRepositoryTest.existsByIsbn(isbn);
		assertThat(exists).isTrue();
	}
	
	@Test
	@DisplayName("Deve retornar false se não existir o isbn informado")
	public void NotExistsIsbnTest() {
		String isbn = "123";		
		boolean exists = bookRepositoryTest.existsByIsbn(isbn);
		assertThat(exists).isFalse();
	}
	
	
	@Test
	@DisplayName("Deve retornar um livro pelo id")
	public void findByIdTest() {
		Book book = Book.builder()
						.title("As aventuras")
						.author("Arthur")
						.isbn("1234")
						.build();
		Book bookSaved = testEntityManager.persist(book);
		Optional<Book> bookFound = bookRepositoryTest.findById(bookSaved.getId());
		assertThat(bookFound.isPresent()).isTrue();
	}
	
	@Test
	@DisplayName("Deve salvar um livro")
	public void saveBookTest() {
		Book book = Book.builder()
						.title("As aventuras")
						.author("Arthur")
						.isbn("1234")
						.build();
		assertDoesNotThrow(() -> {
			Book bookSave = bookRepositoryTest.save(book);
			assertThat(bookSave.getId()).isNotNull();
		});
	}
}
