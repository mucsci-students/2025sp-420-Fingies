import javax.swing.JFrame;

public class GUIMenuItem extends JFrame
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
