package dev.nipafx.lab.loom.crawl.page;

public sealed interface SuccessfulPage extends Page permits ExternalPage, GitHubPage {

	String content();

}
