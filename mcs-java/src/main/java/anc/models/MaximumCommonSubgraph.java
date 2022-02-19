package anc.models;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.base.Objects;

public class MaximumCommonSubgraph {
    public final Graph g1;
    public final Graph g2;
    /**
     * List of vertex mappings to maximum common subgraph. For every pair, v1 is
     * form g1 and v2 is from g2. List is sorted by v1.
     */
    public final List<Pair> M;
    public final int[][] adjacencyMatrix;
    public final List<Integer> bestGenome;

    public MaximumCommonSubgraph(List<Pair> M, Graph g1, Graph g2, List<Integer> bestGenome) {
        this.M = M;
        this.g1 = g1;
        this.g2 = g2;
        this.bestGenome = bestGenome;

        // Build adjacency matrix
        final int n = M.size();
        this.adjacencyMatrix = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                adjacencyMatrix[i][j] = g1.M[M.get(i).v1][M.get(j).v1];
            }
        }
    }

    public MaximumCommonSubgraph(List<Pair> M, Graph g1, Graph g2) {
        this(M, g1, g2, null);
    }

    /** Results are sorted indeces */
    public List<Integer> verticesFromG1NotInMcs() {
        final List<Integer> result = IntStream.range(0, g1.n).boxed().collect(Collectors.toList());
        final List<Integer> v1s = M.stream().map(p -> p.v1).collect(Collectors.toList());
        result.removeAll(v1s);
        return result;
    }

    /** Results are sorted indeces */
    public List<Integer> verticesFromG2NotInMcs() {
        final List<Integer> result = IntStream.range(0, g2.n).boxed().collect(Collectors.toList());
        final List<Integer> v2s = M.stream().map(p -> p.v2).sorted().collect(Collectors.toList());
        result.removeAll(v2s);
        return result;
    }

    public boolean fromGenetic() {
        return bestGenome != null;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(g1, g2, M, adjacencyMatrix, bestGenome);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MaximumCommonSubgraph other = (MaximumCommonSubgraph) obj;
        return Objects.equal(g1, other.g1) && Objects.equal(g2, other.g2) && Objects.equal(M, other.M)
                && Objects.equal(adjacencyMatrix, other.adjacencyMatrix) && Objects.equal(bestGenome, other.bestGenome);
    }

}
