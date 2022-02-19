using Mcs.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mcs.Readers
{
    public class InputReader
    {
        public static Input Read(string inputFilePath)
        {
            using (StreamReader reader = new StreamReader(inputFilePath))
            {
                Graph g1 = ReadGraph(reader);
                Graph g2 = ReadGraph(reader);
                return new Input(g1, g2);
            };
        }

        private static Graph ReadGraph(StreamReader reader)
        {
            if (!int.TryParse(reader.ReadLine(), out int n))
            {
                throw new SystemException("Unable to read size of the graph!");
            }
            List<List<int>> adjacentMatrix = new List<List<int>>();
            for (int i = 0; i < n; i++)
            {
                adjacentMatrix.Add(new List<int>());
                string[] inputs = (reader.ReadLine() ?? "").Trim(' ').Split();
                if (inputs.Length != n)
                {
                    throw new SystemException("Row " + i + " of adjacency matrix has " + inputs.Length + " items instead of " + n);
                }
                for (int j = 0; j < n; j++)
                {
                    if (!int.TryParse(inputs[j], out int value))
                    {
                        throw new SystemException("Unable to read AdjacencyMatrix[" + i + "][" + j + "] item!");
                    }
                    adjacentMatrix[i].Add(value);
                }
            }
            return new Graph(n, adjacentMatrix);
        }
    }
}
