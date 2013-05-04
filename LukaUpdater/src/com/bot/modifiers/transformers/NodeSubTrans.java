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
		for(final Object o : node.methods) { 
			final MethodNode mn = (MethodNode) o;
			if(methodContains("getfield ifnonnull goto ALOAD getfield ALOAD getfield putfield ALOAD getfield", mn)){
				if(node.superName.equals(Hook.map.get("Node"))){
					Hook.map.put("NodeSub", node.name);
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
