using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HuntTheWumpus
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
                Console.WriteLine("I feel a draft!");
            }
        }

        public override void AffectPlayer()
        {
            // Lose the game
            Console.WriteLine("YYYIIIIEEEE . . . fell in a pit");
            Map.EndGame(false);
        }
    }
}
