/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.scripting.jruby;

import java.util.Map;

/**
 * http://opensource.atlassian.com/projects/spring/browse/SPR-3038
 *
 * @author Rick Evans
 */
public interface WrapperAdder {

	Integer addInts(Integer x, Integer y);

	Short addShorts(Short x, Short y);

	Long addLongs(Long x, Long y);

	Float addFloats(Float x, Float y);

	Double addDoubles(Double x, Double y);

	Boolean resultIsPositive(Integer x, Integer y);

	String concatenate(Character c, Character d);

	Character echo(Character c);

	String concatArrayOfIntegerWrappers(Integer[] numbers);

	Short[] populate(Short one, Short two);

	String[][] createListOfLists(String one, String second, String third);

	Map toMap(String key, Object value);

}
