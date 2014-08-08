package com.github.xeranic.blacksmith.deferred;

import static org.junit.Assert.*;

import org.junit.Test;

import com.github.xeranic.blacksmith.deferred.Promise.State;

public class DeferredObjectTest {

	@Test
	public void testCreateResolved() {
		Deferred<String> deferred = DeferredObject.createResolved("yes");
		assertEquals(State.RESOLVED, deferred.getState());
		assertFalse(deferred.isPending());
		assertFalse(deferred.isRejected());
		assertTrue(deferred.isResolved());
	}
	
	@Test
	public void testCreateRejected() {
		Deferred<String> deferred = DeferredObject.createRejected(new RuntimeException());
		assertEquals(State.REJECTED, deferred.getState());
		assertFalse(deferred.isPending());
		assertFalse(deferred.isResolved());
		assertTrue(deferred.isRejected());
	}
	
}
