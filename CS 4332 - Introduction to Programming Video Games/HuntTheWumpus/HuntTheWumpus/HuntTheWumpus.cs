using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HuntTheWumpus
{
    class HuntTheWumpus
    {
        static Map map;
        static int seed;
        static void Main(string[] args)
        {
            Init();
            while (true)
                Update();

        }

        static void Update()
        {
            map.Update();
            if (!map.playing)
                QueryReplay();
        }

        static void QueryReplay()
        {
            Console.WriteLine("Press 1 to play again, 2 to play with new random locations for hazards, or anything else to quit: ");
            char key = Console.ReadKey(false).KeyChar;
            Console.WriteLine();

            if (key == '1')
                map = new Map(seed);
            else if (key == '2')
                Init();
            else
                Environment.Exit(0);
        }

        static void Init()
        {
            seed = (int)DateTime.Now.Ticks & 0x0000FFFF;
            map = new Map(seed);

            
        }
    }
}
