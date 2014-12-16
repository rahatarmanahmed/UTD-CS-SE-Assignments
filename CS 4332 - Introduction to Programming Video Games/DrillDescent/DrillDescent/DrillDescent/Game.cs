using System;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;
namespace DrillDescent
{

    public class MacGame : Game {
        private GraphicsDeviceManager graphics;
        SpriteBatch spriteBatch;
        Map map;
        int level = 0;

        public MacGame() {
            graphics = new GraphicsDeviceManager (this);
            Content.RootDirectory = "Content";
        }

        protected override void Initialize () {
            graphics.PreferredBackBufferWidth = 640;
            graphics.PreferredBackBufferHeight = 480;
            graphics.ApplyChanges();

            base.Initialize();
            this.IsMouseVisible = true;
            // TODO: Add your initialization logic here
            NextLevel (100);
        }

        protected override void LoadContent()
        {
            // Create a new SpriteBatch, which can be used to draw textures.
            spriteBatch = new SpriteBatch(GraphicsDevice);
            Art.Load(Content);
            // TODO: use this.Content to load your game content here
        }

        protected override void Update(GameTime gameTime)
        {
            if(level <= 3)
                map.Update();

            base.Update(gameTime);
        }

        protected override void Draw(GameTime gameTime) {
            GraphicsDevice.Clear(Color.Black);

            spriteBatch.Begin(SpriteSortMode.Immediate, BlendState.AlphaBlend, null, null, null, null, map.Camera.GetTransform());

            if (level <= 3)
                map.Draw (spriteBatch);
            else
                spriteBatch.DrawString (Art.font, "You Win! You killed them all... you monster!", new Vector2 (10, 10), Color.White); 
            // TODO: Add your drawing code here

            spriteBatch.End();
            base.Draw(gameTime);
        }

        public void NextLevel(float Health)
        {
            level++;
            map = new DungeonGenerator().generateMap(5*level);
            Rectangle window = GraphicsDevice.Viewport.Bounds;
            Camera Camera = new Camera(window.Width, window.Height);
            map.Camera = Camera;
            map.Game = this;
            map.Init ();
            map.Player.Health = Health;
        }
    }
}