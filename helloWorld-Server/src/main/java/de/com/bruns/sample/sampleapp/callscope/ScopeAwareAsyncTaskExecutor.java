package de.com.bruns.sample.sampleapp.callscope;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.core.task.AsyncTaskExecutor;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ScopeAwareAsyncTaskExecutor implements AsyncTaskExecutor {

	private final AsyncTaskExecutor delegate;

	@Override
	public void execute(Runnable task) {
		delegate.execute(wrap(task));
	}

	/**
	 * @deprecated @see {@link AsyncTaskExecutor#execute(Runnable, long)}
	 */
	@Override
	@Deprecated
	public void execute(Runnable task, long startTimeout) {
		delegate.execute(wrap(task), startTimeout);

	}

	@Override
	public Future<?> submit(Runnable task) {
		return delegate.submit(wrap(task));
	}

	@Override
	public <T> Future<T> submit(Callable<T> task) {
		return delegate.submit(wrap(task));
	}

	Runnable wrap(Runnable task) {
		return new ScopeAwareRunnable(task);
	}

	<T> Callable<T> wrap(Callable<T> task) {
		return new ScopeAwareCallable<>(task);
	}

}

@RequiredArgsConstructor
class ScopeAwareRunnable implements Runnable {

	private final Runnable delegate;
	private final CallScope delegateScope = CallScopeHolder.getScope();

	private CallScope originalScope;

	@Override
	public void run() {
		this.originalScope = CallScopeHolder.getScope();
		try {
			CallScopeHolder.setScope(delegateScope);
			this.delegate.run();
		} finally {
			CallScopeHolder.clearScope();
			CallScopeHolder.setScope(originalScope);
		}
	}
}

@RequiredArgsConstructor
class ScopeAwareCallable<T> implements Callable<T> {

	private final Callable<T> delegate;
	private final CallScope delegateScope = CallScopeHolder.getScope();

	private CallScope originalScope;

	@Override
	public T call() throws Exception {
		this.originalScope = CallScopeHolder.getScope();
		try {
			CallScopeHolder.setScope(delegateScope);
			return delegate.call();
		} finally {
			CallScopeHolder.clearScope();
			CallScopeHolder.setScope(originalScope);
		}
	}

}
