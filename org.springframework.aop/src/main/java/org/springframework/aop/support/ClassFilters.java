/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.aop.support;

import java.io.Serializable;

import org.springframework.aop.ClassFilter;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;

/**
 * Static utility methods for composing
 * {@link org.springframework.aop.ClassFilter ClassFilters}.
 *
 * @author Rod Johnson
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @since 11.11.2003
 * @see MethodMatchers
 * @see Pointcuts
 */
public abstract class ClassFilters {

	/**
	 * Match all classes that <i>either</i> (or both) of the given ClassFilters matches.
	 * @param cf1 the first ClassFilter
	 * @param cf2 the second ClassFilter
	 * @return a distinct ClassFilter that matches all classes that either
	 * of the given ClassFilter matches
	 */
	public static ClassFilter union(ClassFilter cf1, ClassFilter cf2) {
		Assert.notNull(cf1, "First ClassFilter must not be null");
		Assert.notNull(cf2, "Second ClassFilter must not be null");
		return new UnionClassFilter(new ClassFilter[] {cf1, cf2});
	}

	/**
	 * Match all classes that <i>either</i> (or all) of the given ClassFilters matches.
	 * @param classFilters the ClassFilters to match
	 * @return a distinct ClassFilter that matches all classes that either
	 * of the given ClassFilter matches
	 */
	public static ClassFilter union(ClassFilter[] classFilters) {
		Assert.notEmpty(classFilters, "ClassFilter array must not be empty");
		return new UnionClassFilter(classFilters);
	}

	/**
	 * Match all classes that <i>both</i> of the given ClassFilters match.
	 * @param cf1 the first ClassFilter
	 * @param cf2 the second ClassFilter
	 * @return a distinct ClassFilter that matches all classes that both
	 * of the given ClassFilter match
	 */
	public static ClassFilter intersection(ClassFilter cf1, ClassFilter cf2) {
		Assert.notNull(cf1, "First ClassFilter must not be null");
		Assert.notNull(cf2, "Second ClassFilter must not be null");
		return new IntersectionClassFilter(new ClassFilter[] {cf1, cf2});
	}

	/**
	 * Match all classes that <i>all</i> of the given ClassFilters match.
	 * @param classFilters the ClassFilters to match
	 * @return a distinct ClassFilter that matches all classes that both
	 * of the given ClassFilter match
	 */
	public static ClassFilter intersection(ClassFilter[] classFilters) {
		Assert.notEmpty(classFilters, "ClassFilter array must not be empty");
		return new IntersectionClassFilter(classFilters);
	}


	/**
	 * ClassFilter implementation for a union of the given ClassFilters.
	 */
	private static class UnionClassFilter implements ClassFilter, Serializable {

		private ClassFilter[] filters;

		public UnionClassFilter(ClassFilter[] filters) {
			this.filters = filters;
		}

		public boolean matches(Class clazz) {
			for (int i = 0; i < this.filters.length; i++) {
				if (this.filters[i].matches(clazz)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean equals(Object other) {
			return (this == other || (other instanceof UnionClassFilter &&
					ObjectUtils.nullSafeEquals(this.filters, ((UnionClassFilter) other).filters)));
		}

		@Override
		public int hashCode() {
			return ObjectUtils.nullSafeHashCode(this.filters);
		}
	}


	/**
	 * ClassFilter implementation for an intersection of the given ClassFilters.
	 */
	private static class IntersectionClassFilter implements ClassFilter, Serializable {

		private ClassFilter[] filters;

		public IntersectionClassFilter(ClassFilter[] filters) {
			this.filters = filters;
		}

		public boolean matches(Class clazz) {
			for (int i = 0; i < this.filters.length; i++) {
				if (!this.filters[i].matches(clazz)) {
					return false;
				}
			}
			return true;
		}

		@Override
		public boolean equals(Object other) {
			return (this == other || (other instanceof IntersectionClassFilter &&
					ObjectUtils.nullSafeEquals(this.filters, ((IntersectionClassFilter) other).filters)));
		}

		@Override
		public int hashCode() {
			return ObjectUtils.nullSafeHashCode(this.filters);
		}
	}

}
