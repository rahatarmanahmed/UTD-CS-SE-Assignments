// ConcentricSquares.java
// Rahat Ahmed
// Exercise 1.3
// Draws the largest set of concentric squares possible in the window.

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;



public class ConcentricSquares extends Frame
{
	public static void main(String[] args)
	{
		new ConcentricSquares();
	}

	public ConcentricSquares()
	{
		super("Exercise 1.3 - Rahat Ahmed - ConcentricSquares.java");
		addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		setSize(400, 300);
		add("Center", new CvConcentricSquares());
		setVisible(true);
	}
}

class CvConcentricSquares extends Canvas
{
	public static final int MARGIN = 10;
	
	int maxX, maxY, centerX, centerY;
	
	CvConcentricSquares()
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
		// TODO: draw from the inside out
		
		// Loop infinitely
		for(int k=0; ; k++)
		{
			// Determine offset to draw inner normal square with
			int innerOffset = (int) Math.pow(2, k);
			
			// Break if it goes offscreen
			if(centerX - innerOffset < 0 || centerX + innerOffset > maxX ||
				centerY - innerOffset < 0 || centerY + innerOffset > maxY)
				break;
			
			// Draw inner normal square
			g.drawLine(centerX-innerOffset, centerY-innerOffset, centerX+innerOffset, centerY-innerOffset);
			g.drawLine(centerX+innerOffset, centerY-innerOffset, centerX+innerOffset, centerY+innerOffset);
			g.drawLine(centerX+innerOffset, centerY+innerOffset, centerX-innerOffset, centerY+innerOffset);
			g.drawLine(centerX-innerOffset, centerY+innerOffset, centerX-innerOffset, centerY-innerOffset);

			// Determine offset to draw outer diagonal square with
			int outerOffset = (int) Math.pow(2, k+1);
			
			// Break if it goes offscreen
			if(centerX - outerOffset < 0 || centerX + outerOffset > maxX ||
					centerY - outerOffset < 0 || centerY + outerOffset > maxY)
					break;

			// Draw outer diagonal square
			g.drawLine(centerX, centerY-outerOffset, centerX+outerOffset, centerY);
			g.drawLine(centerX+outerOffset, centerY, centerX, centerY+outerOffset);
			g.drawLine(centerX, centerY+outerOffset, centerX-outerOffset, centerY);
			g.drawLine(centerX-outerOffset, centerY, centerX, centerY-outerOffset);
		}
	}
}
