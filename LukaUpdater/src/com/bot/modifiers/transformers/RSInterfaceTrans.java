package com.bot.modifiers.transformers;

import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;
import com.bot.utils.ASMUtils;
import com.bot.utils.JarUtils;

public class RSInterfaceTrans extends AbstractTransformer {

	@Override
	protected boolean canRun(ClassNode node) {
		int counter = 0;
		ListIterator<MethodNode> mns = node.methods.listIterator();
		String previous = null;
		while (mns.hasNext()) {
			MethodNode s = mns.next();
			if(methodContains("getfield iconst_0 bipush iastore ALOAD getfield", s)) {	
				if(node.superName.equals("java/lang/Object")) {
					ListIterator<FieldNode> fns = node.fields.listIterator();
					while (fns.hasNext()) {
						FieldNode f = fns.next();	
						if(f.desc.equals("[L" + node.name + ";")) {
							if(ASMUtils.isStatic(f)) {
								counter++;
							}
						}
						if(f.desc.equals("[[I")) {
							counter++;
						}
					}
				}
			}
		}
		return counter >= 2;
	}

	@Override
	protected void runTransformer(ClassNode node) {
		Log("<----Found Class----> " + node.name);
		JarUtils.injectInterface(node, "com/bot/accessors/RSInterface");
		ListIterator<FieldNode> fns = node.fields.listIterator();
		while (fns.hasNext()) {
			FieldNode f = fns.next();	
			for(String hook : Hook.fields) {
				if(hook.equals("RSInterfaceCache")) {
					if(f.desc.equals("[[I")) {
						JarUtils.addGetterMethod(node, f.name, "getInterfaceIndex", f.desc);
					}
				}
			}
		}
	}
}
