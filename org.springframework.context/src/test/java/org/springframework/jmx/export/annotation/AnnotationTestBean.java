/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.jmx.export.annotation;

import org.springframework.jmx.IJmxTestBean;
import org.springframework.jmx.support.MetricType;
import org.springframework.stereotype.Service;

/**
 * @author Rob Harrop
 * @author Juergen Hoeller
 */
@Service("testBean")
@ManagedResource(objectName = "bean:name=testBean4", description = "My Managed Bean", log = true,
		logFile = "jmx.log", currencyTimeLimit = 15, persistPolicy = "OnUpdate", persistPeriod = 200,
		persistLocation = "./foo", persistName = "bar.jmx")
@ManagedNotifications({@ManagedNotification(name="My Notification", notificationTypes={"type.foo", "type.bar"})})
public class AnnotationTestBean implements IJmxTestBean {

	private String name;

	private String nickName;

	private int age;

	private boolean isSuperman;


	@ManagedAttribute(description = "The Age Attribute", currencyTimeLimit = 15)
	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	@ManagedOperation(currencyTimeLimit = 30)
	public long myOperation() {
		return 1L;
	}

	@ManagedAttribute(description = "The Name Attribute",
			currencyTimeLimit = 20,
			defaultValue = "bar",
			persistPolicy = "OnUpdate")
	public void setName(String name) {
		this.name = name;
	}

	@ManagedAttribute(defaultValue = "foo", persistPeriod = 300)
	public String getName() {
		return name;
	}
	
	@ManagedAttribute(description = "The Nick Name Attribute")
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getNickName() {
		return this.nickName;
	}

	public void setSuperman(boolean superman) {
		this.isSuperman = superman;
	}

	@ManagedAttribute(description = "The Is Superman Attribute")
	public boolean isSuperman() {
		return isSuperman;
	}

	@org.springframework.jmx.export.annotation.ManagedOperation(description = "Add Two Numbers Together")
	@ManagedOperationParameters({@ManagedOperationParameter(name="x", description="Left operand"),
	@ManagedOperationParameter(name="y", description="Right operand")})
	public int add(int x, int y) {
		return x + y;
	}

	/**
	 * Test method that is not exposed by the MetadataAssembler.
	 */
	public void dontExposeMe() {
		throw new RuntimeException();
	}
	
	@ManagedMetric(description="The QueueSize metric", currencyTimeLimit = 20, persistPolicy="OnUpdate", persistPeriod=300, 
			category="utilization", metricType = MetricType.COUNTER, displayName="Queue Size", unit="messages")
	public long getQueueSize() {
		return 100l;
	}
	
	@ManagedMetric
	public int getCacheEntries() {
		return 3;
	}

	

}
