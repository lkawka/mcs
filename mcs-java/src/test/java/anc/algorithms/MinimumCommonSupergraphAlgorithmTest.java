package anc.algorithms;

import static anc.TestData.CASE1;
import static anc.TestData.CASE2;
import static anc.TestData.CASE5;
import static anc.TestData.CASE4;
import static anc.TestData.SUPERGRAPH_CASE4;
import static anc.TestData.SUPERGRAPH_CASE5;
import static anc.TestData.createAdjacencyMatrix;
import static org.assertj.core.api.Assertions.assertThat;

import com.google.common.collect.ImmutableListMultimap;

import anc.models.GeneticConfiguration;
import anc.models.MinimumCommonSupergraph;
import anc.models.Pair;

import org.junit.Before;
import org.junit.Test;

public class MinimumCommonSupergraphAlgorithmTest {

  @Before
  public void setup() {
    Genetic.random.setSeed(Genetic.SEED);
  }

  @Test
  public void shouldComputeMinimumCommonSuperGraphForCase1() {
    MinimumCommonSupergraphAlgorithm algo = new MinimumCommonSupergraphAlgorithm(new McSplit());

    MinimumCommonSupergraph MCS = algo.compute(CASE1.g1, CASE1.g2);

    assertThat(MCS.adjacencyMatrix).isDeepEqualTo(createAdjacencyMatrix(10, new ImmutableListMultimap.Builder<Integer, Integer>()
      .putAll(0, 1, 5, 6, 7)
      .putAll(1, 7, 9)
      .putAll(2, 3, 7)
      .putAll(3, 4)
      .putAll(4, 5, 7, 9)
      .putAll(8, 9).build()));
    assertThat(MCS.M).containsExactly(new Pair(1, 0), new Pair(2, 7), new Pair(4, 4), new Pair(5, 3), new Pair(6, 2),
          new Pair(7, 1), new Pair(0, null), new Pair(3, null), new Pair(null, 5), new Pair(null, 6));
  }

  @Test
  public void shouldComputeMinimumCommonSuperGraphApproximationForCase1() {
    MinimumCommonSupergraphAlgorithm algo = new MinimumCommonSupergraphAlgorithm(
        Genetic.fromConfiguration(GeneticConfiguration.fromPaper()));

    MinimumCommonSupergraph MCS = algo.compute(CASE1.g1, CASE1.g2);

    assertThat(MCS.adjacencyMatrix).hasDimensions(14, 14);
    assertThat(MCS.M).hasSizeLessThanOrEqualTo(14);
  }

  @Test
  public void shouldComputeMinimumCommonSuperGraphForCase2() {
    MinimumCommonSupergraphAlgorithm algo = new MinimumCommonSupergraphAlgorithm(new McSplit());

    MinimumCommonSupergraph MCS = algo.compute(CASE2.g1, CASE2.g2);

    assertThat(MCS.adjacencyMatrix).isDeepEqualTo(createAdjacencyMatrix(3, new ImmutableListMultimap.Builder<Integer, Integer>()
      .putAll(0, 1)
      .putAll(1, 2).build()));
    assertThat(MCS.M).containsExactly(new Pair(0, 0), new Pair(1, 1), new Pair(null, 2));
  }

  @Test
  public void shouldComputeMinimumCommonSuperGraphApproximationForCase2() {
    MinimumCommonSupergraphAlgorithm algo = new MinimumCommonSupergraphAlgorithm(
      Genetic.fromConfiguration(GeneticConfiguration.fromPaper()));

    MinimumCommonSupergraph MCS = algo.compute(CASE2.g1, CASE2.g2);

    assertThat(MCS.adjacencyMatrix).isDeepEqualTo(createAdjacencyMatrix(4, new ImmutableListMultimap.Builder<Integer, Integer>()
      .putAll(0, 1, 3)
      .putAll(2, 3).build()));
    assertThat(MCS.M).containsExactly(new Pair(0, 2), new Pair(1, null), new Pair(null, 0), new Pair(null, 1));
  }

  @Test
  public void shouldComputeMinimumCommonSuperGraphForCase4() {
    MinimumCommonSupergraphAlgorithm algo = new MinimumCommonSupergraphAlgorithm(new McSplit());

    MinimumCommonSupergraph MCS = algo.compute(CASE4.g1, CASE4.g2);

    assertThat(MCS.adjacencyMatrix).isDeepEqualTo(SUPERGRAPH_CASE4);
    assertThat(MCS.M).containsExactly(new Pair(0, 1), new Pair(1, 2), new Pair(2, 4), new Pair(4, 0), new Pair(3, null),
        new Pair(null, 3), new Pair(null, 5));
  }

  @Test
  public void shouldComputeMinimumCommonSuperGraphApproximationForCase4() {
    MinimumCommonSupergraphAlgorithm algo = new MinimumCommonSupergraphAlgorithm(
        Genetic.fromConfiguration(GeneticConfiguration.fromPaper()));

    MinimumCommonSupergraph MCS = algo.compute(CASE4.g1, CASE4.g2);

    assertThat(MCS.adjacencyMatrix).hasDimensions(9, 9);
    assertThat(MCS.M).hasSizeLessThanOrEqualTo(9);
  }

  @Test
  public void shouldComputeMinimumCommonSuperGraphForCase5() {
    MinimumCommonSupergraphAlgorithm algo = new MinimumCommonSupergraphAlgorithm(new McSplit());

    MinimumCommonSupergraph MCS = algo.compute(CASE5.g1, CASE5.g2);

    assertThat(MCS.adjacencyMatrix).isDeepEqualTo(SUPERGRAPH_CASE5);
    assertThat(MCS.M).containsExactly(new Pair(0, 0), new Pair(1, 1), new Pair(2, 2), new Pair(3, null));
  }

  @Test
  public void shouldComputeMinimumCommonSuperGraphApproximationForCase5() {
    MinimumCommonSupergraphAlgorithm algo = new MinimumCommonSupergraphAlgorithm(Genetic.fromConfiguration(GeneticConfiguration.fromPaper()));

    MinimumCommonSupergraph MCS = algo.compute(CASE5.g2, CASE5.g1);

    assertThat(createAdjacencyMatrix(5, new ImmutableListMultimap.Builder<Integer, Integer>()
      .putAll(0, 1, 2, 3)
      .putAll(1, 2, 3, 4)
      .putAll(3, 4).build()));
    assertThat(MCS.M).containsExactly(new Pair(0, 3), new Pair(2, 1), new Pair(1, null), new Pair(null, 0), new Pair(null, 2));
  }
}
