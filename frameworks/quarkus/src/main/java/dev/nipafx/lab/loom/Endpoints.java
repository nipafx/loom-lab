package dev.nipafx.lab.loom;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("api")
public class Endpoints {

	@GET
	@Path("current-thread")
	@RunOnVirtualThread
	public String currentThread() {
		return """
			{
				"current-thread": "%s",
				"is-virtual": "%s"
			}
			""".formatted(Thread.currentThread(), Thread.currentThread().isVirtual());
	}

}
