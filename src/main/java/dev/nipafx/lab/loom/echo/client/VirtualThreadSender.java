package dev.nipafx.lab.loom.echo.client;

import java.io.UncheckedIOException;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

/**
 * Spawns virtual threads that send messages to a destination defined
 * by the specified {@code sender} (see constructor).
 */
class VirtualThreadSender implements Sender {

	private final Consumer<String> sender;
	private final int messageCount;

	public VirtualThreadSender(Consumer<String> sender, int messageCount) {
		this.sender = requireNonNull(sender);
		this.messageCount = messageCount;
	}

	@Override
	public void sendMessages(String messageRoot) throws UncheckedIOException {
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			IntStream.range(0, messageCount)
					.forEach(counter -> {
						String message = messageRoot + " " + counter;
						Runnable send = () -> sender.accept(message);
						executor.execute(send);
					});
		} catch (Exception ex) {
			if (ex.getCause() instanceof RuntimeException runtimeException)
				throw runtimeException;
			else
				throw new RuntimeException(ex.getCause());
		}
	}

}
