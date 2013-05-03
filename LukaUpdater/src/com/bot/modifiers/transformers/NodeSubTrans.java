package com.bot.modifiers.transformers;

import java.util.HashMap;
import java.util.ListIterator;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;
import com.bot.utils.Utils;

public class NodeSubTrans extends AbstractTransformer {

	@Override
	protected boolean canRun(ClassNode node) {
		ListIterator<MethodNode> mns = node.methods.listIterator();
		String previous = null;
		while (mns.hasNext()) {
			MethodNode s = mns.next();
			if(methodContains("getfield ifnonnull goto ALOAD getfield ALOAD getfield putfield ALOAD getfield", s)){
				if(node.superName.equals(Hook.Class_Node)) {
					Hook.Class_NodeSub = node.name;
				return true;
				}
			}
		
		}
		return false;
	}

	
	
	@Override
	protected void runTransformer(ClassNode node) {
		Log("<----Found Class----> " + node.name);
		for(String hook : Hook.fields) {
			
		}
		
	}

}
