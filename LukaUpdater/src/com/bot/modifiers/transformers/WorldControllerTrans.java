package com.bot.modifiers.transformers;

import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;
import com.bot.utils.ASMUtils;
import com.bot.utils.JarUtils;

public class WorldControllerTrans extends AbstractTransformer {
	@Override
	protected boolean canRun(ClassNode node) {
		ListIterator<MethodNode> mns = node.methods.listIterator();
		String previous = null;
		while (mns.hasNext()) {
			MethodNode s = mns.next();
			if(node.superName.equals("java/lang/Object")) {
				ListIterator<FieldNode> fns = node.fields.listIterator();
				while (fns.hasNext()) {
					FieldNode f = fns.next();	
					if(f.desc.equals("[[[L" + Hook.Class_Ground+ ";")) {		
						return true;
					}
				}
			}
		}


		return false;
	}

	@Override
	protected void runTransformer(ClassNode node) {
		Log("<----Found Class----> " + node.name);
		ListIterator<FieldNode> fns = node.fields.listIterator();
		while (fns.hasNext()) {
			FieldNode f = fns.next();	
			for(String hook : Hook.fields) {
				if(hook.equals("GroundArray")) {
					if(f.desc.equals("[[[L" + Hook.Class_Ground + ";" )) {
						JarUtils.addGetterMethod(node, f.name, "getGroundArray", "[[[Lcom/bot/accessors/Ground;");
					}
				}
				if(hook.equals("[obj5")) {		
					if(f.desc.contains("[L") && !f.desc.contains("[[[L")) {
						if(!ASMUtils.isStatic(f)) {
							JarUtils.addGetterMethod(node, f, "getCache", "[Lcom/bot/accessors/PhysicalObject;");
							String object5ClassName = f.desc;
							object5ClassName = object5ClassName.replace("[L", "");
							object5ClassName = object5ClassName.replace(";", "");
							Hook.Class_Obj5 = object5ClassName;
						}
					}
				}
			}
		}

		for(String hook : Hook.fields) {

		}
	}


}
