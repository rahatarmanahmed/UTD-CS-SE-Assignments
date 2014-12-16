// Canvas3D.java: Used in Painter.java and ZBuf.java.
// Uses class Obj3D, discussed above.
import java.awt.*;

// Copied from Section 6.3 of
//    Ammeraal, L. (1998) Computer Graphics for Java Programmers,
//       Chichester: John Wiley.

abstract class Canvas3D extends Canvas
{  abstract Obj3D getObj(); 
   abstract void setObj(Obj3D obj);
}
