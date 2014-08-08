package com.github.xeranic.blacksmith.deferred;

public class DeferredObject<T> extends AbstractPromise<T> implements Deferred<T> {

	public static <T> Deferred<T> createResolved(T result) {
		return new DeferredObject<T>().resolve(result);
	}
	
	public static <T> Deferred<T> createRejected(Throwable failure) {
		return new DeferredObject<T>().reject(failure);
	}
	
	@Override
	public final synchronized Deferred<T> resolve(T result) {
		if (!isPending()) {
			throw new IllegalStateException("already resolved or rejected");
		}
		this.state = State.RESOLVED;
		this.result = result;
		triggerDone();
		triggerAlways();
		return this;
	}

	@Override
	public final synchronized Deferred<T> reject(Throwable failure) {
		if (!isPending()) {
			throw new IllegalStateException("already resolved or rejected");
		}
		this.state = State.REJECTED;
		this.failure = failure;
		triggerFail();
		triggerAlways();
		return this;
	}

	@Override
    public final synchronized Deferred<T> notify(Progress progress) {
    	if (!isPending()) {
    		throw new IllegalStateException("already resolved or rejected");
    	}
    	this.progress = progress;
    	triggerProgress();
    	return this;
    }

	@Override
	public final Promise<T> promise() {
		return this;
	}

}
