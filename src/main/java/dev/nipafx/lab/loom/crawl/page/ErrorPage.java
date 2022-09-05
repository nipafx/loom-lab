package dev.nipafx.lab.loom.crawl.page;

import java.net.URI;

import static java.util.Objects.requireNonNull;

public record ErrorPage(URI url, Exception ex) implements Page {

	public ErrorPage {
		requireNonNull(url);
		requireNonNull(ex);
	}

	@Override
	public String toString() {
		return "Error @ " + url.toString();
	}

}
