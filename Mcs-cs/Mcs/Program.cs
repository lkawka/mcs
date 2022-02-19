using Mcs.Algorithms;
using Mcs.Models;
using Mcs.Readers;
using System;
using System.Diagnostics;

namespace Mcs
{
    public class Program
    {
        private static readonly Stopwatch STOPWATCH = new Stopwatch();

        static void Main(string[] args)
        {
            Console.Title = "Mcs";

            RunOnce();
            while (ConfigurationReader.KeepRunning())
            {
                RunOnce();
            }

            Console.Write("Press any key to close the window...");
            Console.ReadKey();
        }

        static void RunOnce()
        {
            try
            {
                Configuration configuration = ConfigurationReader.Read();

                Input input = InputReader.Read(configuration.InputFilePath);
                input.G1.Validate();
                Console.WriteLine("Graph 1 validated!");
                input.G2.Validate();
                Console.WriteLine("Graph 2 validated!");
                Console.WriteLine();

                // Genetic algorithm requires that |g1| <= |g2|,
                // so if |g1| > |g2| then we need to swap them
                Graph g1;
                Graph g2;
                bool areGraphsSwapped;
                if (input.G1.N > input.G2.N)
                {
                    g1 = input.G2;
                    g2 = input.G1;
                    areGraphsSwapped = true;
                }
                else
                {
                    g1 = input.G1;
                    g2 = input.G2;
                    areGraphsSwapped = false;
                }

                if (configuration.AlgorithmType == AlgorithmType.MC_SPLIT || configuration.AlgorithmType == AlgorithmType.BOTH)
                {
                    Console.Write("Processing results for the exact (McSplit) algorithm...\n\n");
                    MinimumCommonSupergraphAlgorithm algorithm = new MinimumCommonSupergraphAlgorithm(new McSplit());
                    STOPWATCH.Restart();
                    MinimumCommonSupergraph minimumCommonSupergraph = algorithm.Compute(g1, g2);
                    STOPWATCH.Stop();
                    Console.WriteLine("Results for the exact (McSplit) algorithm:");
                    Writer.Write(minimumCommonSupergraph, areGraphsSwapped, STOPWATCH.ElapsedMilliseconds);
                }

                if (configuration.AlgorithmType == AlgorithmType.GENETIC || configuration.AlgorithmType == AlgorithmType.BOTH)
                {
                    Console.Write("Processing results for the approximate (Genetic) algorithm...\n\n");
                    MinimumCommonSupergraphAlgorithm algorithm = new MinimumCommonSupergraphAlgorithm(Genetic.FromConfiguration(configuration.GeneticConfiguration));
                    STOPWATCH.Restart();
                    MinimumCommonSupergraph minimumCommonSupergraph = algorithm.Compute(g1, g2);
                    STOPWATCH.Stop();
                    Console.WriteLine("Results for the approximate (Genetic) algorithm:");
                    Writer.Write(minimumCommonSupergraph, areGraphsSwapped, STOPWATCH.ElapsedMilliseconds);
                }
            }
            catch (SystemException ex)
            {
                Console.WriteLine("Something went wrong and program failed! Reason: " + ex.Message);
            }
        }
    }
}