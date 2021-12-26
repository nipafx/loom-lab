package dev.nipafx.lab.loom.disk.virtual_threads;

import dev.nipafx.lab.loom.disk.FileStats;
import dev.nipafx.lab.loom.disk.FolderStats;
import dev.nipafx.lab.loom.disk.Stats;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.StructuredExecutor;
import java.util.concurrent.atomic.LongAdder;

import static java.util.function.Predicate.not;

public class DiskStats {

	private static final LongAdder VIRTUAL_THREAD_COUNT = new LongAdder();

	public static void main(String[] args) throws InterruptedException {
		long startTime = System.currentTimeMillis();
		var rootDirectory = Path.of("/home/nipa");
		var stats = analyzeFolder(rootDirectory);
		System.out.println(stats);
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("Time: " + elapsedTime + "ms");
		System.out.println("Virtual threads: " + VIRTUAL_THREAD_COUNT);
	}

	private static FolderStats analyzeFolder(Path folder) throws UncheckedIOException, InterruptedException {
		try (var executor = StructuredExecutor.open();
				var content = Files.list(folder)) {

			var handler = new StructuredExecutor.ShutdownOnFailure();
			List<Future<Stats>> childrenTasks = content
					.filter(not(Files::isSymbolicLink))
					.<Callable<Stats>>map(path -> Files.isDirectory(path)
							? () -> analyzeFolder(path)
							: () -> analyzeFile(path))
					.map(action -> executor.fork(action, handler))
					.toList();

			VIRTUAL_THREAD_COUNT.add(childrenTasks.size());
			executor.join();
			handler.throwIfFailed();

			var children = childrenTasks.stream()
					.map(Future::resultNow)
					.toList();
			long totalSize = children.stream().mapToLong(Stats::size).sum();
			return new FolderStats(folder, totalSize, children);
		} catch (ExecutionException ex) {
			// TODO: can the cause be an interrupted exception?
			if (ex.getCause() instanceof RuntimeException runtimeException)
				throw runtimeException;
			else
				throw new RuntimeException(ex);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	private static FileStats analyzeFile(Path file) throws UncheckedIOException {
		try {
			return new FileStats(file, Files.size(file));
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

}
