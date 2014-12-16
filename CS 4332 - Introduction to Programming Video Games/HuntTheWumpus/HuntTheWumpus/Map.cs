using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HuntTheWumpus
{
    class Map
    {
        public Random random = new Random();
        private const int NumBats = 1;
        private const int NumPits = 2;

        private int[][] Rooms;

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

            playing = true;
        }

        public void Update()
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
            Console.WriteLine("You are in room {0}", Player.Position);
            Console.WriteLine("Tunnels lead to {0} {1} {2}", adjRooms[0], adjRooms[1], adjRooms[2]);

            // Do player actions
            Player.Update();

            // At the end of turn, check if anything in the same room as player
            HazardCheck();

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
            if (won)
                Console.WriteLine("Hee hee hee - the Wumpus'll getcha next time!!");
            else
                Console.WriteLine("You died. Game Over.");
            playing = false;
        }
    }
}

