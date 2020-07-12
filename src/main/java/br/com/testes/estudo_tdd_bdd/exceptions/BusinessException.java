package br.com.testes.estudo_tdd_bdd.exceptions;

public class BusinessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3971967746918842869L;
	
	
	public BusinessException(String msg) {
		super(msg);
	}

}
