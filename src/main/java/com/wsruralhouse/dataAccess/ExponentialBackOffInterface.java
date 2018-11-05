package com.wsruralhouse.dataAccess;

@FunctionalInterface
public interface ExponentialBackOffInterface<T> {
	T execute();
}