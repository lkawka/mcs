package anc.other;

import static com.google.common.collect.Lists.newArrayList;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;

import anc.models.Graph;
import anc.models.Input;

public class InputGenerator {
    private static final String PATH = "inputs/";
    private static final Random RANDOM_PATTERN1 = new Random(17);
    private static final Random RANDOM_PATTERN2 = new Random(13);

    public static void main(String[] args) throws Exception {
        for (int n : newArrayList(2, 5, 10, 15, 20, 21, 25, 50, 75, 100)) {
            generatePattern1File(n);
        }

        for (int n : newArrayList(5, 10, 15, 20, 25, 28, 30, 50, 75, 100, 150, 200)) {
            generatePattern2File(n);
        }

        for (int n : newArrayList(4, 10, 14, 18, 50, 74, 100, 150, 200)) {
            generatePattern3File(n);
        }

        generateIsomorphicGraphs1File();
    }

    private static void generatePattern1File(int n) {
        Input input = generatePattern1(n);
        String size = (n < 5 ? "tiny" : (n < 15 ? "small" : (n < 25 ? "medium" : "big"))) + "_";
        writeInputToFile(input,
                PATH + "smaller_graph_isomorphic_to_subgraph__pattern1__" + size + n + "x" + (2 * n - 1) + ".txt");
    }

    private static void generateIsomorphicGraphs1File() {
        final Input input = generateIsomorphicGraphs1();
        writeInputToFile(input, PATH + "isomorphic_graphs__small_7x7.txt");
    }

    private static void generatePattern2File(int n) {
        final Input input = generatePattern2(n);
        String size = (n < 5 ? "tiny" : (n < 15 ? "small" : (n < 35 ? "medium" : "big"))) + "_" + n + "x" + n;
        writeInputToFile(input, PATH + "isomorphic_graphs__pattern2__" + size + ".txt");
    }

    private static void generatePattern3File(int n) {
        final Input input = generatePattern3(n);
        String size = (n < 4 ? "tiny" : (n < 14 ? "small" : (n < 24 ? "medium" : "big"))) + "_" + n + "x" + (n + 1);
        writeInputToFile(input, PATH + "nonisomorphic_graphs__pattern3__" + size + ".txt");
    }

    private static Input generatePattern1(int n) {
        final int n1 = n;
        final ListMultimap<Integer, Integer> l1 = ArrayListMultimap.create();
        for (int i = 0; i < n - 1; i++) {
            l1.put(i, i + 1);
        }
        final Graph g1 = new Graph(n1, createAdjacencyMatrix(n1, l1));

        final int n2 = 2 * n - 1;
        final ListMultimap<Integer, Integer> l2 = ArrayListMultimap.create();
        for (int i = 0; i < 2 * n - 2; i += 2) {
            l2.put(i, i + 1);
            l2.put(i, i + 2);
            l2.put(i + 1, i + 2);
        }
        final Graph g2 = new Graph(n2, createAdjacencyMatrix(n2, l2));

        return new Input(g1, g2);
    }

    private static Input generateIsomorphicGraphs1() {
        final int n = 7;
        final Graph g1 = new Graph(n, createAdjacencyMatrix(n, new ImmutableListMultimap.Builder<Integer, Integer>()
                .putAll(0, 1, 2).putAll(1, 2).putAll(2, 3).putAll(3, 4, 5).putAll(5, 6).build()));
        final Graph g2 = new Graph(n, createAdjacencyMatrix(n, new ImmutableListMultimap.Builder<Integer, Integer>()
                .putAll(0, 2, 3, 6).putAll(1, 3, 5).putAll(3, 5).putAll(4, 6).build()));
        return new Input(g1, g2);
    }

    private static Input generatePattern2(int n) {
        if (n < 4) {
            throw new RuntimeException("Pattern 2 must have at least 4 vertices!");
        }

        final ListMultimap<Integer, Integer> l1 = ArrayListMultimap.create();
        final ListMultimap<Integer, Integer> l2 = ArrayListMultimap.create();

        final List<Integer> g1Indeces = IntStream.range(0, n).boxed().collect(Collectors.toList());
        Collections.shuffle(g1Indeces, RANDOM_PATTERN1);
        final List<Integer> g2Indeces = IntStream.range(0, n).boxed().collect(Collectors.toList());
        Collections.shuffle(g2Indeces, RANDOM_PATTERN2);

        for (int i = 1; i < n; i++) {
            l1.put(g1Indeces.get(0), g1Indeces.get(i));
            l2.put(g2Indeces.get(0), g2Indeces.get(i));
        }

        for (int i = 1; i < n - 1; i++) {
            l1.put(g1Indeces.get(i), g1Indeces.get(i + 1));
            l2.put(g2Indeces.get(i), g2Indeces.get(i + 1));
        }

        l1.put(g1Indeces.get(1), g1Indeces.get(n - 1));
        l2.put(g2Indeces.get(1), g2Indeces.get(n - 1));

        final Graph g1 = new Graph(n, createAdjacencyMatrix(n, l1));
        final Graph g2 = new Graph(n, createAdjacencyMatrix(n, l2));
        return new Input(g1, g2);
    }

    private static Input generatePattern3(int n) {
        if (n % 2 == 1) {
            throw new RuntimeException("n must be divisible by 2!");
        }

        final ListMultimap<Integer, Integer> l1 = ArrayListMultimap.create();
        for (int i = 1; i < n; i++) {
            l1.put(0, i);
        }
        for (int i = 1; i < n - 1; i++) {
            l1.put(i, i + 1);
        }
        l1.put(1, n - 1);
        final Graph g1 = new Graph(n, createAdjacencyMatrix(n, l1));

        final ListMultimap<Integer, Integer> l2 = ArrayListMultimap.create();
        final int n2 = n + 1;
        for (int i = 0; i < n2 - 2; i += 2) {
            l2.put(i, i + 1);
            l2.put(i, i + 2);
            l2.put(i + 1, i + 2);
        }
        final Graph g2 = new Graph(n2, createAdjacencyMatrix(n2, l2));

        return new Input(g1, g2);
    }

    private static void writeInputToFile(Input input, String filePath) {

        PrintWriter writer;
        try {
            writer = new PrintWriter(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        writer.println(input.g1.n);
        for (int i = 0; i < input.g1.n; i++) {
            for (int j = 0; j < input.g1.n; j++) {
                writer.print("" + input.g1.M[i][j] + " ");
            }
            writer.print("\n");
        }

        writer.println(input.g2.n);
        for (int i = 0; i < input.g2.n; i++) {
            for (int j = 0; j < input.g2.n; j++) {
                writer.print("" + input.g2.M[i][j] + " ");
            }
            writer.print("\n");
        }

        writer.close();
    }

    private static int[][] createAdjacencyMatrix(int n, ListMultimap<Integer, Integer> adjacencyList) {
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
