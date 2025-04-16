package org.fingies.View;

import javax.swing.JMenuItem;

import org.fingies.Controller.Action;

public class GUIMenuItem extends JMenuItem
{
	public final Action action;
	
	public GUIMenuItem(String name)
	{
		super(name);
		action = null;
	}
	
	public GUIMenuItem(String name, Action a)
	{
		super(name);
		action = a;
	}
}
