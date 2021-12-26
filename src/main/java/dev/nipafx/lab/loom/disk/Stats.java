package dev.nipafx.lab.loom.disk;

import java.nio.file.Path;
import java.util.List;

public sealed interface Stats permits FileStats, FolderStats{

	Path path();

	long size();

	default List<Stats> children() {
		return List.of();
	}

}
