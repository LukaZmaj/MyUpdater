package com.bot.modifiers.transformers;

import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;
import com.bot.updater.Updater;
import com.bot.utils.ASMUtils;
import com.bot.utils.JarUtils;

public class PlayerTrans extends AbstractTransformer {

	@Override
	protected boolean canRun(ClassNode node) {
		for(final Object o : node.methods) { 
			final MethodNode mn = (MethodNode) o;
			if(methodContains("getfield i2l ladd putfield return", mn)){
				if(node.superName.equals(Hook.map.get("Entity")) && !node.name.equals(Hook.map.get("NPC"))) {
					for(final Object fns : node.fields) { 
						final FieldNode f = (FieldNode) fns;
						if(f.desc.equals("J")) {					
							Hook.map.put("Player" , node.name);
							return true;
						}
					}
				}
			}

		}
		return false;
	}

	@Override
	protected void runTransformer(ClassNode node) {
		Log("<----Found Class----> " + node.name);
		JarUtils.injectInterface(node, "com/bot/accessors/PlayerAccessor");
		for(final Object fns : node.fields) { 
			final FieldNode f = (FieldNode) fns;
			for(String hook : Hook.fields) {
				if(hook.equals("Name")) {
					if(f.desc.equals("Ljava/lang/String;")) {
						JarUtils.addGetterMethod(node, f, "getName", f.desc);
					}
				}
				if(hook.equals("EntityDef")) {
					if(f.desc.equals("L" + Hook.map.get("EntityDef") + ";")) {
						JarUtils.addGetterMethod(node, f, "getEntityDef", "Lcom/bot/accessors/EntityDef");
					}
				}

			}
		}

		for(String hook : Hook.fields) {
			if(hook.equals("combatLevel")) {
				JarUtils.addGetterMethod(node, getFieldName(node, "invokevirtual putfield ALOAD ALOAD invokevirtual putfield ALOAD iconst_1", "invokevirtual putfield ALOAD ALOAD invokevirtual putfield ALOAD iconst_1 iand", 1,1), "getCombatLevel" , "I");
			}

			if(hook.equals("skullIcon")) {
				JarUtils.addGetterMethod(node, getFieldName(node, "ALOAD invokevirtual putfield ALOAD aconst_null putfield", "ALOAD invokevirtual putfield ALOAD aconst_null putfield", 2,2), "getSkullIcon" , "I");
			}

		}


	}

}
