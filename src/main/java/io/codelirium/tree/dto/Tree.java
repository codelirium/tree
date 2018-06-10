package io.codelirium.tree.dto;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedDeque;


public final class Tree<T> {

	private T head;

	private Tree<T> parent;
	private ConcurrentLinkedDeque<Tree<T>> leafs;


	public Tree(final T head) {

		this.head = head;
		this.parent = null;
		this.leafs = new ConcurrentLinkedDeque<>();

	}


	public Tree<T> addLeaf(final T leaf) {

		final Tree<T> tree = new Tree<T>(leaf);

		leafs.add(tree);
		tree.parent = this;


		return tree;
	}


	public T getHead() {

		return head;

	}


	public Tree<T> getParent() {

		return parent;

	}


	public Collection<Tree<T>> getSubTrees() {

		return leafs;

	}
}
