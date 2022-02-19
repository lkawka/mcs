using Mcs.Models;
using System;
using System.Collections.Generic;
using System.Linq;

namespace Mcs
{
    public class Writer
    {
        private static readonly string TEXT_PADDING = "  ";
        private static readonly string MATRIX_PADDING = "    ";
        private static readonly int SIDE_BY_SIDE_SIZE_LIMIT = 21;
        private static readonly int ADJACENCY_MATRIX_SIZE_LIMIT = 41;

        private static readonly ConsoleColor DEFAULT_COLOR = ConsoleColor.Gray;
        private static readonly List<ConsoleColor> COLORS = new List<ConsoleColor>()
        {
            ConsoleColor.Red,
            ConsoleColor.Green,
            ConsoleColor.Blue,
            ConsoleColor.Yellow,
            ConsoleColor.Cyan,
            ConsoleColor.Magenta,
            ConsoleColor.White,
            ConsoleColor.DarkGreen,
            ConsoleColor.DarkBlue,
            ConsoleColor.DarkYellow,
            ConsoleColor.DarkCyan,
        };

        public static void Write(MinimumCommonSupergraph mcs, bool areGraphsSwapped, long executionTime)
        {
            Graph g1 = areGraphsSwapped ? mcs.Mcs.G2 : mcs.Mcs.G1;
            Graph g2 = areGraphsSwapped ? mcs.Mcs.G1 : mcs.Mcs.G2;
            List<Pair> M = areGraphsSwapped ? mcs.M.Select(pair => new Pair(pair.V2, pair.V1)).ToList() : mcs.M;
            List<List<int>> adjacencyMatrix = mcs.AdjacencyMatrix;

            Console.WriteLine($"{TEXT_PADDING}Number of vertices in the maximum common subgraph: {mcs.Mcs.M.Count}");
            Console.WriteLine($"{TEXT_PADDING}Number of vertices in the minimum common supergraph: {M.Count}");
            Console.WriteLine($"{TEXT_PADDING}Execution time: {executionTime} milliseconds");
            Console.WriteLine();

            Console.WriteLine($"{TEXT_PADDING}Original input graphs:");
            WriteInputGraphs(g1, g2, M);
            Console.WriteLine();

            WriteBothMcs(mcs.Mcs.AdjacencyMatrix, mcs.AdjacencyMatrix);
            Console.WriteLine();

            Console.WriteLine($"{TEXT_PADDING}Vertex mapping table (applies to the maximum common subgraph and the minimum common supergraph, because the former is a subgraph of the latter):");
            WriteMappingTable(M);
            Console.WriteLine();
        }

        private static void WriteInputGraphs(Graph g1, Graph g2, List<Pair> M)
        {
            int n = Math.Max(g1.N, g2.N);
            List<int> v1sToColor = M.Select(pair => pair.V1).ToList();
            List<int> v2sToColor = M.Select(pair => pair.V2).ToList();

            if (n < SIDE_BY_SIDE_SIZE_LIMIT)
            {
                WriteInputGraphsSideBySide("G1", g1.AdjacencyMatrix, "G2", g2.AdjacencyMatrix, v1sToColor, v2sToColor);
            }
            else
            {
                if (g1.N < ADJACENCY_MATRIX_SIZE_LIMIT)
                {
                    WriteAdjacencyMatrix("G1", g1.AdjacencyMatrix, v1sToColor);
                }
                else
                {
                    Console.WriteLine(MATRIX_PADDING + "G1 (adjacency list)");
                    WriteAdjacencyList(g1.AdjacencyMatrix, v1sToColor);
                }
                Console.WriteLine();
                if (g2.N < ADJACENCY_MATRIX_SIZE_LIMIT)
                {
                    WriteAdjacencyMatrix("G2", g2.AdjacencyMatrix, v2sToColor);
                }
                else
                {
                    Console.WriteLine(MATRIX_PADDING + "G2 (adjacency list)");
                    WriteAdjacencyList(g2.AdjacencyMatrix, v2sToColor);
                }
            }
        }

        private static void WriteBothMcs(List<List<int>> adjacencyMatrixSubgraph, List<List<int>> adjacencyMatrixSupergraph)
        {
            List<int> vsToColor = Enumerable.Range(0, adjacencyMatrixSupergraph.Count).ToList();
            if (adjacencyMatrixSupergraph.Count < SIDE_BY_SIDE_SIZE_LIMIT)
            {
                Console.WriteLine($"{TEXT_PADDING}The maximum common subgraph (on the left) and the minimum common supergraph (on the right):");
                WriteInputGraphsSideBySide("mcs", adjacencyMatrixSubgraph, "MCS", adjacencyMatrixSupergraph, vsToColor, vsToColor);
            }
            else
            {
                Console.WriteLine($"{TEXT_PADDING}The maximum common subgraph:");
                if (adjacencyMatrixSubgraph.Count < ADJACENCY_MATRIX_SIZE_LIMIT)
                {
                    WriteAdjacencyMatrix("mcs", adjacencyMatrixSubgraph, vsToColor);
                }
                else
                {
                    Console.WriteLine(MATRIX_PADDING + "(adjacency list)");
                    WriteAdjacencyList(adjacencyMatrixSubgraph, vsToColor);
                }
                Console.WriteLine();
                Console.WriteLine($"{TEXT_PADDING}The minimum common supergraph:");
                if (adjacencyMatrixSupergraph.Count < ADJACENCY_MATRIX_SIZE_LIMIT)
                {
                    WriteAdjacencyMatrix("MCS", adjacencyMatrixSupergraph, vsToColor);
                }
                else
                {
                    Console.WriteLine(MATRIX_PADDING + "(adjacency list)");
                    WriteAdjacencyList(adjacencyMatrixSupergraph, vsToColor);
                }
            }
        }

        private static void WriteMappingTable(List<Pair> M)
        {
            const int cell1 = -7;
            const int cell = -3;
            int width1 = Math.Abs(cell1);
            int width = Math.Abs(cell);
            string spaceBetween = " ";
            int rowLength = (width1 + 1) + 2 * (width + spaceBetween.Length);

            Console.WriteLine($"{MATRIX_PADDING}{"mcs/MCS",cell1}|{spaceBetween}{"G1",cell}|{spaceBetween}{"G2",cell}");

            Console.Write(MATRIX_PADDING);
            for (int i = 0; i < rowLength; i++)
            {
                if ((i == width1) || (i == (width1 + 1 + spaceBetween.Length + width)))
                {
                    Console.Write("+");
                }
                else
                {
                    Console.Write("-");
                }
            }
            Console.Write("\n");

            List<int> v1sToColor = M.Select(pair => pair.V1).ToList();
            List<int> v2sToColor = M.Select(pair => pair.V2).ToList();
            for (int i = 0; i < M.Count; i++)
            {
                string v1 = M[i].V1 == -1 ? "" : M[i].V1.ToString();
                string v2 = M[i].V2 == -1 ? "" : M[i].V2.ToString();
                ConsoleColor color = COLORS[i % COLORS.Count];

                Console.Write(MATRIX_PADDING);
                ColorWrite($"{i,cell1}", color);
                Console.Write("|");
                ColorWrite($" {v1,cell}", color);
                Console.Write("|");
                ColorWrite($" {v2,cell}", color);
                Console.Write("\n");
            }
        }

        private static void WriteAdjacencyMatrix(string name, List<List<int>> adjacencyMatrix, List<int> vsToColor)
        {
            const int cell1 = -3;
            const int cell = -2;
            int width1 = Math.Abs(cell1);
            int width = Math.Abs(cell);
            string leftPadding = MATRIX_PADDING;
            string spaceBetween = " ";
            int n = adjacencyMatrix.Count;
            int rowLength = (width1 + 1) + n * (width + spaceBetween.Length);

            // first 2 rows
            Console.Write($"{leftPadding}{name,cell1}|");
            for (int i = 0; i < n; i++)
            {
                ColorWrite($" {i,cell}", i, vsToColor);
            }
            Console.Write($"\n{leftPadding}");
            for (int i = 0; i < rowLength; i++)
            {
                if (i == width1)
                {
                    Console.Write('+');
                }
                else
                {
                    Console.Write('-');
                }
            }
            Console.Write("\n");

            // next n rows
            for (int i = 0; i < n; i++)
            {
                // first column
                Console.Write($"{leftPadding}");
                ColorWrite($"{i,cell1}", i, vsToColor);
                Console.Write("|");
                // n columns
                for (int j = 0; j < n; j++)
                {
                    Console.Write($"{spaceBetween}{adjacencyMatrix[i][j],cell}");
                }
                Console.Write("\n");
            }
        }

        private static void WriteMcsForBoth(string name, int subgraphSize, List<List<int>> adjacencyMatrix, List<int> vsToColor)
        {
            const int cell1 = -3;
            const int cell = -2;
            int width1 = Math.Abs(cell1);
            int width = Math.Abs(cell);
            string leftPadding = MATRIX_PADDING;
            string spaceBetween = " ";
            int n = adjacencyMatrix.Count;
            int rowLength = (width1 + 1) + n * (width + spaceBetween.Length);

            // first row
            Console.Write($"{leftPadding}{name,cell1}|");
            for (int i = 0; i < n; i++)
            {
                if (i == subgraphSize)
                {
                    Console.Write($"{spaceBetween}|{spaceBetween}");
                }
                ColorWrite($" {i,cell}", i, vsToColor);
            }
            Console.Write($"\n{leftPadding}");
            // second row
            for (int i = 0; i < rowLength; i++)
            {
                if ((width1 + 1) + subgraphSize * (width + spaceBetween.Length) == i)
                {
                    Console.Write("-+-");
                }
                if (i == width1)
                {
                    Console.Write('+');
                }
                else
                {
                    Console.Write('-');
                }
            }
            Console.Write("\n");

            // next n rows
            for (int i = 0; i < n; i++)
            {
                if (i == subgraphSize)
                {
                    Console.Write(leftPadding);
                    for (int j = 0; j < (width1 + 1) + subgraphSize * (width + spaceBetween.Length); j++)
                    {
                        if (j == width1)
                        {
                            Console.Write('+');
                        }
                        else
                        {
                            Console.Write('-');
                        }
                    }
                    Console.Write("-+\n");
                }
                // first column
                Console.Write($"{leftPadding}");
                ColorWrite($"{i,cell1}", i, vsToColor);
                Console.Write("|");
                // n columns
                for (int j = 0; j < n; j++)
                {
                    if (j == subgraphSize)
                    {
                        if (i < subgraphSize)
                        {
                            Console.Write($"{spaceBetween}|{spaceBetween}");
                        }
                        else
                        {
                            Console.Write($"{spaceBetween} {spaceBetween}");
                        }
                    }
                    Console.Write($"{spaceBetween}{adjacencyMatrix[i][j],cell}");
                }
                Console.Write("\n");
            }
        }

        private static void WriteAdjacencyList(List<List<int>> adjacencyMatrix, List<int> vsToColor)
        {
            for (int i = 0; i < adjacencyMatrix.Count; i++)
            {
                Console.Write(MATRIX_PADDING);
                ColorWrite($"{i}", i, vsToColor);
                Console.Write(": ");
                for (int j = 0; j < adjacencyMatrix.Count; j++)
                {
                    if (adjacencyMatrix[i][j] == 1)
                    {
                        ColorWrite($"{j} ", j, vsToColor);
                    }
                }
                Console.Write("\n");
            }
        }

        private static void WriteInputGraphsSideBySide(string name1, List<List<int>> adjacencyMatrix1, string name2, List<List<int>> adjacencyMatrix2, List<int> v1sToColor, List<int> v2sToColor)
        {
            const int cell1 = -3;
            const int cell = -2;
            int width1 = Math.Abs(cell1);
            int width = Math.Abs(cell);
            string leftPadding = MATRIX_PADDING;
            string spaceBetweenRows = " ";
            string spaceBetweenMatrices = "          ";
            int n1 = adjacencyMatrix1.Count;
            int n2 = adjacencyMatrix2.Count;
            int rowLength1 = (width1 + 1) + n1 * (width + spaceBetweenRows.Length);
            int rowLength2 = (width1 + 1) + n2 * (width + spaceBetweenRows.Length);

            // first row
            Console.Write($"{leftPadding}{name1,cell1}|");
            for (int i = 0; i < n1; i++)
            {
                ColorWrite($" {i,cell}", i, v1sToColor);
            }
            Console.Write($"{spaceBetweenMatrices}{name2,cell1}|");
            for (int i = 0; i < n2; i++)
            {
                ColorWrite($" {i,cell}", i, v2sToColor);
            }
            Console.Write($"\n");

            // second row
            Console.Write(leftPadding);
            for (int i = 0; i < rowLength1; i++)
            {
                if (i == width1)
                {
                    Console.Write('+');
                }
                else
                {
                    Console.Write('-');
                }
            }
            Console.Write(spaceBetweenMatrices);
            for (int i = 0; i < rowLength2; i++)
            {
                if (i == width1)
                {
                    Console.Write('+');
                }
                else
                {
                    Console.Write('-');
                }
            }
            Console.Write("\n");

            // next max(n1, n2) rows
            for (int i = 0; i < Math.Max(n1, n2); i++)
            {
                Console.Write(leftPadding);
                // matrix 1
                if (i >= n1)
                {
                    for (int j = 0; j < rowLength1; j++)
                    {
                        Console.Write(" ");
                    }
                }
                else
                {
                    ColorWrite($"{i,cell1}", i, v1sToColor);
                    Console.Write("|");
                    for (int j = 0; j < n1; j++)
                    {
                        Console.Write($"{spaceBetweenRows}{adjacencyMatrix1[i][j],cell}");
                    }
                }

                Console.Write(spaceBetweenMatrices);

                // matrix 2
                if (i < n2)
                {
                    ColorWrite($"{i,cell1}", i, v2sToColor);
                    Console.Write("|");
                    for (int j = 0; j < n2; j++)
                    {
                        Console.Write($"{spaceBetweenRows}{adjacencyMatrix2[i][j],cell}");
                    }
                }

                Console.Write("\n");
            }
        }

        private static void ColorWrite(string s, ConsoleColor color)
        {
            Console.ForegroundColor = color;
            Console.Write(s);
            Console.ForegroundColor = DEFAULT_COLOR;
        }

        private static void ColorWrite(string s, int v, List<int> vsToColor)
        {
            int colorIndex = vsToColor.IndexOf(v);
            if (colorIndex == -1)
            {
                Console.Write(s);
                return;
            }
            Console.ForegroundColor = COLORS[colorIndex % COLORS.Count];
            Console.Write(s);
            Console.ForegroundColor = DEFAULT_COLOR;
        }
    }
}
