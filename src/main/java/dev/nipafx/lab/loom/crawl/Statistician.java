package dev.nipafx.lab.loom.crawl;

import dev.nipafx.lab.loom.crawl.page.ErrorPage;
import dev.nipafx.lab.loom.crawl.page.ExternalPage;
import dev.nipafx.lab.loom.crawl.page.GitHubIssuePage;
import dev.nipafx.lab.loom.crawl.page.GitHubPage;
import dev.nipafx.lab.loom.crawl.page.GitHubPrPage;
import dev.nipafx.lab.loom.crawl.page.Page;

import java.util.HashSet;
import java.util.Set;

public class Statistician {

	private final Set<Page> evaluatedPages;

	private int numberOfIssues;
	private int numberOfPrs;
	private int numberOfExternalLinks;
	private int numberOfErrors;

	private Statistician() {
		this.evaluatedPages = new HashSet<>();
	}

	public static Stats evaluate(Page rootPage) {
		Statistician statistician = new Statistician();
		statistician.evaluateTree(rootPage);
		return statistician.result();
	}

	private void evaluateTree(Page page) {
		if (page instanceof GitHubPage ghPage)
			ghPage.subtree().forEach(this::evaluatePage);
		else
			evaluatePage(page);
	}

	private void evaluatePage(Page page) {
		if (evaluatedPages.contains(page))
			return;
		evaluatedPages.add(page);

		switch (page) {
			case ErrorPage __ -> numberOfErrors++;
			case ExternalPage __ -> numberOfExternalLinks++;
			case GitHubIssuePage __ -> numberOfIssues++;
			case GitHubPrPage __ -> numberOfPrs++;
		}
	}

	private Stats result() {
		return new Stats(numberOfIssues, numberOfPrs, numberOfExternalLinks, numberOfErrors);
	}

	public record Stats(int numberOfIssues, int numberOfPrs, int numberOfExternalLinks, int numberOfErrors) { }

}
