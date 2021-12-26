package dev.nipafx.lab.loom.transmogrifier.client;

import java.io.UncheckedIOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

public class PooledSender implements Sender {

	private final Consumer<String> sendMessage;

	public PooledSender(Consumer<String> sendMessage) {
		this.sendMessage = requireNonNull(sendMessage);
	}

	@Override
	public void sendMessages(String messageRoot) throws UncheckedIOException, InterruptedException {
		try (var pool = Executors.newFixedThreadPool(24)) {
			IntStream.range(0, 50)
					.forEach(counter -> {
						String message = messageRoot + " " + counter;
						Runnable send = () -> sendMessage.accept(message);
						CompletableFuture.runAsync(send, pool);
					});
		}
	}

}
