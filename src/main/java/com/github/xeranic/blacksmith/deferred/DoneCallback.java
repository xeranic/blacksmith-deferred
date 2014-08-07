package com.github.xeranic.blacksmith.deferred;

public interface DoneCallback<T> {
    
    void onDone(T result);
    
}
