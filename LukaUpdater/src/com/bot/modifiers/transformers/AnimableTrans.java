package com.bot.modifiers.transformers;

import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;

public class AnimableTrans extends AbstractTransformer {
	@Override
	protected boolean canRun(ClassNode node) {
		for(final Object o : node.methods) { 
			final MethodNode mn = (MethodNode) o;
			if(methodContains("ALOAD invokevirtual astore ALOAD ifnull ALOAD", mn)){
				if(node.superName.equals(Hook.map.get("NodeSub"))) {
					Hook.map.put("Animable", node.name);
				return true;
				}
			}
		
		}
		return false;
	}

	
	
	@Override
	protected void runTransformer(ClassNode node) {
		Log("<----Found Class----> " + node.name);
		
	}

}
