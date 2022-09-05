package dev.nipafx.lab.loom.crawl.page;

import java.net.URI;

import static java.util.Objects.requireNonNull;

public record ExternalPage(URI url, String content) implements SuccessfulPage {

	public ExternalPage {
		requireNonNull(url);
		requireNonNull(content);
	}

	@Override
	public String toString() {
		return "External @ " + url.toString();
	}

}
