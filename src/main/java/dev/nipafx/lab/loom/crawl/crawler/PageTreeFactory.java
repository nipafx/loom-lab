package dev.nipafx.lab.loom.crawl.crawler;

import dev.nipafx.lab.loom.crawl.page.ErrorPage;
import dev.nipafx.lab.loom.crawl.page.GitHubPage;
import dev.nipafx.lab.loom.crawl.page.Page;
import jdk.incubator.concurrent.StructuredTaskScope;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static java.util.Objects.requireNonNull;

public class PageTreeFactory {

	private final HttpClient client;
	private final ConcurrentMap<URI, Page> resolvedPages;

	public PageTreeFactory(HttpClient client) {
		this.client = requireNonNull(client);
		resolvedPages = new ConcurrentHashMap<>();
	}

	public Page createPage(URI url, int depth) throws InterruptedException {
		if (resolvedPages.containsKey(url)) {
			System.out.printf("Found cached '%s'%n", url);
			return resolvedPages.get(url);
		}

		System.out.printf("Resolving '%s'...%n", url);
		var pageWithLinks = fetchPageWithLinks(url);
		var page = pageWithLinks.page();
		resolvedPages.computeIfAbsent(page.url(), __ -> page);
		System.out.printf("Resolved '%s' with children: %s%n", url, pageWithLinks.links());

		if (page instanceof GitHubPage ghPage) {
			Collection<Page> resolvedLinks = resolveLinks(pageWithLinks.links(), depth - 1);
			ghPage.links().addAll(resolvedLinks);
		}
		return pageWithLinks.page();
	}

	private Collection<Page> resolveLinks(Set<URI> links, int depth) throws InterruptedException {
		if (depth < 0)
			return Collections.emptySet();

		try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
			List<Future<Page>> futurePages = new ArrayList<>();
			for (URI link : links)
				futurePages.add(scope.fork(() -> createPage(link, depth)));

			scope.join();
			scope.throwIfFailed();

			return futurePages.stream()
					.map(Future::resultNow)
					.toList();
		} catch (ExecutionException ex) {
			// this should not happen as `ErrorPage` instances should have been created for all errors
			throw new IllegalStateException("Error cases should have been handled during page creation!", ex);
		}
	}

	private PageWithLinks fetchPageWithLinks(URI url) throws InterruptedException {
		try {
			var pageBody = fetchPageAsString(url);
			return PageFactory.parsePage(url, pageBody);
		} catch (InterruptedException iex) {
			throw iex;
		} catch (Exception ex) {
			return new PageWithLinks(new ErrorPage(url, ex));
		}
	}

	private String fetchPageAsString(URI url) throws IOException, InterruptedException {
		var request = HttpRequest
				.newBuilder(url)
				.GET()
				.build();
		return client.send(request, BodyHandlers.ofString()).body();
	}


}
