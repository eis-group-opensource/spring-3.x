/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.format.number;

import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.format.annotation.NumberFormat;
import org.springframework.format.annotation.NumberFormat.Style;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.StringValueResolver;
import org.springframework.validation.DataBinder;

/**
 * @author Keith Donald
 * @author Juergen Hoeller
 */
public class NumberFormattingTests {

	private FormattingConversionService conversionService = new FormattingConversionService();

	private DataBinder binder;

	@Before
	public void setUp() {
		DefaultConversionService.addDefaultConverters(conversionService);
		conversionService.setEmbeddedValueResolver(new StringValueResolver() {
			public String resolveStringValue(String strVal) {
				if ("${pattern}".equals(strVal)) {
					return "#,##.00";
				} else {
					return strVal;
				}
			}
		});
		conversionService.addFormatterForFieldType(Number.class, new NumberFormatter());
		conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
		LocaleContextHolder.setLocale(Locale.US);
		binder = new DataBinder(new TestBean());
		binder.setConversionService(conversionService);
	}

	@After
	public void tearDown() {
		LocaleContextHolder.setLocale(null);
	}

	@Test
	public void testDefaultNumberFormatting() {
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("numberDefault", "3,339.12");
		binder.bind(propertyValues);
		assertEquals(0, binder.getBindingResult().getErrorCount());
		assertEquals("3,339", binder.getBindingResult().getFieldValue("numberDefault"));
	}

	@Test
	public void testDefaultNumberFormattingAnnotated() {
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("numberDefaultAnnotated", "3,339.12");
		binder.bind(propertyValues);
		assertEquals(0, binder.getBindingResult().getErrorCount());
		assertEquals("3,339.12", binder.getBindingResult().getFieldValue("numberDefaultAnnotated"));
	}

	@Test
	public void testCurrencyFormatting() {
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("currency", "$3,339.12");
		binder.bind(propertyValues);
		assertEquals(0, binder.getBindingResult().getErrorCount());
		assertEquals("$3,339.12", binder.getBindingResult().getFieldValue("currency"));
	}

	@Test
	public void testPercentFormatting() {
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("percent", "53%");
		binder.bind(propertyValues);
		assertEquals(0, binder.getBindingResult().getErrorCount());
		assertEquals("53%", binder.getBindingResult().getFieldValue("percent"));
	}

	@Test
	public void testPatternFormatting() {
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("pattern", "1,25.00");
		binder.bind(propertyValues);
		assertEquals(0, binder.getBindingResult().getErrorCount());
		assertEquals("1,25.00", binder.getBindingResult().getFieldValue("pattern"));
	}

	@Test
	public void testPatternArrayFormatting() {
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("patternArray", new String[] { "1,25.00", "2,35.00" });
		binder.bind(propertyValues);
		assertEquals(0, binder.getBindingResult().getErrorCount());
		assertEquals("1,25.00", binder.getBindingResult().getFieldValue("patternArray[0]"));
		assertEquals("2,35.00", binder.getBindingResult().getFieldValue("patternArray[1]"));

		propertyValues = new MutablePropertyValues();
		propertyValues.add("patternArray[0]", "1,25.00");
		propertyValues.add("patternArray[1]", "2,35.00");
		binder.bind(propertyValues);
		assertEquals(0, binder.getBindingResult().getErrorCount());
		assertEquals("1,25.00", binder.getBindingResult().getFieldValue("patternArray[0]"));
		assertEquals("2,35.00", binder.getBindingResult().getFieldValue("patternArray[1]"));
	}

	@Test
	public void testPatternListFormatting() {
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("patternList", new String[] { "1,25.00", "2,35.00" });
		binder.bind(propertyValues);
		assertEquals(0, binder.getBindingResult().getErrorCount());
		assertEquals("1,25.00", binder.getBindingResult().getFieldValue("patternList[0]"));
		assertEquals("2,35.00", binder.getBindingResult().getFieldValue("patternList[1]"));

		propertyValues = new MutablePropertyValues();
		propertyValues.add("patternList[0]", "1,25.00");
		propertyValues.add("patternList[1]", "2,35.00");
		binder.bind(propertyValues);
		assertEquals(0, binder.getBindingResult().getErrorCount());
		assertEquals("1,25.00", binder.getBindingResult().getFieldValue("patternList[0]"));
		assertEquals("2,35.00", binder.getBindingResult().getFieldValue("patternList[1]"));
	}

	@Test
	public void testPatternList2FormattingListElement() {
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("patternList2[0]", "1,25.00");
		propertyValues.add("patternList2[1]", "2,35.00");
		binder.bind(propertyValues);
		assertEquals(0, binder.getBindingResult().getErrorCount());
		assertEquals("1,25.00", binder.getBindingResult().getFieldValue("patternList2[0]"));
		assertEquals("2,35.00", binder.getBindingResult().getFieldValue("patternList2[1]"));
	}

	@Test
	public void testPatternList2FormattingList() {
		MutablePropertyValues propertyValues = new MutablePropertyValues();
		propertyValues.add("patternList2[0]", "1,25.00");
		propertyValues.add("patternList2[1]", "2,35.00");
		binder.bind(propertyValues);
		assertEquals(0, binder.getBindingResult().getErrorCount());
		assertEquals("1,25.00,2,35.00", binder.getBindingResult().getFieldValue("patternList2"));
	}

	@SuppressWarnings("unused")
	private static class TestBean {

		private Integer numberDefault;

		@NumberFormat
		private Double numberDefaultAnnotated;

		@NumberFormat(style = Style.CURRENCY)
		private BigDecimal currency;

		@NumberFormat(style = Style.PERCENT)
		private BigDecimal percent;

		@NumberFormat(pattern = "${pattern}")
		private BigDecimal pattern;

		@NumberFormat(pattern = "#,##.00")
		private BigDecimal[] patternArray;

		@NumberFormat(pattern = "#,##.00")
		private List<BigDecimal> patternList;

		@NumberFormat(pattern = "#,##.00")
		private List<BigDecimal> patternList2;

		public Integer getNumberDefault() {
			return numberDefault;
		}

		public void setNumberDefault(Integer numberDefault) {
			this.numberDefault = numberDefault;
		}

		public Double getNumberDefaultAnnotated() {
			return numberDefaultAnnotated;
		}

		public void setNumberDefaultAnnotated(Double numberDefaultAnnotated) {
			this.numberDefaultAnnotated = numberDefaultAnnotated;
		}

		public BigDecimal getCurrency() {
			return currency;
		}

		public void setCurrency(BigDecimal currency) {
			this.currency = currency;
		}

		public BigDecimal getPercent() {
			return percent;
		}

		public void setPercent(BigDecimal percent) {
			this.percent = percent;
		}

		public BigDecimal getPattern() {
			return pattern;
		}

		public void setPattern(BigDecimal pattern) {
			this.pattern = pattern;
		}

		public BigDecimal[] getPatternArray() {
			return patternArray;
		}

		public void setPatternArray(BigDecimal[] patternArray) {
			this.patternArray = patternArray;
		}

		public List<BigDecimal> getPatternList() {
			return patternList;
		}

		public void setPatternList(List<BigDecimal> patternList) {
			this.patternList = patternList;
		}

		public List<BigDecimal> getPatternList2() {
			return patternList2;
		}

		public void setPatternList2(List<BigDecimal> patternList2) {
			this.patternList2 = patternList2;
		}

	}

}
