package dev.nipafx.lab.loom.quarkus;

import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("api")
public class QuarkusEndpoints {

	@GET
	@Path("current-thread")
//	@RunOnVirtualThread
	public String currentThread() {
		return """
			{
				"current-thread": "%s",
				"is-virtual": "%s"
			}
			""".formatted(Thread.currentThread(), Thread.currentThread().isVirtual());
	}

}
