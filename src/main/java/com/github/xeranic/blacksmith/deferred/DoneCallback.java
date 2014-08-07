package com.github.xeranic.blacksmith.deferred;

interface DoneCallback<T> {
    
    void onDone(T result);
    
}
