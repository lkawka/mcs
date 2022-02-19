package anc.other;

import static java.lang.String.format;

import java.util.function.Function;
import java.util.function.Supplier;

import anc.algorithms.Genetic;
import anc.algorithms.MaximumCommonSubgraphAlgorithm;
import anc.algorithms.McSplit;
import anc.algorithms.MinimumCommonSupergraphAlgorithm;
import anc.models.AlgorithmType;
import anc.models.GeneticConfiguration;
import anc.models.Input;
import anc.models.MaximumCommonSubgraph;
import anc.models.MinimumCommonSupergraph;
import anc.models.ProblemType;
import anc.readers.InputReader;

public class EmpiricalTesting {
    private static String RESOURCE_DIR = "src/main/resources/";
    private static String INPUT_2 = RESOURCE_DIR + "input2.txt";
    private static String INPUT_3 = RESOURCE_DIR + "input3.txt";
    private static String INPUT_4 = RESOURCE_DIR + "input4.txt";
    private static String INPUT_5 = RESOURCE_DIR + "input4.txt";
    private static Function<Integer, String> PATTERN1_N = (Integer n) -> RESOURCE_DIR + format("pattern1_n%s.txt", n);

    public static void main(String[] args) throws Exception {
        runAndDisplayEmpiricalTests(INPUT_2);
        runAndDisplayEmpiricalTests(INPUT_3);
        runAndDisplayEmpiricalTests(INPUT_4);
        runAndDisplayEmpiricalTests(INPUT_5);

        // Pattern 1
        runAndDisplayEmpiricalTests(PATTERN1_N.apply(10));
        runAndDisplayEmpiricalTests(PATTERN1_N.apply(15));
        runAndDisplayEmpiricalTests(PATTERN1_N.apply(20));
        runAndDisplayEmpiricalTests(AlgorithmType.GENETIC, PATTERN1_N.apply(25));
        runAndDisplayEmpiricalTests(AlgorithmType.GENETIC, PATTERN1_N.apply(50));
    }

    static void runAndDisplayEmpiricalTests(String inputFilePath) {
        final Input input = readAndDisplayInput(inputFilePath);

        testAndDisplay(ProblemType.MAXIMUM_COMMON_SUBGRAPH, AlgorithmType.MC_SPLIT, input);
        testAndDisplay(ProblemType.MAXIMUM_COMMON_SUBGRAPH, AlgorithmType.GENETIC, input);
        testAndDisplay(ProblemType.MINIMUM_COMMON_SUPERGRAPH, AlgorithmType.MC_SPLIT, input);
        testAndDisplay(ProblemType.MINIMUM_COMMON_SUPERGRAPH, AlgorithmType.GENETIC, input);

        System.out.println("");
        System.gc();
    }

    static void runAndDisplayEmpiricalTests(AlgorithmType algorithmType, String inputFilePath) {
        final Input input = readAndDisplayInput(inputFilePath);

        testAndDisplay(ProblemType.MAXIMUM_COMMON_SUBGRAPH, algorithmType, input);
        testAndDisplay(ProblemType.MINIMUM_COMMON_SUPERGRAPH, algorithmType, input);

        System.out.println("");
        System.gc();
    }

    static void runAndDisplayEmpiricalTests(ProblemType problemType, AlgorithmType algorithmType,
            String inputFilePath) {
        final Input input = readAndDisplayInput(inputFilePath);

        testAndDisplay(problemType, algorithmType, input);

        System.out.println("");
        System.gc();
    }

    static void testAndDisplay(ProblemType problemType, AlgorithmType algorithmType, Input input) {
        final MaximumCommonSubgraphAlgorithm subgraphAlgo = algorithmType == AlgorithmType.MC_SPLIT ? new McSplit()
                : Genetic.fromConfiguration(GeneticConfiguration.fromPaper());

        if (problemType == ProblemType.MAXIMUM_COMMON_SUBGRAPH) {
            final TimeItResult<MaximumCommonSubgraph> subgraphResult = timeIt(
                    () -> subgraphAlgo.compute(input.g1, input.g2));
            displayEmpiracalResult(subgraphResult);
        } else {
            final MinimumCommonSupergraphAlgorithm supergraphAlgo = new MinimumCommonSupergraphAlgorithm(subgraphAlgo);
            final TimeItResult<MinimumCommonSupergraph> supergraphResult = timeIt(
                    () -> supergraphAlgo.compute(input.g1, input.g2));
            displayEmpiracalResult(supergraphResult);
        }
    }

    static <T> void displayEmpiracalResult(TimeItResult<T> result) {
        final String indent = "- ";

        int n;
        if (result.result instanceof MaximumCommonSubgraph) {
            final MaximumCommonSubgraph mcs = (MaximumCommonSubgraph) result.result;
            final String algo = mcs.fromGenetic() ? "Genetic" : "McSplit";
            System.out.println(format("Maximum common subgraph(%s):", algo));
            n = mcs.M.size();
        } else {
            final MinimumCommonSupergraph MCS = (MinimumCommonSupergraph) result.result;
            final String algo = MCS.mcs.fromGenetic() ? "Genetic" : "McSplit";
            System.out.println(format("Minimum common supergraph(%s):", algo));
            n = MCS.M.size();
        }
        final long executionTime = result.executionTime;

        System.out.println(indent + format("Size: %s", n));
        System.out.println(indent + format("Execution time: %s milliseconds", executionTime));
    }

    static Input readAndDisplayInput(String inputFilePath) {
        final Input input = readInput(inputFilePath);
        System.out.println(format("Input file: %s", inputFilePath));
        System.out.println(format("Graphs of size: %s and %s", input.g1.n, input.g2.n));
        return input;
    }

    static Input readInput(String inputFilePath) {
        final Input input = InputReader.read(inputFilePath);
        input.g1.validate();
        input.g2.validate();

        // Genetic algorithm requires that |g1| <= |g2|,
        // so if |g1| > |g2| then we need to swap them
        if (input.g1.n > input.g2.n) {
            return new Input(input.g2, input.g1);
        }
        return input;
    }

    static <T> TimeItResult<T> timeIt(Supplier<T> function) {
        final long startTime = System.currentTimeMillis();
        final T result = function.get();
        final long endTime = System.currentTimeMillis();
        final long executionTime = (endTime - startTime);
        return new TimeItResult<T>(result, executionTime);
    }

    static class TimeItResult<T> {
        final T result;
        final long executionTime; // in milliseconds

        public TimeItResult(T result, long executionTime) {
            this.result = result;
            this.executionTime = executionTime;
        }
    }
}
