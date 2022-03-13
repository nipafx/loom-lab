package dev.nipafx.lab.loom.echo.client;

import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

/**
 * Uses a simple thread pool to send messages
 * to a destination defined by the specified {@code sender} (see constructor).
 */
class PooledSender implements Sender {

	private final Consumer<String> sender;
	private final ExecutorService pool;
	private final int messageCount;

	public PooledSender(Consumer<String> sender, int messageCount, int threadCount) {
		this.sender = requireNonNull(sender);
		this.messageCount = messageCount;
		this.pool = Executors.newFixedThreadPool(threadCount);
	}

	@Override
	public void sendMessages(String messageRoot) throws UncheckedIOException {
		try (pool) {
			IntStream.range(0, messageCount)
					.forEach(counter -> {
						String message = messageRoot + " " + counter;
						Runnable send = () -> sender.accept(message);
						CompletableFuture.runAsync(send, pool);
					});
		}
	}

}
