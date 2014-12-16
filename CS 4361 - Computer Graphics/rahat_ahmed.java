// rahat_ahmed.java
// Rahat Ahmed
// Exercise 2.10
// Draws a tree of pythagoras from user defined points.



import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;
import java.util.Random;

// Boilerplate set up
public class rahat_ahmed extends Frame
{
	CvTreeOfPythagoras canvas;
	public static void main(String[] args)
	{
		new rahat_ahmed();
	}

	public rahat_ahmed()
	{
		super("Exercise 2.10 - Rahat Ahmed - rahat_ahmed.java");
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		
		// adds scroll pane and canvas and sets sizes
		ScrollPane s = new ScrollPane();
		s.setSize(400, 300);
		setSize(400, 300);
		canvas = new CvTreeOfPythagoras();
		canvas.setSize(4000, 3000);
		s.add("Center", canvas);
		add("Center", s);
		
		// Create menu
		MenuBar menubar = new MenuBar();
		
		Menu menu = new Menu("Menu");
		MenuItem py = new MenuItem("Pythagoras");
		py.setActionCommand("py");
		MenuItem quit = new MenuItem("Quit");
		quit.setActionCommand("quit");
		menu.add(py);
		menu.add(quit);
		menu.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				// Start listening for clicks
				if(e.getActionCommand().equals("py"))
				{
					canvas.menuClicked();
				}
				// quit
				else
					System.exit(0);
			}
		});
		
		menubar.add(menu);
		
		setMenuBar(menubar);
		
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setVisible(true);
	}
}

class CvTreeOfPythagoras extends Canvas
{
	int centerX, centerY;
	float pixelSize, logicalWidth = 10.0F, logicalHeight = 10.0F,
			actualLogicalWidth, actualLogicalHeight;
	float r = -1, hexHeight, hexWidth;
	
	Random random;
	long seed = System.currentTimeMillis();
	
	boolean listening = false;
	
	Point2F a, b;

	CvTreeOfPythagoras()
	{
		MouseAdapter listener = new MouseAdapter() {
			public void mousePressed(MouseEvent e)
			{
				// Save points
				if(a == null && listening)
					a = fp(e.getPoint());
				else if (b == null && listening)
				{
					b = fp(e.getPoint());
				}
				repaint();
			}
		};
		addMouseListener(listener);
	}
	
	// Menu clicked, start listening for clicks and reset points and seed
	public void menuClicked(){
		listening = true;
		a = b = null;
		seed = System.currentTimeMillis();
		repaint();
	}

	// Recalculate values for isotropic mapping mode
	protected void initgr()
	{
		Dimension d = getSize();
		int maxX = d.width - 1, maxY = d.height - 1;
		pixelSize = Math.max(logicalWidth / maxX, logicalHeight / maxY);
		centerX = maxX / 2;
		centerY = maxY / 2;
		
		// Since pixel size is max of width/height over their device sizes, one of these
		// values will actually be larger. This should be used to use all of the canvas
		actualLogicalWidth = maxX * pixelSize;
		actualLogicalHeight = maxY * pixelSize;
		
		random = new Random(seed);
	}

	// Functions taken from the book
	int iX(float x)
	{
		return Math.round(centerX + x / pixelSize);
	}

	int iY(float y)
	{
		return Math.round(centerY - y / pixelSize);
	}
	
	Point iP(Point2F p)
	{
		return new Point(iX(p.x), iY(p.y));
	}

	float fx(int X)
	{
		return (X - centerX) * pixelSize;
	}

	float fy(int Y)
	{
		return (centerY - Y) * pixelSize;
	}

	Point2F fp(Point2D p)
	{
		return new Point2F(fx((int) p.getX()), fy((int) p.getY()));
	}
	
	public void paint(Graphics g)
	{
		initgr();

		g.setColor(Color.black);
		
		// We have the points, draw the tree
		if(b != null)
		{
			drawTreeOfPythagoras(g, a, b);
		}
	}
	
	// Recursively draws a tree of pythagoras
	public void drawTreeOfPythagoras(Graphics g, Point2F A, Point2F B)
	{
		// Quit if distance is <= 2
		System.out.println(pixelSize);
		if(A.distanceSq(B) <= pixelSize)
			return;
		// Calculate C and D
		// Find vector AB
		Point2F AB = B.sub(A);
		// Rotate by 90 degrees CCW
		Point2F ABrotated = new Point2F(-AB.y, AB.x);
		
		Point2F C = B.add(ABrotated);
		Point2F D = A.add(ABrotated);
		
		// Calculate E
		Point2F E = new Point2F((C.x + D.x) / 2, (C.y + D.y) / 2).add(ABrotated.scale(.5f));
		
		// Fill the square and triangle
		g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
		g.fillPolygon(new int[] {
				iX(A.x), iX(B.x), iX(C.x), iX(D.x)
		}, new int[] {
				iY(A.y), iY(B.y), iY(C.y), iY(D.y)
		}, 4);
		
		g.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
		
		g.fillPolygon(new int[] {
				iX(D.x), iX(C.x), iX(E.x)
		}, new int[] {
				iY(D.y), iY(C.y), iY(E.y)
		}, 3);
		
		g.setColor(Color.black);
		
		// Draw the square and triangle
		g.drawLine(iX(A.x), iY(A.y), iX(B.x), iY(B.y));
		g.drawLine(iX(B.x), iY(B.y), iX(C.x), iY(C.y));
		g.drawLine(iX(C.x), iY(C.y), iX(D.x), iY(D.y));
		g.drawLine(iX(D.x), iY(D.y), iX(A.x), iY(A.y));
		g.drawLine(iX(D.x), iY(D.y), iX(C.x), iY(C.y));
		g.drawLine(iX(C.x), iY(C.y), iX(E.x), iY(E.y));
		g.drawLine(iX(E.x), iY(E.y), iX(D.x), iY(D.y));
		
		// Recurse!
		drawTreeOfPythagoras(g, D, E);
		drawTreeOfPythagoras(g, E, C);
	}
	
	// Convenience class to work with 2d points in logical coordinates
	class Point2F
	{
		public float x,y;
		public Point2F(float x, float y)
		{
			this.x = x;
			this.y = y;
		}
		
		@Override
		public String toString()
		{
			return "("+x+", "+y+")";
		}
		
		// Returns new Point2F that is the addition of this and p
		public Point2F add(Point2F p)
		{
			return new Point2F(x + p.x, y + p.y);
		}
		
		// Returns new Point2F scaled by this scalar
		public Point2F scale(float s)
		{
			return new Point2F(x*s, y*s);
		}
		
		// Returns new Point2F that is the negation of this 
		public Point2F neg()
		{
			return scale(-1);
		}
		
		// Returns new Point2F that is the subtraction of this and p
		public Point2F sub(Point2F p)
		{
			return add(p.neg());
		}
		
		// Returns distance squared to point p
		public float distanceSq(Point2F p)
		{
			return (p.x-x)*(p.x-x) + (p.y-y)*(p.y-y);
		}
	}
}