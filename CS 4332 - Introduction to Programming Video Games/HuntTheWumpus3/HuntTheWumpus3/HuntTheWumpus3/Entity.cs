using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Microsoft.Xna.Framework;

namespace HuntTheWumpus3
{
    // A class to generalize "Entities" on the map
    abstract class Entity
    {
        protected Map Map;
        public int Position;
        public Vector3 PositionCoords
        {
            get
            {
                return new Vector3(Map.roomPositions[Position], 0);
            }
        }
        public Entity(Map Map, int Position)
        {
            this.Map = Map;
            this.Position = Position;
        }

        public abstract void Update();

        public void Move(int room)
        {
            Position = room;
        }

        public bool isAdjacentToPlayer()
        {
            var adjRooms = Map.getAdjacentRooms(Map.Player.Position);
                if (adjRooms.Contains(Position))
                    return true;
            return false;
        }

        // If the entity is an enemy, this is called
        // when it is in the same room as the player
        public virtual void AffectPlayer()
        {

        }
    }
}
