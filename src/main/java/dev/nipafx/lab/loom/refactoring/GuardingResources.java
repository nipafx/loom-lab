package dev.nipafx.lab.loom.refactoring;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

public class GuardingResources {

	// WITH THREAD POOL

	private static final ExecutorService DATABASE_POOL = Executors.newFixedThreadPool(16);

	public <T> Future<T> queryDatabase_Pool(Callable<T> query) {
		// thread pool limits to 16 concurrent queries
		return DATABASE_POOL.submit(query);
	}


	// WITH SEMAPHORE

	private static final Semaphore DATABASE_SEMAPHORE = new Semaphore(16);

	public <T> T queryDatabase_Semaphore(Callable<T> query) throws Exception {
		// semaphore limits to 16 concurrent queries
		DATABASE_SEMAPHORE.acquire();
		try {
			return query.call();
		} finally {
			DATABASE_SEMAPHORE.release();
		}
	}


}
