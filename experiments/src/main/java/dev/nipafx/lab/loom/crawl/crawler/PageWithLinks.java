package dev.nipafx.lab.loom.crawl.crawler;

import dev.nipafx.lab.loom.crawl.page.Page;

import java.net.URI;
import java.util.Set;

import static java.util.Objects.requireNonNull;

record PageWithLinks(Page page, Set<URI> links) {

	PageWithLinks {
		requireNonNull(page);
		requireNonNull(links);
		links = Set.copyOf(links);
	}

	public PageWithLinks(Page page) {
		this(page, Set.of());
	}

}
