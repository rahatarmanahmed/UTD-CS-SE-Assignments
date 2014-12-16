using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace DrillDescent
{
    class Camera
    {
        private const int DEADZONE_RADIUS = 150;
        // The position of the camera.
        public Vector2 Position
        {
            get { return mCameraPosition; }
            set { mCameraPosition = value; }
        }
        Vector2 mCameraPosition;

        public Vector2 Center
        {
            get
            {
                return new Vector2(mCameraPosition.X + Width / 2.0f, mCameraPosition.Y + Height / 2.0f);
            }
            set
            {
                mCameraPosition.X = value.X - Width / 2;
                mCameraPosition.Y = value.Y - Height / 2;
            }
        }

        public Vector2 Origin { get; set; }

        public float Zoom { get; set; }

        public float Rotation { get; set; }

        private Vector2 mScrollSpeed = new Vector2(20, 18);

        public int Width, Height;

        public Camera(int width, int height)
        {
            Position = Vector2.Zero;
            Origin = Vector2.Zero;
            Zoom = 1;
            Rotation = 0;
            Width = width;
            Height = height;
        }

        public Matrix GetTransform()
        {
            return Matrix.CreateTranslation(new Vector3(mCameraPosition, 0.0f)) *
                   Matrix.CreateRotationZ(Rotation) *
                   Matrix.CreateScale(Zoom, Zoom, 1.0f) *
                   Matrix.CreateTranslation(new Vector3(Origin, 0.0f));
        }

        public void MoveCamera(Map map)
        {
            if (map.Player != null)
            {
                // ugh can't get camera deadzone to work
                //Vector2 centerDiff = map.Player.Center - Center;
                //if (centerDiff.LengthSquared() > DEADZONE_RADIUS * DEADZONE_RADIUS)
                //{
                //    //centerDiff.Normalize();
                //    Center = map.Player.Center;// +(centerDiff * DEADZONE_RADIUS);
                //}
                //Center = -map.Player.Center;
                Position = -new Vector2(map.Player.Position.X - Width / 2, map.Player.Position.Y - Height / 2);

            }
        }
    }
}
