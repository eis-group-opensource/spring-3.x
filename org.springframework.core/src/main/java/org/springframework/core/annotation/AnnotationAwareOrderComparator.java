/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.core.annotation;

import org.springframework.core.OrderComparator;
import org.springframework.core.Ordered;

/**
 * {@link java.util.Comparator} implementation that checks
 * {@link org.springframework.core.Ordered} as well as the
 * {@link Order} annotation, with an order value provided by an
 * <code>Ordered</code> instance overriding a statically defined
 * annotation value (if any).
 *
 * @author Juergen Hoeller
 * @since 2.0.1
 * @see org.springframework.core.Ordered
 * @see Order
 */
public class AnnotationAwareOrderComparator extends OrderComparator {

	@Override
	protected int getOrder(Object obj) {
		if (obj instanceof Ordered) {
			return ((Ordered) obj).getOrder();
		}
		if (obj != null) {
			Order order = obj.getClass().getAnnotation(Order.class);
			if (order != null) {
				return order.value();
			}
		}
		return Ordered.LOWEST_PRECEDENCE;
	}

}
