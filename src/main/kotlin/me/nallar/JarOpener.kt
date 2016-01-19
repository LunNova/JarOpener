package me.nallar

import me.nallar.javatransformer.api.AccessFlags
import me.nallar.javatransformer.api.JavaTransformer
import java.io.File

public object JarOpener {
	public fun main(args: Array<String>) {
		if (args.size != 2) {
			System.err.println("Usage: java -jar jaropener.jar input.jar output.jar")
			return
		}

		val transformer = JavaTransformer()
		transformer.addTransformer({
			it.accessFlags({ open(it) })
			it.methods.forEach({ it.accessFlags({ open(it) }) })
			it.fields.forEach({ it.accessFlags({ open(it) }) })
		})

		transformer.transformJar(File(args[0]), File(args[1]))
	}

	private fun open(flag: AccessFlags): AccessFlags {
		return flag.without(AccessFlags.ACC_FINAL).makeAccessible(true)
	}
}
