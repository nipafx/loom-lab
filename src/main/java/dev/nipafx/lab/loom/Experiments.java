package dev.nipafx.lab.loom;

import dev.nipafx.lab.loom.disk.DiskStats;
import dev.nipafx.lab.loom.echo.client.Send;
import dev.nipafx.lab.loom.echo.server.Echo;

import java.util.Arrays;

public class Experiments {

	public static void main(String[] args) throws Exception {
		if (args.length == 0)
			throw new IllegalArgumentException("Please specify the experiment.");
		var experiment = args[0];
		var experimentArgs = Arrays.copyOfRange(args, 1, args.length);
		switch (experiment) {
			case "DiskStats" -> DiskStats.main(experimentArgs);
			case "EchoServer" -> Echo.main(experimentArgs);
			case "EchoClient" -> Send.main(experimentArgs);
			default -> throw new IllegalArgumentException("Unknown experiment: " + experiment);
		}
	}

}
