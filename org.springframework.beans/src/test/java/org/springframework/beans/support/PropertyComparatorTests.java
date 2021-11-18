/* Copyright © 2016 EIS Group and/or one of its affiliates. All rights reserved. Unpublished work under U.S. copyright laws.
 CONFIDENTIAL AND TRADE SECRET INFORMATION. No portion of this work may be copied, distributed, modified, or incorporated into any other media without EIS Group prior written consent.*/

package org.springframework.beans.support;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.springframework.util.comparator.CompoundComparator;

/**
 * Unit tests for {@link PropertyComparator}
 * 
 * @see org.springframework.util.comparator.ComparatorTests
 * 
 * @author Keith Donald
 * @author Chris Beams
 */
public class PropertyComparatorTests {

	@Test
	public void testPropertyComparator() {
		Dog dog = new Dog();
		dog.setNickName("mace");

		Dog dog2 = new Dog();
		dog2.setNickName("biscy");

		PropertyComparator c = new PropertyComparator("nickName", false, true);
		assertTrue(c.compare(dog, dog2) > 0);
		assertTrue(c.compare(dog, dog) == 0);
		assertTrue(c.compare(dog2, dog) < 0);
	}

	@Test
	public void testPropertyComparatorNulls() {
		Dog dog = new Dog();
		Dog dog2 = new Dog();
		PropertyComparator c = new PropertyComparator("nickName", false, true);
		assertTrue(c.compare(dog, dog2) == 0);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCompoundComparator() {
		CompoundComparator<Dog> c = new CompoundComparator<Dog>();
		c.addComparator(new PropertyComparator("lastName", false, true));

		Dog dog1 = new Dog();
		dog1.setFirstName("macy");
		dog1.setLastName("grayspots");

		Dog dog2 = new Dog();
		dog2.setFirstName("biscuit");
		dog2.setLastName("grayspots");

		assertTrue(c.compare(dog1, dog2) == 0);

		c.addComparator(new PropertyComparator("firstName", false, true));
		assertTrue(c.compare(dog1, dog2) > 0);

		dog2.setLastName("konikk dog");
		assertTrue(c.compare(dog2, dog1) > 0);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testCompoundComparatorInvert() {
		CompoundComparator<Dog> c = new CompoundComparator<Dog>();
		c.addComparator(new PropertyComparator("lastName", false, true));
		c.addComparator(new PropertyComparator("firstName", false, true));
		Dog dog1 = new Dog();
		dog1.setFirstName("macy");
		dog1.setLastName("grayspots");

		Dog dog2 = new Dog();
		dog2.setFirstName("biscuit");
		dog2.setLastName("grayspots");

		assertTrue(c.compare(dog1, dog2) > 0);
		c.invertOrder();
		assertTrue(c.compare(dog1, dog2) < 0);
	}


	private static class Dog implements Comparable<Object> {

		private String nickName;

		private String firstName;

		private String lastName;

		public int compareTo(Object o) {
			return nickName.compareTo(((Dog)o).nickName);
		}

		public String getNickName() {
			return nickName;
		}

		public void setNickName(String nickName) {
			this.nickName = nickName;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}
	}

}
