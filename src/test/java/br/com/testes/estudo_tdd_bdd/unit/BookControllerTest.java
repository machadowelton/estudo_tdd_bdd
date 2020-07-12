
package br.com.testes.estudo_tdd_bdd.unit;



import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.testes.estudo_tdd_bdd.dto.BookDTO;
import br.com.testes.estudo_tdd_bdd.exceptions.BusinessException;
import br.com.testes.estudo_tdd_bdd.model.entity.Book;
import br.com.testes.estudo_tdd_bdd.services.BookService;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class BookControllerTest {
	
	
	static final String BOOK_API = "/books";
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	BookService bookService;
//	
//	BookServiceImpl bookServiceImpl;
//	
//	@MockBean
//	BookRepository bookRepository;
//	
//	@BeforeEach
//	public void setUp() {
//		this.bookServiceImpl = new BookServiceImpl(bookRepository);
//	}
	
	
	@Test
	@DisplayName("Deve criar um livro com sucesso")
	public void createBookTest() throws Exception{
		BookDTO dto = createNewBook();
		Book savedBook = Book.builder()
								.id(10l)
								.author("Arthur")
								.title("As aventuras")
								.isbn("001")
								.build();
		BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(savedBook);
		String json = new ObjectMapper().writeValueAsString(dto);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
						.post(BOOK_API)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(json);
		mockMvc.perform(request)
			.andExpect(status().isCreated())
			.andExpect(jsonPath("id").isNotEmpty())
			.andExpect(jsonPath("title").value(dto.getTitle()))
			.andExpect(jsonPath("author").value(dto.getAuthor()))
			.andExpect(jsonPath("isbn").value(dto.getIsbn()));
	}
	
	@Test
	@DisplayName("Deve lançar um erro quando não passar na validação")
	public void createInvalidBookTest() throws Exception {
		String json = new ObjectMapper().writeValueAsString(new BookDTO());
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);
		mockMvc.perform(request)
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("errors", hasSize(3)));
	}
	
	private BookDTO createNewBook() {
		return BookDTO.builder()
				.author("Arthur")
				.title("As aventuras")
				.isbn("001")
				.build();
	}
	
	@Test
	@DisplayName("Deve lançar um erro ao tentar inserir isbn existente")
	public void createBookWithDuplicateIsbn() throws Exception {
		BookDTO dto = createNewBook();
		String msgErro = "Isbn já cadastrado";
		BDDMockito.given(bookService.save(Mockito.any(Book.class))).willThrow(new BusinessException(msgErro));//		
		String json = new ObjectMapper().writeValueAsString(dto);
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
						.post(BOOK_API)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
						.content(json);
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errors", hasSize(1)))
				.andExpect(jsonPath("errors[0]").value(msgErro));
	}
	
	
	@Test
	@DisplayName("Deve retornar um livro pelo id")
	public void getBookByIdTest() throws Exception{
		Long id = 1l;
		Book book = Book.builder().id(id).author("Fulano").title("As aventuras").isbn("123").build();
		Optional<Book> op = Optional.of(book);
		BDDMockito.given(bookService.getById(id)).willReturn(op);
		MockHttpServletRequestBuilder request =
					MockMvcRequestBuilders
						.get(BOOK_API.concat("/" + id))
						.accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(request)
					.andExpect(status().isOk())
					.andExpect(jsonPath("id").value(id))
					.andExpect(jsonPath("title").value(book.getTitle()))
					.andExpect(jsonPath("author").value(book.getAuthor()))
					.andExpect(jsonPath("isbn").value(book.getIsbn()));
	}
	
	@Test
	@DisplayName("Deve lançar erro quando livro não for encontrado")
	public void bookNotFound() throws Exception {		
		BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.empty());
		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders
					.get(BOOK_API.concat("/" + 1))
					.accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(request)
				.andExpect(status().isNotFound());
	}
	
	
	@Test
	@DisplayName("Deve deletar um livro")
	public void shouldDeleteBookTest() throws Exception {		
		BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.of(Book.builder().id(Mockito.anyLong()).build()));
		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders
					.delete(BOOK_API.concat("/" + 1))
					.accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(request)
				.andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("Deve retornar um erro 404")
	public void failureOnDeleteBookIfIdNotExistsTest() throws Exception {		
		BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.empty());
		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders
					.delete(BOOK_API.concat("/" + 1))
					.accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(request)
				.andExpect(status().isNotFound());
	}
	
	@Test
	@DisplayName("Deve atualizar um lviro")
	public void updateBookTest() throws Exception{
		  Long id = 1l;
	        String json = new ObjectMapper().writeValueAsString(createNewBook());

	        Book updatingBook = Book.builder().id(1l).title("some title").author("some author").isbn("321").build();
	        BDDMockito.given( bookService.getById(id) ).willReturn( Optional.of(updatingBook) );
	        Book updatedBook = Book.builder().id(id).author("Arthur").title("As aventuras").isbn("321").build();
	        BDDMockito.given(bookService.update(updatingBook)).willReturn(updatedBook);

	        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
	                .put(BOOK_API.concat("/" + 1))
	                .content(json)
	                .accept(MediaType.APPLICATION_JSON)
	                .contentType(MediaType.APPLICATION_JSON);

	        mockMvc.perform( request )
	                .andExpect( status().isOk() )
	                .andExpect( jsonPath("id").value(id) )
	                .andExpect( jsonPath("title").value(createNewBook().getTitle()) )
	                .andExpect( jsonPath("author").value(createNewBook().getAuthor()) )
	                .andExpect( jsonPath("isbn").value("321") );
	}
	
	@Test
	@DisplayName("Deve retornar 404 se um lviro não existe")
	public void updateNotExistBookTest() throws Exception{
		BookDTO bookToUpdate = createNewBook();
		bookToUpdate.setId(1l);
		String json = new ObjectMapper().writeValueAsString(bookToUpdate);
		BDDMockito.given(bookService.getById(bookToUpdate.getId())).willReturn(Optional.empty());		
		MockHttpServletRequestBuilder request =
				MockMvcRequestBuilders
					.put((BOOK_API.concat("/" + bookToUpdate.getId())))
					.content(json)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON);
		mockMvc.perform(request)
			.andExpect(status().isNotFound());
	}
	
}
