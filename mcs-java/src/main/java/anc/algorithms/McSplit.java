package anc.algorithms;

import static com.google.common.collect.Lists.newArrayList;
import static java.lang.String.format;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import anc.models.Graph;
import anc.models.MaximumCommonSubgraph;
import anc.models.Pair;

public class McSplit implements MaximumCommonSubgraphAlgorithm {
    private Graph g1;
    private Graph g2;
    private List<Pair> bestM;

    @Override
    public MaximumCommonSubgraph compute(Graph g1, Graph g2) {
        this.g1 = g1;
        this.g2 = g2;
        bestM = newArrayList();
        search(LabelClasses.from(g1, g2), newArrayList());
        final List<Pair> sortedM = bestM.stream().sorted(Comparator.comparingInt(p -> p.v1)).collect(Collectors.toList());
        return new MaximumCommonSubgraph(sortedM, g1, g2);
    }

    private void search(LabelClasses labelClasses, List<Pair> M) {
        if (M.size() > bestM.size()) {
            bestM = newArrayList(M);
        }

        int bound = M.size() + labelClasses.sumPairsOfVertices();
        if (bound <= bestM.size()) {
            return;
        }

        LabelClass selectedLabelClass = labelClasses.select();
        int v = selectedLabelClass.selectVertexFromG();
        for (int w : selectedLabelClass.H) {
            LabelClasses newLabelClasses = LabelClasses.empty();
            for (LabelClass labelClass : labelClasses.labelClasses) {
                List<Integer> neighborsOfV = labelClass.neighborsFromGOf(v);
                List<Integer> neighborsOfW = labelClass.neighborsFromHOf(w);
                if (!neighborsOfV.isEmpty() && !neighborsOfW.isEmpty()) {
                    newLabelClasses.add(new LabelClass(g1, g2, neighborsOfV, neighborsOfW));
                }
                List<Integer> notNeighborsOfV = labelClass.notNeighborsFromGOf(v);
                List<Integer> notNeighborsOfW = labelClass.notNeighborsFromHOf(w);
                if (!notNeighborsOfV.isEmpty() && !notNeighborsOfW.isEmpty()) {
                    newLabelClasses.add(new LabelClass(g1, g2, notNeighborsOfV, notNeighborsOfW));
                }
            }
            M.add(new Pair(v, w));
            search(newLabelClasses, M);
            M.remove(M.size() - 1);
        }
        labelClasses.remove(selectedLabelClass);
        if (selectedLabelClass.G.size() > 1) {
            labelClasses.add(selectedLabelClass.copyWithoutVertexFromG(v));
        }
        if (!labelClasses.labelClasses.isEmpty()) {
            search(labelClasses, M);
        }

    }

    static class LabelClass {
        private final Graph g1;
        private final Graph g2;

        public final List<Integer> G;
        public final List<Integer> H;
        public final Integer maxDegreeG;
        public final Integer maxDegreeVertexG;

        public LabelClass(Graph g1, Graph g2, List<Integer> G, List<Integer> H) {
            this.g1 = g1;
            this.g2 = g2;
            this.G = G;
            this.H = H;

            if (G.isEmpty()) {
                this.maxDegreeG = null;
                this.maxDegreeVertexG = null;
            } else {
                int maxDegreeG = g1.degreeOf(G.get(0));
                int maxDegreeVertexG = G.get(0);
                for (int i = 1; i < G.size(); i++) {
                    int degree = g1.degreeOf(G.get(i));
                    if (degree > maxDegreeG) {
                        maxDegreeG = degree;
                        maxDegreeVertexG = G.get(i);
                    }
                }
                this.maxDegreeG = maxDegreeG;
                this.maxDegreeVertexG = maxDegreeVertexG;
            }
        }

        public LabelClass copyWithoutVertexFromG(int vertex) {
            return new LabelClass(g1, g2, G.stream().filter(v -> v != vertex).collect(Collectors.toList()), H);
        }

        public int selectVertexFromG() {
            return maxDegreeVertexG;
        }

        public List<Integer> neighborsFromGOf(int vertex) {
            return G.stream().filter(v -> g1.M[vertex][v] == 1).collect(Collectors.toList());
        }

        public List<Integer> neighborsFromHOf(int vertex) {
            return H.stream().filter(v -> g2.M[vertex][v] == 1).collect(Collectors.toList());
        }

        public List<Integer> notNeighborsFromGOf(int vertex) {
            return G.stream().filter(v -> v != vertex && g1.M[vertex][v] == 0).collect(Collectors.toList());
        }

        public List<Integer> notNeighborsFromHOf(int vertex) {
            return H.stream().filter(v -> v != vertex && g2.M[vertex][v] == 0).collect(Collectors.toList());
        }

        @Override
        public String toString() {
            return "LabelClass [G=" + G + ", H=" + H + "]";
        }
    }

    static class LabelClasses {
        public final List<LabelClass> labelClasses;

        public LabelClasses(List<LabelClass> labelClasses) {
            this.labelClasses = labelClasses;
        }

        public static LabelClasses empty() {
            return new LabelClasses(new ArrayList<>());
        }

        public static LabelClasses from(Graph g1, Graph g2) {
            List<Integer> G = IntStream.range(0, g1.n).boxed().collect(Collectors.toList());
            List<Integer> H = IntStream.range(0, g2.n).boxed().collect(Collectors.toList());
            return new LabelClasses(newArrayList(new LabelClass(g1, g2, G, H)));
        }

        public void add(LabelClass labelClass) {
            labelClasses.add(labelClass);
        }

        public void remove(LabelClass labelClass) {
            if (!labelClasses.remove(labelClass)) {
                throw new RuntimeException(format("Unable to remove label class %s!", labelClass));
            }
        }

        public int sumPairsOfVertices() {
            int sum = 0;
            for (LabelClass labelClass : labelClasses) {
                sum += Math.min(labelClass.G.size(), labelClass.H.size());
            }
            return sum;
        }

        /**
         * Selects a label class with the smallest max(|G|,|H|) and breaks ties by
         * selecting a class containing a vertex in G with the largest degree
         */
        public LabelClass select() {
            // TODO: Speed up
            LabelClass best = labelClasses.get(0);
            int maxVerticesBest = Math.max(best.G.size(), best.H.size());
            for (LabelClass labelClass : labelClasses) {
                int maxVerticesLabelClass = Math.max(labelClass.G.size(), labelClass.H.size());
                if (maxVerticesBest < maxVerticesLabelClass) {
                    best = labelClass;
                    maxVerticesBest = maxVerticesLabelClass;
                } else if (maxVerticesLabelClass == maxVerticesBest) {
                    if (best.maxDegreeG < labelClass.maxDegreeG) {
                        best = labelClass;
                        maxVerticesBest = maxVerticesLabelClass;
                    }
                }
            }
            return best;
        }

        @Override
        public String toString() {
            return "LabelClasses [labelClasses=" + labelClasses + "]";
        }
    }
}
