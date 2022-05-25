package dev.nipafx.lab.loom.refactoring;

import java.util.concurrent.locks.ReentrantLock;

public class Synchronization {

	// WITH SYNCHRONIZATION

	// `synchronized` guarantees sequential access
	public synchronized String accessResource_sync() {
		return accessResource();
	}

	private String accessResource() {
		return "Resource";
	}

	// WITH REENTRANT LOCK

	private static final ReentrantLock RESOURCE_LOCK = new ReentrantLock();

	public  String accessResource_lock() {
		// lock guarantees sequential access
		RESOURCE_LOCK.lock();
		try {
			return accessResource();
		} finally {
			RESOURCE_LOCK.unlock();
		}
	}

}
