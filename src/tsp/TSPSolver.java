package tsp;

import crossover.CrossoverOperator;
import mutation.MutationOperator;
import survivors.SurvivorsOperator;

import java.util.ArrayList;
import java.util.Arrays;

public class TSPSolver {

    private final TSPInstance instance;

    private final CrossoverOperator crossover;
    private final MutationOperator mutation;
    private SurvivorsOperator survivors;
    private final int N;
    private final int m;
    private final double crossoverProbability;
    private final double mutationProbability;
    private final int k;
    private final int n;

    /*
     * 2) Configurar los distintos parámetros y componentes del algoritmo evolutivo (método
     * de generación de la población inicial, método de selección de padres, operador de cruce,
     * operador de mutación, método de selección de sobrevivientes, tamaño de la población
     * inicial, probabilidad de cruce, probabilidad de mutación, condición de corte, etc.
     */

    public TSPSolver(TSPInstance instance, CrossoverOperator crossover, MutationOperator mutation,
                     SurvivorsOperator survivors, int N, int m, double crossoverProbability, double mutationProbability,
                     int k, int n) {
        this.instance = instance;
        this.crossover = crossover;
        this.mutation = mutation;
        this.survivors = survivors;
        this.N = N;
        this.m = m;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.k = k;
        this.n = n;
    }

    public ArrayList<Individual> generateInitialPopulation() {

        ArrayList<Individual> initialPopulation = new ArrayList<>();
        for (int i = 0; i < N - m; i++)
            initialPopulation.add(generateRandomGenotype());

        return initialPopulation;
    }

    private Individual generateRandomGenotype() {

        int dim = instance.DIMENSION;
        int[] r = new int [dim];
        ArrayList<Integer> candidates = new ArrayList<>();
        for (int i = 0; i < dim; i++)
            candidates.add(i);

        for (int i = 0; i < dim; i++) {
            int pos = (int) (Math.random() * candidates.size());
            r[i] = candidates.get(pos);
            candidates.remove(pos);
        }

        return new Individual(instance, r);
    }

    private int[][] parentSelectionProcess(ArrayList<Individual> population) {

        int[][] pairParents = new int[N / (2 * 2)][2];
        int[] individuals = new int[N];

        for (int j = 0; j < pairParents.length; j++) {

            int parent = selectParentByTournament(individuals, population);
            pairParents[j][0] = parent;
            individuals[parent] = 1;

            parent = selectParentByTournament(individuals, population);
            pairParents[j][1] = parent;
            individuals[parent] = 1;
        }

        return pairParents;
    }

    private int selectParentByTournament(int[] individuals, ArrayList<Individual> population) {
        int parentWinner = -1;
        double parentFitness = -1;
        int[] candidates = new int[k];

        for (int i = 0; i < k; i++) {
            int posIndividual;
            do
                posIndividual = (int) (Math.random() * N);
            while (individuals[posIndividual] != 0);

            candidates[i] = posIndividual;
            individuals[posIndividual] = 2;
        }

        for (int c : candidates) {
            double candidateFitness = population.get(c).getFitness();
            if (candidateFitness > parentFitness) {
                parentWinner = c;
                parentFitness = candidateFitness;
            }
            individuals[c] = 0;
        }

        return parentWinner;
    }

    private ArrayList<Individual> crossoverOperation(int[][] pairParents, ArrayList<Individual> population) {
        ArrayList<Individual> newSons = new ArrayList<>();

        for (int[] pair: pairParents)
            if (Math.random() < crossoverProbability) {
                Individual[] parents = new Individual[2];
                parents[0] = population.get(pair[0]);
                parents[1] = population.get(pair[1]);
                Individual[] inds = this.crossover.applyOperator(parents);
                newSons.add(inds[0]);
                newSons.add(inds[1]);
            }

        return newSons;
    }

    private void mutationOperation(ArrayList<Individual> sons) {
        for (Individual son : sons)
            if (Math.random() < mutationProbability)
                mutation.applyOperator(son);
    }

    private void selectionOfSurvivors(ArrayList<Individual> population, ArrayList<Individual> sons) {

        int s = n;
        if ((sons.size() < n))
            s = sons.size();

        survivors.applyOperator(population, sons, s);
    }

    /*
     * 3) Ejecutar al algoritmo evolutivo en base a la configuración definida.
     */
    public Individual iterateGeneration(ArrayList<Individual> population) {

        int[][] pairParents = parentSelectionProcess(population);
        ArrayList<Individual> sons = crossoverOperation(pairParents, population);
        mutationOperation(sons);
        selectionOfSurvivors(population, sons);

        population.sort(Individual::compareTo);

        System.out.println(Arrays.toString(population.toArray()));
        System.out.println();

        return population.get(N-1);
    }
}
