package com.github.xeranic.blacksmith.deferred;

public interface ProgressPipe<S> {

	Promise<S> pipeProgress(Progress progress);

}
