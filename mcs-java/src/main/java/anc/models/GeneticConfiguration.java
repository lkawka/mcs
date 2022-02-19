package anc.models;

public class GeneticConfiguration {
    public final int nGenomes;
    public final int nGenerations;
    public final int nCb;
    public final int nTribes;
    public final double pCrossover;
    public final double pMutation;


    public GeneticConfiguration(int nGenomes, int nGenerations, int nCb, int nTribes, double pCrossover,
            double pMutation) {
        this.nGenomes = nGenomes;
        this.nGenerations = nGenerations;
        this.nCb = nCb;
        this.nTribes = nTribes;
        this.pCrossover = pCrossover;
        this.pMutation = pMutation;
    }

    public static GeneticConfiguration fromPaper() {
        return new GeneticConfiguration(256, 512, 32, 4, 0.9, 0.5);
    }
}
