using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mcs.Models
{
    public class GeneticConfiguration
    {
        public int NGenomes;
        public int NGenerations;
        public int NCb;
        public int NTribes;
        public double PCrossover;
        public double PMutation;

        public GeneticConfiguration(int nGenomes, int nGenerations, int nCb, int nTribes, double pCrossover,
            double pMutation)
        {
            NGenomes = nGenomes;
            NGenerations = nGenerations;
            NCb = nCb;
            NTribes = nTribes;
            PCrossover = pCrossover;
            PMutation = pMutation;
        }

        public static GeneticConfiguration FromPaper()
        {
            return new GeneticConfiguration(256, 512, 32, 4, 0.9, 0.5);
        }

    }
}
