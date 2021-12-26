package dev.nipafx.lab.loom.transmogrifier.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.net.Socket;

public class Send {

	public static void main(String[] args) throws InterruptedException {
		Sender sender = switch(args[0]) {
			case "pooled" -> new PooledSender(Send::sendMessage);
			case "virtual" -> new VirtualThreadSender(Send::sendMessage);
			default -> throw new IllegalArgumentException();
		};
		sender.sendMessages("fOo bAR");
	}

	private static void sendMessage(String message) {
		System.out.printf("Sending: '%s'%n", message);
		try (var socket = new Socket("localhost", 8080)) {
			byte[] messageBytes = (message + "\n").getBytes();
			socket.getOutputStream().write(messageBytes);

			var reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			var reply = reader.readLine();
			System.out.printf("Received: '%s' for '%s'%n", reply, message);
		} catch (IOException ex) {
			throw new UncheckedIOException(ex);
		}
	}

}
