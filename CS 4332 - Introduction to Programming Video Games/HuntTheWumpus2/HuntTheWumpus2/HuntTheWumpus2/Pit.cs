using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HuntTheWumpus2
{
    class Pit : Entity
    {
        public Pit(Map map, int position)
            : base(map, position)
        {

        }

        public override void Update()
        {
            if (isAdjacentToPlayer())
            {
                FakeConsole.WriteLine("I feel a draft!");
            }
        }

        public override void AffectPlayer()
        {
            // Lose the game
            FakeConsole.WriteLine("YYYIIIIEEEE . . . fell in a pit");
            Map.lastHazardRoom = Position;
            Map.EndGame(false);
        }
    }
}
