package dev.nipafx.lab.loom.crawl;

import dev.nipafx.lab.loom.crawl.crawler.PageTreeFactory;
import dev.nipafx.lab.loom.crawl.operations.Pretty;
import dev.nipafx.lab.loom.crawl.operations.Statistician;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;

public class GitHubCrawl {

	/**
	 * @param args 0: path to GitHub issue or PR page
	 *             1: depth of tree that will be built
	 */
	public static void main(String[] args) throws Exception {
		var config = Configuration.parse(args);

		System.out.println("For maximum effect, run this command while the app is resolving a bunch of links:");
		System.out.printf("jcmd %s Thread.dump_to_file -format=json threads.json%n", ProcessHandle.current().pid());

		var client = HttpClient.newHttpClient();

		var factory = new PageTreeFactory(client);
		var rootPage = factory.createPage(config.seedUrl(), config.depth());

		System.out.println(STR."""

				---

				\{Statistician.evaluate(rootPage)}

				---

				\{Pretty.printPageList(rootPage)}

				---

				\{Pretty.printPageTree(rootPage)}

				---
				""");
	}

	private record Configuration(URI seedUrl, int depth) {

		static Configuration parse(String[] args) throws URISyntaxException {
			if (args.length < 2)
				throw new IllegalArgumentException("Please specify the seed URL and depth.");
			var seedUrl = new URI(args[0]);
			var depth = Integer.parseInt(args[1]);
			return new Configuration(seedUrl, depth);
		}

	}

}
