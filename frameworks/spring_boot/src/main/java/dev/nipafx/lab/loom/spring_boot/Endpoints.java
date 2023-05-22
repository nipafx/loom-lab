package dev.nipafx.lab.loom.spring_boot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api")
public class Endpoints {

	@GetMapping(path = "/current-thread", produces = APPLICATION_JSON_VALUE)
	public String currentThread() {
		return """
			{
				"current-thread": "%s",
				"is-virtual": "%s"
			}
			""".formatted(Thread.currentThread(), Thread.currentThread().isVirtual());
	}

}