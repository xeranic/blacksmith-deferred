package com.github.xeranic.blacksmith.deferred;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AbstractPromise<T> implements Promise<T> {

	private static final Logger log = Logger.getLogger(AbstractPromise.class
			.getName());

	protected State state = State.PENDING;
	protected T result;
	protected Throwable failure;
	protected Progress progress;

	private List<DoneCallback<T>> doneCallbacks;
	private List<FailCallback> failCallbacks;
	private List<AlwaysCallback<T>> alwaysCallbacks;
	private List<ProgressCallback> progressCallbacks;

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
	public final Promise<T> then(DoneCallback<T> doneCallback) {
		return done(doneCallback);
	}

	@Override
	public final Promise<T> then(DoneCallback<T> doneCallback,	FailCallback failCallback) {
		return done(doneCallback).fail(failCallback);
	}

	@Override
	public final Promise<T> then(DoneCallback<T> doneCallback,	FailCallback failCallback, ProgressCallback progressCallback) {
		return done(doneCallback).fail(failCallback).progress(progressCallback);
	}

	@Override
	public final <S> Promise<S> then(DoneFilter<T, S> doneFilter) {
		return new FilteredPromise<T, S>(this, doneFilter, null, null);
	}

	@Override
	public final <S> Promise<S> then(DoneFilter<T, S> doneFilter,	FailFilter failFilter) {
		return new FilteredPromise<T, S>(this, doneFilter, failFilter, null);
	}

	@Override
	public final <S> Promise<S> then(DoneFilter<T, S> doneFilter,	FailFilter failFilter, ProgressFilter progressFilter) {
		return new FilteredPromise<T, S>(this, doneFilter, failFilter, progressFilter);
	}

	@Override
	public final <S> Promise<S> then(DonePipe<T, S> donePipe) {
		return new PipedPromise<T, S>(this, donePipe, null, null);
	}

	@Override
	public final <S> Promise<S> then(DonePipe<T, S> donePipe, FailPipe<S> failPipe) {
		return new PipedPromise<T, S>(this, donePipe, failPipe, null);
	}

	@Override
	public final <S> Promise<S> then(DonePipe<T, S> donePipe, FailPipe<S> failPipe, ProgressPipe<S> progressPipe) {
		return new PipedPromise<T, S>(this, donePipe, failPipe, progressPipe);
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
	public final synchronized Promise<T> always(AlwaysCallback<T> alwaysCallback) {
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
	public final synchronized Promise<T> progress(
			ProgressCallback progressCallback) {
		if (progressCallback == null) {
			return this;
		}

		if (isPending()) {
			if (progressCallbacks == null) {
				progressCallbacks = new ArrayList<>();
			}
			progressCallbacks.add(progressCallback);
		}
		triggerProgress(progressCallback);
		return this;
	}

	protected final synchronized void triggerDone() {
		if (doneCallbacks != null) {
			for (int i = 0, len = doneCallbacks.size(); i < len; i++) {
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
			for (int i = 0, len = failCallbacks.size(); i < len; i++) {
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
			for (int i = 0, len = alwaysCallbacks.size(); i < len; i++) {
				triggerAlways(alwaysCallbacks.get(i));
			}
		}
	}

	private void triggerAlways(AlwaysCallback<T> alwaysCallback) {
		try {
			alwaysCallback.onAlways(state, result, failure);
		} catch (Throwable t) {
			log.log(Level.SEVERE, "unexpected exception", t);
		}
	}

	protected final synchronized void triggerProgress() {
		if (progressCallbacks != null) {
			for (int i = 0, len = progressCallbacks.size(); i < len; i++) {
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
