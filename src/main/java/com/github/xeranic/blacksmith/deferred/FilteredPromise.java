package com.github.xeranic.blacksmith.deferred;

class FilteredPromise<T, S> extends DeferredObject<S> {

	public FilteredPromise(Promise<T> promise,
			final DoneFilter<T, S> doneFilter, final FailFilter failFilter,
			final ProgressFilter progressFilter) {
		promise.done(new DoneCallback<T>() {
			@Override
			public void onDone(T result) {
				processDone(doneFilter, result);
			}
		}).fail(new FailCallback() {
			@Override
			public void onFail(Throwable failure) {
				processFail(failFilter, failure);
			}
		}).progress(new ProgressCallback() {
			@Override
			public void onProgress(Progress progress) {
				processProgress(progressFilter, progress);
			}
		});

	}

	@SuppressWarnings("unchecked")
	private void processDone(DoneFilter<T, S> doneFilter, T result) {
		try {
			S filtered = null;
			if (doneFilter == null) {
				filtered = (S) result;
			} else {
				filtered = doneFilter.filterDone(result);
			}
			resolve(filtered);
		} catch (Throwable t) {
			reject(t);
		}
	}

	private void processFail(FailFilter failFilter, Throwable failure) {
		if (failFilter == null) {
			reject(failure);
		} else {
			reject(failFilter.filterFail(failure));
		}
	}

	private void processProgress(ProgressFilter progressFilter,
			Progress progress) {
		if (progressFilter == null) {
			notify(progress);
		} else {
			notify(progressFilter.filterProgress(progress));
		}
	}

}
