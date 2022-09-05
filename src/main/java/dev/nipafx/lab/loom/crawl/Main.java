package dev.nipafx.lab.loom.crawl;

import dev.nipafx.lab.loom.crawl.page.PageTreeFactory;

import java.net.URI;
import java.net.http.HttpClient;

public class Main {

	public static void main(String[] args) throws Exception {
		var client = HttpClient.newHttpClient();
		var seedUrl = new URI("https://github.com/junit-pioneer/junit-pioneer/pull/627");

		var factory = new PageTreeFactory(client);
		var rootPage = factory.createPage(seedUrl, 3);
	}

}
