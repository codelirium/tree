package io.codelirium.tree;

import io.codelirium.tree.consumer.TreeNodeCounter;
import io.codelirium.tree.dto.Tree;
import io.codelirium.tree.producer.StringTreeExpander;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.concurrent.ScheduledExecutorService;

import static io.codelirium.tree.util.Util.getNumberOfCpus;
import static io.codelirium.tree.util.Util.getRandomString;
import static java.lang.System.out;
import static java.util.concurrent.Executors.newScheduledThreadPool;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static java.util.concurrent.TimeUnit.SECONDS;


@SpringBootApplication
public class TreeApplication implements CommandLineRunner {


	public static void main(final String[] args) {

		SpringApplication.run(TreeApplication.class, args);

	}


	@Override
	public void run(final String... args) throws InterruptedException {

		final Tree<String> root = new Tree<>(getRandomString());


		out.println("Starting tree expansion ...");

		final StringTreeExpander stringTreeExpander = new StringTreeExpander(1, root, 20, 10000);

		newSingleThreadExecutor().submit(stringTreeExpander);


		final TreeNodeCounter<String> treeNodeCounterOne   = new TreeNodeCounter<>(1, root);
		final TreeNodeCounter<String> treeNodeCounterTwo   = new TreeNodeCounter<>(2, root);
		final TreeNodeCounter<String> treeNodeCounterThree = new TreeNodeCounter<>(3, root) ;

		final ScheduledExecutorService consumerExecutor = newScheduledThreadPool(getNumberOfCpus());

		consumerExecutor.scheduleAtFixedRate(treeNodeCounterOne,  0, 1, SECONDS);
		consumerExecutor.scheduleAtFixedRate(treeNodeCounterTwo,  0, 2, SECONDS);
		consumerExecutor.scheduleAtFixedRate(treeNodeCounterThree,0, 3, SECONDS);
	}
}