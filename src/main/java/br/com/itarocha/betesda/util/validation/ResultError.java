package br.com.itarocha.betesda.util.validation;

import java.util.ArrayList;
import java.util.List;

public class ResultError<T> {
	
	private List<FieldValidationError> errors;
	
	public ResultError() {
		this.errors = new ArrayList();
	}

	public List<FieldValidationError> getErrors() {
		return errors;
	}

	public void setErrors(List<FieldValidationError> errors) {
		this.errors = errors;
	}
}
