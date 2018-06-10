package io.codelirium.tree.consumer;

import io.codelirium.tree.dto.Tree;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.out;
import static org.springframework.util.Assert.notNull;


//
// TODO: Create an alternative counter with a fork/join pool and the visitor design pattern.
//
public final class TreeNodeCounter<T> implements Runnable {

	private final int id;
	private final Tree<T> root;
	private final AtomicInteger nodeCount;


	public TreeNodeCounter(final int id, final Tree<T> root) {

		this.id = id;
		this.root = root;
		this.nodeCount = new AtomicInteger(1);

	}


	@Override
	public void run() {

		nodeCount.set(1);

		count(root);

		out.println("TreeNodeCounter[" + id + "]: The tree node count is: " + getNodeCount());
	}


	public int getNodeCount() {

		return nodeCount.get();

	}


	private void count(final Tree<T> tree) {

		notNull(tree, "The tree cannot be null.");


		if (!tree.getSubTrees().isEmpty()) {

			final Collection<Tree<T>> subTrees = tree.getSubTrees();

			nodeCount.addAndGet(subTrees.size());

			subTrees.parallelStream().forEach(this::count);
		}
	}
}
