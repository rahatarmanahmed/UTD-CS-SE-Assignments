using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace DrillDescent
{
    abstract class Entity
    {
        public Vector2 Position, Velocity, Acceleration;
        public float Rotation;
        public Texture2D Sprite;
        public Rectangle BoundingBox
        {
            get
            {
                return new Rectangle((int)Position.X, (int)Position.Y, Sprite.Width, Sprite.Height);
            }
        }
        public Vector2 Center
        {
            get
            {
                return new Vector2(Position.X + Sprite.Width / 2.0f, Position.Y + Sprite.Height / 2.0f);
            }
            set
            {
                Position.X = value.X - Sprite.Width / 2;
                Position.Y = value.Y - Sprite.Height / 2;
            }
        }
        public Color Tint;
        public Map Map;
        public bool MarkedForDeletion;
        public bool Visible = false;
        public float Health = 100f;

        public Entity(Texture2D sprite, Map map)
        {
            Position = new Vector2();
            Velocity = new Vector2();
            Acceleration = new Vector2();
            Rotation = 0;

            this.Sprite = sprite;
            Tint = Color.White;

            this.Map = map;
            MarkedForDeletion = false;
        }

        public virtual void Update()
        {
            Velocity += Acceleration;
            Position += Velocity;
            CheckMapCollision();
            CheckEntityCollision ();
            var tilePoint = Util.WorldToTile (Center);
            Visible = Map.Tiles [tilePoint.X, tilePoint.Y].Visible;
        }

        protected virtual void CheckMapCollision()
        {
            Rectangle bounds = BoundingBox;
            int leftTile = (int)Math.Floor((float)bounds.Left / Tile.TileSize);
            int rightTile = (int)Math.Ceiling(((float)bounds.Right / Tile.TileSize)) - 1;
            int topTile = (int)Math.Floor((float)bounds.Top / Tile.TileSize);
            int bottomTile = (int)Math.Ceiling(((float)bounds.Bottom / Tile.TileSize)) - 1;
            for (int y = topTile; y <= bottomTile; y++)
            {
                for (int x = leftTile; x <= rightTile; x++)
                {
                    Tile tile = Map.Tiles[x, y];
                    if (tile.Type != Tile.TileType.STONE)
                    {
                        Rectangle tileBounds = tile.BoundingBox;
                        Vector2 depth = Util.GetIntersectionDepth(bounds, tileBounds);
                        if (depth != Vector2.Zero)
                        {
                            OnCollideMap(tile, depth);
                            bounds = BoundingBox;
                        }
                    }
                }
            }
        }

        protected virtual void CheckEntityCollision()
        {
            Rectangle bounds = BoundingBox;
            foreach(var entity in Map.Entities)
            {
                Rectangle entityBounds = entity.BoundingBox;
                Vector2 depth = Util.GetIntersectionDepth (bounds, entityBounds);
                if (depth != Vector2.Zero) {
                    OnCollideEntity (entity, depth);
                    bounds = BoundingBox;
                }
            }
        }

        protected virtual void OnCollideMap(Tile tile, Vector2 depth)
        {
            if (Math.Abs(depth.Y) < Math.Abs(depth.X))
                Position = new Vector2(Position.X, Position.Y + depth.Y);
            else
                Position = new Vector2(Position.X + depth.X, Position.Y);
        }

        protected virtual void OnCollideEntity(Entity entity, Vector2 depth) {

        }

        public virtual void Draw(SpriteBatch spriteBatch)
        {
            if(Visible)
                spriteBatch.Draw(Sprite, Center, null, Tint, Rotation, Center-Position, Vector2.One, SpriteEffects.None, 0);
        }

        public virtual void Damage(float damage)
        {
            Health -= damage;
            if (Health <= 0)
                MarkedForDeletion = true;
        }
    }
}
