/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.util.NumberUtils;

/**
 * Converts from a Character to any JDK-standard Number implementation.
 *
 * <p>Support Number classes including Byte, Short, Integer, Float, Double, Long, BigInteger, BigDecimal. This class
 * delegates to {@link NumberUtils#convertNumberToTargetClass(Number, Class)} to perform the conversion.
 *
 * @author Keith Donald
 * @since 3.0
 * @see java.lang.Byte
 * @see java.lang.Short
 * @see java.lang.Integer
 * @see java.lang.Long
 * @see java.math.BigInteger
 * @see java.lang.Float
 * @see java.lang.Double
 * @see java.math.BigDecimal
 * @see NumberUtils 
 */
final class CharacterToNumberFactory implements ConverterFactory<Character, Number> {

	public <T extends Number> Converter<Character, T> getConverter(Class<T> targetType) {
		return new CharacterToNumber<T>(targetType);
	}

	private static final class CharacterToNumber<T extends Number> implements Converter<Character, T> {

		private final Class<T> targetType;
		
		public CharacterToNumber(Class<T> targetType) {
			this.targetType = targetType;
		}
	
		public T convert(Character source) {
			return NumberUtils.convertNumberToTargetClass((short) source.charValue(), this.targetType);
		}
	}

}
