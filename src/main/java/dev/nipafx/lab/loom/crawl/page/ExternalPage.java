package dev.nipafx.lab.loom.crawl.page;

import java.net.URI;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

public record ExternalPage(URI url, String content) implements SuccessfulPage {

	public ExternalPage {
		requireNonNull(url);
		requireNonNull(content);
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
		return "External @ " + url.toString();
	}

}
