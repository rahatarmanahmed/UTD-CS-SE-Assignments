using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework;

namespace DrillDescent
{
    class Tile
    {
        public const int TileSize = 16;
        public int X, Y;
        public bool Visible = false;
        public enum TileType
        {
            DIRT, STONE, OBSIDIAN, STAIRSDOWN
        }

        public TileType Type;
        private int durability;
        public int Durability
        {
            get
            {
                return durability;
            }
            set
            {
                if (Type == TileType.OBSIDIAN)
                    return;
                durability = value;
                if (durability <= 0)
                    Type = TileType.STONE;
            }
        }
        public Rectangle BoundingBox
        {
            get
            {
                return new Rectangle(X*TileSize, Y*TileSize, TileSize, TileSize);
            }
        }

        public Tile(TileType type, int x, int y)
        {
            this.Type = type;
            this.X = x;
            this.Y = y;
            Durability = 100;
        }

        public void Draw(SpriteBatch spriteBatch)
        {
            if (!Visible)
                return;
            Texture2D texture;
            switch (Type)
            {
                case TileType.DIRT:
                    texture = Art.dirt;
                    break;
                case TileType.STONE:
                    texture = Art.stone;
                    break;
                case TileType.OBSIDIAN:
                    texture = Art.obsidian;
                    break;
                case TileType.STAIRSDOWN:
                    texture = Art.stairsdown;
                    break;
                default:
                    texture = Art.pixel;
                    break;
            }
            spriteBatch.Draw(texture, new Rectangle(X*TileSize, Y*TileSize, TileSize, TileSize), Color.White);
        }
    }
}
