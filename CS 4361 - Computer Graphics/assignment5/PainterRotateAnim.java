// Painter.java: Perspective drawing using an input file that lists
//    vertices and faces. Based on the Painter's algorithm.
// Uses: Fr3D (Section 6.3) and CvPainter (Section 6.4).

// Copied from Section 6.4 of
//    Ammeraal, L. (1998) Computer Graphics for Java Programmers,
//       Chichester: John Wiley.

import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PainterRotateAnim extends Frame
{  public static void main(String[] args)
   {  new Fr3D("cube.dat", new CvPainter());
   }
}

