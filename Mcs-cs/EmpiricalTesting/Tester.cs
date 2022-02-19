using Mcs.Algorithms;
using Mcs.Models;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using static EmpiricalTesting.Result;

namespace EmpiricalTesting
{
    public class Tester
    {
        private static readonly int N_INVOCATIONS = 5;
        private static readonly Stopwatch STOPWATCH = new Stopwatch();

        public static Dictionary<int, Result> Evaluate(List<int> sizesForBoth, List<int> sizesForApproximation, Func<int, Input> reader)
        {
            Dictionary<int, Result> results = new Dictionary<int, Result>();

            foreach (int n in sizesForBoth)
            {
                Console.Write($" {n}");
                Input input = reader.Invoke(n);
                Stats exact = Test(AlgorithmType.MC_SPLIT, input);
                Stats approximation = Test(AlgorithmType.GENETIC, input);
                results.Add(n, new Result(exact, approximation));
            }
            foreach (int n in sizesForApproximation)
            {
                Console.Write($" {n}");
                Input input = reader.Invoke(n);
                Stats approximation = Test(AlgorithmType.GENETIC, input);
                results.Add(n, new Result(null, approximation));
            }
            Console.Write($"\n");
            return results;
        }

        private static Stats Test(AlgorithmType algorithmType, Input input)
        {
            List<long> times = new List<long>(N_INVOCATIONS);
            int nMaximumCommonSubgraph = -1;
            int nMinimumCommonSupergraph = -1;
            for (int i = 0; i < N_INVOCATIONS; i++)
            {
                MinimumCommonSupergraphAlgorithm algo = GetAlgorithm(algorithmType);
                STOPWATCH.Restart();
                MinimumCommonSupergraph supergraph = algo.Compute(input.G1, input.G2);
                STOPWATCH.Stop();
                times.Add(STOPWATCH.ElapsedMilliseconds);
                nMaximumCommonSubgraph = supergraph.Mcs.M.Count;
                nMinimumCommonSupergraph = supergraph.M.Count;
            }
            long avgTime = times.Sum() / N_INVOCATIONS;
            return new Stats(avgTime, nMaximumCommonSubgraph, nMinimumCommonSupergraph);
        }

        private static MinimumCommonSupergraphAlgorithm GetAlgorithm(AlgorithmType algorithmType)
        {
            MaximumCommonSubgraphAlgorithm subgraphAlgo;
            if (algorithmType == AlgorithmType.MC_SPLIT)
            {
                subgraphAlgo = new McSplit();
            }
            else
            {
                subgraphAlgo = Genetic.FromConfiguration(GeneticConfiguration.FromPaper());
            }
            return new MinimumCommonSupergraphAlgorithm(subgraphAlgo);
        }
    }
}
