using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mcs.Models
{
    public class MinimumCommonSupergraph
    {
        public readonly MaximumCommonSubgraph Mcs;
        /// <summary>
        /// List of vertex mappings to minimum common supergraph. For every pair, V1 is
        /// form G1 and V2 is from G2.
        /// </summary>
        public readonly List<Pair> M;
        public readonly List<List<int>> AdjacencyMatrix;

        public MinimumCommonSupergraph(MaximumCommonSubgraph mcs, List<Pair> m, List<List<int>> adjacencyMatrix)
        {
            Mcs = mcs;
            M = m;
            AdjacencyMatrix = adjacencyMatrix;
        }
    }
}
