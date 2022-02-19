package anc.models;

import java.util.List;

import com.google.common.base.Objects;

public class MinimumCommonSupergraph {
    public final MaximumCommonSubgraph mcs;
    /**
     * List of vertex mappings to minimum common supergraph. For every pair, v1 is
     * form g1 and v2 is from g2.
     */
    public final List<Pair> M;
    public final int[][] adjacencyMatrix;

    public MinimumCommonSupergraph(MaximumCommonSubgraph mcs, List<Pair> M, int[][] adjacencyMatrix) {
        this.mcs = mcs;
        this.M = M;
        this.adjacencyMatrix = adjacencyMatrix;
    }
    
    public MinimumCommonSupergraph(List<Pair> M, int[][] adjacencyMatrix) {
        this(null, M, adjacencyMatrix);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(mcs, M, adjacencyMatrix);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MinimumCommonSupergraph other = (MinimumCommonSupergraph) obj;
        return Objects.equal(mcs, other.mcs) && Objects.equal(M, other.M)
                && Objects.equal(adjacencyMatrix, other.adjacencyMatrix);
    }
}
