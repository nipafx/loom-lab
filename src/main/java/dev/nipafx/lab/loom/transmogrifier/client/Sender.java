package dev.nipafx.lab.loom.transmogrifier.client;

import java.io.UncheckedIOException;

public interface Sender {

	void sendMessages(String messageRoot) throws UncheckedIOException, InterruptedException;

}
