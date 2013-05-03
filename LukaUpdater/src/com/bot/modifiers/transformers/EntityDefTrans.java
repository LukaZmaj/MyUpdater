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
		ListIterator<MethodNode> mns = node.methods.listIterator();
		String previous = null;
		while (mns.hasNext()) {
			MethodNode s = mns.next();
			if(methodContains("iadd bipush irem putstatic", s)){
				if(node.superName.equals("java/lang/Object")) {
					if(methodContains("new dup", s)){
					ListIterator<FieldNode> fns = node.fields.listIterator();
					while (fns.hasNext()) {
						FieldNode f = fns.next();	
						if(f.desc.equals("[L" + node.name + ";")) {					
						Hook.Class_EntityDef = node.name;
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
					if(hook.equals("ID")) {
						if(f.desc.equals("J")) {
							JarUtils.addGetterMethod(node, f, "getID", f.desc);
						}
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
