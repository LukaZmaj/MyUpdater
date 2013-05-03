package com.bot.modifiers.transformers;

import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;
import com.bot.utils.JarUtils;

public class NPCTrans extends AbstractTransformer {

	@Override
	protected boolean canRun(ClassNode node) {
		int total = 0;
		boolean EntityDefFound = false;
		if(node.superName.equals(Hook.Class_Entity)) {
			ListIterator<FieldNode> fields = node.fields.listIterator();
			while(fields.hasNext()) {
				FieldNode s = fields.next();
				if(s.desc.equals("L" + Hook.Class_EntityDef + ";")) {
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
		JarUtils.injectInterface(node, "com/bot/accessors/NPC");
		ListIterator<FieldNode> fns = node.fields.listIterator();
		while (fns.hasNext()) {
			FieldNode f = fns.next();	
			for(String hook : Hook.fields) {
				if(hook.equals("EntityDef")) {
					if(f.desc.equals("L" + Hook.Class_EntityDef+";")) {
						JarUtils.addGetterMethod(node, f, "getEntityDef", "Lcom/bot/accessors/EntityDef;");
					}
				}
			}
		}
	}
}
