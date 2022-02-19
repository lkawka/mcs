package anc.models;

public class Configuration {
    public final ProblemType problemType;
    public final AlgorithmType algorithmType;
    public final GeneticConfiguration geneticConfiguration;
    public final String inputFilePath;

    public Configuration(ProblemType problemType, AlgorithmType algorithmType, String inputFilePath) {
        this.problemType = problemType;
        this.algorithmType = algorithmType;
        this.geneticConfiguration = null;
        this.inputFilePath = inputFilePath;
    }

    public Configuration(ProblemType problemType, AlgorithmType algorithmType, GeneticConfiguration geneticConfiguration, String inputFilePath) {
        this.problemType = problemType;
        this.algorithmType = algorithmType;
        this.geneticConfiguration = geneticConfiguration;
        this.inputFilePath = inputFilePath;
    }
}
