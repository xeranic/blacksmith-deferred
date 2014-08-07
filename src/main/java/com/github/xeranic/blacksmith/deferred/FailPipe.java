package com.github.xeranic.blacksmith.deferred;

public interface FailPipe<T> {

	Promise<T> pipeFail(Throwable failure);

}
