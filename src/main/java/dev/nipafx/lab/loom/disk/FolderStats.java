package dev.nipafx.lab.loom.disk;

import java.nio.file.Path;
import java.util.List;

import static java.util.Objects.requireNonNull;

public record FolderStats(Path path, long size, List<Stats> children) implements Stats {

	public FolderStats {
		requireNonNull(path);
		children = List.copyOf(children);
	}

	@Override
	public String toString() {
		return "FolderStats: path='%s', size=%d}".formatted(path, size);
	}

}
