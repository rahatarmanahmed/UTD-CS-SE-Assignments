using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework.Input;
using Microsoft.Xna.Framework.Graphics;
using Microsoft.Xna.Framework;

namespace DrillDescent
{
    class Player : Entity
    {
        private const float MOVE_SPEED = 3f;
        private Cooldown ShootBulletCooldown, DamageCooldown;

        public Player(Map map) : base(Art.player, map)
        {
            ShootBulletCooldown = new Cooldown(250, ShootBullet);
            DamageCooldown = new Cooldown (1000, ActuallyTakeDamage);
        }

        public override void Update()
        {
            MouseState mouse = Mouse.GetState();
            Vector2 mouseVector = Util.MouseVector(mouse);
            mouseVector -= Map.Camera.Position;
            
            Rotation = (float)Math.Atan2(mouseVector.Y - Center.Y, mouseVector.X - Center.X);
            if (mouse.LeftButton == ButtonState.Pressed)
                ShootBulletCooldown.Activate();

            KeyboardState kb = Keyboard.GetState();
            Velocity = Vector2.Zero;
            if (kb.IsKeyDown(Keys.W))
                Velocity.Y -= MOVE_SPEED;
            if (kb.IsKeyDown(Keys.S))
                Velocity.Y += MOVE_SPEED;
            if (kb.IsKeyDown(Keys.A))
                Velocity.X -= MOVE_SPEED;
            if (kb.IsKeyDown(Keys.D))
                Velocity.X += MOVE_SPEED;

            base.Update();
        }

        public void ShootBullet(params object[] args)
        {
            Bullet bullet = new Bullet(Map);
            Vector2 rotationVector = Util.AngleToVector(Rotation);
            Vector2 rotationVectorPerp = Util.AngleToVector(Rotation + Math.PI / 2);
            bullet.Rotation = Rotation;
            bullet.Center = Center + rotationVector * 10 + rotationVectorPerp * 4;
            bullet.Velocity = rotationVector * Bullet.SPEED;
            Map.AddEntity(bullet);
        }

        public override void Draw(SpriteBatch spriteBatch)
        {
            Tint = (DamageCooldown.Ready ()) ? Color.White : Color.Red;
            base.Draw(spriteBatch);

            Entity nearestEnemy = null;
            float nearestEnemyDistance = float.MaxValue;
            foreach (Entity e in Map.Entities) {
                if (!(e is Enemy))
                    continue;
                float distance = (e.Center - Center).Length();
                if (distance < nearestEnemyDistance) {
                    nearestEnemy = e;
                    nearestEnemyDistance = distance;
                }
            }
            if (nearestEnemy != null) {
                Vector2 diff = nearestEnemy.Center - Center;
                float angle = (float)Math.Atan2 (diff.Y, diff.X);
                Vector2 indCenter = (Util.AngleToVector (angle) * 20);
                //            spriteBatch.Draw(Sprite, Center, null, Tint, Rotation, Center-Position, Vector2.One, SpriteEffects.None, 0);
                spriteBatch.Draw (Art.bullet,
                    Center + indCenter,
                    null,
                    Color.Red,
                    angle,
                    -new Vector2 (Art.bullet.Width / 2, Art.bullet.Height / 2),
                    Vector2.One,
                    SpriteEffects.None,
                    0);
            }

//            MouseState mouse = Mouse.GetState();
//            Vector2 mouseVector = Util.MouseVector(mouse);
//            mouseVector -= Map.Camera.Position;
//            Util.DrawDebugLine(spriteBatch, Center, mouseVector, Color.Black);
//            Util.DrawDebugLine(spriteBatch, Position, Center, Color.Red);
//            Util.DrawDebugRect(spriteBatch, Position, Sprite.Width, Sprite.Height, Color.Black);
//            Vector2 rotationVector = Util.AngleToVector(Rotation);
//            Vector2 rotationVectorPerp = Util.AngleToVector(Rotation + Math.PI / 2);
//            Util.DrawDebugLine(spriteBatch, Center, Center + rotationVector * 10 + rotationVectorPerp * 4, Color.Green);
        }

        public override void Damage(float damage)
        {
            DamageCooldown.Activate (damage);
        }

        private void ActuallyTakeDamage(params object[] args)
        {
            float damage = (float)args [0];
            Health -= damage;
            Position += 2 * Velocity;
            if (Health <= 0) {
                Map.endGame ();
            }
        }
    }
}
