package dev.nipafx.lab.loom.spring_boot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatProtocolHandlerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;

import java.util.concurrent.Executors;

@org.springframework.boot.autoconfigure.SpringBootApplication
public class SpringBootApplication {

	private static boolean virtual = false;

	/**
	 * @param args 0: threading strategy: "platform" (default) or "virtual"
	 */
	public static void main(String[] args) {
		virtual = args.length > 0 && args[0].equals("virtual");
		SpringApplication.run(SpringBootApplication.class, args);
	}

	@Bean
	public TomcatProtocolHandlerCustomizer<?> createExecutorForSyncCalls() {
		return virtual
				? handler -> handler.setExecutor(Executors.newVirtualThreadPerTaskExecutor())
				// rely on Spring Boot interpreting a `null` return as no-op
				: null;
	}

	@Bean
	public AsyncTaskExecutor createExecutorForAsyncCalls() {
		return virtual
				? new TaskExecutorAdapter(Executors.newVirtualThreadPerTaskExecutor())
				: null;
	}

}
