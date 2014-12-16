using System;
using Microsoft.Xna.Framework;

namespace DrillDescent {
    class Enemy : Entity {
        private const float SPEED = 1.0f;
        private const float DAMAGE = 20f;
        private const int COOLDOWN_MIN = 250;
        private const int COOLDOWN_MAX = 2000;
        private const float CHANCE_STOP = 0.2f;

        Cooldown updateWanderCooldown;
//        Vector2 wanderDir;

        public Enemy (Map map) : base(Art.enemy, map){
            updateWanderCooldown = new Cooldown (1000, updateWander);
        }

        public override void Update()
        {
            updateWanderCooldown.Activate ();
            updateWanderMicro ();

            base.Update();
        }

        private void updateWander(params object[] args)
        {
            if (Visible) {
                Velocity = (Map.Player.Center - Center);
                Velocity.Normalize ();
                Velocity *= SPEED;
            } else {
                if (Util.Rand.NextDouble() < CHANCE_STOP) {
                    Velocity = Vector2.Zero;
                } else {
                    double randAngle = Util.Rand.Next (360) * Math.PI / 180;
                    Velocity = new Vector2 (
                        (float) Math.Cos (randAngle) * SPEED,
                        (float) Math.Sin (randAngle) * SPEED);
                }
            }

                
            updateWanderCooldown.setCooldown(Util.RandRange(COOLDOWN_MIN, COOLDOWN_MAX));
        }

        private void updateWanderMicro()
        {
            int microRotateAngle = Util.RandRange (-5, 6);
            Velocity = Util.rotateDeg (Velocity, microRotateAngle);
        }

        protected override void OnCollideMap(Tile tile, Vector2 depth)
        {
            base.OnCollideMap (tile, depth);
            Velocity *= -1;          
        }

        protected override void OnCollideEntity(Entity entity, Vector2 depth)
        {
            base.OnCollideEntity (entity, depth);
            if (entity is Player)
                entity.Damage (DAMAGE);
        }
    }
}

