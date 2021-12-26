package dev.nipafx.lab.loom.disk.single;

import dev.nipafx.lab.loom.disk.FileStats;
import dev.nipafx.lab.loom.disk.FolderStats;
import dev.nipafx.lab.loom.disk.Stats;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.function.Predicate.not;

public class DiskStats {

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		var rootDirectory = Path.of("/home/nipa");
		var stats = analyzeFolder(rootDirectory);
		System.out.println(stats);
		long elapsedTime = System.currentTimeMillis() - startTime;
		System.out.println("Time: " + elapsedTime + "ms");
	}

	private static FolderStats analyzeFolder(Path folder) throws UncheckedIOException {
		try (var content = Files.list(folder)) {
			var children = content
					.filter(not(Files::isSymbolicLink))
					. <Stats>map(path -> Files.isDirectory(path)
							? analyzeFolder(path)
							: analyzeFile(path))
					.toList();
			long totalSize = children.stream().mapToLong(Stats::size).sum();
			return new FolderStats(folder, totalSize, children);
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
