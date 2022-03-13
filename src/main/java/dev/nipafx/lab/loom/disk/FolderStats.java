package dev.nipafx.lab.loom.disk;

import java.nio.file.Path;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.summarizingLong;
import static java.util.stream.Collectors.teeing;

record FolderStats(Path path, long cardinality, long size, List<Stats> children) implements Stats {

	public FolderStats {
		requireNonNull(path);
		children = List.copyOf(children);
	}

	public static FolderStats createWithChildren(Path folder, List<Stats> children) {
		record Sizes(long cardinality, long size) { }
		var sizes = children.stream()
				.collect(
						teeing(
								summarizingLong(Stats::cardinality),
								summarizingLong(Stats::size),
								(cardinality, size) -> new Sizes(cardinality.getSum(), size.getSum())
						));
		return new FolderStats(folder, sizes.cardinality(), sizes.size(), children);
	}

	@Override
	public String toString() {
		return "FolderStats: path='%s', cardinality=%,d, size=%,d".formatted(path, cardinality, size);
	}

}
