using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace DrillDescent
{
    class Stairs : Entity
    {
        public Stairs(Map map) : base(Art.stairsdown, map)
        {
        }
            
        protected override void OnCollideEntity(Entity entity, Vector2 depth) {
            if (entity is Player && Map.numEnemies == 0) {
                Map.Game.NextLevel (entity.Health);
            }
        }
    }
}
