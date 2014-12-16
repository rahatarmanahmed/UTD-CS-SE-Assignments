// SuperPixels.java
// Rahat Ahmed
// Exercise 4.3
// Demonstrates drawLine and drawCircle methods with "super pixels".



import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

// Boilerplate set up
public class SuperPixels extends Frame
{
	public static void main(String[] args) throws FileNotFoundException
	{
		new SuperPixels();
	}

	public SuperPixels() throws FileNotFoundException
	{
		super("Exercise 4.3 - Rahat Ahmed - SuperPixels.java");
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		setSize(400, 300);
		add("Center", new CvSuperPixels());
		setVisible(true);
	}
}

class CvSuperPixels extends Canvas
{
	int dGrid = 10;
	int centerX, centerY;
	float pixelSize, logicalWidth = 10.0F, logicalHeight = 10.0F,
			actualLogicalWidth, actualLogicalHeight;
	float r = -1, hexHeight, hexWidth;

	int[][] lines;
	int circleX, circleY, circleR;
	CvSuperPixels() throws FileNotFoundException
	{
		Scanner scan = new Scanner(new File("input.txt"));
		int n = scan.nextInt();
		lines = new int[n][4];
		for(int k=0;k<n;k++)
		{
			lines[k] = new int[] {
				scan.nextInt(),
				scan.nextInt(),
				scan.nextInt(),
				scan.nextInt()
			};
		}
		circleX = scan.nextInt();
		circleY = scan.nextInt();
		circleR = scan.nextInt();
		scan.close();
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
	
	float fx(int X)
	{
		return (X - centerX) * pixelSize;
	}

	float fy(int Y)
	{
		return (centerY - Y) * pixelSize;
	}

	public void paint(Graphics g)
	{
		initgr();
		
		drawGrid(g);
		for(int[] line : lines)
		{
			drawLine(g, line[0], line[1], line[2], line[3]);
		}
		drawCircle(g, circleX, circleY, circleR); // g, xC, yC, r
	}
	
	private void drawGrid(Graphics g)
	{
		Dimension d = getSize();
		int maxX = d.width - 1, maxY = d.height - 1;
		for(int x = dGrid; x < maxX; x += dGrid)
		{
			for(int y = dGrid; y < maxY; y += dGrid)
			{
				g.drawLine(x, y, x, y);
			}
		}
	}
	
	private void putPixel(Graphics g, int x, int y)
	{
		g.drawOval(x*dGrid - dGrid/2, y*dGrid - dGrid/2, dGrid, dGrid);
	}
	
	void drawLine(Graphics g, int xP, int yP, int xQ, int yQ)
	{  int x = xP, y = yP, d = 0, dx = xQ - xP, dy = yQ - yP,
	      c, m, xInc = 1, yInc = 1;
	   if (dx < 0){xInc = -1; dx = -dx;}
	   if (dy < 0){yInc = -1; dy = -dy;}
	   if (dy <= dx)
	   {  c = 2 * dx; m = 2 * dy;
	     if (xInc < 0) dx++;
	     for (;;)
	     {  putPixel(g, x, y);
	        if (x == xQ) break;
	        x += xInc;
	        d += m;
	        if (d >= dx){y += yInc; d -= c;}
	     }
	   }
	   else
	   {  c = 2 * dy; m = 2 * dx;
	      if (yInc < 0) dy++;
	      for (;;)
	      {  putPixel(g, x, y);
	         if (y == yQ) break;
	         y += yInc;
	         d += m;
	         if (d >= dy){x += xInc; d -= c;}
	   }
	 }
	}
	
	void drawCircle(Graphics g, int xC, int yC, int r)
	{  int x = 0, y = r, u = 1, v = 2 * r - 1, e = 0;
	   while (x < y)
	   {  putPixel(g, xC + x, yC + y); // NNE
	      putPixel(g, xC + y, yC - x); // ESE
	      putPixel(g, xC - x, yC - y); // SSW
	      putPixel(g, xC - y, yC + x); // WNW
	      x++; e += u; u += 2;
	      if (v < 2 * e){y--; e -= v; v -= 2;}
	      if (x > y) break;
	      putPixel(g, xC + y, yC + x); // ENE
	      putPixel(g, xC + x, yC - y); // SSE
	      putPixel(g, xC - y, yC - x); // WSW
	      putPixel(g, xC - x, yC + y); // NNW
	   }
	}

}

