using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Graphics;

namespace DrillDescent
{
    class Map
    {
        private const int MAX_ENEMIES_PER_ROOM = 5;

        public Tile[,] Tiles;
        public Rectangle[] Rooms;
        public readonly int Width, Height;

        public MacGame Game;
        public Camera Camera;

        public List<Entity> Entities;
        private List<Entity> EntitiesToAdd;
        public Player Player;
        public int numEnemies, totalEnemies;

        public bool Playing = true;

        public Map(int w, int h)
        {
            Width = w;
            Height = h;
            Tiles = new Tile[w, h];
            for (var x = 0; x < w; x++)
                for (var y = 0; y < h; y++)
                    Tiles[x, y] = new Tile(Tile.TileType.DIRT, x, y);
            Entities = new List<Entity>();
            EntitiesToAdd = new List<Entity>();
        }

        public void Init()
        {
            Player = new Player(this);
            Entities.Add(Player);
            Rectangle playerRoom = Rooms[0];
            float px = (playerRoom.X + playerRoom.Width/2.0f) * Tile.TileSize;
            float py = (playerRoom.Y + playerRoom.Height/2.0f) * Tile.TileSize;
            Player.Center = new Vector2(px, py);

            totalEnemies = 0;
            for (int k = 1; k < Rooms.Length; k++) {
                int numEnemiesInRoom = Util.Rand.Next (MAX_ENEMIES_PER_ROOM) + 1;
                for (int j = 0; j < numEnemiesInRoom; j++) {
                    var enemy = new Enemy (this);
                    Entities.Add (enemy);
                    Rectangle enemyRoom = Rooms [k];
                    float ex = (enemyRoom.X + enemyRoom.Width / 2.0f) * Tile.TileSize;
                    float ey = (enemyRoom.Y + enemyRoom.Height / 2.0f) * Tile.TileSize;
                    enemy.Center = new Vector2 (ex, ey);
                    totalEnemies++;
                }
            }
            numEnemies = totalEnemies;

            var stairs = new Stairs (this);
            Entities.Add (stairs);
            Rectangle stairsRoom = Rooms [Rooms.Length - 1];
            float sx = (stairsRoom.X + stairsRoom.Width / 2.0f) * Tile.TileSize;
            float sy = (stairsRoom.Y + stairsRoom.Width / 2.0f) * Tile.TileSize;
            stairs.Center = new Vector2 (sx, sy);
        }

        public void Update()
        {
            if (Playing) {
                foreach (Entity entity in Entities)
                {
                    entity.Update();
                }
                foreach (Entity entity in EntitiesToAdd)
                {
                    Entities.Add(entity);
                }
                EntitiesToAdd.Clear();
                for (int k = Entities.Count()-1; k >= 0; k--)
                {
                    if (Entities [k].MarkedForDeletion) {
                        if (Entities [k] is Enemy)
                            numEnemies--;
                        Entities.RemoveAt(k);
                    }

                }
                RaycastUpdate();
                Camera.MoveCamera(this);
            }
        }

        private const float NUM_RAYS = 800f;
        private void RaycastUpdate()
        {
            int rayLength = Math.Max(Camera.Width, Camera.Height) / Tile.TileSize;
            for (int k = 0; k < NUM_RAYS; k++)
            {
                double angle = k/NUM_RAYS * 2 * Math.PI - (1 / 4.0 * Math.PI);
                Point playerTile = Util.WorldToTile(Player.Center);
                Point end = new Point(playerTile.X + (int)(Math.Cos(angle)* rayLength), playerTile.Y + (int)(Math.Sin(angle)* rayLength)) ;
                List<Point> points = Util.BresenhamLine(playerTile, end);
                if (points[0] == end)
                {
                    points.Reverse();
                }
                bool visible = true;
                foreach(Point point in points)
                {
                    if(tileExists(point))
                    {
                        Tiles[point.X, point.Y].Visible = visible;
                        if (Tiles[point.X, point.Y].Type != Tile.TileType.STONE)
                            break;
                    }
                }

            }

        }

        public void Draw(SpriteBatch spriteBatch)
        {
            for (var x = 0; x < Width; x++)
                for (var y = 0; y < Height; y++)
                    Tiles[x, y].Draw(spriteBatch);

            foreach (Entity entity in Entities)
            {
                entity.Draw(spriteBatch);
            }

            DrawString (spriteBatch, "Health: " + Player.Health + " / " + 100, new Vector2 (10, 10) - Camera.Position, Color.White);
            if(numEnemies != 0)
                DrawString (spriteBatch, "Kill Remaining Enemies: " + numEnemies + " / " + totalEnemies, new Vector2 (10, 30) - Camera.Position, Color.Red);
            else
                DrawString (spriteBatch, "Find the Stairs!", new Vector2 (10, 30) - Camera.Position, Color.LimeGreen);
            if (!Playing)
                DrawString (spriteBatch, "GAME OVER!", Player.Center - new Vector2(50, 10), Color.Red);
        }

        private void DrawString(SpriteBatch spriteBatch, string str, Vector2 pos, Color color) {
            spriteBatch.DrawString (Art.font, str, pos+new Vector2(-1,1), Color.Black);
            spriteBatch.DrawString (Art.font, str, pos, color);
        }

        public void AddEntity(Entity e)
        {
            EntitiesToAdd.Add(e);
        }

        public bool tileExists(Point point)
        {
            return point.X >= 0 && point.X < Width &&
                point.Y >= 0 && point.Y < Height;
        }

        public void endGame()
        {
            Playing = false;
        }
    }
}
