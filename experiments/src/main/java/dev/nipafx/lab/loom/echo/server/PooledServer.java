package dev.nipafx.lab.loom.echo.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static java.util.Objects.requireNonNull;

class PooledServer implements Server {

	private final Consumer<Socket> echo;
	private final ExecutorService pool;

	PooledServer(Consumer<Socket> echo, int threadCount) {
		this.echo = requireNonNull(echo);
		this.pool = Executors.newFixedThreadPool(threadCount);
	}

	@Override
	public void listen() throws IOException {
		ServerSocket server = new ServerSocket(8080);
		try (pool) {
			while (true) {
				Socket socket = server.accept();
				pool.submit(() -> echo.accept(socket));
			}
		}
	}

}
