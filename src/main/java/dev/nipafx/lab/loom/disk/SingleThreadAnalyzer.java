package dev.nipafx.lab.loom.disk;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.function.Predicate.not;

/**
 * Uses a single thread to gather statistics (hence: sequential).
 */
class SingleThreadAnalyzer implements Analyzer {

	@Override
	public FolderStats analyzeFolder(Path folder) throws UncheckedIOException {
		try (var content = Files.list(folder)) {
			var children = content
					.filter(not(Files::isSymbolicLink))
					.<Stats>map(path -> Files.isDirectory(path)
							? analyzeFolder(path)
							: analyzeFile(path))
					.toList();
			return FolderStats.createWithChildren(folder, children);
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
