using System;

namespace DrillDescent
{
#if WINDOWS || XBOX
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        static void Main(string[] args)
        {
            using (MacGame game = new MacGame())
            {
                game.Run();
            }
        }
    }
#endif
}

