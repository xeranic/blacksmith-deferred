package com.github.xeranic.blacksmith.deferred;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractPromise<T> implements Promise<T> {

	private static final Logger log = Logger.getLogger(AbstractPromise.class.getName());

	protected State state;

	private List<DoneCallback<T>> doneCallbacks;
	private List<FailCallback> failCallbacks;
	private List<AlwaysCallback> alwaysCallbacks;
	private List<ProgressCallback> progressCallbacks;

	private T result;
	private Throwable failure;
	private Progress progress;

	@Override
	public final synchronized State getState() {
		return state;
	}

	@Override
	public final synchronized boolean isPending() {
		return state == State.PENDING;
	}

	@Override
	public final synchronized boolean isResolved() {
		return state == State.RESOLVED;
	}

	@Override
	public final synchronized boolean isRejected() {
		return state == State.REJECTED;
	}

	@Override
	public final synchronized Promise<T> done(DoneCallback<T> doneCallback) {
		if (doneCallback == null) {
			return this;
		}

		if (isPending()) {
			if (doneCallbacks == null) {
				doneCallbacks = new ArrayList<>();
			}
			doneCallbacks.add(doneCallback);
		} else if (isResolved()) {
			triggerDone(doneCallback);
		}
		return this;
	}

	@Override
	public final synchronized Promise<T> fail(FailCallback failCallback) {
		if (failCallback == null) {
			return this;
		}

		if (isPending()) {
			if (failCallbacks == null) {
				failCallbacks = new ArrayList<>();
			}
			failCallbacks.add(failCallback);
		} else if (isRejected()) {
			triggerFail(failCallback);
		}
		return this;
	}

	@Override
	public final synchronized Promise<T> always(AlwaysCallback alwaysCallback) {
		if (alwaysCallback == null) {
			return this;
		}

		if (isPending()) {
			if (alwaysCallbacks == null) {
				alwaysCallbacks = new ArrayList<>();
			}
			alwaysCallbacks.add(alwaysCallback);
		} else {
			triggerAlways(alwaysCallback);
		}
		return this;
	}

	@Override
	public final synchronized Promise<T> progress(ProgressCallback progressCallback) {
		if (progressCallback == null) {
			return this;
		}

		if (isPending()) {
			if (progressCallbacks == null) {
				progressCallbacks = new ArrayList();
			}
			progressCallbacks.add(progressCallback);
		}
		triggerProgress(progressCallback);
		return this;
	}

	protected final synchronized void triggerDone() {
		if (doneCallbacks != null) {
			for (int i=0, len=doneCallbacks.size(); i<len; i++) {
				triggerDone(doneCallbacks.get(i));
			}
		}
	}

	private void triggerDone(DoneCallback<T> doneCallback) {
		try {
			doneCallback.onDone(result);
		} catch (Throwable t) {
			log.log(Level.SEVERE, "unexpected exception", t);
		}
	}

	protected final synchronized void triggerFail() {
		if (failCallbacks != null) {
			for (int i=0, len=failCallbacks.size(); i<len; i++) {
				triggerFail(failCallbacks.get(i));
			}			
		}
	}

	private void triggerFail(FailCallback failCallback) {
		try {
			failCallback.onFail(failure);
		} catch (Throwable t) {
			log.log(Level.SEVERE, "unexpected exception", t);
		}
	}

	protected final synchronized void triggerAlways() {
		if (alwaysCallbacks != null) {
			for (int i=0, len=alwaysCallbacks.size(); i<len; i++) {
				triggerAlways(alwaysCallbacks.get(i));
			}			
		}
	}

	private void triggerAlways(AlwaysCallback alwaysCallback) {
		try {
			alwaysCallback.onAlways(state, result, failure);
		} catch (Throwable t) {
			log.log(Level.SEVERE, "unexpected exception", t);
		}
	}

	protected final synchronized void triggerProgress() {
		if (progressCallbacks != null) {
			for (int i=0, len=progressCallbacks.size(); i<len; i++) {
				triggerProgress(progressCallbacks.get(i));
			}
		}
	}

	private void triggerProgress(ProgressCallback progressCallback) {
		try {
			progressCallback.onProgress(progress);
		} catch (Throwable t) {
			log.log(Level.SEVERE, "unexpected exception", t);
		}
	}

}
