package dev.nipafx.lab.loom.disk;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.atomic.LongAdder;

import static java.util.function.Predicate.not;

/**
 * Uses a new virtual thread for each file and folder to analyze (i.e. easily hundreds of thousands or millions).
 */
class VirtualThreadsAnalyzer implements Analyzer {

	private static final LongAdder VIRTUAL_THREAD_COUNT = new LongAdder();

	@Override
	public FolderStats analyzeFolder(Path folder) throws UncheckedIOException, InterruptedException {
		try (var scope = new StructuredTaskScope.ShutdownOnFailure();
			 var content = Files.list(folder)) {
			List<StructuredTaskScope.Subtask<Stats>> childrenTasks = content
					.filter(not(Files::isSymbolicLink))
					.<Callable<Stats>>map(path -> Files.isDirectory(path)
							? () -> analyzeFolder(path)
							: () -> analyzeFile(path))
					.map(scope::fork)
					.toList();

			VIRTUAL_THREAD_COUNT.add(childrenTasks.size());
			scope.join();
			scope.throwIfFailed();

			var children = childrenTasks.stream()
					.map(StructuredTaskScope.Subtask::get)
					.toList();
			return FolderStats.createWithChildren(folder, children);
		} catch (ExecutionException ex) {
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

	@Override
	public String analyzerStats() {
		return "Number of created virtual threads: %,d%n".formatted(VIRTUAL_THREAD_COUNT.sum());
	}

}
