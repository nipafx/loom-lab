package dev.nipafx.lab.loom.crawl;

import dev.nipafx.lab.loom.crawl.page.PageTreeFactory;

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
		var seedUrl = new URI("https://github.com/junit-pioneer/junit-pioneer/pull/627");

		var factory = new PageTreeFactory(client);
		var rootPage = factory.createPage(config.seedUrl(), config.depth());

		System.out.println("\n---\n");
		System.out.println(Statistician.evaluate(rootPage));
		System.out.println("\n---\n");
		System.out.println(Pretty.printPageList(rootPage));
		System.out.println("\n---\n");
		System.out.println(Pretty.printPageTree(rootPage));
		System.out.println("\n---\n");
	}

	private record Configuration(URI seedUrl, int depth) {

		static Configuration parse(String[] args) throws URISyntaxException {
			if (args.length < 2)
				throw new IllegalArgumentException("Please specify the seed URL and depth.");
			var seedUrl = new URI("https://github.com/junit-pioneer/junit-pioneer/pull/627");
			var depth = Integer.parseInt(args[1]);
			return new Configuration(seedUrl, depth);
		}

	}

}
