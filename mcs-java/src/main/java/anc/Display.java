package anc;

import java.util.Arrays;
import java.util.List;

import anc.models.Graph;
import anc.models.MaximumCommonSubgraph;
import anc.models.MinimumCommonSupergraph;
import anc.models.Pair;

public class Display {

    public static void displayMaximumCommonSubgraph(Graph g1, Graph g2, MaximumCommonSubgraph mcs,
            boolean areGraphsSwapped) {
        System.out.println("Number of vertices in maximum common subgraph: " + mcs.M.size());
        System.out.println("");

        System.out.println("Adjacency list of the maximum common subgraph: ");
        displayAdjacencyList(mcs.adjacencyMatrix);
        System.out.println("");

        System.out.println("Vertex mapping table:");
        displayMappingTable(mcs.M, areGraphsSwapped);
        System.out.println("");

        if(mcs.fromGenetic()) {
            displayBestGenome(mcs.bestGenome, g1.n);
        }
    }

    public static void displayMinimumCommonSupergraph(Graph g1, Graph g2, MinimumCommonSupergraph mcs,
            boolean areGraphsSwapped) {
        System.out.println("Number of vertices in minimum common supergraph: " + mcs.adjacencyMatrix.length);
        System.out.println("");

        System.out.println("Adjacency list of the minimum common supergraph: ");
        displayAdjacencyList(mcs.adjacencyMatrix);
        System.out.println("");

        System.out.println("Vertex mapping table:");
        displayMappingTable(mcs.M, areGraphsSwapped);
        System.out.println("");

        if(mcs.mcs.fromGenetic()) {
            displayBestGenome(mcs.mcs.bestGenome, g1.n);
        }
    }

    private static void displayAdjacencyList(int[][] adjacencyMatrix) {
        final int n = adjacencyMatrix.length;
        for (int i = 0; i < n; i++) {
            boolean hasEdge = false;
            for (int j = 0; j < n; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    hasEdge = true;
                    break;
                }
            }
            if (!hasEdge) {
                continue;
            }
            System.out.print("" + i + ": ");
            for (int j = 0; j < n; j++) {
                if (adjacencyMatrix[i][j] == 1) {
                    System.out.print("" + j + " ");
                }
            }
            System.out.print("\n");
        }
    }

    private static void displayMappingTable(List<Pair> M, boolean areGraphsSwapped) {
        final String[][] mappingTable = new String[M.size() + 1][3];
        mappingTable[0][0] = "MCS";
        mappingTable[0][1] = "G1";
        mappingTable[0][2] = "G2";

        for (int i = 0; i < M.size(); i++) {
            mappingTable[i + 1][0] = "" + i;
            final int index1 = areGraphsSwapped ? 2 : 1;
            final int index2 = areGraphsSwapped ? 1 : 2;
            mappingTable[i + 1][index1] = M.get(i).v1 != null ? "" + M.get(i).v1 : "";
            mappingTable[i + 1][index2] = M.get(i).v2 != null ? "" + M.get(i).v2 : "";
        }

        System.out.println(
                Arrays.deepToString(mappingTable).replace("],", "\n").replace(",", "\t| ").replaceAll("[\\[\\]]", " "));
    }

    private static void displayBestGenome(List<Integer> genome, int n1) {
        System.out.println("Best found genome (G1 to G2 vertex mapping before applying reduction function):");
        for(int i = 0; i < n1; i++) {
            System.out.println(""+i+": " + genome.get(i));
        }
        System.out.println("");
    }
}
