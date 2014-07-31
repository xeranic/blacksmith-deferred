public interface Deferred<T> {
	
	void resolve(T result);
	
	void reject(Throwable failure);
	
}