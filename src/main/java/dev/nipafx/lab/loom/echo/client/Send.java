package dev.nipafx.lab.loom.echo.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.net.Socket;

/**
 * Sends a bunch of messages localhost:8080.
 */
public class Send {

	/**
	 * @param args 0: threading strategy: "pooled" or "virtual" (required)
	 *             1: number of messages (optional - defaults to {@link Configuration#DEFAULT_MESSAGE_COUNT DEFAULT_MESSAGE_COUNT}
	 *             2: number of threads for the pooled sender (optional - defaults to {@link Configuration#DEFAULT_THREAD_COUNT DEFAULT_THREAD_COUNT}
	 */
	public static void main(String[] args) throws InterruptedException {
		var config = Configuration.parse(args);
		Sender sender = switch (config.threading()) {
			case POOLED -> new PooledSender(
					Send::sendMessageAndWaitForReply,
					config.messageCount(),
					config.threadCount());
			case VIRTUAL -> new VirtualThreadSender(
					Send::sendMessageAndWaitForReply,
					config.messageCount());
		};

		System.out.printf("Sender up - start sending %d messages...%n", config.messageCount());
		var startTime = System.currentTimeMillis();
		sender.sendMessages("fOo bAR");
		var elapsedTime = System.currentTimeMillis() - startTime;
		System.out.printf("Done in %dms%n(NOTE: This measurement is very unreliable for various reasons! E.g. `println` itself.)%n", elapsedTime);
	}

	private static void sendMessageAndWaitForReply(String message) {
		System.out.printf("Sending: '%s'%n", message);
		try (var socket = new Socket("localhost", 8080);
				var receiver = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				var sender = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))
		) {
			sender.println(message);
			sender.flush();
			var reply = receiver.readLine();
			System.out.printf("Received: '%s'.%n", reply);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

	private record Configuration(Threading threading, int messageCount, int threadCount) {

		static final int DEFAULT_MESSAGE_COUNT = 100;
		static final int DEFAULT_THREAD_COUNT = Runtime.getRuntime().availableProcessors();

		static Configuration parse(String[] args) {
			if (args.length == 0)
				throw new IllegalArgumentException("Please specify the implementation.");
			var implementation = Threading.valueOf(args[0].toUpperCase());
			var messageCount = args.length >= 2 ? Integer.parseInt(args[1]) : DEFAULT_MESSAGE_COUNT;
			var threadCount = args.length == 3 ? Integer.parseInt(args[2]) : DEFAULT_THREAD_COUNT;
			return new Configuration(implementation, messageCount, threadCount);
		}

	}

	private enum Threading {
		POOLED,
		VIRTUAL
	}

}
