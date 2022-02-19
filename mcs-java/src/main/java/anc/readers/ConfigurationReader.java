package anc.readers;

import static com.google.common.collect.Lists.newArrayList;

import java.util.Scanner;
import java.util.function.Function;

import anc.models.AlgorithmType;
import anc.models.Configuration;
import anc.models.GeneticConfiguration;
import anc.models.ProblemType;

public class ConfigurationReader {

    public static Configuration read() {
        final Scanner in = new Scanner(System.in);

        final ProblemType problemType = readProblemType(in);
        final AlgorithmType algorithmType = readAlgorithmType(in);
        GeneticConfiguration geneticConfiguration = null;
        if (algorithmType == AlgorithmType.GENETIC) {
            geneticConfiguration = readGeneticConfiguration(in);
        }
        final String inputFilePath = readInputFilePath(in);

        in.close();
        return new Configuration(problemType, algorithmType, geneticConfiguration, inputFilePath);
    }

    private static ProblemType readProblemType(Scanner in) {
        System.out.println("Select a problem you want to solve (1/2):");
        System.out.println("1. Maximum common subgraph");
        System.out.println("2. Minimum common supergraph");

        final int problemTypeInput = readInt(in, v -> !newArrayList(1, 2).contains(v),
                "You must type 1 or 2 when selecting problem type!");

        return (problemTypeInput == 1) ? ProblemType.MAXIMUM_COMMON_SUBGRAPH : ProblemType.MINIMUM_COMMON_SUPERGRAPH;
    }

    private static AlgorithmType readAlgorithmType(Scanner in) {
        System.out.println("Select which algorithm should be used (1/2):");
        System.out.println("1. Mc Split");
        System.out.println("2. Genetic");

        final int algorithmTypeInput = readInt(in, v -> !newArrayList(1, 2).contains(v),
                "You must type 1 or 2 when selecting algorithm type!");

        return (algorithmTypeInput == 1) ? AlgorithmType.MC_SPLIT : AlgorithmType.GENETIC;
    }

    private static GeneticConfiguration readGeneticConfiguration(Scanner in) {
        System.out.println("Do you want to configure genetic algorithm? (y/n):");

        final char configurationInput = in.next().charAt(0);
        if (!newArrayList('y', 'n').contains(configurationInput)) {
            throw new RuntimeException(
                    "You must type y or n when deciding if you want to configure genetic algorithm!");
        }

        if (configurationInput == 'n') {
            return GeneticConfiguration.fromPaper();
        }

        System.out.println("Provide the size of population:");
        final int nGenomes = readInt(in, v -> v < 1, "Population size must be an integer, and at least 1!");

        System.out.println("Provide number of generations:");
        final int nGenerations = readInt(in, v -> v < 1, "Number of generations must be an integer, and at least 1!");

        System.out.println("Provide number of generation after which entire population is shuffled:");
        final int nCb = readInt(in, v -> v < 1 || v >= nGenerations,
                "Number of generation after which entire population is shuffled must be an integer, at least 1, and smaller then number of generations!");

        System.out.println("Provide number of tribes (size of the population must be devisable by it):");
        final int nTribes = readInt(in, v -> v < 1 || nGenomes % v != 0,
                "Number of tribes must be an integer, at least 1, and size of population must be devisable by it!");

        System.out.println("Provide probability of crossover (between 0 and 1):");
        final double pCrossover = readDouble(in, v -> v < 0 || v > 1,
                "Probability of crossover must be a floating point number between 0 and 1 (inclusive)!");

        System.out.println("Provide probability of mutation (between 0 and 1):");
        final double pMutation = readDouble(in, v -> v < 0 || v > 1,
                "Probability of mutation must be a floating point number between 0 and 1 (inclusive)!");

        return new GeneticConfiguration(nGenomes, nGenerations, nCb, nTribes, pCrossover, pMutation);
    }

    private static String readInputFilePath(Scanner in) {
        in.nextLine();
        System.out.println("Please provide input file path:");
        return in.nextLine();
    }

    private static int readInt(Scanner in, Function<Integer, Boolean> throwCondition, String errorMessage) {
        if (!in.hasNextInt()) {
            throw new RuntimeException(errorMessage);
        }
        final int value = in.nextInt();
        if (throwCondition.apply(value)) {
            throw new RuntimeException(errorMessage);
        }
        return value;
    }

    private static double readDouble(Scanner in, Function<Double, Boolean> throwCondition, String errorMessage) {
        if (!in.hasNextDouble()) {
            throw new RuntimeException(errorMessage);
        }
        final double value = in.nextDouble();
        if (throwCondition.apply(value)) {
            throw new RuntimeException(errorMessage);
        }
        return value;
    }

}
