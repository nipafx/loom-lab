package dev.nipafx.lab.loom.transmogrifier.client;

import java.io.UncheckedIOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredExecutor;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;

public class VirtualThreadSender implements Sender {

	private final Consumer<String> sendMessage;

	public VirtualThreadSender(Consumer<String> sendMessage) {
		this.sendMessage = requireNonNull(sendMessage);
	}

	@Override
	public void sendMessages(String messageRoot) throws UncheckedIOException, InterruptedException {
		try (var executor = StructuredExecutor.open()) {
			var handler = new StructuredExecutor.ShutdownOnFailure();
			IntStream.range(0, 50)
					.forEach(counter -> {
						String message = messageRoot + " " + counter;
						Runnable send = () -> sendMessage.accept(message);
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
