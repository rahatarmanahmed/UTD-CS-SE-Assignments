using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;

namespace HuntTheWumpus2
{
    /// <summary>
    /// This is the main type for your game
    /// </summary>
    public class Game : Microsoft.Xna.Framework.Game
    {
        GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;

        Map map;
        int seed;

        public static Texture2D square;
        public static Texture2D pixel;
        public static Texture2D bat;
        public static Texture2D pit;
        public static Texture2D wumpus;
        public static SpriteFont font;

        Vector2[] roomPositions = new Vector2[21] {
                new Vector2 (-1,-1), // 0 not an actual room
		        new Vector2 (220,25),
		        new Vector2 (420,166),
		        new Vector2 (343,403),
		        new Vector2 (96,403),
		        new Vector2 (20,167),
		        new Vector2 (88,188),
		        new Vector2 (140,120),
		        new Vector2 (220,93),
		        new Vector2 (303,120),
		        new Vector2 (352,189),
		        new Vector2 (352,279),
		        new Vector2 (303,347),
		        new Vector2 (219,371),
		        new Vector2 (136,345),
		        new Vector2 (90,275),
		        new Vector2 (154,255),
		        new Vector2 (181,174),
		        new Vector2 (263,177),
		        new Vector2 (286,254),
		        new Vector2 (219,301)
            };

        public Game()
        {
            graphics = new GraphicsDeviceManager(this);
            graphics.PreferredBackBufferWidth = 1000;
            graphics.PreferredBackBufferHeight = 480;
            Content.RootDirectory = "Content";
            this.IsMouseVisible = true;
        }

        /// <summary>
        /// Allows the game to perform any initialization it needs to before starting to run.
        /// This is where it can query for any required services and load any non-graphic
        /// related content.  Calling base.Initialize will enumerate through any components
        /// and initialize them as well.
        /// </summary>
        protected override void Initialize()
        {
            // TODO: Add your initialization logic here
            seed = (int)DateTime.Now.Ticks & 0x0000FFFF;
            map = new Map(seed);
            base.Initialize();
        }

        /// <summary>
        /// LoadContent will be called once per game and is the place to load
        /// all of your content.
        /// </summary>
        protected override void LoadContent()
        {
            // Create a new SpriteBatch, which can be used to draw textures.
            spriteBatch = new SpriteBatch(GraphicsDevice);

            // TODO: use this.Content to load your game content here
            square = Content.Load<Texture2D>("square");
            pixel = Content.Load<Texture2D>("pixel");
            bat = Content.Load<Texture2D>("bat");
            pit = Content.Load<Texture2D>("pit");        
            wumpus = Content.Load<Texture2D>("wumpus");
            font = Content.Load<SpriteFont>("SpriteFont1");
        }

        /// <summary>
        /// UnloadContent will be called once per game and is the place to unload
        /// all content.
        /// </summary>
        protected override void UnloadContent()
        {
            // TODO: Unload any non ContentManager content here
        }

        /// <summary>
        /// Allows the game to run logic such as updating the world,
        /// checking for collisions, gathering input, and playing audio.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Update(GameTime gameTime)
        {
            // Allows the game to exit
            if (GamePad.GetState(PlayerIndex.One).Buttons.Back == ButtonState.Pressed)
                this.Exit();

            map.Update();
            if (!map.playing)
            {
                int replayOption = getReplayClick();
                if (replayOption == 1)
                {
                    map = new Map(seed);
                }
                else if (replayOption == 2)
                {
                    seed = (int)DateTime.Now.Ticks & 0x0000FFFF;
                    map = new Map(seed);
                }
            }
                //QueryReplay();
            // TODO: Add your update logic here

            base.Update(gameTime);
        }

        private MouseState currentMouseState;

        public int getReplayClick()
        {
            KeyboardState kb = Keyboard.GetState();
            if(kb.IsKeyDown(Keys.D1) || kb.IsKeyDown(Keys.NumPad1))
                return 1;
            if (kb.IsKeyDown(Keys.D2) || kb.IsKeyDown(Keys.NumPad2))
                return 2;
            return -1;
        }

        /// <summary>
        /// This is called when the game should draw itself.
        /// </summary>
        /// <param name="gameTime">Provides a snapshot of timing values.</param>
        protected override void Draw(GameTime gameTime)
        {
            GraphicsDevice.Clear(Color.CornflowerBlue);

            // TODO: Add your drawing code here
            spriteBatch.Begin();

            map.Draw(spriteBatch);

            spriteBatch.End();

            base.Draw(gameTime);
        }

        public void DrawLine(SpriteBatch spriteBatch, Vector2 begin, Vector2 end, Color color, int width = 1)
        {
            Rectangle r = new Rectangle((int)begin.X, (int)begin.Y, (int)(end - begin).Length()+width, width);
            Vector2 v = Vector2.Normalize(begin - end);
            float angle = (float)Math.Acos(Vector2.Dot(v, -Vector2.UnitX));
            if (begin.Y > end.Y) angle = MathHelper.TwoPi - angle;
            spriteBatch.Draw(pixel, r, null, color, angle, Vector2.Zero, SpriteEffects.None, 0);
        }
    }
}
