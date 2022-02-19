using Mcs.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mcs.Algorithms
{
    public class MinimumCommonSupergraphAlgorithm
    {
        private readonly MaximumCommonSubgraphAlgorithm MaximumCommonSubgraphAlgorithm;

        public MinimumCommonSupergraphAlgorithm(MaximumCommonSubgraphAlgorithm maximumCommonSubgraphAlgorithm)
        {
            MaximumCommonSubgraphAlgorithm = maximumCommonSubgraphAlgorithm;
        }

        public MinimumCommonSupergraph Compute(Graph g1, Graph g2)
        {
            MaximumCommonSubgraph mcs = MaximumCommonSubgraphAlgorithm.Compute(g1, g2);

            int n1 = mcs.M.Count;
            int n2 = g1.N - n1;
            int n3 = g2.N - n1;
            int n = n1 + n2 + n3;
            List<Pair> M = new List<Pair>(mcs.M);
            List<List<int>> adjacencyMatrix = new List<List<int>>(n);

            for (int i = 0; i < n; i++)
            {
                adjacencyMatrix.Add(new List<int>(n));
                for (int j = 0; j < n; j++)
                {
                    adjacencyMatrix[i].Add(0);
                }
            }

            for (int i = 0; i < n1; i++)
            {
                for (int j = 0; j < n1; j++)
                {
                    adjacencyMatrix[i][j] = mcs.AdjacencyMatrix[i][j];
                }
            }

            List<int> g1MinusMcs = mcs.VerticesFromG1NotInMcs();
            List<Pair> g1MinusMcsP = g1MinusMcs.ConvertAll(x => new Pair(x, -1));
            M.AddRange(g1MinusMcsP);

            // Add edges from g1-mcs to mcs
            for (int g1MinusMcsIndex = 0; g1MinusMcsIndex < g1MinusMcs.Count; g1MinusMcsIndex++)
            {
                for (int mcsIndex = 0; mcsIndex < mcs.M.Count; mcsIndex++)
                {
                    int mcsIndexAsG1Index = mcs.M[mcsIndex].V1;
                    if (g1.AdjacencyMatrix[g1MinusMcs[g1MinusMcsIndex]][mcsIndexAsG1Index] == 1)
                    {
                        int g1MinusMcsIndexAsMIndex = n1 + g1MinusMcsIndex;
                        adjacencyMatrix[mcsIndex][g1MinusMcsIndexAsMIndex] = 1;
                        adjacencyMatrix[g1MinusMcsIndexAsMIndex][mcsIndex] = 1;
                    }
                }
            }

            // Add edges from g1-mcs (between each other)
            for (int i = 0; i < g1MinusMcs.Count; i++)
            {
                for (int j = i + 1; j < g1MinusMcs.Count; j++)
                {
                    if (g1.AdjacencyMatrix[g1MinusMcs[i]][g1MinusMcs[j]] == 1)
                    {
                        adjacencyMatrix[n1 + i][n1 + j] = 1;
                        adjacencyMatrix[n1 + j][n1 + i] = 1;
                    }
                }
            }

            List<int> g2MinusMcs = mcs.VerticesFromG2NotInMcs();
            List<Pair> g2MinusMcsP = g2MinusMcs.ConvertAll(x => new Pair(-1, x));
            M.AddRange(g2MinusMcsP);

            // Add edges from g2-mcs to mcs
            for (int g2MinusMcsIndex = 0; g2MinusMcsIndex < g2MinusMcs.Count; g2MinusMcsIndex++)
            {
                for (int mcsIndex = 0; mcsIndex < mcs.M.Count; mcsIndex++)
                {
                    int mcsIndexAsG2Index = mcs.M[mcsIndex].V2;
                    if (g2.AdjacencyMatrix[g2MinusMcs[g2MinusMcsIndex]][mcsIndexAsG2Index] == 1)
                    {
                        int g2MinusMcsIndexAsMIndex = n1 + n2 + g2MinusMcsIndex;
                        adjacencyMatrix[mcsIndex][g2MinusMcsIndexAsMIndex] = 1;
                        adjacencyMatrix[g2MinusMcsIndexAsMIndex][mcsIndex] = 1;
                    }
                }
            }

            // Add edges from g2-mcs (between each other)
            for (int i = 0; i < g2MinusMcs.Count; i++)
            {
                for (int j = i + 1; j < g2MinusMcs.Count; j++)
                {
                    if (g2.AdjacencyMatrix[g2MinusMcs[i]][g2MinusMcs[j]] == 1)
                    {
                        adjacencyMatrix[n1 + n2 + i][n1 + n2 + j] = 1;
                        adjacencyMatrix[n1 + n2 + j][n1 + n2 + i] = 1;
                    }
                }
            }

            return new MinimumCommonSupergraph(mcs, M, adjacencyMatrix);
        }
    }
}
