// ArbitrarySquare.java
// Rahat Ahmed
// Exercise 2.1
// Draws a square ABCD from the user arbitrarily picking A and b



import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Point2D;

// Boilerplate set up
public class ArbitrarySquare extends Frame
{
	public static void main(String[] args)
	{
		new ArbitrarySquare();
	}

	public ArbitrarySquare()
	{
		super("Exercise 2.1 - Rahat Ahmed - ArbitrarySquare.java");
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		setSize(400, 300);
		add("Center", new CvArbitrarySquare());
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		setVisible(true);
	}
}

class CvArbitrarySquare extends Canvas
{
	int centerX, centerY;
	float pixelSize, logicalWidth = 10.0F, logicalHeight = 10.0F,
			actualLogicalWidth, actualLogicalHeight;
	float r = -1, hexHeight, hexWidth;
	
	Point2F A, B, C, D;

	CvArbitrarySquare()
	{
		MouseAdapter listener = new MouseAdapter() {
			public void mousePressed(MouseEvent e)
			{
				// Save points
				if(A == null && B == null)
					A = fp(e.getPoint());
				else if (B == null)
				{
					B = fp(e.getPoint());
					// Calculate C and D
					// Find vector AB
					Point2F AB = B.sub(A);
					// Rotate by 90 degrees CCW
					Point2F ABrotated = new Point2F(-AB.y, AB.x);
					
					C = B.add(ABrotated);
					D = A.add(ABrotated);
					
				}
				else // Reset
					A = B = null;
				repaint();
			}
		};
		addMouseListener(listener);
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
		
		// Draw A point and label
		if(A != null)
		{
			g.drawRect(iX(A.x)-1, iY(A.y)-1, 2, 2);
			g.drawString("A", iX(A.x), iY(A.y)+10);
		}
			
		
		if(B != null)
		{
			// Draw BCD points
			g.drawRect(iX(B.x)-1, iY(B.y)-1, 2, 2);
			g.drawRect(iX(C.x)-1, iY(C.y)-1, 2, 2);
			g.drawRect(iX(D.x)-1, iY(D.y)-1, 2, 2);
			
			// Draw BCD labels
			g.drawString("b", iX(B.x), iY(B.y)+10);
			g.drawString("C", iX(C.x), iY(C.y)+10);
			g.drawString("D", iX(D.x), iY(D.y)+10);
			
			// Draw the rect
			g.drawLine(iX(A.x), iY(A.y), iX(B.x), iY(B.y));
			g.drawLine(iX(B.x), iY(B.y), iX(C.x), iY(C.y));
			g.drawLine(iX(C.x), iY(C.y), iX(D.x), iY(D.y));
			g.drawLine(iX(D.x), iY(D.y), iX(A.x), iY(A.y));
		}
	}
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
	
	// Returns new Point2F that is the negation of this 
	public Point2F neg()
	{
		return new Point2F(-x, -y);
	}
	
	// Returns new Point2F that is the subtraction of this and p
	public Point2F sub(Point2F p)
	{
		return add(p.neg());
	}
}
