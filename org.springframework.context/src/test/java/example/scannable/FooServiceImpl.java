/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package example.scannable;

import java.util.List;
import java.util.concurrent.Future;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * @author Mark Fisher
 * @author Juergen Hoeller
 */
@Service @Lazy @DependsOn("myNamedComponent")
public class FooServiceImpl implements FooService {

	@Autowired private FooDao fooDao;

	@Autowired public BeanFactory beanFactory;

	@Autowired public List<ListableBeanFactory> listableBeanFactory;

	@Autowired public ResourceLoader resourceLoader;

	@Autowired public ResourcePatternResolver resourcePatternResolver;

	@Autowired public ApplicationEventPublisher eventPublisher;

	@Autowired public MessageSource messageSource;

	@Autowired public ApplicationContext context;

	@Autowired public ConfigurableApplicationContext[] configurableContext;

	@Autowired public AbstractApplicationContext genericContext;

	private boolean initCalled = false;

	@SuppressWarnings("unused")
	@PostConstruct
	private void init() {
		if (this.initCalled) {
			throw new IllegalStateException("Init already called");
		}
		this.initCalled = true;
	}

	public String foo(int id) {
		return this.fooDao.findFoo(id);
	}

	public Future<String> asyncFoo(int id) {
		System.out.println(Thread.currentThread().getName());
		Assert.state(ServiceInvocationCounter.getThreadLocalCount() != null, "Thread-local counter not exposed");
		return new AsyncResult<String>(this.fooDao.findFoo(id));
	}

	public boolean isInitCalled() {
		return this.initCalled;
	}

}
