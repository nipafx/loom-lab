package dev.nipafx.lab.loom.echo.client;

import java.io.UncheckedIOException;

/**
 * Sends a bunch of messages - where and how exactly depends on the implementation.
 */
interface Sender {

	void sendMessages(String messageRoot) throws UncheckedIOException, InterruptedException;

}
