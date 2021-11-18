/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.validation;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.TestBean;

/**
 * Unit tests for {@link ValidationUtils}.
 * 
 * @author Juergen Hoeller
 * @author Rick Evans
 * @author Chris Beams
 * @since 08.10.2004
 */
public class ValidationUtilsTests {

	@Test(expected=IllegalArgumentException.class)
	public void testInvokeValidatorWithNullValidator() throws Exception {
		TestBean tb = new TestBean();
		Errors errors = new BeanPropertyBindingResult(tb, "tb");
		ValidationUtils.invokeValidator(null, tb, errors);
	}

	@Test(expected=IllegalArgumentException.class)
	public void testInvokeValidatorWithNullErrors() throws Exception {
		TestBean tb = new TestBean();
		ValidationUtils.invokeValidator(new EmptyValidator(), tb, null);
	}

	@Test
	public void testInvokeValidatorSunnyDay() throws Exception {
		TestBean tb = new TestBean();
		Errors errors = new BeanPropertyBindingResult(tb, "tb");
		ValidationUtils.invokeValidator(new EmptyValidator(), tb, errors);
		assertTrue(errors.hasFieldErrors("name"));
		assertEquals("EMPTY", errors.getFieldError("name").getCode());
	}

	@Test
	public void testValidationUtilsSunnyDay() throws Exception {
		TestBean tb = new TestBean("");

		Validator testValidator = new EmptyValidator();
		tb.setName(" ");
		Errors errors = new BeanPropertyBindingResult(tb, "tb");
		testValidator.validate(tb, errors);
		assertFalse(errors.hasFieldErrors("name"));

		tb.setName("Roddy");
		errors = new BeanPropertyBindingResult(tb, "tb");
		testValidator.validate(tb, errors);
		assertFalse(errors.hasFieldErrors("name"));
	}

	@Test
	public void testValidationUtilsNull() throws Exception {
		TestBean tb = new TestBean();
		Errors errors = new BeanPropertyBindingResult(tb, "tb");
		Validator testValidator = new EmptyValidator();
		testValidator.validate(tb, errors);
		assertTrue(errors.hasFieldErrors("name"));
		assertEquals("EMPTY", errors.getFieldError("name").getCode());
	}

	@Test
	public void testValidationUtilsEmpty() throws Exception {
		TestBean tb = new TestBean("");
		Errors errors = new BeanPropertyBindingResult(tb, "tb");
		Validator testValidator = new EmptyValidator();
		testValidator.validate(tb, errors);
		assertTrue(errors.hasFieldErrors("name"));
		assertEquals("EMPTY", errors.getFieldError("name").getCode());
	}

	@Test
	public void testValidationUtilsEmptyVariants() {
		TestBean tb = new TestBean();

		Errors errors = new BeanPropertyBindingResult(tb, "tb");
		ValidationUtils.rejectIfEmpty(errors, "name", "EMPTY_OR_WHITESPACE", new Object[] {"arg"});
		assertTrue(errors.hasFieldErrors("name"));
		assertEquals("EMPTY_OR_WHITESPACE", errors.getFieldError("name").getCode());
		assertEquals("arg", errors.getFieldError("name").getArguments()[0]);

		errors = new BeanPropertyBindingResult(tb, "tb");
		ValidationUtils.rejectIfEmpty(errors, "name", "EMPTY_OR_WHITESPACE", new Object[] {"arg"}, "msg");
		assertTrue(errors.hasFieldErrors("name"));
		assertEquals("EMPTY_OR_WHITESPACE", errors.getFieldError("name").getCode());
		assertEquals("arg", errors.getFieldError("name").getArguments()[0]);
		assertEquals("msg", errors.getFieldError("name").getDefaultMessage());
	}

	@Test
	public void testValidationUtilsEmptyOrWhitespace() throws Exception {
		TestBean tb = new TestBean();
		Validator testValidator = new EmptyOrWhitespaceValidator();

		// Test null
		Errors errors = new BeanPropertyBindingResult(tb, "tb");
		testValidator.validate(tb, errors);
		assertTrue(errors.hasFieldErrors("name"));
		assertEquals("EMPTY_OR_WHITESPACE", errors.getFieldError("name").getCode());

		// Test empty String
		tb.setName("");
		errors = new BeanPropertyBindingResult(tb, "tb");
		testValidator.validate(tb, errors);
		assertTrue(errors.hasFieldErrors("name"));
		assertEquals("EMPTY_OR_WHITESPACE", errors.getFieldError("name").getCode());

		// Test whitespace String
		tb.setName(" ");
		errors = new BeanPropertyBindingResult(tb, "tb");
		testValidator.validate(tb, errors);
		assertTrue(errors.hasFieldErrors("name"));
		assertEquals("EMPTY_OR_WHITESPACE", errors.getFieldError("name").getCode());

		// Test OK
		tb.setName("Roddy");
		errors = new BeanPropertyBindingResult(tb, "tb");
		testValidator.validate(tb, errors);
		assertFalse(errors.hasFieldErrors("name"));
	}

	@Test
	public void testValidationUtilsEmptyOrWhitespaceVariants() {
		TestBean tb = new TestBean();
		tb.setName(" ");

		Errors errors = new BeanPropertyBindingResult(tb, "tb");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "EMPTY_OR_WHITESPACE", new Object[] {"arg"});
		assertTrue(errors.hasFieldErrors("name"));
		assertEquals("EMPTY_OR_WHITESPACE", errors.getFieldError("name").getCode());
		assertEquals("arg", errors.getFieldError("name").getArguments()[0]);

		errors = new BeanPropertyBindingResult(tb, "tb");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "EMPTY_OR_WHITESPACE", new Object[] {"arg"}, "msg");
		assertTrue(errors.hasFieldErrors("name"));
		assertEquals("EMPTY_OR_WHITESPACE", errors.getFieldError("name").getCode());
		assertEquals("arg", errors.getFieldError("name").getArguments()[0]);
		assertEquals("msg", errors.getFieldError("name").getDefaultMessage());
	}


	private static class EmptyValidator implements Validator {

		public boolean supports(Class clazz) {
			return TestBean.class.isAssignableFrom(clazz);
		}

		public void validate(Object obj, Errors errors) {
			ValidationUtils.rejectIfEmpty(errors, "name", "EMPTY", "You must enter a name!");
		}
	}


	private static class EmptyOrWhitespaceValidator implements Validator {

		public boolean supports(Class clazz) {
			return TestBean.class.isAssignableFrom(clazz);
		}

		public void validate(Object obj, Errors errors) {
			ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "EMPTY_OR_WHITESPACE", "You must enter a name!");
		}
	}

}
