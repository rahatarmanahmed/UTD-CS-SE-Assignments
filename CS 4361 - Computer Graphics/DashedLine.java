// DashedLine.java
// Rahat Ahmed
// Exercise 1.5
// Defines a function to draw a dashed line

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



public class DashedLine extends Frame
{
	public static void main(String[] args)
	{
		new DashedLine();
	}

	public DashedLine()
	{
		super("Exercise 1.5 - Rahat Ahmed - DashedLine.java");
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		setSize(400, 300);
		add("Center", new CvDashedLine());
		setVisible(true);
	}
}

class CvDashedLine extends Canvas
{
	public static final int MARGIN = 10;
	
	int maxX, maxY, centerX, centerY;
	
	CvDashedLine()
	{
	}

	protected void initgr()
	{
		// Calculate boundaries and center
		Dimension d = getSize();
		maxX = d.width - 1;
		maxY = d.height - 1;
		centerX = maxX / 2;
		centerY = maxY / 2;
	}

	
	
	public void paint(Graphics g)
	{
		initgr();
		
		
		// Some test lines
		Lines.drawDashedLine(g, 50, 50, 50, 200, 10);
		Lines.drawDashedLine(g, 50, 50, 75, 175, 10);
		Lines.drawDashedLine(g, 50, 50, 100, 150, 10);
		Lines.drawDashedLine(g, 50, 50, 125, 125, 10);
		Lines.drawDashedLine(g, 50, 50, 150, 100, 10);
		Lines.drawDashedLine(g, 50, 50, 175, 75, 10);
		Lines.drawDashedLine(g, 50, 50, 200, 50, 10);
	}
}

class Lines
{
	public static void drawDashedLine(Graphics g, int xA, int yA, int xB, int yB, int dashLength)
	{	

		float length = (float) Math.sqrt((xB-xA)*(xB-xA) + (yB-yA)*(yB-yA));

		// Case where line is less than dash length, draw line
		if(length <= dashLength)
		{
			g.drawLine(xA, yA, xB, yB);
		}
		// Case where line is shorter than 2 dashes, so no room for gap
		else if(length <= 2*dashLength)
		{
			// I have no clue how this is supposed to be handled,
			// Just going to assume the two dashes on either end overlap and there is no ga
			g.drawLine(xA, yA, xB, yB);
		}
		// Cases where gaps are visible
		else
		{
			// Calc how many dashes
			int n = (int) Math.ceil(((length / (float) dashLength) + 1) / 2);
			// Decrement so when adding to coordinate to get endpoint, extra pixel is not taken
			dashLength--;
			
			// Calculate x and y length of the gap
			float gap = ((length - dashLength*n) / (float) (n-1));
			float xGap = (xB-xA) / length * gap;
			float yGap = (yB-yA) / length * gap;
			
			// Calculate x and y length of dash
			float xDash = (xB-xA) / length * dashLength;
			float yDash = (yB-yA) / length * dashLength;

			// Draw each dash
			for(int k=0; k<n; k++)
			{
				float xI = xA + k*(xDash+xGap);
				float yI = yA + k*(yDash+yGap);
				float xK = xA + k*(xDash+xGap) + xDash;
				float yK = yA + k*(yDash+yGap) + yDash;
				g.drawLine((int)xI, (int)yI, (int)xK, (int)yK);
			}
			
		}
	}
}
