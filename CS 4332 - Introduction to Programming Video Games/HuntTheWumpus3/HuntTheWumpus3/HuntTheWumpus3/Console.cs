using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace HuntTheWumpus3
{
    class FakeConsole
    {
        public static List<String> output = new List<String>();

        public static void WriteLine(String str) {
            output.Add(str);
        }

        public static void Write(String str)
        {
            output.Add(str);
        }
    }

}
