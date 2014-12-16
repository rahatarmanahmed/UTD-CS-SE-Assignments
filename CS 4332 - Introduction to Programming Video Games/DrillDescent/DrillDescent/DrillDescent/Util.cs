using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace DrillDescent
{
    static class Util
    {
        public static Random Rand = new Random();
        public static T RandomPick<T>(IList<T> list)
        {
            return list.ElementAt<T>(Rand.Next(list.Count()));
        }

        public static Vector2 MouseVector(MouseState mouse)
        {
            return new Vector2(mouse.X, mouse.Y);
        }

        // Function I found to draw a line
        public static void DrawDebugLine(SpriteBatch spriteBatch, Vector2 begin, Vector2 end, Color color, int width = 1)
        {
            Rectangle r = new Rectangle((int)begin.X, (int)begin.Y, (int)(end - begin).Length() + width, width);
            Vector2 v = Vector2.Normalize(begin - end);
            float angle = (float)Math.Acos(Vector2.Dot(v, -Vector2.UnitX));
            if (begin.Y > end.Y) angle = MathHelper.TwoPi - angle;
            spriteBatch.Draw(Art.pixel, r, null, color, angle, Vector2.Zero, SpriteEffects.None, 0);
        }

        public static void DrawDebugRect(SpriteBatch spriteBatch, Vector2 begin, int width, int height, Color color, int thickness = 1)
        {
            DrawDebugLine(spriteBatch, begin, new Vector2(begin.X + width, begin.Y), color, thickness);
            DrawDebugLine(spriteBatch, begin, new Vector2(begin.X, begin.Y + height), color, thickness);
            DrawDebugLine(spriteBatch, new Vector2(begin.X + width, begin.Y), new Vector2(begin.X + width, begin.Y + height), color, thickness);
            DrawDebugLine(spriteBatch, new Vector2(begin.X, begin.Y + height), new Vector2(begin.X + width, begin.Y + height), color, thickness);
        }

        /// <summary>
        /// Calculates the signed depth of intersection between two rectangles.
        /// </summary>
        /// <returns>
        /// The amount of overlap between two intersecting rectangles. These
        /// depth values can be negative depending on which wides the rectangles
        /// intersect. This allows callers to determine the correct direction
        /// to push objects in order to resolve collisions.
        /// If the rectangles are not intersecting, Vector2.Zero is returned.
        /// </returns>
        public static Vector2 GetIntersectionDepth(this Rectangle rectA, Rectangle rectB)
        {
            // Calculate half sizes.
            float halfWidthA = rectA.Width / 2.0f;
            float halfHeightA = rectA.Height / 2.0f;
            float halfWidthB = rectB.Width / 2.0f;
            float halfHeightB = rectB.Height / 2.0f;

            // Calculate centers.
            Vector2 centerA = new Vector2(rectA.Left + halfWidthA, rectA.Top + halfHeightA);
            Vector2 centerB = new Vector2(rectB.Left + halfWidthB, rectB.Top + halfHeightB);

            // Calculate current and minimum-non-intersecting distances between centers.
            float distanceX = centerA.X - centerB.X;
            float distanceY = centerA.Y - centerB.Y;
            float minDistanceX = halfWidthA + halfWidthB;
            float minDistanceY = halfHeightA + halfHeightB;

            // If we are not intersecting at all, return (0, 0).
            if (Math.Abs(distanceX) >= minDistanceX || Math.Abs(distanceY) >= minDistanceY)
                return Vector2.Zero;

            // Calculate and return intersection depths.
            float depthX = distanceX > 0 ? minDistanceX - distanceX : -minDistanceX - distanceX;
            float depthY = distanceY > 0 ? minDistanceY - distanceY : -minDistanceY - distanceY;
            return new Vector2(depthX, depthY);
        }

        public static Vector2 AngleToVector(double radians)
        {
            return new Vector2(
                (float)Math.Cos(radians),
                (float)Math.Sin(radians)
            );
        }

        // Swap the values of A and B
        public static void Swap<T>(ref T a, ref T b)
        {
            T c = a;
            a = b;
            b = c;
        }

        // Returns the list of points from p0 to p1 
        public static List<Point> BresenhamLine(Point p0, Point p1)
        {
            return BresenhamLine((int)p0.X, (int)p0.Y, (int)p1.X, (int)p1.Y);
        }

        // Returns the list of points from (x0, y0) to (x1, y1)
        private static List<Point> BresenhamLine(int x0, int y0, int x1, int y1)
        {
            // Optimization: it would be preferable to calculate in
            // advance the size of "result" and to use a fixed-size array
            // instead of a list.
            List<Point> result = new List<Point>();

            bool steep = Math.Abs(y1 - y0) > Math.Abs(x1 - x0);
            if (steep)
            {
                Swap(ref x0, ref y0);
                Swap(ref x1, ref y1);
            }
            if (x0 > x1)
            {
                Swap(ref x0, ref x1);
                Swap(ref y0, ref y1);
            }

            int deltax = x1 - x0;
            int deltay = Math.Abs(y1 - y0);
            int error = 0;
            int ystep;
            int y = y0;
            if (y0 < y1) ystep = 1; else ystep = -1;
            for (int x = x0; x <= x1; x++)
            {
                if (steep) result.Add(new Point(y, x));
                else result.Add(new Point(x, y));
                error += deltay;
                if (2 * error >= deltax)
                {
                    y += ystep;
                    error -= deltax;
                }
            }

            return result;
        }

        public static Point WorldToTile(Vector2 world)
        {
            return new Point((int)(world.X / Tile.TileSize), (int)(world.Y / Tile.TileSize));
        }

        public static Vector2 TileToWorld(Point tile)
        {
            return new Vector2(tile.X * Tile.TileSize, tile.Y * Tile.TileSize);
        }

        public static Vector2 PointToVector(Point p)
        {
            return new Vector2(p.X, p.Y);
        }

        public static Point VectorToPoint(Vector2 v)
        {
            return new Point((int)v.X, (int)v.Y);
        }

        public static int RandRange(int begin, int end)
        {
            return Rand.Next (end - begin) + begin;
        }

        public static Vector2 rotateRad (Vector2 v, float radians) {
            float cos = (float)Math.Cos(radians);
            float sin = (float)Math.Sin(radians);

            float newX = v.X * cos - v.Y * sin;
            float newY = v.X * sin + v.Y * cos;

            return new Vector2(newX, newY);
        }

        public static Vector2 rotateDeg(Vector2 v, float degrees) {
            return rotateRad (v, (float) (degrees * Math.PI / 180));
        }



    }


}
