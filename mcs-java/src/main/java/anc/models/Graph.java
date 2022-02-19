package anc.models;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

import java.util.List;

import com.google.common.base.Objects;

public class Graph {
    public final int n;
    public final int[][] M;

    public Graph(int n, int[][] M) {
        this.n = n;
        this.M = M;
    }

    public void validate() {
        validateSize();
        validateUndirected();
        validateUnweighted();
        validateConnected();
    }

    public int degreeOf(int vertex) {
        int degree = 0;
        for (int i = 0; i < n; i++) {
            degree += M[vertex][i];
        }
        return degree;
    }

    public List<Pair> listOfEdges() {
        List<Pair> res = newArrayList();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                if (M[i][j] == 1)
                    res.add(new Pair(i, j));

            }
        }
        return res;
    }

    private void validateSize() {
        if (M == null) {
            throw new RuntimeException("Adjecency matrix cannot be null!");
        }
        if (M.length != n) {
            throw new RuntimeException(
                    format("Adjeceny matrix has wrong height! Actual value is %s, but expected was %s.", M.length, n));
        }
        for (int i = 0; i < n; i++) {
            if (M[i].length != n) {
                throw new RuntimeException(
                        format("Row %s of adjeceny matrix has wrong length! Actual value is %s, but expected was %s.",
                                i, M[i].length, n));
            }
        }
    }

    private void validateUndirected() {
        for (int i = 0; i < n; i++) {
            if (M[i][i] != 0) {
                throw new RuntimeException(
                        format("It is forbidden for vertices to have edges to itself! Vertex %s broke this rule.", i));
            }
            for (int j = i + 1; j < n; j++) {
                if (M[i][j] != M[j][i]) {
                    throw new RuntimeException(format(
                            "Adjecency matrix is not symmetric! Vertices %s and %s have contradicting relations.", i,
                            j));
                }
            }
        }
    }

    private void validateUnweighted() {
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (M[i][j] != 0 && M[i][j] != 1) {
                    throw new RuntimeException(format(
                            "Graph must be unweighted and all values of adjecency matrix must have values of 0 or 1! M[%s][%s]=%s.",
                            i, j, M[i][j]));
                }
            }
        }
    }

    private void validateConnected() {
        final boolean[] visited = new boolean[n];
        dfs(0, visited);
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                throw new RuntimeException("Graph must be connected!");
            }
        }
    }

    private void dfs(int v, boolean[] visited) {
        visited[v] = true;
        for (int i = 0; i < n; i++) {
            if (!visited[i] && M[v][i] == 1) {
                dfs(i, visited);
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(n, M);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Graph other = (Graph) obj;
        return Objects.equal(n, other.n) && Objects.equal(M, other.M);
    }
}
