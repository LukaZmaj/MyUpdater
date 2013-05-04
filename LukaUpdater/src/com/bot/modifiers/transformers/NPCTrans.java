package com.bot.modifiers.transformers;

import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;
import com.bot.utils.JarUtils;

public class NPCTrans extends AbstractTransformer {
	private int total = 0;
	@Override
	protected boolean canRun(ClassNode node) {
		total = 0;
		boolean EntityDefFound = false;
		if(node.superName.equals(Hook.map.get("Entity"))) {
			for(final Object fns : node.fields) { 
				final FieldNode f = (FieldNode) fns;
				if(f.desc.equals("L" + Hook.map.get("EntityDef") + ";")) {
					EntityDefFound = true;
					continue;
				}
				total++;
			}
		}

		return total < 2 && EntityDefFound;
	}

	@Override
	protected void runTransformer(ClassNode node) {
		Log("<----Found Class----> " + node.name);
		Hook.map.put("NPC" , node.name);
		JarUtils.injectInterface(node, "com/bot/accessors/NPC");
		for(final Object fns : node.fields) { 
			final FieldNode f = (FieldNode) fns;
			for(String hook : Hook.fields) {
				if(hook.equals("EntityDef")) {
					if(f.desc.equals("L" + Hook.map.get("EntityDef")+";")) {
						JarUtils.addGetterMethod(node, f, "getEntityDef", "Lcom/bot/accessors/EntityDef;");
					}
				}
			}
		}
	}
}
