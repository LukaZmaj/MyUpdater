package com.bot.modifiers.transformers;

import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;
import com.bot.updater.Updater;
import com.bot.utils.JarUtils;

public class Obj5Trans extends AbstractTransformer {

	@Override
	protected boolean canRun(ClassNode node) {
		return node.name.equals(Hook.map.get("Obj5"));
	}

	@Override
	protected void runTransformer(ClassNode node) {
		Log("<----Found Class----> " + node.name);
		JarUtils.injectInterface(node, "com/bot/accessors/PhysicalObject");
		
	}		
	}


