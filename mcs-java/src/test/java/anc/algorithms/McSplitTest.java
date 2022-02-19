package anc.algorithms;

import static anc.TestData.CASE1;
import static anc.TestData.CASE2;
import static anc.TestData.CASE3;
import static anc.TestData.CASE4;
import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import anc.algorithms.McSplit.LabelClass;
import anc.algorithms.McSplit.LabelClasses;
import anc.models.MaximumCommonSubgraph;
import anc.models.Pair;

public class McSplitTest {

    @Test
    public void shouldComputeMaximumCommonSubgraphFor8x8Graphs() {
        MaximumCommonSubgraph mcs = new McSplit().compute(CASE1.g1, CASE1.g2);

        assertThat(mcs.M).containsOnly(new Pair(1, 0), new Pair(2, 7), new Pair(4, 4), new Pair(5, 3), new Pair(6, 2),
                new Pair(7, 1));
    }

    @Test
    public void shouldComputeMaximumCommonSubgraphForMiniGraphs() {
        MaximumCommonSubgraph mcs = new McSplit().compute(CASE2.g1, CASE2.g2);

        assertThat(mcs.M).hasSize(2);
        assertThat(mcs.M).extracting(p -> p.v1).doesNotHaveDuplicates();
        assertThat(mcs.M).extracting(p -> p.v2).doesNotHaveDuplicates();
        assertThat(newArrayList(mcs.M.get(0).v2, mcs.M.get(1).v2)).doesNotContainSequence(0, 2)
                .doesNotContainSequence(2, 0);
    }

    @Test
    public void shouldComputeMaximumCommonSubgraphForGraphsWhereSecondIsBigger() {
        MaximumCommonSubgraph mcs = new McSplit().compute(CASE3.g1, CASE3.g2);

        assertThat(mcs.M).containsOnly(new Pair(0, 4), new Pair(1, 2), new Pair(2, 3), new Pair(3, 0), new Pair(4, 5));
    }

    @Test
    public void shouldComputeMaximumCommonSubgraphForGraphsWhereFirstIsBigger() {
        MaximumCommonSubgraph mcs = new McSplit().compute(CASE3.g2, CASE3.g1);

        assertThat(mcs.M).containsOnly(new Pair(0, 3), new Pair(2, 2), new Pair(3, 1), new Pair(4, 0), new Pair(5, 4));
    }

    @Test
    public void shouldComputeMaximumCommonSubgraphForCase4() {
        MaximumCommonSubgraph mcs = new McSplit().compute(CASE4.g1, CASE4.g2);

        assertThat(mcs.M).satisfiesAnyOf(
            M -> assertThat(mcs.M).containsOnly(new Pair(0, 0), new Pair(1, 5), new Pair(2, 3), new Pair(4, 1)),
            M -> assertThat(mcs.M).containsOnly(new Pair(0, 1), new Pair(1, 2), new Pair(2, 4), new Pair(4, 0)));
    }

    @Test
    public void shouldComputeMaximumCommonSubgraphForIdenticalGraphs() {
        MaximumCommonSubgraph mcs = new McSplit().compute(CASE1.g1, CASE1.g1);

        assertThat(mcs.M).containsOnly(new Pair(0, 0), new Pair(1, 1), new Pair(2, 2), new Pair(3, 3), new Pair(4, 4),
                new Pair(5, 5), new Pair(6, 6), new Pair(7, 7));
    }

    public static class LabelClassTest {

        @Test
        public void shouldComputeMaxDegreeVertexForG() {
            List<Integer> G = Arrays.asList(0, 2, 6);
            List<Integer> H = Arrays.asList(1, 2, 3);

            LabelClass labelClass = new LabelClass(CASE1.g1, CASE1.g2, G, H);

            assertThat(labelClass.selectVertexFromG()).isEqualTo(6);
            assertThat(labelClass.maxDegreeVertexG).isEqualTo(6);
            assertThat(labelClass.maxDegreeG).isEqualTo(3);
        }

        @Test
        public void shouldCopyWithoutVertexFromG() {
            int vertex = 5;
            List<Integer> G = Arrays.asList(0, 2, vertex);
            List<Integer> H = Arrays.asList(1, 2, 3);
            LabelClass existingLabelClass = new LabelClass(CASE1.g1, CASE1.g2, G, H);

            LabelClass copy = existingLabelClass.copyWithoutVertexFromG(vertex);

            assertThat(copy.G).containsExactly(0, 2);
        }

        @Test
        public void shouldFindAllNeighborsFromG() {
            int vertex = 6;
            List<Integer> G = Arrays.asList(0, 5, vertex);
            List<Integer> H = Arrays.asList(1, 2, 5);
            LabelClass labelClass = new LabelClass(CASE1.g1, CASE1.g2, G, H);

            List<Integer> neighbors = labelClass.neighborsFromGOf(vertex);

            assertThat(neighbors).containsOnly(5);
        }

        @Test
        public void shouldFindAllNeighborsFromH() {
            int vertex = 1;
            List<Integer> G = Arrays.asList(0, 5, 6);
            List<Integer> H = Arrays.asList(vertex, 2, 5);
            LabelClass labelClass = new LabelClass(CASE1.g1, CASE1.g2, G, H);

            List<Integer> neighbors = labelClass.neighborsFromHOf(vertex);

            assertThat(neighbors).containsOnly(2);
        }

        @Test
        public void shouldFindAllNotNeighborsFromG() {
            int vertex = 6;
            List<Integer> G = Arrays.asList(0, 5, vertex);
            List<Integer> H = Arrays.asList(1, 2, 5);
            LabelClass labelClass = new LabelClass(CASE1.g1, CASE1.g2, G, H);

            List<Integer> neighbors = labelClass.notNeighborsFromGOf(vertex);

            assertThat(neighbors).containsOnly(0);
        }

        @Test
        public void shouldFindAllNotNeighborsFromH() {
            int vertex = 1;
            List<Integer> G = Arrays.asList(0, 5, 6);
            List<Integer> H = Arrays.asList(vertex, 2, 5);
            LabelClass labelClass = new LabelClass(CASE1.g1, CASE1.g2, G, H);

            List<Integer> neighbors = labelClass.notNeighborsFromHOf(vertex);

            assertThat(neighbors).containsOnly(5);
        }
    }

    public static class LabalClassesTest {

        @Test
        public void shouldCreateLabelClassesFromTwoGraphs() {
            LabelClasses labelClasses = LabelClasses.from(CASE1.g1, CASE1.g2);

            assertThat(labelClasses.labelClasses).hasSize(1);
            assertThat(labelClasses.labelClasses.get(0).G).containsOnly(0, 1, 2, 3, 4, 5, 6, 7);
            assertThat(labelClasses.labelClasses.get(0).H).containsOnly(0, 1, 2, 3, 4, 5, 6, 7);
        }

        @Test
        public void shouldSumPairsOfVertices() {
            LabelClass lb1 = new LabelClass(CASE1.g1, CASE1.g2, Arrays.asList(0, 1), Arrays.asList(0, 1, 2));
            LabelClass lb2 = new LabelClass(CASE1.g1, CASE1.g2, Arrays.asList(2, 3, 4), Arrays.asList());
            LabelClasses labelClasses = new LabelClasses(Arrays.asList(lb1, lb2));

            int sumPairsOfVertices = labelClasses.sumPairsOfVertices();

            assertThat(sumPairsOfVertices).isEqualTo(2);
        }

        @Test
        public void shouldSelectLabelClassWithSmallestMaxGH() {
            LabelClass lb1 = new LabelClass(CASE1.g1, CASE1.g2, Arrays.asList(0, 1), Arrays.asList(0, 1, 2));
            LabelClass lb2 = new LabelClass(CASE1.g1, CASE1.g2, Arrays.asList(2, 3), Arrays.asList());
            LabelClasses labelClasses = new LabelClasses(Arrays.asList(lb1, lb2));

            LabelClass selected = labelClasses.select();

            assertThat(selected).isEqualTo(lb1);
        }

        @Test
        public void shouldSelectLabelClassWithVertexInGWithHighestDegree() {
            LabelClass lb1 = new LabelClass(CASE1.g1, CASE1.g2, Arrays.asList(0, 2), Arrays.asList(0, 1, 2));
            LabelClass lb2 = new LabelClass(CASE1.g1, CASE1.g2, Arrays.asList(1, 3, 5), Arrays.asList());
            LabelClasses labelClasses = new LabelClasses(Arrays.asList(lb1, lb2));

            LabelClass selected = labelClasses.select();

            assertThat(selected).isEqualTo(lb2);
        }
    }
}
