package dev.nipafx.lab.loom.disk;

import java.nio.file.Path;

public class DiskStats {

	/**
	 * @param args:
	 * 		0: kind of threading: "single" or "virtual"
	 * 	    1: path to analyze
	 */
	public static void main(String[] args) throws InterruptedException {
		Analyzer analyzer = switch (args[0]) {
			case "virtual" -> new VirtualThreadsAnalyzer();
			case "single" -> new SingleThreadAnalyzer();
			default -> throw new IllegalArgumentException("Unknown analyzer: " + args[0]);
		};
		var rootDirectory = Path.of(args[1]);

		long startTime = System.currentTimeMillis();
		var stats = analyzer.analyzeFolder(rootDirectory);
		long elapsedTime = System.currentTimeMillis() - startTime;

		System.out.println(stats);
		System.out.println("Time: " + elapsedTime + "ms");
	}

}
