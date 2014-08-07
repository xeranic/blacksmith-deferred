package com.github.xeranic.blacksmith.deferred;

import com.github.xeranic.blacksmith.deferred.Promise.State;

public interface AlwaysCallback<T> {
    
    void onAlways(State state, T result, Throwable failure);
    
}
