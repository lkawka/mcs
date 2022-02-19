using Mcs.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Mcs.Algorithms
{
    public interface MaximumCommonSubgraphAlgorithm
    {
        MaximumCommonSubgraph Compute(Graph g1, Graph g2);
    }
}
