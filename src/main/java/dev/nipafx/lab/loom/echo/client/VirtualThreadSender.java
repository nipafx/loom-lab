package dev.nipafx.lab.loom.echo.client;

import java.io.UncheckedIOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredExecutor;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

/**
 * Uses Loom's {@link StructuredExecutor} to spawn virtual threads that send messages
 * to a destination defined by the specified {@code sender} (see constructor).
 */
class VirtualThreadSender implements Sender {

	private final Consumer<String> sender;
	private final int messageCount;

	public VirtualThreadSender(Consumer<String> sender, int messageCount) {
		this.sender = requireNonNull(sender);
		this.messageCount = messageCount;
	}

	@Override
	public void sendMessages(String messageRoot) throws UncheckedIOException, InterruptedException {
		try (var executor = StructuredExecutor.open()) {
			var handler = new StructuredExecutor.ShutdownOnFailure();
			IntStream.range(0, messageCount)
					.forEach(counter -> {
						String message = messageRoot + " " + counter;
						Runnable send = () -> sender.accept(message);
						executor.execute(send);
					});

			executor.join();
			handler.throwIfFailed();
		} catch (ExecutionException ex) {
			if (ex.getCause() instanceof RuntimeException runtimeException)
				throw runtimeException;
			else
				throw new RuntimeException(ex.getCause());
		}
	}

}
