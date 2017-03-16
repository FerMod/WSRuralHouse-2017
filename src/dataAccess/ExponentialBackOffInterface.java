package dataAccess;

@FunctionalInterface
public interface ExponentialBackOffInterface<T> {
	T execute();
}