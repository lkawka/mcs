using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mcs.Models
{
    public class Configuration
    {
        public readonly AlgorithmType AlgorithmType;
        public readonly GeneticConfiguration GeneticConfiguration;
        public readonly string InputFilePath;

        public Configuration(AlgorithmType algorithmType, GeneticConfiguration geneticConfiguration, string inputFilePath)
        {
            AlgorithmType = algorithmType;
            GeneticConfiguration = geneticConfiguration;
            InputFilePath = inputFilePath;
        }

        public Configuration(AlgorithmType algorithmType, string inputFilePath) : this(algorithmType, null, inputFilePath)
        {
        }
    }
}
