/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/
package test.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeSet;


/**
 * @author Juergen Hoeller
 * @since 11.11.2003
 */
public class IndexedTestBean {

	private TestBean[] array;

	private Collection<TestBean> collection;

	private List<TestBean> list;

	private Set<TestBean> set;

	private SortedSet<TestBean> sortedSet;

	private Map<String, Object> map;

	private SortedMap<String, TestBean> sortedMap;

	public IndexedTestBean() {
		this(true);
	}

	public IndexedTestBean(boolean populate) {
		if (populate) {
			populate();
		}
	}

	public void populate() {
		TestBean tb0 = new TestBean("name0", 0);
		TestBean tb1 = new TestBean("name1", 0);
		TestBean tb2 = new TestBean("name2", 0);
		TestBean tb3 = new TestBean("name3", 0);
		TestBean tb4 = new TestBean("name4", 0);
		TestBean tb5 = new TestBean("name5", 0);
		TestBean tb6 = new TestBean("name6", 0);
		TestBean tb7 = new TestBean("name7", 0);
		TestBean tbX = new TestBean("nameX", 0);
		TestBean tbY = new TestBean("nameY", 0);
		this.array = new TestBean[] { tb0, tb1 };
		this.list = new ArrayList<TestBean>();
		this.list.add(tb2);
		this.list.add(tb3);
		this.set = new TreeSet<TestBean>();
		this.set.add(tb6);
		this.set.add(tb7);
		this.map = new HashMap<String, Object>();
		this.map.put("key1", tb4);
		this.map.put("key2", tb5);
		this.map.put("key.3", tb5);
		List<TestBean> list = new ArrayList<TestBean>();
		list.add(tbX);
		list.add(tbY);
		this.map.put("key4", list);
	}

	public TestBean[] getArray() {
		return array;
	}

	public void setArray(TestBean[] array) {
		this.array = array;
	}

	public Collection<?> getCollection() {
		return collection;
	}

	public void setCollection(Collection<TestBean> collection) {
		this.collection = collection;
	}

	public List<TestBean> getList() {
		return list;
	}

	public void setList(List<TestBean> list) {
		this.list = list;
	}

	public Set<TestBean> getSet() {
		return set;
	}

	public void setSet(Set<TestBean> set) {
		this.set = set;
	}

	public SortedSet<TestBean> getSortedSet() {
		return sortedSet;
	}

	public void setSortedSet(SortedSet<TestBean> sortedSet) {
		this.sortedSet = sortedSet;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public SortedMap<String, TestBean> getSortedMap() {
		return sortedMap;
	}

	public void setSortedMap(SortedMap<String, TestBean> sortedMap) {
		this.sortedMap = sortedMap;
	}

}
