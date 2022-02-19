package anc.algorithms;

import anc.models.Graph;
import anc.models.MaximumCommonSubgraph;
import anc.models.MinimumCommonSupergraph;
import anc.models.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MinimumCommonSupergraphAlgorithm {
    private final MaximumCommonSubgraphAlgorithm maximumCommonSubgraphAlgorithm;

    public MinimumCommonSupergraphAlgorithm(MaximumCommonSubgraphAlgorithm maximumCommonSubgraphAlgorithm) {
        this.maximumCommonSubgraphAlgorithm = maximumCommonSubgraphAlgorithm;
    }

    public MinimumCommonSupergraph compute(Graph g1, Graph g2) {
        final MaximumCommonSubgraph mcs = maximumCommonSubgraphAlgorithm.compute(g1, g2);

        final int n1 = mcs.M.size();
        final int n2 = g1.n - n1;
        final int n3 = g2.n - n1;
        final int n = n1 + n2 + n3;
        final List<Pair> M = new ArrayList<>(mcs.M);
        final int[][] adjacencyMatrix = new int[n][n];

        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n1; j++) {
                adjacencyMatrix[i][j] = mcs.adjacencyMatrix[i][j];
            }
        }

        final List<Integer> g1MinusMcs = mcs.verticesFromG1NotInMcs();
        M.addAll(g1MinusMcs.stream().map(v1 -> new Pair(v1, null)).collect(Collectors.toList()));
        // Add edges from g1-mcs to mcs
        for (int g1MinusMcsIndex = 0; g1MinusMcsIndex < g1MinusMcs.size(); g1MinusMcsIndex++) {
            for (int mcsIndex = 0; mcsIndex < mcs.M.size(); mcsIndex++) {
                final int mcsIndexAsG1Index = mcs.M.get(mcsIndex).v1;
                if (g1.M[g1MinusMcs.get(g1MinusMcsIndex)][mcsIndexAsG1Index] == 1) {
                    final int g1MinusMcsIndexAsMIndex = n1 + g1MinusMcsIndex;
                    adjacencyMatrix[mcsIndex][g1MinusMcsIndexAsMIndex] = 1;
                    adjacencyMatrix[g1MinusMcsIndexAsMIndex][mcsIndex] = 1;
                }
            }
        }
        // Add edges from g1-mcs (between each other)
        for (int i = 0; i < g1MinusMcs.size(); i++) {
            for (int j = i + 1; j < g1MinusMcs.size(); j++) {
                if (g1.M[g1MinusMcs.get(i)][g1MinusMcs.get(j)] == 1) {
                    adjacencyMatrix[n1 + i][n1 + j] = 1;
                    adjacencyMatrix[n1 + j][n1 + i] = 1;
                }
            }
        }

        final List<Integer> g2MinusMcs = mcs.verticesFromG2NotInMcs();
        M.addAll(g2MinusMcs.stream().map(v2 -> new Pair(null, v2)).collect(Collectors.toList()));
        // Add edges from g2-mcs to mcs
        for (int g2MinusMcsIndex = 0; g2MinusMcsIndex < g2MinusMcs.size(); g2MinusMcsIndex++) {
            for (int mcsIndex = 0; mcsIndex < mcs.M.size(); mcsIndex++) {
                final int mcsIndexAsG2Index = mcs.M.get(mcsIndex).v2;
                if (g2.M[g2MinusMcs.get(g2MinusMcsIndex)][mcsIndexAsG2Index] == 1) {
                    final int g2MinusMcsIndexAsMIndex = n1 + n2 + g2MinusMcsIndex;
                    adjacencyMatrix[mcsIndex][g2MinusMcsIndexAsMIndex] = 1;
                    adjacencyMatrix[g2MinusMcsIndexAsMIndex][mcsIndex] = 1;
                }
            }
        }
        // Add edges from g2-mcs (between each other)
        for (int i = 0; i < g2MinusMcs.size(); i++) {
            for (int j = i + 1; j < g2MinusMcs.size(); j++) {
                if (g2.M[g2MinusMcs.get(i)][g2MinusMcs.get(j)] == 1) {
                    adjacencyMatrix[n1 + n2 + i][n1 + n2 + j] = 1;
                    adjacencyMatrix[n1 + n2 + j][n1 + n2 + i] = 1;
                }
            }
        }

        return new MinimumCommonSupergraph(mcs, M, adjacencyMatrix);
    }
}
