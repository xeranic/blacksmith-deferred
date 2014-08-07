package com.github.xeranic.blacksmith.deferred;

public interface DoneFilter<T, S> {

	S filterDone(T result);

}
