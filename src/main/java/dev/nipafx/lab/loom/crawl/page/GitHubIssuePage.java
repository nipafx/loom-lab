package dev.nipafx.lab.loom.crawl.page;

import java.net.URI;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public record GitHubIssuePage(URI url, int issueNumber, String content, Set<Page> links) implements GitHubPage {

	public GitHubIssuePage {
		requireNonNull(url);
		if (issueNumber <= 0)
			throw new IllegalArgumentException("Issue number must be 1 or greater - was '%s' at '%s'.".formatted(issueNumber, url));
		requireNonNull(content);
		links = new HashSet<>(links);
	}

	public GitHubIssuePage(URI url, int issueNumber, String content) {
		this(url, issueNumber, content, new HashSet<>());
	}

	@Override
	public boolean equals(Object other) {
		return other == this
				|| other instanceof GitHubIssuePage page
				&& this.url.equals(page.url());
	}

	@Override
	public int hashCode() {
		return Objects.hash(url);
	}

	@Override
	public String toString() {
		return "GitHub issue @ " + url.toString();
	}

}
