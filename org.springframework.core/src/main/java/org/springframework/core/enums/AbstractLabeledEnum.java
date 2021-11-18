/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.enums;

/**
 * Abstract base superclass for LabeledEnum implementations.
 *
 * @author Keith Donald
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @since 1.2.2
 * @deprecated as of Spring 3.0, in favor of Java 5 enums.
 */
@Deprecated
public abstract class AbstractLabeledEnum implements LabeledEnum {

	/**
	 * Create a new AbstractLabeledEnum instance.
	 */
	protected AbstractLabeledEnum() {
	}

	public Class getType() {
		// Could be coded as getClass().isAnonymousClass() on JDK 1.5
		boolean isAnonymous = (getClass().getDeclaringClass() == null && getClass().getName().indexOf('$') != -1);
		return (isAnonymous ? getClass().getSuperclass() : getClass());
	}

	public int compareTo(Object obj) {
		if (!(obj instanceof LabeledEnum)) {
			throw new ClassCastException("You may only compare LabeledEnums");
		}
		LabeledEnum that = (LabeledEnum) obj;
		if (!this.getType().equals(that.getType())) {
			throw new ClassCastException("You may only compare LabeledEnums of the same type");
		}
		return this.getCode().compareTo(that.getCode());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LabeledEnum)) {
			return false;
		}
		LabeledEnum other = (LabeledEnum) obj;
		return (this.getType().equals(other.getType()) && this.getCode().equals(other.getCode()));
	}

	@Override
	public int hashCode() {
		return (getType().hashCode() * 29 + getCode().hashCode());
	}

	@Override
	public String toString() {
		return getLabel();
	}

}
