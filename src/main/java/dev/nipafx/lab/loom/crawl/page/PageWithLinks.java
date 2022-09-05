package dev.nipafx.lab.loom.crawl.page;

import java.net.URI;
import java.util.Set;

record PageWithLinks(Page page, Set<URI> links) {

	public PageWithLinks(Page page) {
		this(page, Set.of());
	}

}
