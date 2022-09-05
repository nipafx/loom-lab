package dev.nipafx.lab.loom.crawl;

import dev.nipafx.lab.loom.crawl.page.PageFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;

public class Main {

	public static void main(String[] args) throws Exception {
		var client = HttpClient.newHttpClient();
		var seedUrl = "https://github.com/junit-pioneer/junit-pioneer/pull/627";

		URI url = new URI(seedUrl);
		var pageBody = getPage(client, url);
		var page = PageFactory.parsePage(url, pageBody);
		System.out.println(page);
	}

	private static String getPage(HttpClient client, URI url) throws IOException, InterruptedException {
		var request = HttpRequest
				.newBuilder(url)
				.GET()
				.build();
		var responseBody = client.send(request, BodyHandlers.ofString()).body();
		return responseBody;
	}

}
