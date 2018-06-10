package io.codelirium.tree.producer;

import io.codelirium.tree.dto.Tree;

import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static io.codelirium.tree.util.Util.getRandomInteger;
import static io.codelirium.tree.util.Util.getRandomString;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.util.concurrent.Executors.newCachedThreadPool;
import static java.util.stream.IntStream.range;
import static org.springframework.util.Assert.notNull;


public class StringTreeExpander implements Runnable {

	private int id;
	private int maxLeafs;
	private int softNodesThreshold;
	private Tree<String> root;
	private AtomicInteger generatedNodes;
	private ExecutorService executor;


	public StringTreeExpander(final int id, final Tree<String> root, final int maxLeafs, final int softNodesThreshold) {

		this.id = id;
		this.root = root;
		this.maxLeafs = maxLeafs;
		this.softNodesThreshold = softNodesThreshold;
		this.generatedNodes = new AtomicInteger(1);
		this.executor = newCachedThreadPool();

	}


	@Override
	public void run() {

		try {

			expand(root);

		} catch (final InterruptedException e) {

			err.println("StringTreeExpander[" + id + "]::run() - exception: " + e.getMessage());

		}
	}


	public int getGeneratedNodes() {

		return generatedNodes.get();

	}


	private void expand(final Tree<String> tree) throws InterruptedException {

		notNull(tree, "The tree cannot be null.");


		if (generatedNodes.get() >= softNodesThreshold) {

			//out.println("StringTreeExpander[" + id + "]: Reached max nodes threshold. Ceasing expansion of the tree ...");

			currentThread().interrupt();

			return;

		}


		//Thread.sleep(50);


		out.println("StringTreeExpander[" + id + "]: This tree has " + tree.getSubTrees().size() + " leaf nodes ...");


		if (tree.getSubTrees().isEmpty()) {

			expandTree(tree);


			out.println("StringTreeExpander[" + id + "]: Generated " + getGeneratedNodes() + " nodes so far ...");


			final Collection<Tree<String>> subTrees = tree.getSubTrees();

			subTrees.parallelStream().forEach(subTree -> {

				final Callable<Void> expansionTask = () -> {

					expand(subTree);

					return null;
				};

				executor.submit(expansionTask);
			});
		}
	}


	private void expandTree(final Tree<String> tree) throws InterruptedException {

		notNull(tree, "The tree cannot be null.");


		final int numOfLeafs = getRandomInteger(1, maxLeafs);

		final CountDownLatch latch = new CountDownLatch(numOfLeafs);


		out.println("StringTreeExpander[" + id + "]: Adding " + numOfLeafs + " leafs to the node ...");


		range(0, numOfLeafs).parallel().forEach(leafIndex -> {

			final Callable<Void> expansionTask = () -> {

				tree.addLeaf(getRandomString());

				generatedNodes.incrementAndGet();

				latch.countDown();

				return null;
			};

			executor.submit(expansionTask);
		});


		latch.await();
	}
}
