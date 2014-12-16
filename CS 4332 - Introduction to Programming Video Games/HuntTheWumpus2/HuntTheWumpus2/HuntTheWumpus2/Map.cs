using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;
using Microsoft.Xna.Framework.Audio;
using Microsoft.Xna.Framework.Content;
using Microsoft.Xna.Framework.GamerServices;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Media;

namespace HuntTheWumpus2
{
    class Map
    {
        public enum GameState
        {
            INIT,
            WAITING_ON_MOVE,
            PICKING_SHOOT_ROOMS,
            GAME_OVER
        }

        private GameState currentState;
        private MouseState currentMouseState;

        public int lastHazardRoom = -1;
        
        public List<int> arrowPath;
        private Boolean arrowPathValid;
        private int currentArrowRoom;
        public List<int> lastFiredPath;

        public Random random = new Random();
        private const int NumBats = 1;
        private const int NumPits = 2;

        private int[][] Rooms;
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

        public bool playing;

        private List<Entity> Entities;
        public Player Player;
        public Wumpus Wumpus;
        public int NumRooms
        {
            get { return Rooms.Length - 1; }
            set { }
        }

        public Map(int seed)
        {
            // Seeding allows us to play the same map
            random = new Random(seed);
            // Create list of rooms and their adjacent rooms
            // Start at index 1 to make life easy
            Rooms = new int[21][] {
                new int [] {-1,-1,-1}, // 0 not an actual room
		        new int [] {2,5,8},
		        new int [] {1,3,10},
		        new int [] {2,4,12},
		        new int [] {3,5,14},
		        new int [] {1,4,6},
		        new int [] {5,7,15},
		        new int [] {6,8,17},
		        new int [] {1,7,9},
		        new int [] {8,10,18},
		        new int [] {2,9,11},
		        new int [] {10,12,19},
		        new int [] {3,11,13},
		        new int [] {12,14,20},
		        new int [] {4,13,15},
		        new int [] {6,14,16},
		        new int [] {15,17,20},
		        new int [] {7,16,18},
		        new int [] {9,17,19},
		        new int [] {11,18,20},
		        new int [] {13,16,19}
            };

            Entities = new List<Entity>();

            // Generate a list of random rooms by shuffling
            // a list of numbers from 1 to number of rooms
            // We skip room 1 because that's for the Player
            var randomRooms = new int[19];
            for (int k = 2; k <= 20; k++)
                randomRooms[k - 2] = k;
            randomRooms = randomRooms.OrderBy(x => random.Next()).ToArray();
            
            // Create all the entities reading off the random positions one by one
            // This makes sure nothing is initially in the same room
            int randCount = 0;
            for (int k = 0; k < NumBats; k++)
                Entities.Add(new Bat(this, randomRooms[randCount++]));
            for (int k = 0; k < NumPits; k++)
                Entities.Add(new Pit(this, randomRooms[randCount++]));

            // Wumpus and Player are special, so they're not in the entity list
            Wumpus = new Wumpus(this, randomRooms[randCount++]);
            Player = new Player(this, 1);

            arrowPath = new List<int>();
            lastFiredPath = new List<int>();

            playing = true;

            currentState = GameState.INIT;
        }

        public void Update()
        {
            // If just beginning, start a turn and wait for player's move
            if (currentState == GameState.INIT)
            {
                UpdateTurn();
                currentState = GameState.WAITING_ON_MOVE;
            }
            // If waiting on player's move, see if they did anything yet
            if (currentState == GameState.WAITING_ON_MOVE)
            {
                int roomClicked = getRoomClicked();
                if (roomClicked != -1)
                {
                    // Valid room movement input
                    if (isAdjacent(Player.Position, roomClicked))
                    {
                        lastHazardRoom = -1;
                        arrowPath = new List<int>();
                        lastFiredPath = new List<int>();
                        Player.Move(roomClicked);
                        HazardCheck();
                        if(playing)
                            UpdateTurn();
                    }
                    else // Invalid room
                    {
                        FakeConsole.WriteLine("Not possible.");
                    }
                }

                // Check if user pressed S to go into shoot mode
                KeyboardState kb = Keyboard.GetState();
                if (kb.IsKeyDown(Keys.S))
                {
                    currentState = GameState.PICKING_SHOOT_ROOMS;
                    arrowPath = new List<int>();
                    lastFiredPath = new List<int>();
                    arrowPathValid = true;
                    currentArrowRoom = Player.Position;
                    FakeConsole.WriteLine("Click up to 5 rooms to shoot through");
                    FakeConsole.WriteLine("Press enter when finished");
                }
            }
            // If in shoot mode, see what user has inputed 
            if (currentState == GameState.PICKING_SHOOT_ROOMS)
            {
                // Check if user hit enter, if so, shoot the arrow and finish the turn
                KeyboardState kb = Keyboard.GetState();
                int roomClicked = getRoomClicked();

                if (kb.IsKeyDown(Keys.Enter))
                {
                    if (arrowPath.Count == 0)
                    {
                        FakeConsole.WriteLine("Okay, suit yourself...");
                    }

                    lastFiredPath = arrowPath;
                    Player.ShootArrow(arrowPath.ToArray());
                    HazardCheck();
                    if (playing)
                    {
                        UpdateTurn();
                        currentState = GameState.WAITING_ON_MOVE;
                    }
                }
                // If a room was clicked, try to add it to the arrow path
                else if(roomClicked != -1)
                {
                    // Player inputed room that would cause A-B-A path, don't accept
                    if (arrowPath.Count > 1 && arrowPath[arrowPath.Count - 2] == roomClicked || arrowPath.Count == 1 && roomClicked == Player.Position)
                    {
                        FakeConsole.WriteLine("Arrows aren't that crooked - try another room");
                    }
                    // Player has entered valid path so far, so accept his input if correct
                    else if (arrowPathValid && isAdjacent(currentArrowRoom, roomClicked))
                    {
                        arrowPath.Add(roomClicked);
                        currentArrowRoom = roomClicked;
                    }
                    // Player inputted an invalid room, so pick one at random
                    else
                    {
                        arrowPathValid = false;
                        var adjRooms = getAdjacentRooms(currentArrowRoom);
                        if (arrowPath.Count > 0)
                        {
                            var forbiddenRoom = (arrowPath.Count == 1) ? Player.Position : arrowPath[arrowPath.Count - 2];
                            if (adjRooms.Contains(forbiddenRoom)) // prevent A-B-A path
                            {
                                var tempRooms = new int[adjRooms.Length - 1];
                                int j = 0;
                                foreach (var adjRoom in adjRooms)
                                    if (adjRoom != forbiddenRoom)
                                        tempRooms[j++] = adjRoom;
                                adjRooms = tempRooms;
                            }
                            var pickRoom = adjRooms[random.Next(adjRooms.Length)];
                            arrowPath.Add(pickRoom);
                            currentArrowRoom = pickRoom;
                            
                        }
                    }
                    // Max rooms hit, fire the arrow
                    if (arrowPath.Count == 5)
                    {
                        lastFiredPath = arrowPath;
                        Player.ShootArrow(arrowPath.ToArray());
                        HazardCheck();
                        if (playing)
                        {
                            UpdateTurn();
                            currentState = GameState.WAITING_ON_MOVE;
                        }
                    }
                }
                

            }
        }

        // Returns the room number clicked if the user clicked one. -1 otherwise
        public int getRoomClicked()
        {
            MouseState lastState = currentMouseState;
            currentMouseState = Mouse.GetState();
            if (lastState.LeftButton == ButtonState.Released && currentMouseState.LeftButton == ButtonState.Pressed)
            {
                for(int k = 1; k < roomPositions.Length; k++)
                {
                    Vector2 roomPos = roomPositions[k];
                    Rectangle bounds = new Rectangle((int)roomPos.X - Game.square.Width / 2, (int)roomPos.Y - Game.square.Height / 2, Game.square.Width, Game.square.Height);
                    if(bounds.Contains(new Point(currentMouseState.X, currentMouseState.Y)))
                        return k;
                }
            }
            return -1;
        }

        // Beginning of turn logic
        public void UpdateTurn()
        {
            var adjRooms = getAdjacentRooms(Player.Position);
            // Update Wumpus first
            Wumpus.Update();

            // Check for adjacent hazards (in entity updates)
            foreach (Entity entity in Entities)
            {
                entity.Update();
            }

            // Tell the player where they are
            FakeConsole.WriteLine("You are in room " + Player.Position);
            FakeConsole.WriteLine("Tunnels lead to " + adjRooms[0] + ", " + adjRooms[1] + ", and " + adjRooms[2]);
            FakeConsole.WriteLine("Your move:");
            FakeConsole.WriteLine("  Click a room to move to it");
            FakeConsole.WriteLine("  or press S to shoot");
        }

        public void Draw(SpriteBatch spriteBatch)
        {
            // Draw lines between rooms
            for (int k = 1; k < NumRooms; k++)
            {
                var index = lastFiredPath.IndexOf(k);
                foreach (int j in getAdjacentRooms(k))
                {
                    Color color = Color.Black;
                    // Red if along the path of an arrow last fired
                    if (index != -1 && (index > 0 && lastFiredPath[index-1] == j) || (index == 0 && Player.Position == j))
                        color = Color.Red;
                    DrawLine(spriteBatch, roomPositions[k], roomPositions[j], color);
                }
            }

            // Draw room sprites
            for (int k = 1; k < roomPositions.Length; k++)
            {
                Vector2 position = new Vector2();
                position.X = roomPositions[k].X - Game.square.Width / 2;
                position.Y = roomPositions[k].Y - Game.square.Height / 2;
                Color color = Color.Black;
                if (Player.Position == k) // blue if the player is in the room
                    color = Color.Blue;
                if (lastFiredPath.Contains(k)) // red if the arrow was last fired in that room
                    color = Color.Red;
                spriteBatch.Draw(Game.square, position, color);

                // If we hit a hazard recently, draw the appropriate sprite instead of a number
                if (lastHazardRoom == k)
                {
                    Boolean found = false;
                    foreach (var entity in Entities)
                    {
                        if(entity.Position == k)
                            if(entity is Bat)
                            {
                                spriteBatch.Draw(Game.bat, position, Color.White);
                                found = true;
                            }
                            else if (entity is Pit)
                            {
                                spriteBatch.Draw(Game.pit, position, Color.White);
                                found = true;
                            }   
                    }
                        
                    if(!found)
                        spriteBatch.Draw(Game.wumpus, position, Color.White);
                }
                else // Draw the room number
                    spriteBatch.DrawString(Game.font, " " + k, position, Color.White);
            }

            // Draw all console output 
            int y = 450;
            for (int k = FakeConsole.output.Count - 1; k >= 0; k--)
            {
                Vector2 position = new Vector2(450, y);
                
                spriteBatch.DrawString(Game.font, FakeConsole.output[k], position, Color.White);
                y -= 20;
            }
        }

        // Function I found to draw a line
        public void DrawLine(SpriteBatch spriteBatch, Vector2 begin, Vector2 end, Color color, int width = 1)
        {
            Rectangle r = new Rectangle((int)begin.X, (int)begin.Y, (int)(end - begin).Length() + width, width);
            Vector2 v = Vector2.Normalize(begin - end);
            float angle = (float)Math.Acos(Vector2.Dot(v, -Vector2.UnitX));
            if (begin.Y > end.Y) angle = MathHelper.TwoPi - angle;
            spriteBatch.Draw(Game.pixel, r, null, color, angle, Vector2.Zero, SpriteEffects.None, 0);
        }

        public int[] getAdjacentRooms(int room)
        {
            return Rooms[room];
        }

        public bool isAdjacent(int fromRoom, int toRoom)
        {
            return getAdjacentRooms(fromRoom).Contains(toRoom);
        }

        // Checks if anything is in the same room as player
        // Calls entity's AffectPlayer method to do something
        public void HazardCheck()
        {
            foreach(Entity entity in Entities)
                if (entity.Position == Player.Position)
                {
                    entity.AffectPlayer();
                    break;
                }
                    
            if (Wumpus.Position == Player.Position)
                Wumpus.AffectPlayer();
        }

        // End the game, pass in true for win, false for lose
        public void EndGame(bool won)
        {
            currentState = GameState.GAME_OVER;
            if (won)
                FakeConsole.WriteLine("Hee hee hee - the Wumpus'll getcha next time!!");
            else
                FakeConsole.WriteLine("You died. Game Over.");
            playing = false;
            FakeConsole.WriteLine("");
            FakeConsole.WriteLine("Press 1 to play again");
            FakeConsole.WriteLine("Press 2 to play with new");
            FakeConsole.WriteLine("  random locations for hazards");
            FakeConsole.WriteLine("");
        }
    }
}

