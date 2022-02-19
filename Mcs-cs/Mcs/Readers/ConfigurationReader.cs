using Mcs.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mcs.Readers
{
    public class ConfigurationReader
    {
        public static Configuration Read()
        {
            AlgorithmType algorithmType = ReadAlgorithmType();
            GeneticConfiguration geneticConfiguration = null;
            if (algorithmType != AlgorithmType.MC_SPLIT)
            {
                geneticConfiguration = ReadGeneticConfiguration();
            }
            string inputFilePath = ReadInputFilePath();
            return new Configuration(algorithmType, geneticConfiguration, inputFilePath);
        }

        public static bool KeepRunning()
        {
            Console.WriteLine("Do you want to try again? (y/n):");
            string input = Console.ReadLine() ?? "";
            return input == "y";
        }

        private static AlgorithmType ReadAlgorithmType()
        {
            Console.WriteLine("Select which algorithm should be used (1/2/3):");
            Console.WriteLine("1. Exact (McSplit)");
            Console.WriteLine("2. Approximation (Genetic)");
            Console.WriteLine("3. Both of the above");

            int input = ReadInt(v => !new List<int>() { 1, 2, 3 }.Contains(v), "You must type 1, 2 or 3 when selecting algorithm type!");

            if(input == 1)
            {
                return AlgorithmType.MC_SPLIT;
            }
            if(input == 2)
            {
                return AlgorithmType.GENETIC;
            }
            return AlgorithmType.BOTH;
        }

        private static GeneticConfiguration ReadGeneticConfiguration()
        {
            Console.WriteLine("Do you want to configure the genetic algorithm? If no, the default configuration will be used (y/n):");
            string input = Console.ReadLine() ?? "";
            if (!new List<string> { "y", "n" }.Contains(input))
            {
                throw new SystemException(
                        "You must type y or n when deciding if you want to configure genetic algorithm!");
            }
            if (input == "n")
            {
                return GeneticConfiguration.FromPaper();
            }

            Console.WriteLine("Provide the size of population:");
            int nGenomes = ReadInt(v => v < 1, "Population size must be an integer, and at least 1!");

            Console.WriteLine("Provide number of generations:");
            int nGenerations = ReadInt(v => v < 1, "Number of generations must be an integer, and at least 1!");

            Console.WriteLine("Provide number of generation after which the entire population is shuffled:");
            int nCb = ReadInt(v => v < 1 || v > nGenerations,
                   "Number of generation after which the entire population is shuffled must be an integer, at least 1, and smaller then number of generations!");

            Console.WriteLine("Provide number of tribes (size of the population must be divisable by it):");
            int nTribes = ReadInt(v => v < 1 || nGenomes % v != 0,
                   "Number of tribes must be an integer, at least 1, and size of population must be devisable by it!");

            Console.WriteLine("Provide probability of crossover (floating point number between 0 and 1 e.g 0.56):");
            double pCrossover = ReadDouble(v => v < 0 || v > 1,
                   "Probability of crossover must be a floating point number between 0 and 1 (inclusive)!");

            Console.WriteLine("Provide probability of mutation (floating point number between 0 and 1 e.g 0.56):");
            double pMutation = ReadDouble(v => v < 0 || v > 1,
                   "Probability of mutation must be a floating point number between 0 and 1 (inclusive)!");

            return new GeneticConfiguration(nGenomes, nGenerations, nCb, nTribes, pCrossover, pMutation);
        }

        private static int ReadInt(Func<int, bool> throwCondition, string errorMessage)
        {
            string line = Console.ReadLine();
            if (!int.TryParse(line, out int value) || throwCondition.Invoke(value))
            {
                throw new SystemException(errorMessage);
            }
            return value;
        }

        private static double ReadDouble(Func<double, bool> throwCondition, string errorMessage)
        {
            string line = Console.ReadLine();
            if (!double.TryParse(line, out double value) || throwCondition.Invoke(value))
            {
                throw new SystemException(errorMessage);
            }
            return value;
        }

        private static string ReadInputFilePath()
        {
            Console.WriteLine("Provide input file path:");
            return Console.ReadLine() ?? "";
        }
    }
}
