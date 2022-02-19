package anc.algorithms;

import anc.models.Graph;
import anc.models.MaximumCommonSubgraph;

public interface MaximumCommonSubgraphAlgorithm {
    MaximumCommonSubgraph compute(Graph g1, Graph g2);
}
