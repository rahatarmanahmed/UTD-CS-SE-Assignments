using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace DrillDescent
{
    
        

    class DungeonGenerator
    {
        Random rand;

        int MIN_ROOM_WIDTH = 6,
        MIN_ROOM_HEIGHT = 6,
        MAX_ROOM_WIDTH = 16,
        MAX_ROOM_HEIGHT = 16,
        ROOM_MARGIN = 1,
        SEPARATION_STEP = 1,
        INITIAL_WORLD_SIZE = 20,
        OBSIDIAN_PADDING = 1;

        public DungeonGenerator()
        {
            rand = new Random();
        }

        public Map generateMap(int numRooms)
        {
            var rooms = generateRooms(numRooms);
            int minX = Int32.MaxValue,
                minY = Int32.MaxValue,
                maxX = Int32.MinValue,
                maxY = Int32.MinValue;
            foreach (var room in rooms)
            {
                minX = (room.X < minX) ? room.X : minX;
                minY = (room.Y < minY) ? room.Y : minY;
                maxX = (room.X + room.Width > maxX) ? room.X + room.Width : maxX;
                maxY = (room.Y + room.Height > maxY) ? room.Y + room.Height : maxY;
            }
            minX -= OBSIDIAN_PADDING;
            minY -= OBSIDIAN_PADDING;
            maxX += OBSIDIAN_PADDING;
            maxY += OBSIDIAN_PADDING;
            int width = maxX - minX + 1,
                height = maxY - minY + 1;
            Map map = new Map(width, height);
            for (int k = 0; k < OBSIDIAN_PADDING; k++)
            {
                for (int j = 0; j < width; j++)
                    map.Tiles[j, k].Type = Tile.TileType.OBSIDIAN;
                for (int j = 0; j < width; j++)
                    map.Tiles[j, height-k-1].Type = Tile.TileType.OBSIDIAN;
                for (int j = 0; j < height; j++)
                    map.Tiles[k, j].Type = Tile.TileType.OBSIDIAN;
                for (int j = 0; j < height; j++)
                    map.Tiles[width-k-1, j].Type = Tile.TileType.OBSIDIAN;
            }
            for(int k = 0; k < rooms.Length; k++)
            {
                var room = rooms[k];
                for (int x = room.X - minX; x < room.X - minX + room.Width; x++)
                    for (int y = room.Y - minY; y < room.Y - minY + room.Height; y++)
                        map.Tiles[x, y].Type = Tile.TileType.STONE;
                rooms[k].X -= minX;
                rooms[k].Y -= minY;
            }
            map.Rooms = rooms;
            return map;
        }

        private Rectangle[] generateRooms(int numRooms)
        {
            Rectangle[] rooms = new Rectangle[numRooms];
            for (int k = 0; k < rooms.Length; k++)
                rooms[k] = generateRoom();

            bool collisionsExist = false;
            do
            {
                collisionsExist = false;
                for (var k = 0; k < rooms.Length; k++)
                {
                    var room = rooms[k];
                    var centerOfMass = new Vector2();
                    var count = 0;
                    for (var i = 0; i < rooms.Length; i++)
                    {
                        if (k == i)
                            continue;
                        var otherRoom = rooms[i];
                        if (rectMargin(room, ROOM_MARGIN).Intersects(otherRoom))
                        {
                            centerOfMass.X += otherRoom.Center.X;
                            centerOfMass.Y += otherRoom.Center.Y;
                            count++;
                        }
                    }

                    if (count != 0)
                    {
                        collisionsExist = true;
                        centerOfMass /= count;
                        room.X += (centerOfMass.X - room.Center.X > 0) ? -SEPARATION_STEP : SEPARATION_STEP;
                        room.Y += (centerOfMass.Y - room.Center.Y > 0) ? -SEPARATION_STEP : SEPARATION_STEP;
                        rooms[k] = room;
                    }
                }
            } while (collisionsExist);

            return rooms;
        }

        private Rectangle generateRoom()
        {
            var w = rand.Next(MAX_ROOM_WIDTH - MIN_ROOM_WIDTH) + MIN_ROOM_WIDTH + 1;
            var h = rand.Next(MAX_ROOM_HEIGHT - MIN_ROOM_HEIGHT) + MIN_ROOM_HEIGHT + 1;
            var x = rand.Next(INITIAL_WORLD_SIZE);
            var y = rand.Next(INITIAL_WORLD_SIZE);
            return new Rectangle(x, y, w, h);
        }

        private Rectangle rectMargin(Rectangle rect, int margin)
        {
            return new Rectangle(rect.X - margin, rect.Y - margin,
                rect.Width + 2 * margin, rect.Height + 2 * margin);
        }
    }
}
