package com.bot.modifiers;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodNode;

import com.bot.utils.InsnSearcher;
import com.bot.utils.JarUtils;



public abstract class AbstractTransformer {
	private InsnSearcher searcher;
	public void run(ClassNode node) {
		if(this.canRun(node)) {
			runTransformer(node);
		}
	}

	protected abstract boolean canRun(ClassNode node);
	protected abstract void runTransformer(ClassNode node);

	protected void Log(String s) {
		System.out.println(s);
	}


	protected void getHook(ClassNode node, ClassNode injection, String firstRegex, String secondRegex,
			String desc, String methodDesc, String methodName, int index,
			int sindex) {
		String searchResult = null;
		String secondSearchResult = null;
		for(final Object o : node.methods) { 
			final MethodNode mn = (MethodNode) o;
			try{
				try {
					searcher = new InsnSearcher(mn.instructions);
					List<AbstractInsnNode[]> result = searcher.search(firstRegex);
					if(result.size() != 0){
						FieldInsnNode f = (FieldInsnNode) result.get(0)[index];				
						searchResult = f.name;
					}
					List<AbstractInsnNode[]> secondResult = searcher.search(secondRegex);
					if(secondResult.size() != 0){
						FieldInsnNode fs = (FieldInsnNode) secondResult.get(0)[sindex];
						secondSearchResult = fs.name;
					}
				} catch (NullPointerException | ClassCastException  x) {}
			}catch(ConcurrentModificationException fd){}
		}
		if(searchResult != null) {
			if(searchResult.equals(secondSearchResult)) {
				JarUtils.addGetterMethod(injection, secondSearchResult, methodName, methodDesc);
			}
		}
	}

	protected boolean methodContains(String regex, MethodNode mn) {
		searcher = new InsnSearcher(mn.instructions);	
		try{
			if(searcher.search(regex).size() == 1) {
				return true;
			}
		}catch (NullPointerException e) {}		
		return false;
	}
	protected String getFieldName(ClassNode node, String firstRegex, String secondRegex, int index, int sindex) {
		String searchResult = null;
		String secondSearchResult = null;
		for(final Object o : node.methods) { 
			final MethodNode mn = (MethodNode) o;
			try{
				try {
					searcher = new InsnSearcher(mn.instructions);
					List<AbstractInsnNode[]> result = searcher.search(firstRegex);
					if(result.size() != 0){
						FieldInsnNode f = (FieldInsnNode) result.get(0)[index];
						searchResult = f.name;
					}
					List<AbstractInsnNode[]> secondResult = searcher.search(secondRegex);
					if(secondResult.size() != 0){
						FieldInsnNode fs = (FieldInsnNode) secondResult.get(0)[sindex];
						secondSearchResult = fs.name;
					}
				} catch (NullPointerException | ClassCastException | IndexOutOfBoundsException  x) {}
			}catch(ConcurrentModificationException fd){}
		}
		return searchResult;
	}


}



