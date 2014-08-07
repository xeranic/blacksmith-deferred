package com.github.xeranic.blacksmith.deferred;

public interface FailFilter {

	Throwable filterFail(Throwable failure);

}
