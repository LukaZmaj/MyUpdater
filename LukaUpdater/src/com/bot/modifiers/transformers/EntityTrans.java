package com.bot.modifiers.transformers;

import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;
import com.bot.updater.Updater;
import com.bot.utils.JarUtils;

public class EntityTrans extends AbstractTransformer {
	@Override
	protected boolean canRun(ClassNode node) {
		for(final Object o : node.methods) { 
			final MethodNode mn = (MethodNode) o;
			if(methodContains("iconst_4 if_icmpge ALOAD getfield iload iaload", mn)){
				if(node.superName.equals(Hook.map.get("Animable"))) {
					Hook.map.put("Entity", node.name);
					return true;
				}
			}

		}


		return false;
	}


	@Override
	protected void runTransformer(ClassNode node) {
		Log("<----Found Class----> " + node.name);
		JarUtils.injectInterface(node, "com/bot/accessors/NPC"); 
		JarUtils.injectInterface(node, "com/bot/accessors/PlayerAccessor");
		JarUtils.injectInterface(node, "com/bot/accessors/RSObjectDef");
		for(String hook : Hook.fields) {
			if(hook.equals("x")) {
				JarUtils.addGetterMethod(node, getFieldName(node, "bipush imul iadd putfield ALOAD ALOAD", "bipush imul iadd putfield ALOAD ALOAD", 3,3), "getX" , "I");
			}
			if(hook.equals("y")) {
				JarUtils.addGetterMethod(node, getFieldName(node, "bipush imul iadd putfield return", "bipush imul iadd putfield return", 3,3), "getY" , "I");
			}
			if(hook.equals("interactingIndex")) {
				JarUtils.addGetterMethod(node, getFieldName(Updater.CLASSES.get("client"), "putfield iload iconst_1 iand", "putfield iload iconst_1 iand", 0, 0), "getInteractingEntity" , "I");
			}


		}
	}
}
