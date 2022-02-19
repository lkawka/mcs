using Mcs.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace EmpiricalTesting
{
    public class Result
    {
        public readonly Stats Exact;
        public readonly Stats Approximation;

        public Result(Stats exact, Stats approximation)
        {
            Exact = exact;
            Approximation = approximation;
        }

        public class Stats
        {
            public readonly long ExecutionTime;
            public readonly int NMaximumCommonSubgraph;
            public readonly int NMinimumCommonSupergraph;

            public Stats(long executionTime, int nMaximumCommonSubgraph, int nMinimumCommonSupergraph)
            {
                ExecutionTime = executionTime;
                NMaximumCommonSubgraph = nMaximumCommonSubgraph;
                NMinimumCommonSupergraph = nMinimumCommonSupergraph;
            }
        }
    }
}
