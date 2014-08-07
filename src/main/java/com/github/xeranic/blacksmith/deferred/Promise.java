package com.github.xeranic.blacksmith.deferred;

public interface Promise<T> {
    
    public enum State {
        PENDING, RESOLVED, REJECTED;
    }
    
    State getState();
    
    boolean isPending();
    
    boolean isResolved();
    
    boolean isRejected();

    Promise<T> done(DoneCallback<T> doneCallback);
    
    Promise<T> fail(FailCallback failCallback);
    
    Promise<T> always(AlwaysCallback<T> alwaysCallback);
    
    Promise<T> progress(ProgressCallback progressCallback);
    
    Promise<T> then(DoneCallback<T> doneCallback);
    
    Promise<T> then(DoneCallback<T> doneCallback, FailCallback failCallback);

    Promise<T> then(DoneCallback<T> doneCallback, FailCallback failCallback, ProgressCallback progressCallback);
    
    <S> Promise<S> then(DoneFilter<T, S> doneFilter);

    <S> Promise<S> then(DoneFilter<T, S> doneFilter, FailFilter failFilter);

    <S> Promise<S> then(DoneFilter<T, S> doneFilter, FailFilter failFilter, ProgressFilter progressFilter);

    <S> Promise<S> then(DonePipe<T, S> donePipe);

    <S> Promise<S> then(DonePipe<T, S> donePipe, FailPipe<S> failPipe);

    <S> Promise<S> then(DonePipe<T, S> donePipe, FailPipe<S> failPipe, ProgressPipe<S> progressPipe);

}
