package dev.nipafx.lab.loom.crawl.page;

import java.util.Set;
import java.util.stream.Stream;

public sealed interface GitHubPage extends SuccessfulPage permits GitHubIssuePage, GitHubPrPage {

	Set<Page> links();

	default Stream<Page> ancestors() {
		return Stream.concat(
				Stream.of(this),
				links().stream().flatMap(
						page -> page instanceof GitHubPage ghPage
								? ghPage.ancestors()
								: Stream.empty()));
	}

}
