using System.Collections.Generic;
using System.Linq;

namespace Mcs.Models
{
    public class MaximumCommonSubgraph
    {
        public readonly Graph G1;
        public readonly Graph G2;
        /// <summary>
        /// List of vertex mappings to maximum common subgraph. For every pair, V1 is
        /// form G1 and V2 is from G2. List is sorted by V1.
        /// </summary>
        public readonly List<Pair> M;
        public readonly List<List<int>> AdjacencyMatrix;
        public readonly List<int> BestGenome;

        public MaximumCommonSubgraph(Graph g1, Graph g2, List<Pair> m, List<int> bestGenome)
        {
            G1 = g1;
            G2 = g2;
            M = m.OrderBy(pair => pair.V1).ToList();
            BestGenome = bestGenome;

            // Build adjacency matrix
            int n = M.Count;
            AdjacencyMatrix = new List<List<int>>();
            for (int i = 0; i < n; i++)
            {
                AdjacencyMatrix.Add(new List<int>());
                for (int j = 0; j < n; j++)
                {
                    AdjacencyMatrix[i].Add(G1.AdjacencyMatrix[M[i].V1][M[j].V1]);
                }
            }
        }

        public MaximumCommonSubgraph(Graph g1, Graph g2, List<Pair> M) : this(g1, g2, M, null) { }

        /// <returns>Sorted list of vertices from G1 not in the maximum common subgraph</returns>
        public List<int> VerticesFromG1NotInMcs()
        {
            List<int> v1s = M.Select(pair => pair.V1).ToList();
            return Enumerable.Range(0, G1.N).Except(v1s).ToList();
        }

        /// <returns>Sorted list of vertices from G2 not in the maximum common subgraph</returns>
        public List<int> VerticesFromG2NotInMcs()
        {
            List<int> v2s = M.Select(pair => pair.V2).OrderBy(v2 => v2).ToList();
            return Enumerable.Range(0, G2.N).Except(v2s).ToList();
        }

        public bool FromGenetic()
        {
            return BestGenome != null;
        }
    }
}
