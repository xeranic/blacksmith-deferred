package com.github.xeranic.blacksmith.deferred;

public interface DonePipe<T, S> {

	Promise<S> pipeDone(T result);

}
