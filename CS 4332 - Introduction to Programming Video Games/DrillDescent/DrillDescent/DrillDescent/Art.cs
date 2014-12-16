using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Content;

namespace DrillDescent
{
    class Art
    {
        public static Texture2D
            pixel,
            stone,
            dirt,
            stairsdown,
            obsidian,
            player,
            bullet,
            enemy;
        public static SpriteFont font;

        public static void Load(ContentManager Content)
        {
            pixel = Content.Load<Texture2D>("pixel");
            stone = Content.Load<Texture2D>("stone");
            dirt = Content.Load<Texture2D>("dirt");
            stairsdown = Content.Load<Texture2D>("stairsdown");
            obsidian = Content.Load<Texture2D>("obsidian");
            player = Content.Load<Texture2D>("player");
            bullet = Content.Load<Texture2D>("bullet");
            enemy = Content.Load<Texture2D> ("mole");

            font = Content.Load<SpriteFont> ("font");
        }
    }
}
