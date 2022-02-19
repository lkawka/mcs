package anc;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;

import anc.models.Graph;

public class TestData {
    /**
     * Test case for 8x8 graphs. Taken from
     * http://www.gabormelli.com/RKB/Maximum_Common_Induced_Subgraph_(MCIS)
     */
    public static final TestCase CASE1 = createCase1();

    /** Simple mini case */
    public static final TestCase CASE2 = createCase2();

    /** Test case for 5x5 and 6x6 graphs */
    public static final TestCase CASE3 = createCase3();

    /** Test case from our documentation */
    public static final TestCase CASE4 = createCase4();

    /** https://www.researchgate.net/figure/Minimum-common-super-graph_fig2_221087751 */
    public static final TestCase CASE5 = createCase5();

    public static final int[][] SUPERGRAPH_CASE4 = createAdjacencyMatrix(7, new ImmutableListMultimap.Builder<Integer, Integer>()
        .putAll(0, 3, 4, 5, 6)
        .putAll(1, 2, 3, 5, 6)
        .putAll(2, 3, 6)
        .putAll(5, 6).build());

    public static final int[][] SUPERGRAPH_CASE5 =
    createAdjacencyMatrix(4, new ImmutableListMultimap.Builder<Integer, Integer>()
        .putAll(0, 1, 2, 3)
        .putAll(1, 2, 3).build());

    public static class TestCase {
        public final Graph g1;
        public final Graph g2;

        public TestCase(Graph g1, Graph g2) {
            this.g1 = g1;
            this.g2 = g2;
        }
    }

    private static TestCase createCase1() {
        Graph g1 = new Graph(8,
                new int[][] {
                    { 0, 1, 0, 0, 0, 0, 0, 0 },
                    { 1, 0, 1, 1, 0, 0, 0, 1 },
                    { 0, 1, 0, 1, 0, 0, 0, 0 },
                    { 0, 1, 1, 0, 1, 0, 1, 0 },
                    { 0, 0, 0, 1, 0, 1, 0, 0 },
                    { 0, 0, 0, 0, 1, 0, 1, 0 },
                    { 0, 0, 0, 1, 0, 1, 0, 1 },
                    { 0, 1, 0, 0, 0, 0, 1, 0 } });
        Graph g2 = new Graph(8,
                new int[][] {
                    { 0, 1, 0, 0, 0, 0, 0, 1 },
                    { 1, 0, 1, 0, 0, 0, 0, 0 },
                    { 0, 1, 0, 1, 0, 0, 1, 0 },
                    { 0, 0, 1, 0, 1, 0, 0, 0 },
                    { 0, 0, 0, 1, 0, 0, 0, 0 },
                    { 0, 0, 0, 0, 0, 0, 1, 0 },
                    { 0, 0, 1, 0, 0, 1, 0, 1 },
                    { 1, 0, 0, 0, 0, 0, 1, 0 } });
        g1.validate();
        g2.validate();
        return new TestCase(g1, g2);
    }

    private static TestCase createCase2() {
        Graph g1 = new Graph(2, new int[][] {
            {0, 1},
            {1, 0}
        });
        Graph g2 = new Graph(3, new int[][] {
            {0, 1, 0},
            {1, 0, 1},
            {0, 1, 0}
        });
        g1.validate();
        g2.validate();
        return new TestCase(g1, g2);
    }

    private static TestCase createCase3() {
        Graph g1 = new Graph(5, new int[][] {
            {0, 0, 0, 1, 1},
            {0, 0, 1, 0, 1},
            {0, 1, 0, 0, 1},
            {1, 0, 0, 0, 0},
            {1, 1, 1, 0, 0}
        });
        Graph g2 = new Graph(6, new int [][] {
            {0, 1, 0, 0, 1, 0},
            {1, 0, 0, 1, 0, 1},
            {0, 0, 0, 1, 0, 1},
            {0, 1, 1, 0, 0, 1},
            {1, 0, 0, 0, 0, 1},
            {0, 1, 1, 1, 1, 0}
        });
        g1.validate();
        g2.validate();
        return new TestCase(g1, g2);
    }

    private static TestCase createCase4() {
        int n1 = 5;
        int[][] M1 = createAdjacencyMatrix(n1, new ImmutableListMultimap.Builder<Integer, Integer>()
            .putAll(0, 3, 4)
            .putAll(1, 2, 4)
            .putAll(2, 4).build());
        Graph g1 = new Graph(n1, M1);

        int n2 = 6;
        int[][] M2 = createAdjacencyMatrix(n2, new ImmutableListMultimap.Builder<Integer, Integer>()
            .putAll(0, 1, 2, 4)
            .putAll(1, 3, 5)
            .putAll(2, 3, 4, 5)
            .putAll(3, 5)
            .putAll(4, 5).build());
        Graph g2 = new Graph(n2, M2);

        g1.validate();
        g2.validate();
        return new TestCase(g1, g2);
    }

    private static TestCase createCase5() {
        int n1 = 4;
        int[][] M1 = createAdjacencyMatrix(n1, new ImmutableListMultimap.Builder<Integer, Integer>()
            .putAll(0, 1, 2, 3)
            .putAll(1, 2, 3).build());
        Graph g1 = new Graph(n1, M1);

        int n2 = 3;
        int [][] M2 = createAdjacencyMatrix(n2, new ImmutableListMultimap.Builder<Integer, Integer>()
            .putAll(0, 1, 2)
            .putAll(1, 2).build());
        Graph g2 = new Graph(n2, M2);

        g1.validate();
        g2.validate();
        return new TestCase(g1, g2);
    }

    public static int[][] createAdjacencyMatrix(int n, ListMultimap<Integer, Integer> adjacencyList) {
        final int[][] M = new int[n][n];
        for (int v : adjacencyList.keySet()) {
            for (int w : adjacencyList.get(v)) {
                M[v][w] = 1;
                M[w][v] = 1;
            }
        }
        return M;
    }
}
