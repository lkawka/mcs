package anc;

import anc.algorithms.Genetic;
import anc.algorithms.MaximumCommonSubgraphAlgorithm;
import anc.algorithms.McSplit;
import anc.algorithms.MinimumCommonSupergraphAlgorithm;
import anc.models.AlgorithmType;
import anc.models.Configuration;
import anc.models.Graph;
import anc.models.Input;
import anc.models.MaximumCommonSubgraph;
import anc.models.MinimumCommonSupergraph;
import anc.models.ProblemType;
import anc.readers.ConfigurationReader;
import anc.readers.InputReader;

public class App {
    public static void main(String[] args) throws Exception {
        try {
            Configuration configuration = ConfigurationReader.read();

            Input input = InputReader.read(configuration.inputFilePath);
            input.g1.validate();
            input.g2.validate();

            // Genetic algorithm requires that |g1| <= |g2|,
            // so if |g1| > |g2| then we need to swap them
            Graph g1;
            Graph g2;
            boolean areGraphsSwapped = false;
            if (input.g1.n > input.g2.n) {
                areGraphsSwapped = true;
                g1 = input.g2;
                g2 = input.g1;
            } else {
                g1 = input.g1;
                g2 = input.g2;
            }

            MaximumCommonSubgraphAlgorithm maximumCommonSubgraphAlgorithm;
            if (configuration.algorithmType == AlgorithmType.MC_SPLIT) {
                maximumCommonSubgraphAlgorithm = new McSplit();
            } else { // configuration.algorithmType == AlgorithmType.GENETIC
                maximumCommonSubgraphAlgorithm = Genetic.fromConfiguration(configuration.geneticConfiguration);
            }

            if (configuration.problemType == ProblemType.MAXIMUM_COMMON_SUBGRAPH) {
                MaximumCommonSubgraph maximumCommonSubgraph = maximumCommonSubgraphAlgorithm.compute(g1, g2);
                Display.displayMaximumCommonSubgraph(g1, g2, maximumCommonSubgraph, areGraphsSwapped);
            } else { // configuration.problemType == ProblemType.MINIMUM_COMMON_SUPERGRAPH
                MinimumCommonSupergraphAlgorithm minimumCommonSupergraphAlgorithm = new MinimumCommonSupergraphAlgorithm(
                        maximumCommonSubgraphAlgorithm);
                MinimumCommonSupergraph minimumCommonSupergraph = minimumCommonSupergraphAlgorithm.compute(g1, g2);
                Display.displayMinimumCommonSupergraph(g1, g2, minimumCommonSupergraph, areGraphsSwapped);
            }
        } catch (RuntimeException exception) {
            System.out.println("Something went wrong and program failed! Reason: " + exception.getMessage());
        }
    }
}
