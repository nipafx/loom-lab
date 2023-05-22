package dev.nipafx.lab.loom.disk;

import java.io.UncheckedIOException;
import java.nio.file.Path;

interface Analyzer {

	FolderStats analyzeFolder(Path folder) throws UncheckedIOException, InterruptedException;

	default String analyzerStats() {
		return "";
	}

}
