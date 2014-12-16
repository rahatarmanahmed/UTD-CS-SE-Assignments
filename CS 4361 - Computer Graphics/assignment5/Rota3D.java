// Rota3D.java: Class used in other program files
//    for rotations about an arbitrary axis.
// Uses: Point3D (discussed above).

// Copied from Section 3.9 of
//    Ammeraal, L. (1998) Computer Graphics for Java Programmers,
//       Chichester: John Wiley.

class Rota3D
{  static double r11, r12, r13, r21, r22, r23,
                 r31, r32, r33, r41, r42, r43;

/* The method initRotate computes the general rotation matrix

            | r11  r12  r13  0 |
       R =  | r21  r22  r23  0 |
            | r31  r32  r33  0 |
            | r41  r42  r43  1 |

   to be used as [x1  y1  z1  1] = [x  y  z  1] R
   by the method 'rotate'.
   Point (x1, y1, z1) is the image of (x, y, z).
   The rotation takes place about the directed axis  
   AB and through the angle alpha.
*/
   static void initRotate(Point3D A, Point3D B,
      double alpha)
   {  double v1 = B.x - A.x, 
             v2 = B.y - A.y,
             v3 = B.z - A.z,
         theta = (v1 == 0 && v2 == 0 ? 0 : Math.atan2(v2, v1)),
            // Corrected because atan2(0, 0) may cause problems,
            // as pointed out by Titus Purdin (21 May 1999).
         phi = Math.atan2(Math.sqrt(v1 * v1 + v2 * v2), v3);
      initRotate(A, theta, phi, alpha);
   }

   static void initRotate(Point3D A, double theta,
      double phi, double alpha)
   {  double cosAlpha, sinAlpha, cosPhi, sinPhi, 
      cosTheta, sinTheta, cosPhi2, sinPhi2, 
      cosTheta2, sinTheta2, pi, c,
      a1 = A.x, a2 = A.y, a3 = A.z;
      cosPhi = Math.cos(phi); sinPhi = Math.sin(phi);
      cosPhi2 = cosPhi * cosPhi; sinPhi2 = sinPhi * sinPhi;
      cosTheta = Math.cos(theta); 
      sinTheta = Math.sin(theta);
      cosTheta2 = cosTheta * cosTheta; 
      sinTheta2 = sinTheta * sinTheta;
      cosAlpha = Math.cos(alpha); 
      sinAlpha = Math.sin(alpha);
      c = 1.0 - cosAlpha;
      r11 = cosTheta2 * (cosAlpha * cosPhi2 + sinPhi2)
            + cosAlpha * sinTheta2;
      r12 = sinAlpha * cosPhi + c * sinPhi2 * cosTheta * sinTheta;
      r13 = sinPhi * (cosPhi * cosTheta * c - sinAlpha * sinTheta);
      r21 = sinPhi2 * cosTheta * sinTheta * c - sinAlpha * cosPhi;
      r22 = sinTheta2 * (cosAlpha * cosPhi2 + sinPhi2) 
            + cosAlpha * cosTheta2;
      r23 = sinPhi * (cosPhi * sinTheta * c + sinAlpha * cosTheta);
      r31 = sinPhi * (cosPhi * cosTheta * c + sinAlpha * sinTheta);
      r32 = sinPhi * (cosPhi * sinTheta * c - sinAlpha * cosTheta);
      r33 = cosAlpha * sinPhi2 + cosPhi2;
      r41 = a1 - a1 * r11 - a2 * r21 - a3 * r31;
      r42 = a2 - a1 * r12 - a2 * r22 - a3 * r32;
      r43 = a3 - a1 * r13 - a2 * r23 - a3 * r33;
   }

   static Point3D rotate(Point3D P)
   {  return new Point3D(
         P.x * r11 + P.y * r21 + P.z * r31 + r41,
         P.x * r12 + P.y * r22 + P.z * r32 + r42,
         P.x * r13 + P.y * r23 + P.z * r33 + r43);
   }
}
