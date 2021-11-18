/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.aspectj;

import org.aspectj.lang.JoinPoint;

/**
 * Definitions of testing types for use in within this package.
 * Wherever possible, test types should be defined local to the java
 * file that makes use of them.  In some cases however, a test type may
 * need to be shared across tests.  Such types reside here, with the
 * intention of reducing the surface area of java files within this
 * package.  This allows developers to think about tests first, and deal
 * with these second class testing artifacts on an as-needed basis.
 * 
 * Types here should be defined as package-private top level classes in
 * order to avoid needing to fully qualify, e.g.: _TestTypes$Foo.
 *
 * @author Chris Beams
 */
final class _TestTypes { }


/**
 * Aspect used as part of before before advice binding tests and
 * serves as base class for a number of more specialized test aspects.
 *
 * @author Adrian Colyer
 * @author Chris Beams
 */
class AdviceBindingTestAspect {

	protected AdviceBindingCollaborator collaborator = null;

	public void setCollaborator(AdviceBindingCollaborator aCollaborator) {
		this.collaborator = aCollaborator;
	}
	
	// "advice" methods
	public void oneIntArg(int age) {
		this.collaborator.oneIntArg(age);
	}
	
	public void oneObjectArg(Object bean) {
		this.collaborator.oneObjectArg(bean);
	}
	
	public void oneIntAndOneObject(int x, Object o) {
		this.collaborator.oneIntAndOneObject(x,o);
	}
	
	public void needsJoinPoint(JoinPoint tjp) {
		this.collaborator.needsJoinPoint(tjp.getSignature().getName());
	}
	
	public void needsJoinPointStaticPart(JoinPoint.StaticPart tjpsp) {
		this.collaborator.needsJoinPointStaticPart(tjpsp.getSignature().getName());
	}


	/**
	 * Collaborator interface that makes it easy to test this aspect is
	 * working as expected through mocking.
	 */
	public interface AdviceBindingCollaborator {

		void oneIntArg(int x);
		void oneObjectArg(Object o);
		void oneIntAndOneObject(int x, Object o);
		void needsJoinPoint(String s);
		void needsJoinPointStaticPart(String s);
	}

}


/**
 * @author Ramnivas Laddad
 */
interface ICounter {

	void increment();

	void decrement();

	int getCount();

	void setCount(int counter);

	void reset();

}


/**
 * A simple counter for use in simple tests (for example, how many times an advice was executed)
 * 
 * @author Ramnivas Laddad
 */
final class Counter implements ICounter {

	private int count;

	public Counter() {
	}

	public void increment() {
		count++;
	}
	
	public void decrement() {
		count--;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int counter) {
		this.count = counter;
	}
	
	public void reset() {
		this.count = 0;
	}
}
