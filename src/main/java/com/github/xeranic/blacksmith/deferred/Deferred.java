package com.github.xeranic.blacksmith.deferred;

public interface Deferred<T> extends Promise<T> {
    
    Deferred<T> resolve(T result);

    Deferred<T> reject(Throwable failure);

    Deferred<T> notify(Progress progress);
    
    Promise<T> promise();
    
}