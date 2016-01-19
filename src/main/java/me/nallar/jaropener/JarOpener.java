package me.nallar.jaropener;

import me.nallar.javatransformer.api.AccessFlags;
import me.nallar.javatransformer.api.JavaTransformer;

import java.io.*;

public class JarOpener {
	public static void main(String[] args) {
		if (args.length != 2) {
			System.err.println("Usage: java -jar jaropener.jar input.jar output.jar");
			return;
		}

		JavaTransformer transformer = new JavaTransformer();
		transformer.addTransformer((x) -> {
			x.accessFlags(JarOpener::open);
			x.getMethods().forEach((it) -> it.accessFlags(JarOpener::open));
			x.getFields().forEach((it) -> it.accessFlags(JarOpener::open));
		});

		transformer.transformJar(new File(args[0]), new File(args[1]));
	}

	private static AccessFlags open(AccessFlags flag) {
		return flag.without(AccessFlags.ACC_FINAL).makeAccessible(true);
	}
}
