package dev.nipafx.lab.loom.ea;

import jdk.incubator.concurrent.StructuredTaskScope;

public class Store {

	private final Bank bank = new Bank();
	private final Inventory inventory = new Inventory();

	public boolean buyItem_v1(String itemTypeId, int cost)
			throws InterruptedException {
		boolean decrementSuccessful = bank.decrement(cost);
		if (!decrementSuccessful)
			return false;

		inventory.give(itemTypeId);
		return true;
	}

	public boolean buyItem_v2(String itemTypeId, int cost)
			throws InterruptedException, AppException {
		boolean decrementSuccessful = bank.decrement(cost);
		if (!decrementSuccessful)
			return false;

		try {
			inventory.give(itemTypeId);
			return true;
		} catch (InterruptedException ex) {
			throw ex;
		} catch (Exception ex) {
			bank.refund(cost);
			throw new AppException(ex);
		}
	}

}

class StoreUser {

	private final Store store = new Store();

	public void useStore(String itemTypeId, int cost) throws InterruptedException {
		try (var scope = new StructuredTaskScope<Boolean>()) {
			scope.fork(() -> store.buyItem_v2(itemTypeId, cost));
			scope.fork(() -> reorderItem(itemTypeId));
			scope.join();
			// maybe handle error
		}

	}

	private boolean reorderItem(String itemTypeId) {
		return true;
	}

}

class Bank {

	public boolean decrement(int cost)
			throws InterruptedException {
		return true;
	}

	public boolean refund(int cost)
			throws InterruptedException {
		return true;
	}

}

class Inventory {

	public String give(String itemTypeId)
			throws InterruptedException {
		return "";
	}

}

class AppException extends Exception {

	public AppException(Throwable cause) {
		super(cause);
	}

}