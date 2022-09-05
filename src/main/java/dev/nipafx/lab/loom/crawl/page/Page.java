package dev.nipafx.lab.loom.crawl.page;

import java.net.URI;

public sealed interface Page permits ErrorPage, SuccessfulPage {

	URI url();

}
