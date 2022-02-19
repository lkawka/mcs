using Mcs.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mcs.Algorithms
{
    public class Genetic : MaximumCommonSubgraphAlgorithm
    {
        private static readonly int SEED = 13;
        private static readonly Random RANDOM = new Random(SEED);

        private readonly int NGenomes;
        private readonly int NGenerations;
        private readonly int NCb;
        private readonly int NTribes;
        private readonly double PCrossover;
        private readonly double PMutation;

        public Genetic(int nGenomes, int nGenerations, int nCb, int nTribes, double pCrossover, double pMutation)
        {
            if (nGenomes % nTribes != 0)
            {
                throw new SystemException("Number of genomes must be devisable by the number of tribes!");
            }

            NGenomes = nGenomes;
            NGenerations = nGenerations;
            NCb = nCb;
            NTribes = nTribes;
            PCrossover = pCrossover;
            PMutation = pMutation;
        }

        public static Genetic FromConfiguration(GeneticConfiguration conf)
        {
            return new Genetic(conf.NGenomes, conf.NGenerations, conf.NCb, conf.NTribes, conf.PCrossover, conf.PMutation);
        }

        public MaximumCommonSubgraph Compute(Graph g1, Graph g2)
        {
            if (g1.N > g2.N)
            {
                throw new SystemException("Graph 1 must not be bigger than graph 2!");
            }

            List<List<int>> D1 = CreateDistanceMatrix(g1);
            List<List<int>> D2 = CreateDistanceMatrix(g2);
            Population population = Population.Generate(NGenomes, g2.N, NTribes, D1, D2);
            Genome bestGenome = population.Genomes.OrderBy(g => g.Score).Last();

            for (int generation = 0; generation < NGenerations; generation++)
            {
                // Shuffling
                if (generation % NCb == 0)
                {
                    population.ShuffleAll();
                }
                else
                {
                    population.ShuffleWithinTribe();
                }

                // Crossbreading
                for (int tribe = 0; tribe < NTribes; tribe++)
                {
                    for (int i = 1; i < population.TribeSize; i += 2)
                    {
                        if (RANDOM.NextDouble() > PCrossover)
                        {
                            continue;
                        }
                        int tribeStart = tribe * population.TribeSize;
                        int index1 = tribeStart + i - 1;
                        int index2 = tribeStart + i;

                        int indexWithSmallerScore = index1;
                        int indexWithBiggerScore = index2;
                        if (population[index1].Score > population[index2].Score)
                        {
                            indexWithSmallerScore = index2;
                            indexWithBiggerScore = index1;
                        }

                        List<Genome> childeren = population[index1]
                            .Crossover(population[index2])
                            .OrderBy(g => g.Score)
                            .ToList();

                        population[indexWithSmallerScore] = childeren[childeren.Count - 2];
                        population[indexWithBiggerScore] = childeren[childeren.Count - 1];

                        if (bestGenome.Score < population[indexWithBiggerScore].Score)
                        {
                            bestGenome = population[indexWithBiggerScore].Copy();
                        }
                    }
                }

                // Mutating
                foreach (Genome genome in population.Genomes)
                {
                    if (RANDOM.NextDouble() <= PMutation)
                    {
                        genome.Mutate();
                        if (bestGenome.Score < genome.Score)
                        {
                            bestGenome = genome.Copy();
                        }
                    }
                }
            }

            List<Pair> M = ReductionFunction(D1, D2, bestGenome.Genes);
            return new MaximumCommonSubgraph(g1, g2, M, bestGenome.Genes);
        }

        static List<List<int>> CreateDistanceMatrix(Graph g)
        {
            List<List<int>> D = new List<List<int>>(g.N);
            List<int> explored = new List<int>(g.N);
            for (int i = 0; i < g.N; i++)
            {
                explored.Add(0);
                D.Add(new List<int>(g.N));
                for (int j = 0; j < g.N; j++)
                {
                    D[i].Add(g.N - 1);
                }
                D[i][i] = 0;
            }

            for (int i = 0; i < g.N; i++)
            {
                // BFS
                Queue<int> queue = new Queue<int>();
                explored[i] = i + 1;
                queue.Enqueue(i);
                while (queue.Any())
                {
                    int v = queue.Dequeue();
                    for (int w = 0; w < g.N; w++)
                    {
                        if (v == w || g.AdjacencyMatrix[v][w] == 0 || explored[w] == i + 1)
                        {
                            continue;
                        }
                        explored[w] = i + 1;
                        int newDistance = Math.Min(D[i][w], D[i][v] + 1);
                        D[i][w] = newDistance;
                        D[w][i] = newDistance;
                        queue.Enqueue(w);
                    }
                }
            }

            return D;
        }

        static int FitnessFunction(List<List<int>> d1, List<List<int>> d2, List<int> genes)
        {
            int n1 = d1.Count;
            int sum = 0;
            for (int i = 0; i < n1; i++)
            {
                for (int j = 0; j < n1; j++)
                {
                    sum += Math.Abs(d1[i][j] - d2[genes[i]][genes[j]]);
                }
            }
            return sum;
        }

        static List<Pair> ReductionFunction(List<List<int>> d1, List<List<int>> d2, List<int> genes)
        {
            int n1 = d1.Count;
            List<int> skipped = NCopies(n1, -1);
            List<Pair> M = new List<Pair>();
            for (int skip = 0; skip < n1 - 1; skip++)
            {
                for (int i = 0; i < skip; i++)
                {
                    skipped[i] = skip;
                }
                for (int i = skip; i < n1; i++)
                {
                    int tmpSum = 0;
                    for (int j = skip; j <= i; j++)
                    {
                        if (skipped[j] == skip)
                        {
                            continue;
                        }
                        for (int k = skip; k <= i; k++)
                        {
                            if (skipped[k] == skip)
                            {
                                continue;
                            }
                            tmpSum += Math.Abs(d1[j][k] - d2[genes[j]][genes[k]]);
                        }
                    }
                    if (tmpSum > 0)
                    {
                        skipped[i] = skip;
                    }
                }
                List<Pair> Mcandidate = new List<Pair>();
                for (int i = 0; i < n1; i++)
                {
                    if (skipped[i] < skip)
                    {
                        Mcandidate.Add(new Pair(i, genes[i]));
                    }
                }
                if (M.Count < Mcandidate.Count)
                {
                    M = Mcandidate;
                }
            }
            return M;
        }

        class Genome
        {
            public readonly List<int> Genes;
            public int Score;
            public readonly List<List<int>> D1;
            public readonly List<List<int>> D2;

            public Genome(List<int> genes, List<List<int>> d1, List<List<int>> d2, int score)
            {
                Genes = new List<int>(genes);
                D1 = d1;
                D2 = d2;
                Score = score;
            }

            public static Genome Create(List<int> genes, List<List<int>> d1, List<List<int>> d2)
            {
                int score = FitnessFunction(d1, d2, genes);
                return new Genome(genes, d1, d2, score);
            }

            public void Mutate()
            {
                int index1 = RANDOM.Next(Genes.Count);
                int index2 = RANDOM.Next(Genes.Count - 1);
                if (index1 == index2)
                {
                    index2 += 1;
                }
                int oldScore = Score;

                Swap(Genes, index1, index2);
                RecalculateScore();
                if (Score < oldScore)
                {
                    // Undo swap
                    Swap(Genes, index1, index2);
                    Score = oldScore;
                }
            }

            /// <summary>
            /// Crossovers with otherParent
            /// </summary>
            /// <param name="otherParent"></param>
            /// <returns>List of four genomes: two parents and two children</returns>
            public List<Genome> Crossover(Genome otherParent)
            {
                int nGenes = Genes.Count;
                List<int> childGenes1 = NCopies(nGenes, -1);
                List<int> childGenes2 = NCopies(nGenes, -1);

                int nCycle = 0;
                for (int i = 0; i < nGenes; i++)
                {
                    int j = i;
                    bool newCycle = false;
                    while (childGenes1[j] < 0)
                    {
                        newCycle = true;
                        if (nCycle % 2 == 0)
                        {
                            childGenes1[j] = Genes[j];
                            childGenes2[j] = otherParent.Genes[j];
                        }
                        else
                        {
                            childGenes1[j] = otherParent.Genes[j];
                            childGenes2[j] = Genes[j];
                        }
                        j = Genes.IndexOf(otherParent.Genes[j]);
                    }
                    if (newCycle)
                    {
                        nCycle += 1;
                    }
                }

                return new List<Genome>() {
                    this,
                    otherParent,
                    Genome.Create(childGenes1, D1, D2),
                    Genome.Create(childGenes2, D1, D2) };
            }

            public Genome Copy()
            {
                return new Genome(Genes, D1, D2, Score);
            }

            private void RecalculateScore()
            {
                Score = FitnessFunction(D1, D2, Genes);
            }
        }

        class Population
        {
            public readonly List<Genome> Genomes;
            public readonly int NTribes;
            public readonly int TribeSize;

            public Population(List<Genome> genomes, int nTribes)
            {
                Genomes = new List<Genome>(genomes);
                NTribes = nTribes;
                TribeSize = genomes.Count / nTribes;

            }

            public static Population Generate(int nGenomes, int genomeSize, int nTribes, List<List<int>> d1, List<List<int>> d2)
            {
                List<Genome> genomes = new List<Genome>(nGenomes);

                for (int i = 0; i < nGenomes; i++)
                {
                    List<int> genes = new List<int>(genomeSize);
                    for (int j = 0; j < genomeSize; j++)
                    {
                        genes.Add(j);
                    }
                    Shuffle(genes);
                    genomes.Add(Genome.Create(genes, d1, d2));
                }
                return new Population(genomes, nTribes);
            }

            public Genome this[int index]
            {
                get => Genomes[index];
                set => Genomes[index] = value;
            }

            public void ShuffleAll()
            {
                Shuffle(Genomes);
            }

            public void ShuffleWithinTribe()
            {
                for (int i = 0; i < NTribes; i++)
                {
                    Shuffle(Genomes, i * TribeSize, (i + 1) * TribeSize);
                }
            }
        }

        private static void Shuffle<T>(IList<T> list, int start, int end)
        {
            int n = end - start;
            while (n > 1)
            {
                n--;
                int k = RANDOM.Next(n + 1);
                T value = list[start + k];
                list[start + k] = list[start + n];
                list[start + n] = value;
            }
        }

        private static void Shuffle<T>(IList<T> list)
        {
            Shuffle(list, 0, list.Count);
        }

        private static void Swap<T>(IList<T> list, int indexA, int indexB)
        {
            T tmp = list[indexA];
            list[indexA] = list[indexB];
            list[indexB] = tmp;
        }

        private static List<T> NCopies<T>(int n, T item)
        {
            List<T> list = new List<T>(n);
            for (int i = 0; i < n; i++)
            {
                list.Add(item);
            }
            return list;
        }
    }
}
