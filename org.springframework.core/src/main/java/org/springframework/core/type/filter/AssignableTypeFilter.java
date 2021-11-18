/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.type.filter;

/**
 * A simple filter which matches classes that are assignable to a given type.
 *
 * @author Rod Johnson
 * @author Mark Fisher
 * @author Ramnivas Laddad
 * @since 2.5
 */
public class AssignableTypeFilter extends AbstractTypeHierarchyTraversingFilter {

	private final Class targetType;


	/**
	 * Create a new AssignableTypeFilter for the given type.
	 * @param targetType the type to match
	 */
	public AssignableTypeFilter(Class targetType) {
		super(true, true);
		this.targetType = targetType;
	}


	@Override
	protected boolean matchClassName(String className) {
		return this.targetType.getName().equals(className);
	}

	@Override
	protected Boolean matchSuperClass(String superClassName) {
		return matchTargetType(superClassName);
	}

	@Override
	protected Boolean matchInterface(String interfaceName) {
		return matchTargetType(interfaceName);
	}

	protected Boolean matchTargetType(String typeName) {
		if (this.targetType.getName().equals(typeName)) {
			return true;
		}
		else if (Object.class.getName().equals(typeName)) {
			return Boolean.FALSE;
		}
		else if (typeName.startsWith("java.")) {
			try {
				Class clazz = getClass().getClassLoader().loadClass(typeName);
				return Boolean.valueOf(this.targetType.isAssignableFrom(clazz));
			}
			catch (ClassNotFoundException ex) {
				// Class not found - can't determine a match that way.
			}
		}
		return null;
	}

}
