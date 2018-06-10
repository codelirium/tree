package io.codelirium.tree.util;

import java.util.UUID;


public class Util {


	private Util() { }


	public static int getRandomInteger(final int min, final int max) {

		return min + (int)(Math.random() * ((max - min) + 1));

	}


	public static String getRandomString() {

		return UUID.randomUUID().toString();

	}


	public static int getNumberOfCpus() {

		return Runtime.getRuntime().availableProcessors();

	}
}

