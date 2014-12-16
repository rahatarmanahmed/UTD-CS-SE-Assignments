using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace DrillDescent
{
    class Cooldown
    {
        public delegate void Action(params object[] args);

        private int CooldownTime;
        private Action CooldownAction;
        private long LastActivated = 0;
        public Cooldown(int cooldownMillis, Action action)
        {
            this.CooldownTime = cooldownMillis;
            this.CooldownAction = action;
        }

        public Boolean Activate(params object[] args)
        {
            long now = DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond;
            if (now - LastActivated >= CooldownTime)
            {
                LastActivated = now;
                CooldownAction(args);
                return true;
            }
            return false;
        }

        public void setCooldown(int millis) {
            CooldownTime = millis;
        }

        public Boolean Ready()
        {
            long now = DateTime.Now.Ticks / TimeSpan.TicksPerMillisecond;
            return (now - LastActivated >= CooldownTime);
        }
    }
}
