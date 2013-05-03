package com.bot.modifiers.transformers;

import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;

public class AnimableTrans extends AbstractTransformer {
	@Override
	protected boolean canRun(ClassNode node) {
		ListIterator<MethodNode> mns = node.methods.listIterator();
		String previous = null;
		while (mns.hasNext()) {
			MethodNode s = mns.next();
			if(methodContains("ALOAD invokevirtual astore ALOAD ifnull ALOAD", s)){
				if(node.superName.equals(Hook.Class_NodeSub)) {
					Hook.Class_Animable = node.name;
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
