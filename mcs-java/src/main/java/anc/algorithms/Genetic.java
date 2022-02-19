package anc.algorithms;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import anc.models.GeneticConfiguration;
import anc.models.Graph;
import anc.models.MaximumCommonSubgraph;
import anc.models.Pair;

public class Genetic implements MaximumCommonSubgraphAlgorithm {
    static final int SEED = 13;
    static final Random random = new Random(SEED);

    private final int nGenomes;
    private final int nGenerations;
    private final int nCb;
    private final int nTribes;
    private final double pCrossover;
    private final double pMutation;

    public Genetic(int nGenomes, int nGenerations, int nCb, int nTribes, double pCrossover, double pMutation) {
        if (nGenomes % nTribes != 0) {
            throw new RuntimeException(format("Number of genomes %s must be devisable by the number of tribes %s!",
                    nGenerations, nTribes));
        }

        this.nGenomes = nGenomes;
        this.nGenerations = nGenerations;
        this.nCb = nCb;
        this.nTribes = nTribes;
        this.pCrossover = pCrossover;
        this.pMutation = pMutation;
    }

    public static Genetic fromConfiguration(GeneticConfiguration conf) {
        return new Genetic(conf.nGenomes, conf.nGenerations, conf.nCb, conf.nTribes, conf.pCrossover, conf.pMutation);
    }

    @Override
    public MaximumCommonSubgraph compute(Graph g1, Graph g2) {
        if (g1.n > g2.n) {
            throw new RuntimeException("Graph 1 must not be bigger than graph 2!");
        }

        final int[][] D1 = createDistanceMatrix(g1);
        final int[][] D2 = createDistanceMatrix(g2);
        final Population population = Population.generate(nGenomes, g2.n, nTribes, D1, D2);
        final List<Integer> shuffledTribeIndeces = IntStream.range(0, population.tribeSize).boxed()
                .collect(Collectors.toList());

        Genome bestGenome = population.genomes.stream().max(Comparator.comparingDouble(genome -> genome.score)).get();

        for (int generation = 0; generation < nGenerations; generation++) {
            // Shuffling
            if (generation % nCb == 0) {
                population.shuffleAll();
            } else {
                population.shuffleWithinTribe();
            }

            // Crossbreading
            for (int tribe = 0; tribe < nTribes; tribe++) {
                Collections.shuffle(shuffledTribeIndeces, random);
                for (int i = 1; i < population.tribeSize; i += 2) {
                    if (random.nextDouble() > pCrossover) {
                        continue;
                    }
                    final int tribeStart = tribe * population.tribeSize;
                    final int index1 = tribeStart + shuffledTribeIndeces.get(i - 1);
                    final int index2 = tribeStart + shuffledTribeIndeces.get(i);

                    int indexWithSmallerScore = index1;
                    int indexWithBiggerScore = index2;
                    if (population.get(index1).score > population.get(index2).score) {
                        indexWithSmallerScore = index2;
                        indexWithBiggerScore = index1;
                    }

                    final List<Genome> childeren = population.get(index1).crossover(population.get(index2)).stream()
                            .sorted(Comparator.comparingDouble(g -> g.score)).collect(Collectors.toList());

                    population.set(indexWithSmallerScore, childeren.get(childeren.size() - 2));
                    population.set(indexWithBiggerScore, childeren.get(childeren.size() - 1));

                    if (bestGenome.score < population.get(indexWithBiggerScore).score) {
                        bestGenome = population.get(indexWithBiggerScore).copy();
                    }
                }
            }

            // Mutating
            for (Genome genome : population.genomes) {
                if (random.nextDouble() <= pMutation) {
                    genome.mutate();
                    if (bestGenome.score < genome.score) {
                        bestGenome = genome.copy();
                    }
                }
            }
        }

        final List<Pair> M = reduceFunction(D1, D2, bestGenome.genes).stream()
                .sorted(Comparator.comparingInt(p -> p.v1)).collect(Collectors.toList());
        return new MaximumCommonSubgraph(M, g1, g2, bestGenome.genes);
    }

    static int[][] createDistanceMatrix(Graph g) {
        final int[][] D = new int[g.n][g.n];
        final int[] explored = new int[g.n];

        for (int i = 0; i < g.n; i++) {
            for (int j = 0; j < g.n; j++) {
                D[i][j] = g.n - 1;
            }
            D[i][i] = 0;
        }

        for (int i = 0; i < g.n; i++) {
            // BFS
            final Queue<Integer> queue = new LinkedList<>();
            explored[i] = i + 1;
            queue.add(i);
            while (!queue.isEmpty()) {
                final int v = queue.poll();
                for (int w = 0; w < g.n; w++) {
                    if (v == w || g.M[v][w] == 0 || explored[w] == i + 1) {
                        continue;
                    }
                    explored[w] = i + 1;
                    final int newDistance = Math.min(D[i][w], D[i][v] + 1);
                    D[i][w] = newDistance;
                    D[w][i] = newDistance;
                    queue.add(w);
                }
            }
        }

        return D;
    }

    static int fitnessFunction(int[][] D1, int[][] D2, List<Integer> genes) {
        final int n1 = D1.length;
        int sum = 0;
        for (int i = 0; i < n1; i++) {
            for (int j = 0; j < n1; j++) {
                sum += Math.abs(D1[i][j] - D2[genes.get(i)][genes.get(j)]);
            }
        }
        return sum;
    }

    static List<Pair> reduceFunction(int[][] D1, int[][] D2, List<Integer> genes) {
        final int n1 = D1.length;
        final int[] skipped = new int[n1];
        Arrays.fill(skipped, -1);
        List<Pair> M = new ArrayList<>();
        for (int skip = 0; skip < n1 - 1; skip++) {
            for (int i = 0; i < skip; i++) {
                skipped[i] = skip;
            }
            for (int i = skip; i < n1; i++) {
                int tmpSum = 0;
                for (int j = skip; j <= i; j++) {
                    if (skipped[j] == skip) {
                        continue;
                    }
                    for (int k = skip; k <= i; k++) {
                        if (skipped[k] == skip) {
                            continue;
                        }
                        tmpSum += Math.abs(D1[j][k] - D2[genes.get(j)][genes.get(k)]);
                    }
                }
                if (tmpSum > 0) {
                    skipped[i] = skip;
                }
            }
            final List<Pair> Mcandidate = new ArrayList<>();
            for (int i = 0; i < n1; i++) {
                if (skipped[i] < skip) {
                    Mcandidate.add(new Pair(i, genes.get(i)));
                }
            }
            if (M.size() < Mcandidate.size()) {
                M = Mcandidate;
            }
        }
        return M;
    }

    public static class Genome {
        public final List<Integer> genes;
        public int score;
        public final int[][] D1;
        public final int[][] D2;

        public Genome(List<Integer> genes, int[][] D1, int[][] D2, int score) {
            this.genes = newArrayList(genes);
            this.D1 = D1;
            this.D2 = D2;
            this.score = score;
        }

        public static Genome create(List<Integer> genes, int[][] D1, int[][] D2) {
            final int score = fitnessFunction(D1, D2, genes);
            return new Genome(genes, D1, D2, score);
        }

        public void mutate() {
            final int index1 = random.nextInt(genes.size());
            int index2 = random.nextInt(genes.size() - 1);
            if (index1 == index2) {
                index2 += 1;
            }
            Collections.swap(genes, index1, index2);

            final int oldScore = score;
            recalculateScore();
            if (score < oldScore) {
                // Undo swap
                Collections.swap(genes, index1, index2);
                score = oldScore;
            }
        }

        public List<Genome> crossover(Genome otherParent) {
            final int nGenes = genes.size();
            final List<Integer> childGenes1 = new ArrayList<>(Collections.nCopies(nGenes, -1));
            final List<Integer> childGenes2 = new ArrayList<>(Collections.nCopies(nGenes, -1));

            int nCycle = 0;
            for (int i = 0; i < nGenes; i++) {
                int j = i;
                boolean newCycle = false;
                while (childGenes1.get(j) < 0) {
                    newCycle = true;
                    if (nCycle % 2 == 0) {
                        childGenes1.set(j, genes.get(j));
                        childGenes2.set(j, otherParent.genes.get(j));
                    } else {
                        childGenes1.set(j, otherParent.genes.get(j));
                        childGenes2.set(j, genes.get(j));
                    }
                    j = genes.indexOf(otherParent.genes.get(j));
                }
                if (newCycle) {
                    nCycle += 1;
                }
            }

            return newArrayList(this, otherParent, Genome.create(childGenes1, D1, D2),
                    Genome.create(childGenes2, D1, D2));
        }

        public Genome copy() {
            return new Genome(genes, D1, D2, score);
        }

        private void recalculateScore() {
            this.score = fitnessFunction(D1, D2, genes);
        }
    }

    public static class Population {
        public final List<Genome> genomes;
        public final int nTribes;
        public final int tribeSize;

        public Population(List<Genome> genomes, int nTribes) {
            this.genomes = newArrayList(genomes);
            this.nTribes = nTribes;
            this.tribeSize = genomes.size() / nTribes;

            if (tribeSize % 2 == 1) {
                throw new RuntimeException(format("Tribe size %s must divisible by 2!", tribeSize));
            }
        }

        public static Population generate(int nGenomes, int genomeSize, int nTribes, int[][] D1, int[][] D2) {
            final List<Genome> genomes = new ArrayList<>(nGenomes);

            for (int i = 0; i < nGenomes; i++) {
                final List<Integer> genes = new ArrayList<>(genomeSize);
                for (int j = 0; j < genomeSize; j++) {
                    genes.add(j);
                }
                Collections.shuffle(genes, random);
                genomes.add(Genome.create(genes, D1, D2));
            }
            return new Population(genomes, nTribes);
        }

        public Genome get(int index) {
            return genomes.get(index);
        }

        public void set(int index, Genome genome) {
            genomes.set(index, genome);
        }

        public void shuffleAll() {
            Collections.shuffle(genomes, random);
        }

        public void shuffleWithinTribe() {
            for (int j = 0; j < nTribes; j++) {
                Collections.shuffle(genomes.subList(j * tribeSize, (j + 1) * tribeSize), random);
            }
        }
    }
}
