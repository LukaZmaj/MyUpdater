package com.bot.modifiers;

import java.util.HashMap;

import org.objectweb.asm.tree.ClassNode;

public class Hook {
	
	public static final String[] fields = {"[obj5", "loggedIn", "cameraX", "cameraY" , "CameraZ" , "CameraXCurve" , "CameraYCurve", "NPCS", "Name","EntityDef","combatLevel", "RSInterfaceCache", "skullIcon", "currentHealth", "ID", "x", "y", "interactingIndex", "GroundArray", "object5cache", "plane"};
	

	
	public static HashMap<String, String> map = new HashMap<>();
}
