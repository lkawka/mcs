package anc;

import static anc.TestData.CASE4;
import static anc.TestData.SUPERGRAPH_CASE4;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import org.junit.Test;

import anc.models.MaximumCommonSubgraph;
import anc.models.MinimumCommonSupergraph;
import anc.models.Pair;

public class DisplayTest {

    @Test
    public void shouldDisplayMaximumCommonSubgraph() {
        MaximumCommonSubgraph mcs = new MaximumCommonSubgraph(
                newArrayList(new Pair(0, 0), new Pair(1, 5), new Pair(2, 3), new Pair(4, 1)), CASE4.g1, CASE4.g2);

        Display.displayMaximumCommonSubgraph(CASE4.g1, CASE4.g2, mcs, false);
    }

    @Test
    public void shouldDisplayMaximumCommonSubgraphForSwappedGraphs() {
        MaximumCommonSubgraph mcs = new MaximumCommonSubgraph(
                newArrayList(new Pair(0, 0), new Pair(1, 5), new Pair(2, 3), new Pair(4, 1)), CASE4.g1, CASE4.g2);

        Display.displayMaximumCommonSubgraph(CASE4.g1, CASE4.g2, mcs, true);
    }

    @Test
    public void shouldDisplayMinimumCommonSupergraph() {
        List<Pair> M = newArrayList(new Pair(0, 0), new Pair(1, 5), new Pair(2, 3), new Pair(4, 1), new Pair(3, null),
                new Pair(null, 2), new Pair(null, 4));
        MaximumCommonSubgraph mcs = new MaximumCommonSubgraph(
                newArrayList(new Pair(0, 0), new Pair(1, 5), new Pair(2, 3), new Pair(4, 1)), CASE4.g1, CASE4.g2);
        MinimumCommonSupergraph MCS = new MinimumCommonSupergraph(mcs, M, SUPERGRAPH_CASE4);

        Display.displayMinimumCommonSupergraph(CASE4.g1, CASE4.g2, MCS, false);
    }

    @Test
    public void shouldDisplayMinimumCommonSupergraphForSwappedGraphs() {
        List<Pair> M = newArrayList(new Pair(0, 0), new Pair(1, 5), new Pair(2, 3), new Pair(4, 1), new Pair(3, null),
                new Pair(null, 2), new Pair(null, 4));
        MaximumCommonSubgraph mcs = new MaximumCommonSubgraph(
                newArrayList(new Pair(0, 0), new Pair(1, 5), new Pair(2, 3), new Pair(4, 1)), CASE4.g1, CASE4.g2);
        MinimumCommonSupergraph MCS = new MinimumCommonSupergraph(mcs, M, SUPERGRAPH_CASE4);

        Display.displayMinimumCommonSupergraph(CASE4.g1, CASE4.g2, MCS, true);
    }

    @Test
    public void shouldDisplayMinimumCommonSupergraphWithBestGenome() {
        List<Pair> M = newArrayList(new Pair(0, 0), new Pair(1, 5), new Pair(2, 3), new Pair(4, 1), new Pair(3, null),
                new Pair(null, 2), new Pair(null, 4));
        MaximumCommonSubgraph mcs = new MaximumCommonSubgraph(
                newArrayList(new Pair(0, 0), new Pair(1, 5), new Pair(2, 3), new Pair(4, 1)), CASE4.g1, CASE4.g2,
                newArrayList(0, 1, 2, 3, 4));
        MinimumCommonSupergraph MCS = new MinimumCommonSupergraph(mcs, M, SUPERGRAPH_CASE4);

        Display.displayMinimumCommonSupergraph(CASE4.g1, CASE4.g2, MCS, false);
    }

}
