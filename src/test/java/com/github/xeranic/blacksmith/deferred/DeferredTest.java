package com.github.xeranic.blacksmith.deferred;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

import com.github.xeranic.blacksmith.deferred.Promise.State;

public class DeferredTest {

	@Test
	public void testCreateResolved() {
		Deferred<String> deferred = Deferred.createResolved("yes");
		assertEquals(State.RESOLVED, deferred.getState());
		assertFalse(deferred.isPending());
		assertFalse(deferred.isRejected());
		assertTrue(deferred.isResolved());
	}
	
	@Test
	public void testCreateRejected() {
		Deferred<String> deferred = Deferred.createRejected(new RuntimeException());
		assertEquals(State.REJECTED, deferred.getState());
		assertFalse(deferred.isPending());
		assertFalse(deferred.isResolved());
		assertTrue(deferred.isRejected());
	}
	
	@Test
	public void testDoneCallback() {
		@SuppressWarnings("unchecked")
		DoneCallback<String> doneCallback = mock(DoneCallback.class);
		
		Deferred<String> deferred = new Deferred<>();
		deferred.promise().done(doneCallback);
		
		verify(doneCallback, never()).onDone((String) any());
		deferred.resolve("yes");
		verify(doneCallback).onDone("yes");
	}
	
}
