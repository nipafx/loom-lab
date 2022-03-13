package dev.nipafx.lab.loom.disk;

import java.nio.file.Path;
import java.util.List;

import static java.util.Objects.requireNonNull;

record FileStats(Path path, long size) implements Stats {

	public FileStats {
		requireNonNull(path);
	}

	@Override
	public long cardinality() {
		return 1;
	}

	@Override
	public List<Stats> children() {
		return List.of();
	}

}
