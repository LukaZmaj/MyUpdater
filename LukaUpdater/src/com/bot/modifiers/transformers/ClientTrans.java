package com.bot.modifiers.transformers;

import java.util.ListIterator;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;

import com.bot.utils.JarUtils;

public class ClientTrans extends AbstractTransformer {

	@Override
	protected boolean canRun(ClassNode node) {	
		return node.name.equals("client") ;
	}

	@Override
	protected void runTransformer(ClassNode node) {
		Log("<----Found Class----> " + node.name);
		JarUtils.injectInterface(node, "com/bot/accessors/Client");
		for(String hook : Hook.fields) {
			if(hook.equals("loggedIn")) {
						getHook(node , node,"iadd putstatic getstatic ifne", "iadd putstatic getstatic ifne", "Z", "Z", "isLoggedIn", 2, 2);	
			}
		
			if(hook.equals("plane")) {
				JarUtils.addGetterMethod(node,getFieldName(node, "ALOAD ALOAD ALOAD getfield ALOAD getfield ALOAD", "ALOAD ALOAD ALOAD getfield ALOAD getfield ALOAD", 3, 3), "getPlane", "I");
			}
			
			if(hook.equals("cameraX")) {
				JarUtils.addGetterMethod(node,getFieldName(node, "ALOAD getfield ALOAD getfield aaload ALOAD getfield bipush", "ALOAD getfield ALOAD getfield aaload ALOAD getfield bipush", 6, 6), "getCameraX", "I");
			}

		}	


	}
	
	}


