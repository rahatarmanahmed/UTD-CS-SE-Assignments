using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace DrillDescent
{
    class Bullet : Entity
    {
        private const int DAMAGE = 50;
        public const int SPEED = 5;
        public Bullet(Map map) : base(Art.bullet, map)
        {
        }

        protected override void OnCollideMap(Tile tile, Vector2 depth)
        {
            tile.Durability -= DAMAGE;
            MarkedForDeletion = true;
        }

        protected override void OnCollideEntity(Entity entity, Vector2 depth) {
            if (entity is Enemy) {
                entity.Damage (DAMAGE);
                MarkedForDeletion = true;
            }
        }
    }
}
