/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.factory.parsing;

import static org.easymock.EasyMock.*;

import org.apache.commons.logging.Log;
import org.junit.Test;
import org.springframework.core.io.DescriptiveResource;

/**
 * @author Rick Evans
 * @author Juergen Hoeller
 * @author Chris Beams
 */
public final class FailFastProblemReporterTests {

	@Test(expected=BeanDefinitionParsingException.class)
	public void testError() throws Exception {
		FailFastProblemReporter reporter = new FailFastProblemReporter();
		reporter.error(new Problem("VGER", new Location(new DescriptiveResource("here")),
				null, new IllegalArgumentException()));
	}

	@Test
	public void testWarn() throws Exception {
		Problem problem = new Problem("VGER", new Location(new DescriptiveResource("here")),
				null, new IllegalArgumentException());

		Log log = createMock(Log.class);
		log.warn(anyObject(), isA(IllegalArgumentException.class));
		replay(log);

		FailFastProblemReporter reporter = new FailFastProblemReporter();
		reporter.setLogger(log);
		reporter.warning(problem);

		verify(log);
	}

}
