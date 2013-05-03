package com.bot.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.bot.updater.Updater;


public class JarUtils {

	public static HashMap<String, ClassNode> parseJar(JarFile jar) {
		HashMap<String, ClassNode> classes = new HashMap<String, ClassNode>();
		try {
			Enumeration<?> enumeration = jar.entries();
			while (enumeration.hasMoreElements()) {
				JarEntry entry = (JarEntry) enumeration.nextElement();
				if (entry.getName().endsWith(".class")) {
					ClassReader classreader = new ClassReader(
							jar.getInputStream(entry));
					ClassNode classNode = new ClassNode();
					classreader.accept(classNode, ClassReader.SKIP_DEBUG
							| ClassReader.SKIP_FRAMES);
					classes.put(classNode.name, classNode);
				}
			}
			jar.close();
			return classes;
		} catch (Exception e) {
			return null;
		}
	}



	public static void injectInterface(ClassNode node, String interfaces) {
		if (node.interfaces.contains(interfaces)) {
			return;
		}
		System.out.println(" *" + node.name + " implemented interface "
				+ interfaces);
		node.interfaces.add(interfaces);
	}

	public static void addGetterMethod(ClassNode clazz, String field,
			String methodName, String desc) {
		ListIterator<FieldNode> fn = clazz.fields.listIterator();
		FieldNode t = null;
		while(fn.hasNext()) {
			FieldNode f = fn.next();
			if(f.name.equals(field)) {
				t = f;
				break;
			}
		}
		if(t != null){ 
			MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, methodName, "()"
					+ desc, null, null);
			method.visitFieldInsn(!ASMUtils.isStatic(t) ? Opcodes.GETFIELD  : Opcodes.GETSTATIC, clazz.name, t.name,
					t.desc);
			method.visitInsn(ASMUtils.getReturnOpcode(t.desc));
			if (clazz.methods.add(method)) {
				System.out.println(" *Added Getter Method called: " + methodName
						+ " in class : " + clazz.name + " -> returning field: " + t.name);
				Updater.fieldsHooked++;
			}
		}
	}




	public static void dumpClasses(HashMap<String, ClassNode> classes, Path destination)
			throws IOException {
		if (Files.exists(destination)) {
			Files.delete(destination);
		}
		if (!Files.exists(destination)) {
			Files.createFile(destination);
		}

		JarOutputStream output = new JarOutputStream(new FileOutputStream(
				destination.toFile()));
		for (ClassNode $class : classes.values()) {
			JarEntry entry = new JarEntry($class.name + ".class");
			output.putNextEntry(entry);
			ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
			$class.accept(writer);
			output.write(writer.toByteArray());
		}
		output.close();


	}

	public static void addStaticFieldAccessor(ClassNode injection, String parent, String ifaceDesc, String desc, String field, String name) {
		MethodNode mn = new MethodNode(Opcodes.ACC_PUBLIC, name, "()" + ifaceDesc, null, null);
		mn.instructions.add(new FieldInsnNode(Opcodes.GETSTATIC, parent, field, desc));
		mn.instructions.add(new InsnNode(ASMUtils.getReturnOpcode(ifaceDesc)));
		if (injection.methods.add(mn)) {
			System.out.println(" *Added Getter Method called: " + name
					+ " in class : " + injection.name + " -> returning field: " + parent + field);
			Updater.fieldsHooked++;
		}

	}

	public static void addGetterMethod(ClassNode clazz, FieldNode field,
			String methodName, String desc) {
		MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, methodName, "()"
				+ desc, null, null);
		method.visitFieldInsn(!ASMUtils.isStatic(field) ? Opcodes.GETFIELD  : Opcodes.GETSTATIC, clazz.name, field.name,
				field.desc);
		method.visitInsn(ASMUtils.getReturnOpcode(field.desc));
		if (clazz.methods.add(method)) {
			System.out.println(" *Added Getter Method called: " + methodName
					+ " in class : " + clazz.name + " -> returning field: " + field.name);
			Updater.fieldsHooked++;
		}
	}
/*	public static void addGetterNonStatic(ClassNode clazz, String field,
			String methodName, String desc) {
		ListIterator<FieldNode> fn = clazz.fields.listIterator();
		FieldNode t = null;
		while(fn.hasNext()) {
			FieldNode f = fn.next();
			if(f.name.equals(field)) {
				t = f;
			}
		}
		MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, methodName, "()"
				+ desc, null, null);
		method.visitVarInsn(Opcodes.ALOAD, 0);
		method.visitFieldInsn(Opcodes.GETFIELD, clazz.name, t.name,
				t.desc);
		method.visitInsn(Type.getType(t.desc).getOpcode(Opcodes.IRETURN));
		method.visitMaxs(0, 0);
		if (clazz.methods.add(method)) {
			System.out.println(" *Added Getter Method called: " + methodName
					+ " in class : " + clazz.name + " -> returning field: " + t.name);
			Updater.fieldsHooked++;
		}


	}

	public static void addGetterNonStatic(ClassNode clazz, FieldNode field,
			String methodName, String desc) {
		MethodNode method = new MethodNode(Opcodes.ACC_PUBLIC, methodName, "()"
				+ desc, null, null);
		method.visitVarInsn(Opcodes.ALOAD, 0);
		method.visitFieldInsn(Opcodes.GETFIELD, clazz.name, field.name,
				field.desc);
		method.visitInsn(Type.getType(field.desc).getOpcode(Opcodes.IRETURN));
		method.visitMaxs(0, 0);
		if (clazz.methods.add(method)) {
			System.out.println(" *Added Getter Method called: " + methodName
					+ " in class : " + clazz.name + " -> returning field: " + field.name);
			Updater.fieldsHooked++;
		}


	}

*/
	public static void addMethodGetter(ClassNode into, ClassNode baseClass, String method, String name, String desc, String sub) {
		MethodNode m = new MethodNode(1, name, sub + desc, null, null);

		MethodNode base = getMethod(baseClass, method, sub);
		boolean isStatic = ASMUtils.isStatic(base);

		if (!isStatic) {
			m.visitVarInsn(25, 0);
		}
		if (!sub.equals("()")) {
			for (int i = 1; i < sub.length() - 1; i++)
				m.visitVarInsn(ASMUtils.getOpcode(sub.substring(i, i + 1)), i);
		}
		m.visitMethodInsn(isStatic ? 184 : 182, baseClass.name, base.name, base.desc);
		if (desc.contains("L")) {
			if (!desc.contains("["))
				m.visitTypeInsn(192, desc.replaceFirst("L", "").replaceAll(";", ""));
			else {
				m.visitTypeInsn(192, desc);
			}
		}

		m.visitInsn(ASMUtils.getReturnOpcode(desc));
		into.methods.add(m);
	}

	private static MethodNode getMethod(ClassNode into2, String name, String desc) {
		ListIterator<MethodNode> fnIt = into2.methods.listIterator();
		while(fnIt.hasNext()) {
			MethodNode m = fnIt.next();
			String s = m.desc.substring(0, m.desc.indexOf(')') + 1);
			if ((m.name.equals(name)) && (s.equals(desc))) {
				return m;
			}
		}
		return null;
	}
}



