package dev.nipafx.lab.loom.refactoring;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.joining;

public class FilesystemOp {

	private static final Object LOCK = new Object();

	public static void main(String[] args) {
		int numberOfReads = 8;
		repeatedlyReadFile(FilesystemOp::readFileSynchronizedMethod, numberOfReads);
//		repeatedlyReadFile(FilesystemOp::readFileSynchronizedMethod, numberOfReads);
//		repeatedlyReadFile(FilesystemOp::readFileSynchronizedBlock, numberOfReads);
	}

	private static void repeatedlyReadFile(Callable<String> read, int numberOfReads) {
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			List<Future<String>> executions = IntStream.range(0, numberOfReads)
					.mapToObj(__ -> executor.submit(read))
					.toList();
			long sum = executions.stream()
					.map(FilesystemOp::unsafeGet)
					.mapToLong(content -> content.lines().count())
					.sum();
			System.out.printf("Read %d lines.%n", sum);
		}
	}

	private static String unsafeGet(Future<String> stringFuture) {
		try {
			return stringFuture.get();
		} catch (InterruptedException | ExecutionException ex) {
			ex.printStackTrace();
			return "";
		}
	}

	private static synchronized String readFileSynchronizedMethod() {
		return readFile();
	}

	private static String readFileSynchronizedBlock() {
		synchronized (LOCK) {
			return readFile();
		}
	}

	private static String readFile() {
		try (var fileStream = FilesystemOp.class.getResourceAsStream("/some_file.md");
				var fileReader = new BufferedReader(new InputStreamReader(fileStream))) {
			Thread.sleep(1_000);
			return fileReader.lines().collect(joining("\n"));
		} catch (IOException | InterruptedException ex) {
			ex.printStackTrace();
			return "";
		}
	}

}
