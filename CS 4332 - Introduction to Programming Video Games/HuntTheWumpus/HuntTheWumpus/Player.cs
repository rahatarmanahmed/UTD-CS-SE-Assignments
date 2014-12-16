using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HuntTheWumpus
{
    // Class representing the player
    class Player : Entity
    {
        int Arrows;
        public Player(Map map, int position) : base(map, position)
        {
            // Start off with 5 arrows
            Arrows = 5;
        }

        public override void Update()
        {
            // Ask for player's choice this move
            PlayerChoice choice = QueryChoice();

            // Shoot an arrow
            if (choice == PlayerChoice.SHOOT)
            {
                var arrowPath = QueryShoot();
                if (arrowPath.Length > 0)
                    ShootArrow(arrowPath);
            } 
            // Move to a room
            else if (choice == PlayerChoice.MOVE)
            {
                var room = QueryMove();
                Move(room);
            }
        }

        private PlayerChoice QueryChoice()
        {
            // Loop until valid input received
            while (true)
            {
                Console.Write("Your move: [S]hoot, [M]ove, or [Q]uit? ");
                char key = Console.ReadKey(false).KeyChar;
                Console.WriteLine();

                if (key == 's' || key == 'S')
                    return PlayerChoice.SHOOT;
                else if (key == 'm' || key == 'M')
                    return PlayerChoice.MOVE;
                else if (key == 'q' || key == 'Q')
                    Environment.Exit(0);
                else
                    Console.WriteLine("That is not a valid option. Try again.");
            }            
        }

        private int QueryMove()
        {
            // Loop until valid input received
            while (true)
            {
                Console.Write("Where to? ");
                string line = Console.ReadLine();
                int room;
                if (Int32.TryParse(line, out room))
                {
                    var adjRooms = Map.getAdjacentRooms(Position);
                    if (adjRooms.Contains(room))
                    {
                        return room;
                    }
                    else // Cannot move to that room
                    {
                        Console.WriteLine("Not Possible");
                    }
                }
                else
                {
                    Console.WriteLine("That's not a valid number. Try again.");
                }
            }
        }

        private int[] QueryShoot()
        {
            // Query number of rooms
            int num;
            while (true)
            {
                Console.Write("No. of rooms (0-5)? ");
                string line = Console.ReadLine();
                if (Int32.TryParse(line, out num))
                {
                    if (0 <= num && num <= 5)
                        break;
                    Console.WriteLine("Number of rooms must be between 0 and 5.");
                }
                else
                    Console.WriteLine("That's not a valid number.");
            }

            int[] result = new int[num];
            int[] input = new int[num];

            if (num == 0)
            {
                Console.WriteLine("Okay, suit yourself...");
                return result; 
            }

            // keeps track of if player gave a valid path so far or not
            bool valid = true;
            int currentRoom = Position;

            // Query a room for the number of rooms specified
            for (int k = 0; k < num; k++)
            {
                int room;
                while (true)
                {
                    Console.Write("Room #? ");
                    string line = Console.ReadLine();
                    if (Int32.TryParse(line, out room))
                    {
                        if (1 <= room && room <= Map.NumRooms)
                        {
                            input[k] = room;
                            // Arrow takes A-B-A path
                            // use input instead of result to evaluate only player input
                            if(k > 1 && input[k-2] == room || k == 1 && room == Position)
                            {
                                Console.WriteLine("Arrows aren't that crooked - try another room");
                            }
                            // The player's path is correct so far, shoot where he said
                            else if(valid && Map.isAdjacent(currentRoom, room))
                            {
                                result[k] = room;
                                currentRoom = result[k];
                                break;
                            }
                            // The player messed up the path, so shoot somewhere random
                            else
                            {
                                valid = false;
                                var adjRooms = Map.getAdjacentRooms(currentRoom);
                                // Remove possibility for A-B-A path
                                if (k > 0)
                                {
                                    var forbiddenRoom = (k == 1) ? Position : result[k - 2];
                                    // If there is a room that creates a A-B-A path, remove it
                                    if (adjRooms.Contains(forbiddenRoom))
                                    {
                                        var tempRooms = new int[adjRooms.Length - 1];
                                        int j = 0;
                                        foreach (var adjRoom in adjRooms)
                                            if (adjRoom != forbiddenRoom)
                                                tempRooms[j++] = adjRoom;
                                        adjRooms = tempRooms;
                                    }
                                }
                                result[k] = adjRooms[Map.random.Next(adjRooms.Length)];
                                currentRoom = result[k];
                                break;
                            }
                        }
                        else // Input isn't a real room on the map
                        {
                            Console.WriteLine("That room doesn't exist.");
                        }
                    }
                    else // Input is an invalid number
                    {
                        Console.WriteLine("That's not a valid number.");
                    }
                }
            }

            return result;
        }

        private void ShootArrow(int[] rooms)
        {
            Arrows--;
            foreach(int room in rooms)
            {
                Console.WriteLine(room);
                // Shot the wumpus
                if (Map.Wumpus.Position == room)
                {
                    Console.WriteLine("Aha! You got the Wumpus!");
                    Map.EndGame(true);
                    return;
                }
                // Shot yourself
                if (Position == room)
                {
                    Console.WriteLine("Ouch! Arrow got you!");
                    Map.EndGame(false);
                    return;
                }
            }
            Console.WriteLine("Missed!");
            // Ran out of arrows and died
            if (Arrows == 0)
            {
                Console.WriteLine("You ran out of arrows! (and you somehow die from this)");
                Map.EndGame(false);
            }
        }
        
    }

    enum PlayerChoice
    {
        MOVE = 0,
        SHOOT = 1
    };
}

