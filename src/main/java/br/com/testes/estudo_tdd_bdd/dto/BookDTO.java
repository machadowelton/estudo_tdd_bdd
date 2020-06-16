package br.com.testes.estudo_tdd_bdd.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookDTO {
	
	private Long id;
	
	private String title;
	
	private String author;
	
	private String isbn;
	
}
