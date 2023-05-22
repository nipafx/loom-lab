module loom.experiments {
	// for virtual thread / structured concurrency APIs
	requires jdk.incubator.concurrent;

	// unrelated
	requires java.net.http;
	requires java.desktop;
}