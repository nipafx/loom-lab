package dev.nipafx.lab.loom.disk;

import java.nio.file.Path;

public class DiskStats {

	/**
	 * @param args 0: threading strategy: "single" or "virtual" (required)
	 *             1: path to analyze (required)
	 */
	public static void main(String[] args) throws InterruptedException {
		var config = Configuration.parse(args);
		Analyzer analyzer = switch (config.threading()) {
			case SINGLE -> new SingleThreadAnalyzer();
			case VIRTUAL -> new VirtualThreadsAnalyzer();
		};

		System.out.printf("Analyzing '%s'...%n", config.root());
		long startTime = System.currentTimeMillis();
		var stats = analyzer.analyzeFolder(config.root());
		long elapsedTime = System.currentTimeMillis() - startTime;

		System.out.println(stats);
		System.out.printf("Done in %dms%n(NOTE: This measurement is very unreliable for various reasons, e.g. disk caching.)%n", elapsedTime);
		System.out.println(analyzer.analyzerStats());
	}

	private record Configuration(Threading threading, Path root) {

		static Configuration parse(String[] args) {
			if (args.length < 2)
				throw new IllegalArgumentException("Please specify the implementation and path.");
			var implementation = Threading.valueOf(args[0].toUpperCase());
			var root = Path.of(args[1]);
			return new Configuration(implementation, root);
		}

	}

	private enum Threading {
		SINGLE,
		VIRTUAL
	}

}
