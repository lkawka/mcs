using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mcs.Models
{
    public class Pair
    {
        /// <summary>
        /// Vertex from G1
        /// </summary>
        public readonly int V1;
        /// <summary>
        /// Vertex from G2
        /// </summary>
        public readonly int V2;

        public Pair(int v1, int v2)
        {
            V1 = v1;
            V2 = v2;
        }
    }
}
