using Mcs.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mcs.Algorithms
{
    public class McSplit : MaximumCommonSubgraphAlgorithm
    {
        private Graph G1;
        private Graph G2;
        private List<Pair> BestM;

        public MaximumCommonSubgraph Compute(Graph g1, Graph g2)
        {
            G1 = g1;
            G2 = g2;
            BestM = new List<Pair>();
            Search(LabelClasses.From(g1, g2), new List<Pair>());
            return new MaximumCommonSubgraph(g1, g2, BestM);
        }

        private void Search(LabelClasses labelClasses, List<Pair> M)
        {
            if (M.Count > BestM.Count)
            {
                BestM = new List<Pair>(M);
            }

            int bound = M.Count + labelClasses.SumPairsOfVertices();
            if (bound <= BestM.Count)
            {
                return;
            }

            LabelClass selectedLabelClass = labelClasses.Select();
            int v = selectedLabelClass.SelectVertexFromG();
            foreach (int w in selectedLabelClass.H)
            {
                LabelClasses newLabelClasses = LabelClasses.Empty();
                for (int i = 0; i < labelClasses.Count; i++)
                {
                    List<int> neighborsOfV = labelClasses[i].NeighborsFromGOf(v);
                    List<int> neighborsOfW = labelClasses[i].NeighborsFromHOf(w);
                    if (neighborsOfV.Any() && neighborsOfW.Any())
                    {
                        newLabelClasses.Add(new LabelClass(G1, G2, neighborsOfV, neighborsOfW));
                    }
                    List<int> notNeighborsOfV = labelClasses[i].NotNeighborsFromGOf(v);
                    List<int> notNeighborsOfW = labelClasses[i].NotNeighborsFromHOf(w);
                    if (notNeighborsOfV.Any() && notNeighborsOfW.Any())
                    {
                        newLabelClasses.Add(new LabelClass(G1, G2, notNeighborsOfV, notNeighborsOfW));
                    }
                }
                M.Add(new Pair(v, w));
                Search(newLabelClasses, M);
                M.RemoveAt(M.Count - 1);
            }
            labelClasses.Remove(selectedLabelClass);
            if (selectedLabelClass.G.Count > 1)
            {
                labelClasses.Add(selectedLabelClass.CopyWithoutVertexFromG(v));
            }
            if (labelClasses.Count > 0)
            {
                Search(labelClasses, M);
            }
        }

        class LabelClass
        {
            private readonly Graph G1;
            private readonly Graph G2;

            public readonly List<int> G;
            public readonly List<int> H;
            public readonly int? MaxDegreeG;
            public readonly int? MaxDegreeVertexG;

            public LabelClass(Graph g1, Graph g2, List<int> G, List<int> H)
            {
                G1 = g1;
                G2 = g2;
                this.G = G;
                this.H = H;

                if (G.Count == 0)
                {
                    MaxDegreeG = null;
                    MaxDegreeVertexG = null;
                }
                else
                {
                    int maxDegreeG = g1.DegreeOf(G[0]);
                    int maxDegreeVertexG = G[0];
                    for (int i = 1; i < G.Count; i++)
                    {
                        int degree = g1.DegreeOf(G[i]);
                        if (degree > maxDegreeG)
                        {
                            maxDegreeG = degree;
                            maxDegreeVertexG = G[i];
                        }
                    }
                    MaxDegreeG = maxDegreeG;
                    MaxDegreeVertexG = maxDegreeVertexG;
                }
            }

            public LabelClass CopyWithoutVertexFromG(int vertex)
            {
                return new LabelClass(G1, G2, G.Where(v => v != vertex).ToList(), H);
            }

            public int SelectVertexFromG()
            {
                if (MaxDegreeVertexG == null)
                {
                    throw new SystemException("No vertices to select from in G!");
                }
                return (int)MaxDegreeVertexG;
            }

            public List<int> NeighborsFromGOf(int vertex)
            {
                return G.Where(v => G1.AdjacencyMatrix[vertex][v] == 1).ToList();
            }

            public List<int> NeighborsFromHOf(int vertex)
            {
                return H.Where(v => G2.AdjacencyMatrix[vertex][v] == 1).ToList();
            }

            public List<int> NotNeighborsFromGOf(int vertex)
            {
                return G.Where(v => v != vertex && G1.AdjacencyMatrix[vertex][v] == 0).ToList();
            }

            public List<int> NotNeighborsFromHOf(int vertex)
            {
                return H.Where(v => v != vertex && G2.AdjacencyMatrix[vertex][v] == 0).ToList();
            }

            public override string ToString()
            {
                return base.ToString();
            }
        }

        class LabelClasses
        {
            private readonly List<LabelClass> Items;

            public LabelClasses(List<LabelClass> labelClasses)
            {
                Items = labelClasses;
            }

            public static LabelClasses Empty()
            {
                return new LabelClasses(new List<LabelClass>());
            }

            public static LabelClasses From(Graph g1, Graph g2)
            {
                List<int> G = Enumerable.Range(0, g1.N).ToList();
                List<int> H = Enumerable.Range(0, g2.N).ToList();

                return new LabelClasses(new List<LabelClass>() { new LabelClass(g1, g2, G, H) });
            }

            public LabelClass this[int index]
            {
                get
                {
                    return Items[index];
                }
            }

            public int Count
            {
                get
                {
                    return Items.Count;
                }
            }

            public void Add(LabelClass labelClass)
            {
                Items.Add(labelClass);
            }

            public void Remove(LabelClass labelClass)
            {
                if (!Items.Remove(labelClass))
                {
                    throw new SystemException("Unable to remove label class!");
                }
            }

            public int SumPairsOfVertices()
            {
                return Items.Select(labelClass => Math.Min(labelClass.G.Count, labelClass.H.Count)).Sum();
            }

            /// <summary>
            ///  Selects a label class with the smallest max(|G|,|H|) and breaks ties by
            ///  selecting a class containing a vertex in G with the largest degree
            /// </summary>
            public LabelClass Select()
            {
                LabelClass best = Items[0];
                int maxVerticesBest = Math.Max(best.G.Count, best.H.Count);
                foreach (LabelClass labelClass in Items)
                {
                    int maxVerticesLabelClass = Math.Max(labelClass.G.Count, labelClass.H.Count);
                    if (maxVerticesBest < maxVerticesLabelClass)
                    {
                        best = labelClass;
                        maxVerticesBest = maxVerticesLabelClass;
                    }
                    else if (maxVerticesLabelClass == maxVerticesBest)
                    {
                        if (best.MaxDegreeG < labelClass.MaxDegreeG)
                        {
                            best = labelClass;
                            maxVerticesBest = maxVerticesLabelClass;
                        }
                    }
                }
                return best;
            }

            public override string ToString()
            {
                return base.ToString();
            }
        }

    }
}

