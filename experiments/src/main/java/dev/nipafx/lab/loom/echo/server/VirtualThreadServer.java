package dev.nipafx.lab.loom.echo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

class VirtualThreadServer implements Server {

	private final Consumer<Socket> echo;

	VirtualThreadServer(Consumer<Socket> echo) {
		this.echo = requireNonNull(echo);
	}

	@Override
	public void listen() throws IOException {
		ServerSocket server = new ServerSocket(8080);
		try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
			while (true) {
				Socket socket = server.accept();
				executor.execute(() -> echo.accept(socket));
			}
		}
	}

}
