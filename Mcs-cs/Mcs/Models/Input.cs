using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mcs.Models
{
    public class Input
    {
        public readonly Graph G1;
        public readonly Graph G2;

        public Input(Graph g1, Graph g2)
        {
            G1 = g1;
            G2 = g2;
        }
    }
}
