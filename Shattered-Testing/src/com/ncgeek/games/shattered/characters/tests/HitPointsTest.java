package com.ncgeek.games.shattered.characters.tests;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ncgeek.games.shattered.characters.HitPoints;

public class HitPointsTest {

	private HitPoints hp;
	
	@Before
	public void setUp() throws Exception {
		hp = new HitPoints();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMax() {
		assertEquals("Initial max", 0, hp.getMax());
		
		final int MAX = 15;
		hp.setMax(MAX);
		assertEquals("Set max", MAX, hp.getMax());
		
		hp.setCurrent(MAX);
		
		final int MAX2 = 10;
		hp.setMax(MAX2);
		assertEquals("Reduce Current", MAX2, hp.getCurrent());
	}

	@Test
	public void testCurrent() {
		assertEquals("Initial current", 0, hp.getCurrent());
		
		final int MAX = 15;
		hp.setMax(MAX);
		
		assertEquals("After max set", 0, hp.getCurrent());
		
		hp.setCurrent(MAX);
		assertEquals("Set current", MAX, hp.getCurrent());
		
		try {
			hp.setCurrent(MAX + 1);
			fail("Should not be able to set current > max");
		} catch(IllegalArgumentException ex) {}
		
		hp.setCurrent(0);
		assertEquals("Zero", 0, hp.getCurrent());
		
		hp.setCurrent(-5);
		assertEquals("<0", 0, hp.getCurrent());
	}

	@Test
	public void testIsDead() {
		assertTrue("Initial isDead", hp.isDead());
		
		hp.setMax(10);
		hp.setCurrent(10);
		
		assertFalse("Not dead", hp.isDead());
		
		hp.setCurrent(0);
		assertTrue("Dead", hp.isDead());
	}

	@Test
	public void testGetBloodiedValue() {
		hp.setMax(20);
		assertEquals("max=20", 10, hp.getBloodiedValue());
		
		hp.setMax(11);
		assertEquals("max=11", 5, hp.getBloodiedValue());
	}

	@Test
	public void testIsBloodied() {
		final int MAX = 20;
		
		hp.setMax(MAX);
		hp.setCurrent(MAX);
		
		assertFalse("Max, !bloodied", hp.isBloodied());
		
		hp.setCurrent(MAX / 2 + 1);
		assertFalse("Max/2+1, !bloodied", hp.isBloodied());
		
		hp.setCurrent(MAX/2);
		assertTrue("Max/2, bloodied", hp.isBloodied());
		
		hp.setCurrent(MAX/2-1);
		assertTrue("Max/2-1, bloodied", hp.isBloodied());
		
		hp.setCurrent(1);
		assertTrue("1, bloodied", hp.isBloodied());
	}

	@Test
	public void testTakeDamage() {
		final int MAX = 20;
		final int DAMAGE = 5;
		
		hp.setMax(MAX);
		hp.setCurrent(MAX);
		
		hp.takeDamage(5);
		assertEquals("take damage partial", MAX-DAMAGE, hp.getCurrent());
		
		hp.takeDamage(MAX);
		assertEquals("take damage over", 0, hp.getCurrent());
	}

	@Test
	public void testHeal() {
		final int MAX = 20;
		final int HEAL = 5;
		
		hp.setMax(MAX);
		hp.setCurrent(MAX);
		
		hp.heal(HEAL);
		assertEquals("max heal", MAX, hp.getCurrent());
		
		hp.setCurrent(MAX - HEAL + 1);
		hp.heal(HEAL);
		assertEquals("overheal", MAX, hp.getCurrent());
		
		hp.setCurrent(MAX-HEAL-1);
		hp.heal(HEAL);
		assertEquals("underheal", MAX-1, hp.getCurrent());
		
		hp.setCurrent(0);
		hp.heal(HEAL);
		assertEquals("healFromDead", HEAL, hp.getCurrent());
	}

}
