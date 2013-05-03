package com.bot.updater;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.bot.modifiers.AbstractTransformer;
import com.bot.modifiers.Hook;
import com.bot.modifiers.transformers.AnimableTrans;
import com.bot.modifiers.transformers.ClientTrans;
import com.bot.modifiers.transformers.EntityTrans;
import com.bot.modifiers.transformers.EntityDefTrans;
import com.bot.modifiers.transformers.GroundTrans;
import com.bot.modifiers.transformers.NPCTrans;
import com.bot.modifiers.transformers.NodeSubTrans;
import com.bot.modifiers.transformers.NodeTrans;
import com.bot.modifiers.transformers.Obj5Trans;
import com.bot.modifiers.transformers.PlayerTrans;
import com.bot.modifiers.transformers.RSInterfaceTrans;
import com.bot.modifiers.transformers.WorldControllerTrans;
import com.bot.utils.JarUtils;

public class Updater {

	public static HashMap<String, ClassNode> CLASSES = new HashMap<>();
	private ArrayList<AbstractTransformer> transformers = new ArrayList<AbstractTransformer>();
	private ArrayList<AbstractTransformer> SecondaryTransformers = new ArrayList<AbstractTransformer>();
	public static int fieldsHooked = 0;

	public Updater() throws InstantiationException, IllegalAccessException,
			ClassNotFoundException, IOException, InterruptedException {
		System.out.println("Updater");
		CLASSES = JarUtils.parseJar(new JarFile(
				"C:\\Users\\Luka\\Downloads\\doop.jar"));
		System.out.println("UpdaterLog: " + CLASSES.values().size()
				+ " Classes parsed\n");
		this.loadAnaylsers();
		this.runAnaysers();
		this.SecondaryloadAnaylsers();
		this.runSecondaryAnaysers();
		final File gamepack = new File("C:\\Users\\Luka\\Desktop\\inject.jar");
		JarUtils.dumpClasses(CLASSES, gamepack.toPath());
		System.out.println("Fields Hooked: " + fieldsHooked);

	}

	private void loadAnaylsers() {
		this.transformers.add(new NodeTrans());
		this.transformers.add(new ClientTrans());
		this.transformers.add(new EntityDefTrans());
		this.transformers.add(new RSInterfaceTrans());
	}

	private void runAnaysers() {
		for (ClassNode node : CLASSES.values()) {
			for (AbstractTransformer transformer : this.transformers) {
				transformer.run(node);

			}
		}
	}
	
	private void SecondaryloadAnaylsers() {
		this.SecondaryTransformers.add(new NodeSubTrans());
		this.SecondaryTransformers.add(new AnimableTrans());
		this.SecondaryTransformers.add(new EntityTrans());
		this.SecondaryTransformers.add(new NPCTrans());
		this.SecondaryTransformers.add(new PlayerTrans());
		this.SecondaryTransformers.add(new GroundTrans());
		this.SecondaryTransformers.add(new WorldControllerTrans());
		this.SecondaryTransformers.add(new Obj5Trans());
	}
	
	private void runSecondaryAnaysers() {
		for (ClassNode node : CLASSES.values()) {
			for (AbstractTransformer transformer : this.SecondaryTransformers) {
				transformer.run(node);

			}
		}
	}
	

	public void print(String t) {
		System.out.println(t);
	}

	public static void main(String[] args) throws InstantiationException,
			IllegalAccessException, ClassNotFoundException, IOException,
			InterruptedException {
		new Updater();
	}

}
