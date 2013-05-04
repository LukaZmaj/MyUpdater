package com.bot.modifiers.transformers;

import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;
import com.bot.utils.JarUtils;

public class GroundTrans extends AbstractTransformer {
	@Override
	protected boolean canRun(ClassNode node) {
	
		for(final Object o : node.methods) { 
			final MethodNode mn = (MethodNode) o;
			if(methodContains("iconst_5 anewarray putfield ALOAD iconst_5 newarray", mn)){
				if(node.superName.equals(Hook.map.get("Node"))) {
					Hook.map.put("Ground" , node.name);
				return true;
				}
			}
		
		}
		return false;
	}

	@Override
	protected void runTransformer(ClassNode node) {
		Log("<----Found Class----> " + node.name);
		JarUtils.injectInterface(node, "com/bot/accessors/Ground");
		
	}


}
