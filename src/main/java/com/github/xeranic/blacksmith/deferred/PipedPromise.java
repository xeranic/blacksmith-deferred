package com.github.xeranic.blacksmith.deferred;

class PipedPromise<T, S> extends DeferredObject<S> {

	public PipedPromise(Promise<T> promise, final DonePipe<T, S> donePipe,
			final FailPipe<S> failPipe, final ProgressPipe<S> progressPipe) {
		promise.done(new DoneCallback<T>() {
			@Override
			public void onDone(T result) {
				processDone(donePipe, result);
			}
		}).fail(new FailCallback() {
			@Override
			public void onFail(Throwable failure) {
				processFail(failPipe, failure);
			}
		}).progress(new ProgressCallback() {
			@Override
			public void onProgress(Progress progress) {
				processProgress(progressPipe, progress);
			}
		});
	}

	private void processDone(DonePipe<T, S> donePipe, T result) {
		if (donePipe == null) {
			try {
				@SuppressWarnings("unchecked")
				S piped = (S) result;
				resolve(piped);
			} catch (Throwable t) {
				reject(t);
			}
		} else {
			processPiped(donePipe.pipeDone(result));
		}
	}

	private void processFail(FailPipe<S> failPipe, Throwable failure) {
		if (failPipe == null) {
			reject(failure);
		} else {
			processPiped(failPipe.pipeFail(failure));
		}
	}

	private void processProgress(ProgressPipe<S> progressPipe, Progress progress) {
		if (progressPipe == null) {
			notify(progress);
		} else {
			processPiped(progressPipe.pipeProgress(progress));
		}
	}

	private void processPiped(Promise<S> pipedPromise) {
		pipedPromise.done(new DoneCallback<S>() {
			@Override
			public void onDone(S result) {
				PipedPromise.this.resolve(result);
			}
		}).fail(new FailCallback() {
			@Override
			public void onFail(Throwable failure) {
				PipedPromise.this.reject(failure);
			}
		}).progress(new ProgressCallback() {
			@Override
			public void onProgress(Progress progress) {
				PipedPromise.this.notify(progress);
			}
		});
	}

}
