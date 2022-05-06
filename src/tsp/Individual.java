package tsp;

import java.util.Arrays;

public class Individual implements Comparable<Individual> {

    public TSPInstance instance;
    public int[] genotype;
    private double fitness;
    private double cost = Double.POSITIVE_INFINITY;
    private static int i = 0;
    private final int iam;

    public Individual(TSPInstance instance, int[] genotype) {
        this.instance = instance;
        this.genotype = genotype;
        this.fitness = -1.0;
        this.iam = i;
        i++;
    }

    private double calculateFitness() {

        int r = 0;
        for (int i = 0; i < genotype.length; i++) {
            int src = genotype[i];
            int dst = genotype[(i + 1) % genotype.length];
            int cost = instance.getCost(src, dst);
            /*if (cost == 0)
                return 0.0;*/
            r += cost;
        }

        cost = r;

        return (double) 1/r;
    }

    public double getFitness() {
        if (fitness == -1.0)
            fitness = calculateFitness();
        return fitness;
    }

    public int getIndexOf(int node) {

        for (int i = 0; i < genotype.length; i++) {
            if (genotype[i] == node)
                return i;
        }

        return -1;
    }

    @Override
    public int compareTo(Individual i) {
        return Double.compare(this.getFitness(), i.getFitness());
    }

    public String toString() {
        return "\n" +
                "I am: " + iam + "\n" +
                "Genotype: " + Arrays.toString(genotype) + "\n" +
                "Cost: " + cost + "\n" +
                "Fitness: " + getFitness() + "\n";
    }
}
