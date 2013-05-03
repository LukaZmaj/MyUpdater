package com.bot.utils;

import java.awt.Point;
import java.awt.Rectangle;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

public class ASMUtils {
	
	private static Map<String, Integer> OPCODE_MAP = new HashMap<>();
	
	public static boolean isStatic(MethodNode method) {
		return Modifier.isStatic(method.access);
	}

	public static boolean isStatic(FieldNode field) {
		return Modifier.isStatic(field.access);
	}

	

	


	public static int[] getFieldCount(ClassNode clazz, boolean acceptStatic,
			String... descriptions) {
		int[] resultSet = new int[descriptions.length];
		for (Object f : clazz.fields) {
			FieldNode field = (FieldNode) f;
			if (acceptStatic != isStatic(field)) {
				continue;
			}
			for (int i = 0; i < descriptions.length; i++) {
				if (field.desc.equals(descriptions[i])) {
					resultSet[i]++;
				}
			}
		}
		return resultSet;
	}

	public static int getReturnOpcode(String desc) {
		desc = desc.substring(desc.indexOf(")") + 1);
		if (desc.length() > 1) {
			return Opcodes.ARETURN;
		}
		final char c = desc.charAt(0);
		switch (c) {
		case 'I':
		case 'Z':
		case 'B':
		case 'S':
		case 'C':
			return Opcodes.IRETURN;
		case 'J':
			return Opcodes.LRETURN;
		case 'F':
			return Opcodes.FRETURN;
		case 'D':
			return Opcodes.DRETURN;
		}
		throw new RuntimeException("Bad Return");
	}

	public static int getOpcode(String d)
	{
		d = d.substring(d.indexOf("L") + 1);
		if (d.length() > 1) {
			return 25;
		}
		char c = d.charAt(0);
		switch (c)
		{
		case 'B':
		case 'C':
		case 'I':
		case 'S':
		case 'Z':
			return 21;
		case 'J':
			return 22;
		case 'F':
			return 23;
		case 'D':
			return 24;
		}
		throw new RuntimeException();
	}

}
