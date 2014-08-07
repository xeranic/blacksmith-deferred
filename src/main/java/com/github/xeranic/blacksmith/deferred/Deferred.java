package com.github.xeranic.blacksmith.deferred;

public interface Deferred<T> extends Promise<T> {
	
	void resolve(T result);
	
	void reject(Throwable failure);
	
}