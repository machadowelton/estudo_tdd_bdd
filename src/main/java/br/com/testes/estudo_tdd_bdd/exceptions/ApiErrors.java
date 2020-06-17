package br.com.testes.estudo_tdd_bdd.exceptions;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.BindingResult;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Data
public class ApiErrors {
	
	private List<String> errors;
	
	public ApiErrors() {
		
	}

	public ApiErrors(BindingResult bindingResult) {
		this.errors = new  ArrayList<String>();
		bindingResult.getAllErrors().forEach((e) -> {
			this.errors.add(e.getDefaultMessage());
		});
	}
	
}
