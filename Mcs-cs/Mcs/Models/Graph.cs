using System;
using System.Collections.Generic;
using System.Linq;

namespace Mcs.Models
{
    public class Graph
    {
        public readonly int N;
        public readonly List<List<int>> AdjacencyMatrix;

        public Graph(int n, List<List<int>> adjacencyMatrix)
        {
            N = n;
            AdjacencyMatrix = adjacencyMatrix;
        }

        public int DegreeOf(int vertex)
        {
            return AdjacencyMatrix[vertex].Sum();
        }

        public void Validate()
        {
            ValidateSize();
            ValidateUndirected();
            ValidateUnweighted();
            ValidateConnected();
        }

        private void ValidateSize()
        {
            if (AdjacencyMatrix == null)
            {
                throw new SystemException("Adjecency matrix cannot be null!");
            }
            if (AdjacencyMatrix.Count != N)
            {
                throw new SystemException("Adjeceny matrix has a wrong height!");
            }
            if (N < 2)
            {
                throw new SystemException("Graph must have a size of at least 2!");
            }
            for (int i = 0; i < N; i++)
            {
                if (AdjacencyMatrix[i] == null)
                {
                    throw new SystemException("Row " + i + " must not be null!");
                }
                if (AdjacencyMatrix[i].Count != N)
                {
                    throw new SystemException("Row " + i + " of adjeceny matrix has wrong length!");
                }
            }
        }

        private void ValidateUndirected()
        {
            for (int i = 0; i < N; i++)
            {
                if (AdjacencyMatrix[i][i] != 0)
                {
                    throw new SystemException("It is forbidden for vertices to have edges to itself!");
                }
                for (int j = 0; j < N; j++)
                {
                    if (AdjacencyMatrix[i][j] != AdjacencyMatrix[j][i])
                    {
                        throw new SystemException("Adjecency matrix is not symmetric!");
                    }
                }
            }
        }

        private void ValidateUnweighted()
        {
            List<int> allowedValues = new List<int>() { 0, 1 };
            for (int i = 0; i < N; i++)
            {
                for (int j = 0; j < N; j++)
                {
                    if (!allowedValues.Contains(AdjacencyMatrix[i][j]))
                        throw new SystemException("Graph must be unweighted and all values of adjecency matrix must have values of 0 or 1!");
                }
            }
        }

        private void ValidateConnected()
        {
            bool[] visited = new bool[N];
            Dfs(0, visited);
            for (int i = 0; i < N; i++)
            {
                if (!visited[i])
                {
                    throw new SystemException("Graph must be connected!");
                }
            }
        }

        private void Dfs(int v, bool[] visited)
        {
            visited[v] = true;
            for (int i = 0; i < N; i++)
            {
                if (!visited[i] && AdjacencyMatrix[v][i] == 1)
                {
                    Dfs(i, visited);
                }
            }
        }
    }
}
