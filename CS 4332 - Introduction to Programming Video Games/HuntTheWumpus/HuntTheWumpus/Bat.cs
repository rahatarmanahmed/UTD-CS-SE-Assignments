using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HuntTheWumpus
{
    class Bat : Entity
    {
        public Bat(Map map, int position) : base(map, position)
        {

        }

        public override void Update()
        {
            if (isAdjacentToPlayer())
            {
                Console.WriteLine("Bats nearby!");
            }
        }

        public override void AffectPlayer()
        {
            // Move player to random room
            Console.WriteLine("Zap--Super Bat snatch! Elsewhereville for you!");
            Map.Player.Move(Map.random.Next(20) + 1);
            Map.HazardCheck();
        }
    }
}
