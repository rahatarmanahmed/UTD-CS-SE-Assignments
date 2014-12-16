// Hexagon.java
// Rahat Ahmed
// Exercise 1.4
// Draws a pattern of hexagons.



import java.awt.Canvas;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Hexagon extends Frame
{
	public static void main(String[] args)
	{
		new Hexagon();
	}

	public Hexagon()
	{
		super("Exercise 1.4 - Rahat Ahmed - Hexagon.java");
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		setSize(400, 300);
		add("Center", new CvHexagon());
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setVisible(true);
	}
}

class CvHexagon extends Canvas
{
	int centerX, centerY;
	float pixelSize, logicalWidth = 10.0F, logicalHeight = 10.0F,
			actualLogicalWidth, actualLogicalHeight;
	float r = -1, hexHeight, hexWidth;
	Point mouse;

	CvHexagon()
	{
		MouseAdapter listener = new MouseAdapter() {
			// Update radius on mouse click or drag
			public void mouseDragged(MouseEvent e)
			{
				calculateRadius(e);
			}

			public void mousePressed(MouseEvent e)
			{
				calculateRadius(e);
			}
			// Calculate r by getting distance from mouse to top left corner
			// Also calc hexagon width/height for convenience
			private void calculateRadius(MouseEvent e)
			{
				mouse = e.getPoint();
				float x = fx(e.getX()), y = fy(e.getY());
				r = distance(x, y, -actualLogicalWidth / 2,
						actualLogicalHeight / 2);
				hexWidth = 2 * r;
				hexHeight = (float) (Math.sqrt(3) * r);
				repaint();

			}
		};
		addMouseListener(listener);
		addMouseMotionListener(listener);
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
	}

	// Gets distance between two points
	float distance(float xA, float yA, float xB, float yB)
	{
		return (float) Math.sqrt((xB - xA) * (xB - xA) + (yB - yA) * (yB - yA));
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

	float fx(int X)
	{
		return (X - centerX) * pixelSize;
	}

	float fy(int Y)
	{
		return (centerY - Y) * pixelSize;
	}

	// Draws a hexagon with center x,y and radius r
	void drawHexagon(Graphics g, float X, float Y, float r)
	{
		for (int i = 0; i < 6; i++)
		{
			float xA = (float) (X + r * Math.cos(i * Math.PI / 3));
			float yA = (float) (Y + r * Math.sin(i * Math.PI / 3));
			float xB = (float) (X + r * Math.cos((i + 1) * Math.PI / 3));
			float yB = (float) (Y + r * Math.sin((i + 1) * Math.PI / 3));

			g.drawLine(iX(xA), iY(yA), iX(xB), iY(yB));
		}
	}

	public void paint(Graphics g)
	{
		initgr();
		if(mouse != null)
		{
			// Calculate offsets to center the hexagon tiles on the screen
			float xOffset;
			if(2*r > actualLogicalWidth)// 0 hexagons wide
				xOffset = 0; 
				
			else if(3.5f*r > actualLogicalWidth || 1.5*hexHeight > actualLogicalHeight) // 1 hexagon wide
				xOffset = (actualLogicalWidth - 2*r) / 2;		
				
			else // 2+ hexagons wide
				xOffset = (float) (((actualLogicalWidth - 2*r) / (1.5*r) % 1) * 1.5*r / 2);
				
			
			float yOffset;
			if(hexHeight > actualLogicalHeight) // 0 hexagons tall
				yOffset = 0;
			else if(1.5*hexHeight > actualLogicalHeight) // 1 hexagon tall
				yOffset = (actualLogicalHeight - hexHeight) / 2;
			else // 2+ hexagons tall
				yOffset = (float) (((actualLogicalHeight - hexHeight) / (.5*hexHeight) % 1) * .5*hexHeight / 2);

			// Shifts even rows
			boolean offRow = false;
			for(float y=actualLogicalHeight/2 - hexHeight/2; y > -actualLogicalHeight/2 + hexHeight/2; y -= hexHeight/2)
			{
				for(float x=-actualLogicalWidth/2 + ((offRow)?2.5f:1f) * r; x < actualLogicalWidth/2 - r; x += 3*r)
				{
					drawHexagon(g, x + xOffset, y - yOffset, r);
				}
				offRow = !offRow;
			}	
		}	
	}
}
