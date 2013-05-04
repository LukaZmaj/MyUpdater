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
		if(node.superName.equals("java/lang/Object")) {
			for(final Object fns : node.fields) { 
				final FieldNode f = (FieldNode) fns;
				if(f.desc.equals("[[[L" + Hook.map.get("Ground")+ ";")) {		
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected void runTransformer(ClassNode node) {
		Log("<----Found Class----> " + node.name);
		for(final Object fns : node.fields) { 
			final FieldNode f = (FieldNode) fns;
			for(String hook : Hook.fields) {
				if(hook.equals("GroundArray")) {
					if(f.desc.equals("[[[L" + Hook.map.get("Ground") + ";" )) {
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
							Hook.map.put("Obj5", node.name);
						}
					}
				}
			}
		}

	}


}
