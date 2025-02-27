import javax.swing.JMenuItem;

public class GUIMenuItem extends JMenuItem
{
	public Action action;
	
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
