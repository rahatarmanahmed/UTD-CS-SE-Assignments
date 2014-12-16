// Fr3D.java: Frame class for Painter.java and ZBuf.java.
// Uses: Obj3D, Canvas3D (both discussed above).

// Copied from Section 6.3 of
//    Ammeraal, L. (1998) Computer Graphics for Java Programmers,
//       Chichester: John Wiley.

import java.awt.*;
import java.awt.event.*;
import java.util.*;

class Fr3D extends Frame
{  private MenuItem open, exit, eyeUp, eyeDown, eyeLeft, eyeRight,
      incrDist, decrDist;
   private String sDir;
   private Canvas3D cv;
   
   Fr3D(String argFileName, Canvas3D cv)
   {  super(cv instanceof CvPainter ? "Painter" : "ZBuf");
      addWindowListener(new WindowAdapter()
         {public void windowClosing(WindowEvent e){System.exit(0);}});
      this.cv = cv;
      MenuBar mBar = new MenuBar();
      // setMenuBar(mBar);
      Menu mF = new Menu("File"),
           mV = new Menu("View");
      mBar.add(mF); mBar.add(mV);

      open = new MenuItem("Open", 
         new MenuShortcut(KeyEvent.VK_O));
      eyeDown = new MenuItem("Viewpoint Down",
         new MenuShortcut(KeyEvent.VK_DOWN));
      eyeUp = new MenuItem("Viewpoint Up", 
         new MenuShortcut(KeyEvent.VK_UP));
      eyeLeft = new MenuItem("Viewpoint to Left", 
         new MenuShortcut(KeyEvent.VK_LEFT));
      eyeRight = new MenuItem("Viewpoint to Right",
         new MenuShortcut(KeyEvent.VK_RIGHT));

      incrDist = new MenuItem("Increase viewing distance",
         new MenuShortcut(KeyEvent.VK_INSERT));
      decrDist = new MenuItem("Decrease viewing distance",
         new MenuShortcut(KeyEvent.VK_DELETE));
      exit = new MenuItem("Exit",
         new MenuShortcut(KeyEvent.VK_Q));
      mF.add(open); mF.add(exit); 
      mV.add(eyeDown); mV.add(eyeUp); 
      mV.add(eyeLeft); mV.add(eyeRight); 
      mV.add(incrDist); mV.add(decrDist);
      MenuCommands mListener = new MenuCommands();
      open.addActionListener(mListener);
      exit.addActionListener(mListener);
      eyeDown.addActionListener(mListener);
      eyeUp.addActionListener(mListener);
      eyeLeft.addActionListener(mListener);
      eyeRight.addActionListener(mListener);
      incrDist.addActionListener(mListener);
      decrDist.addActionListener(mListener);
      add("Center", cv);
      Dimension dim = getToolkit().getScreenSize();
      setSize(dim.width/2, dim.height/2);
      setLocation(dim.width/4, dim.height/4);
      if (argFileName != null) 
      {  Obj3D obj = new Obj3D();
         if (obj.read(argFileName)){cv.setObj(obj); cv.repaint();}
      }
      cv.setBackground(new Color(180, 180, 255));   
      show();
   }

   void vp(float dTheta, float dPhi, float fRho) // Viewpoint
   {  Obj3D obj = cv.getObj();
      if (obj == null || !obj.vp(cv, dTheta, dPhi, fRho))
         Toolkit.getDefaultToolkit().beep();
   }

   class MenuCommands implements ActionListener
   {  public void actionPerformed(ActionEvent ae)
      {  if (ae.getSource() instanceof MenuItem)
         {  MenuItem mi = (MenuItem)ae.getSource();
            if (mi == open)
            {  FileDialog fDia = new FileDialog(Fr3D.this, 
                  "Open", FileDialog.LOAD);
               fDia.setDirectory(sDir);
               fDia.setFile("*.dat");
               fDia.show();	
               String sDir1 = fDia.getDirectory();
               String sFile = fDia.getFile();
               String fName = sDir1 + sFile;
               Obj3D obj = new Obj3D();
               if (obj.read(fName))
               {  sDir = sDir1;
                  cv.setObj(obj);
                  cv.repaint(); 
               }
            }  
            else
            if (mi == exit) System.exit(0); else
            if (mi == eyeDown) vp(0, .1F, 1); else
            if (mi == eyeUp) vp(0, -.1F, 1); else
            if (mi == eyeLeft) vp(-.1F, 0, 1); else
            if (mi == eyeRight) vp(.1F, 0, 1); else
            if (mi == incrDist) vp(0, 0, 2); else
            if (mi == decrDist) vp(0, 0, .5F);
         }
      }
   } 
} 
