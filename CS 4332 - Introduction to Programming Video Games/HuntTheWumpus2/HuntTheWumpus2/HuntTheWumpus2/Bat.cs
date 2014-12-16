using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HuntTheWumpus2
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
                FakeConsole.WriteLine("Bats nearby!");
            }
        }

        public override void AffectPlayer()
        {
            // Move player to random room
            FakeConsole.WriteLine("Zap--Super Bat snatch! Elsewhereville for you!");
            Map.Player.Move(Map.random.Next(20) + 1);
            Map.lastHazardRoom = Position;
            Map.HazardCheck();
        }
    }
}
