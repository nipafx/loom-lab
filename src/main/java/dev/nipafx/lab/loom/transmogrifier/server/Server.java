package dev.nipafx.lab.loom.transmogrifier.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.StructuredExecutor;

import static java.util.Objects.requireNonNull;

public class Server {

	public static void main(String[] args) throws IOException {
		var executor = StructuredExecutor.open();
		ServerSocket server = new ServerSocket(8080);
		System.out.println("Server up on 8080");

		while (true) {
			Socket socket = server.accept();
			var connection = new Transmogrifier(socket);
			executor.execute(connection);
		}
	}

	private static String transmogrify(String message) {
		try { Thread.sleep(100); } catch (InterruptedException ex) { }
		// TODO: left as exercise for the reader
		return message;
	}

	private static class Transmogrifier implements Runnable {

		private final Socket socket;

		private Transmogrifier(Socket socket) {
			this.socket = requireNonNull(socket);
		}

		@Override
		public void run() {
			try (socket;
					var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
				var message = reader.readLine();

				var reply = transmogrify(message);

				var outputStream = socket.getOutputStream();
				outputStream.write((reply + "\n").getBytes());
				System.out.printf("Received '%s', replied with '%s'.%n", message, reply);
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
		}

	}

}
