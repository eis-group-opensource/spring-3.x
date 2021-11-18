/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package org.springframework.scripting.jruby;

/**
 * http://opensource.atlassian.com/projects/spring/browse/SPR-3026
 *
 * @author Rick Evans
 */
public interface PrimitiveAdder {

	int addInts(int x, int y);

	short addShorts(short x, short y);

	long addLongs(long x, long y);

	float addFloats(float x, float y);

	double addDoubles(double x, double y);

	boolean resultIsPositive(int x, int y);

	String concatenate(char c, char d);

	char echo(char c);
}
