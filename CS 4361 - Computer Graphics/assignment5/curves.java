// curves.java
import java.io.*;
import java.awt.*;
import java.awt.event.*;

public class curves extends Frame
{  public static void main(String[] args)
   {  
      if (args.length == 0)
         System.out.println("Use filename as program argument.");
      else
         new curves(args[0]);
   }
   curves(String fileName)
   {  super("Click left or right mouse button to change the level");
      addWindowListener(new WindowAdapter()
         {public void windowClosing(
                 WindowEvent e){System.exit(0);}});
      setSize (800, 600);
      add("Center", new Cvcurves(fileName));
      show();
   }
}

class Cvcurves extends Canvas
{  String fileName, axiom, strF, strf, strX, strY;
   int maxX, maxY, level = 1;
   double xLast, yLast, dir, lastDir, rotation, dirStart, fxStart, fyStart,
      lengthFract, reductFact;

   void error(String str)
   {  System.out.println(str);
      System.exit(1);
   }

   Cvcurves(String fileName)
   {  Input inp = new Input(fileName);
      if (inp.fails())
         error("Cannot open input file."); 
      axiom = inp.readString(); inp.skipRest();
      strF = inp.readString(); inp.skipRest();
      strf = inp.readString(); inp.skipRest();
      strX = inp.readString(); inp.skipRest();
      strY = inp.readString(); inp.skipRest();
      rotation = inp.readFloat(); inp.skipRest();
      dirStart = inp.readFloat(); inp.skipRest();
      fxStart = inp.readFloat(); inp.skipRest();
      fyStart = inp.readFloat(); inp.skipRest();
      lengthFract = inp.readFloat(); inp.skipRest();
      reductFact = inp.readFloat();
      if (inp.fails())
         error("Input file incorrect.");

      addMouseListener(new MouseAdapter()
      {  public void mousePressed(MouseEvent evt)
         {  if ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0)
            {  level--;       // Right mouse button decreases level
               if (level < 1)
                  level = 1;
            }
            else
               level++;     // Left mouse button increases level
            repaint();
         }
      });

}

Graphics g;
int iX(double x){return (int)Math.round(x);}
int iY(double y){return (int)Math.round(maxY-y);}

void onlyDraw(Graphics g, double dx, double dy)
{
   g.drawLine(iX(xLast), iY(yLast), iX(xLast + dx ) ,iY(yLast + dy));
}

void drawTo(Graphics g, double dx, double dy)
{  g.drawLine(iX(xLast), iY(yLast), iX(xLast + dx) ,iY(yLast + dy));
   xLast = xLast + dx;
   yLast = yLast + dy;
}

void moveTo(Graphics g, double x, double y)
{  xLast = x;
   yLast = y;
} 
public void paint(Graphics g)
{  Dimension d = getSize();
   maxX = d.width - 1;
   maxY = d.height - 1;
   xLast = fxStart * maxX;
   yLast = fyStart * maxY;
   dir = dirStart; // Initial direction in degrees
   lastDir = dirStart;
   String instructions = axiom;
   double finalLen = lengthFract * maxY;

   // We need to have the final instruction string
   // instead of recursively interpreting it
   // Because we need the context of previous and next
   // characters which isn't available when you do it recursively

   // So just expand the string for however many levels we need
   for(int k=0;k<level;k++)
   {
      String newInstructions = "";
      for(int j=0;j<instructions.length();j++)
      {
         char c = instructions.charAt(j);
         switch(c)
         {
            case 'F':
               newInstructions += strF;
               break;
            case 'f':
               newInstructions += strf;
               break;
            case 'X':
               newInstructions += strX;
               break;
            case 'Y':
               newInstructions += strY;
               break;
            default:
               newInstructions += c;
               break;
         }
      }
      instructions = newInstructions;
      finalLen *= lengthFract;
   }
   // Remove anything that's not F, f, +, -, [, or ]
   instructions = instructions.replaceAll("[^Ff\\+\\-\\]\\[]", "");
   // Remove angles that cancel each other out, makes coding corners easier
   instructions = instructions.replaceAll("\\+\\-|\\-\\+", "");

   turtleGraphics(g, instructions, level, finalLen);
}

private static final double CORNER_FRACTION = .3;

// Modified to not be recursive
public void turtleGraphics(Graphics g, String instruction,
   int depth, double len)
{  double xMark=0, yMark=0, dirMark=0; // TODO: stack these
   double rad, dx, dy;
   for (int i=0;i<instruction.length();i++)
   {  char ch = instruction.charAt(i),
         nextCh = (instruction.length() > i+1) ? instruction.charAt(i+1) : '_',
         prevCh = (i > 0) ? instruction.charAt(i-1) : '_';
      switch(ch)
      {
      case 'F': // Step forward and draw
         // Start: (xLast, yLast), direction: dir, steplength: len
         rad = Math.PI/180 * dir; // Degrees -> radians
         dx = len * Math.cos(rad);
         dy = len * Math.sin(rad);
         if((nextCh != '_' && (nextCh == '+' || nextCh == '-')) ||
            (prevCh != '_' && (prevCh == '+' || prevCh == '-')))
            {
               drawTo(g, dx*(1-CORNER_FRACTION), dy*(1-CORNER_FRACTION));
            }
            else
            {
               drawTo(g, dx, dy);   
            }
               // drawTo(g, dx, dy);   
         break;
      case 'f': // Step forward without drawing
         // Start: (xLast, yLast), direction: dir, steplength: len
         rad = Math.PI/180 * dir; // Degrees -> radians
         dx = len * Math.cos(rad);
         dy = len * Math.sin(rad);
         moveTo(g, xLast + dx, yLast + dy);
         break;
      case '+': // Turn right
         
         if((rotation == 90 || rotation == -90) && prevCh != '_' && prevCh == 'F' && nextCh != '_' && nextCh == 'F')
         {
            double cornerLen = len * Math.sqrt(CORNER_FRACTION*CORNER_FRACTION + CORNER_FRACTION*CORNER_FRACTION);
            rad = Math.PI/180 * ((dir-rotation/2) % 360); // Degrees -> radians
            dx = cornerLen * Math.cos(rad);
            dy = cornerLen * Math.sin(rad);
            drawTo(g, dx, dy);
         }
         dir -= rotation; break;
      case '-': // Turn left
         if((rotation == 90 || rotation == -90) && prevCh != '_' && prevCh == 'F' && nextCh != '_' && nextCh == 'F')
         {
            double cornerLen = len * Math.sqrt(CORNER_FRACTION*CORNER_FRACTION + CORNER_FRACTION*CORNER_FRACTION);
            rad = Math.PI/180 * ((dir+rotation/2) % 360); // Degrees -> radians
            dx = cornerLen * Math.cos(rad);
            dy = cornerLen * Math.sin(rad);
            drawTo(g, dx, dy);
         }
         dir += rotation; break;
      case '[': // Save position and direction
         xMark = xLast; yMark = yLast; dirMark = dir; break;
      case ']': // Back to saved position and direction
         xLast = xMark; yLast = yMark; dir = dirMark; break;
      }
    }
  }
}



class Input
{  private PushbackInputStream pbis;
   private boolean ok = true;
   private boolean eoFile = false;

   Input(){pbis = new PushbackInputStream(System.in);}

   Input(String fileName)
   {  try
      {   InputStream is = new FileInputStream(fileName);
          pbis = new PushbackInputStream(is);
      }
      catch(IOException ioe){ok = false;}
   }

   int readInt()
   {  boolean neg = false;
      char ch;
      do {ch = readChar();}while (Character.isWhitespace(ch));
      if (ch == '-'){neg = true; ch = readChar();}
      if (!Character.isDigit(ch))
      {  pushBack(ch);
         ok = false;
         return 0;
      }
      int x = ch - '0';
      for (;;)
      {  ch = readChar();
         if (!Character.isDigit(ch)){pushBack(ch); break;}
         x = 10 * x + (ch -  '0');
      }
      return (neg ? -x : x);
    }
float readFloat()
{  char ch;
   int nDec = -1;
   boolean neg = false;
   do
   {  ch = readChar();
   }   while (Character.isWhitespace(ch));
   if (ch == '-'){neg = true; ch = readChar();}
   if (ch == '.'){nDec = 1; ch = readChar();}
   if (!Character.isDigit(ch)){ok = false; pushBack(ch); return 0;}
   float x = ch - '0';
   for (;;)
   {  ch = readChar();
      if (Character.isDigit(ch))
      {  x = 10 * x + (ch - '0');
         if (nDec >= 0) nDec++;
      }
      else
      if (ch == '.' && nDec == -1) nDec = 0;
      else break;
   }
   while (nDec > 0){x *= 0.1; nDec--;}
   if (ch == 'e'  ||   ch == 'E')
   {  int exp = readInt();
      if (!fails())
      {   while (exp < 0){x *= 0.1; exp++;}
          while (exp > 0){x *= 10; exp--;}
      }
   }
   else pushBack(ch);
   return (neg ? -x : x);
}

char readChar()
{  int ch=0;
   try
   {   ch = pbis.read();
       if (ch == -1) {eoFile = true; ok = false;}
   }
   catch(IOException ioe){ok = false;}
   return (char)ch;
}

   String readString()    // Read first string between quotes (").
   {  String str = " ";
      char ch;
      do ch = readChar(); while (!(eof()  || ch == '"'));
                                                   // Initial quote
      for (;;)
      {   ch = readChar();
         if (eof()  ||   ch == '"') // Final quote (end of string)
            break;
         str += ch;
      }
      return str;
   }

   void skipRest()   // Skip rest of line
   {  char ch;
      do ch = readChar(); while (!(eof()  || ch == '\n'));
   }

   boolean fails(){return !ok;}
   boolean eof(){return eoFile;}
   void clear(){ok = true;}

   void close()
   {  if (pbis != null)
      try {pbis.close();}catch(IOException ioe){ok = false;}
   }

   void pushBack(char ch)
   {  try {pbis.unread(ch);}catch(IOException ioe){}
   }
}

