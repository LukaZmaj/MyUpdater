package com.bot.modifiers.transformers;

import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;
import com.bot.utils.JarUtils;

public class EntityDefTrans extends AbstractTransformer {
	
	@Override
	protected boolean canRun(ClassNode node) {
		for(final Object o : node.methods) { 
			final MethodNode mn = (MethodNode) o;
			if(methodContains("iadd bipush irem putstatic", mn)){
				if(node.superName.equals("java/lang/Object")) {
					if(methodContains("new dup", mn)){
						for(final Object fns : node.fields) { 
							final FieldNode f = (FieldNode) fns;
							if(f.desc.equals("[L" + node.name + ";")) {					
								Hook.map.put("EntityDef" , node.name);
								return true;
							}
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
		JarUtils.injectInterface(node, "com/bot/accessors/EntityDef");
		ListIterator<FieldNode> fns = node.fields.listIterator();

		while (fns.hasNext()) {
			FieldNode f = fns.next();	
			for(String hook : Hook.fields) {
				if(hook.equals("Name")) {
					if(f.desc.equals("[Ljava/lang/String;")) {
						JarUtils.addGetterMethod(node, f, "getActions", f.desc);
					}
				}
				if(hook.equals("ID")) {
					if(f.desc.equals("J")) {
						JarUtils.addGetterMethod(node, f, "getID", f.desc);
					}
				}


			}
		}
		for(String hook : Hook.fields) {
			if(hook.equals("combatLevel")) {
				JarUtils.addGetterMethod(node, getFieldName(node, "aastore ALOAD sipush putfield ALOAD sipush", "aastore ALOAD sipush putfield ALOAD sipush", 3,3), "getCombatLevel" , "I");
			}


		}
	}


}
