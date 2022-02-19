package anc.algorithms;

import static anc.TestData.CASE1;
import static anc.TestData.CASE2;
import static anc.TestData.CASE3;
import static anc.TestData.CASE4;
import static com.google.common.collect.Lists.newArrayList;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import anc.algorithms.Genetic.Genome;
import anc.algorithms.Genetic.Population;
import anc.models.MaximumCommonSubgraph;
import anc.models.Pair;

public class GeneticTest {
    protected static int[][] EMPTY_D = new int[][] {};

    @Test
    public void shouldFindMaximumCommonSubgraphForTestCase1() {
        Genetic genetic = new Genetic(256, 512, 32, 4, 0.9, 0.5);

        MaximumCommonSubgraph mcs = genetic.compute(CASE1.g1, CASE1.g2);

        assertThat(mcs.M).hasSizeBetween(2, 6);
        assertThat(mcs.M).extracting(p -> p.v1).doesNotHaveDuplicates();
        assertThat(mcs.M).extracting(p -> p.v2).doesNotHaveDuplicates();
    }

    @Test
    public void shouldFindMaximumCommonSubgraphForTestCase2() {
        Genetic genetic = new Genetic(256, 512, 32, 4, 0.9, 0.5);

        MaximumCommonSubgraph mcs = genetic.compute(CASE2.g1, CASE2.g2);

        assertThat(mcs.M).hasSizeBetween(1, 2);
        assertThat(mcs.M).extracting(p -> p.v1).doesNotHaveDuplicates();
        assertThat(mcs.M).extracting(p -> p.v2).doesNotHaveDuplicates();
    }

    @Test
    public void shouldFindMaximumCommonSubgraphForTestCase3() {
        Genetic genetic = new Genetic(256, 512, 32, 4, 0.9, 0.5);

        MaximumCommonSubgraph mcs = genetic.compute(CASE3.g1, CASE3.g2);

        assertThat(mcs.M).hasSizeBetween(1, 5);
        assertThat(mcs.M).extracting(p -> p.v1).doesNotHaveDuplicates();
        assertThat(mcs.M).extracting(p -> p.v2).doesNotHaveDuplicates();
    }

    @Test
    public void shouldFindMaximumCommonSubgraphForTestCase4() {
        Genetic genetic = new Genetic(256, 512, 32, 4, 0.9, 0.5);

        MaximumCommonSubgraph mcs = genetic.compute(CASE4.g1, CASE4.g2);

        assertThat(mcs.M).hasSizeBetween(1, 5);
        assertThat(mcs.M).extracting(p -> p.v1).doesNotHaveDuplicates();
        assertThat(mcs.M).extracting(p -> p.v2).doesNotHaveDuplicates();
    }

    @Test
    public void shouldFindMaximumCommonSubgraphForIndenticalGraphs() {
        Genetic genetic = new Genetic(256, 512, 32, 4, 0.9, 0.5);

        MaximumCommonSubgraph mcs = genetic.compute(CASE1.g1, CASE1.g1);

        System.out.println(mcs.M);
    }

    @Test
    public void shouldComputeDistanceMatrix() {
        int[][] D = Genetic.createDistanceMatrix(CASE3.g1);

        assertThat(D).isDeepEqualTo(new int[][] { { 0, 2, 2, 1, 1 }, { 2, 0, 1, 3, 1 }, { 2, 1, 0, 3, 1 },
                { 1, 3, 3, 0, 2 }, { 1, 1, 1, 2, 0 } });
    }

    @Test
    public void shouldCalculateScore() {
        int[][] D1 = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 } };
        int[][] D2 = new int[][] { { 9, 8, 7 }, { 6, 5, 4 }, { 3, 2, 1 } };
        List<Integer> genes = newArrayList(1, 2, 0);

        int score = Genetic.fitnessFunction(D1, D2, genes);

        assertThat(score).isEqualTo(20);
    }

    @Test
    public void shouldReduceToMaximumCommonSubgraph() {
        int[][] D1 = Genetic.createDistanceMatrix(CASE4.g1);
        int[][] D2 = Genetic.createDistanceMatrix(CASE4.g2);
        List<Integer> genes = newArrayList(0, 5, 3, 2, 1);

        List<Pair> reduced = Genetic.reduceFunction(D1, D2, genes);

        assertThat(reduced).containsOnly(new Pair(0, 0), new Pair(1, 5), new Pair(2, 3), new Pair(4, 1));
    }

    @Test
    public void shouldReduceToCommonSubgraph() {
        int[][] D1 = Genetic.createDistanceMatrix(CASE4.g1);
        int[][] D2 = Genetic.createDistanceMatrix(CASE4.g2);
        List<Integer> genes = newArrayList(2, 5, 3, 0, 1);

        List<Pair> reduced = Genetic.reduceFunction(D1, D2, genes);

        assertThat(reduced).containsOnly(new Pair(1, 5), new Pair(2, 3), new Pair(4, 1));
    }

    @Test
    public void shouldReduceForIdenticalGraphs() {
        int[][] D1 = Genetic.createDistanceMatrix(CASE1.g1);
        int[][] D2 = Genetic.createDistanceMatrix(CASE1.g1);
        List<Integer> genes = newArrayList(0, 1, 2, 3, 4, 5, 6, 7);

        List<Pair> reduced = Genetic.reduceFunction(D1, D2, genes);

        assertThat(reduced).containsOnly(new Pair(0, 0), new Pair(1, 1), new Pair(2, 2), new Pair(3, 3), new Pair(4, 4),
                new Pair(5, 5), new Pair(6, 6), new Pair(7, 7));
    }

    public static class GenomeTest {

        @Test
        public void shouldCrossover() {
            Genome genome1 = Genome.create(newArrayList(0, 1, 2, 3, 4, 5, 6, 7), EMPTY_D, EMPTY_D);
            Genome genome2 = Genome.create(newArrayList(7, 4, 1, 0, 2, 5, 3, 6), EMPTY_D, EMPTY_D);

            List<Genome> children = genome1.crossover(genome2);

            assertThat(children).hasSize(4);
            assertThat(children.get(0)).isEqualTo(genome1);
            assertThat(children.get(1)).isEqualTo(genome2);
            assertThat(children.get(2).genes).containsExactly(0, 4, 1, 3, 2, 5, 6, 7);
            assertThat(children.get(3).genes).containsExactly(7, 1, 2, 0, 4, 5, 3, 6);
        }
    }

    public static class PopulationTest {

        @Test
        public void shouldGeneratePopulation() {
            int nGenomes = 4;
            int n2 = 3;

            Population population = Population.generate(nGenomes, n2, 2, EMPTY_D, EMPTY_D);

            assertThat(population.genomes).hasSize(4);
            for (int i = 0; i < 4; i++) {
                assertThat(population.get(i).genes).containsOnly(0, 1, 2);
            }
        }

        @Test
        public void shouldShuffleWithinTribes() {
            Genome genome1 = new Genome(newArrayList(), EMPTY_D, EMPTY_D, 0);
            Genome genome2 = new Genome(newArrayList(), EMPTY_D, EMPTY_D, 0);
            Genome genome3 = new Genome(newArrayList(), EMPTY_D, EMPTY_D, 0);
            Genome genome4 = new Genome(newArrayList(), EMPTY_D, EMPTY_D, 0);
            Genome genome5 = new Genome(newArrayList(), EMPTY_D, EMPTY_D, 0);
            Genome genome6 = new Genome(newArrayList(), EMPTY_D, EMPTY_D, 0);
            Population population = new Population(newArrayList(genome1, genome2, genome3, genome4, genome5, genome6),
                    3);

            population.shuffleWithinTribe();

            assertThat(population.genomes.subList(0, 2)).containsOnly(genome1, genome2);
            assertThat(population.genomes.subList(2, 4)).containsOnly(genome3, genome4);
            assertThat(population.genomes.subList(4, 6)).containsOnly(genome5, genome6);
        }
    }
}
