package tsp;

import crossover.CrossoverOperator;
import mutation.MutationOperator;
import survivors.SurvivorsOperator;

import java.util.ArrayList;
import java.util.Comparator;

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
    public int exitos = 0;
    public int total = 0;
    public int exitos_m = 0;
    public int total_m = 0;

    /*
     * 2) Configurar los distintos parámetros y componentes del algoritmo evolutivo (método
     * de generación de la población inicial, método de selección de padres, operador de cruce,
     * operador de mutación, método de selección de sobrevivientes, tamaño de la población
     * inicial, probabilidad de cruce, probabilidad de mutación, condición de corte, etc.
     */

    public TSPSolver(TSPInstance instance, CrossoverOperator crossover, MutationOperator mutation, int N, int m,
                     double crossoverProbability, double mutationProbability, int k, int n) {
        this.instance = instance;
        this.crossover = crossover;
        this.mutation = mutation;
        this.N = N;
        this.m = m;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.k = k;
        this.n = n;
    }

    public void setSurvivorsOperator(SurvivorsOperator survivors) {
        this.survivors = survivors;
    }

    public double funcFitness(ArrayList<Integer> s) {

        int r = instance.getCost(s.get(s.size()-1), s.get(0));
        for (int i = 1; i < s.size(); i++)
            r += instance.getCost(s.get(i-1), s.get(i));

        return (double) 1/r;
    }

    public ArrayList<ArrayList<Integer>> generateInitialPopulation() {
        ArrayList<ArrayList<Integer>> initialPopulation = new ArrayList<>();
        for (int i = 0; i < N - m; i++)
            initialPopulation.add(generateRandomGenotype());

        for (int j = N - m; j < N; j++)
            initialPopulation.add(getGenotypeBySelectiveInitialization());

        return initialPopulation;
    }

    private ArrayList<Integer> generateRandomGenotype() {
        ArrayList<Integer> r;
        int dim = this.instance.getDIMENSION();

        do {
            ArrayList<Integer> candidates = new ArrayList<>();
            for (int i = 0; i < dim; i++)
                candidates.add(i);
            r =  new ArrayList<>();
            for (int i = 0; i < dim; i++) {
                int pos = (int) (Math.random() * candidates.size());
                r.add(candidates.get(pos));
                candidates.remove(pos);
            }
        } while (!isAValidGenotype(r));

        return r;
    }

    private ArrayList<Integer> getGenotypeBySelectiveInitialization() {
        return null;
    }

    private boolean isAValidGenotype(ArrayList<Integer> genotype) {
        if (!isValidPath(genotype.get(genotype.size()-1), genotype.get(0)))
            return false;
        for (int i = 1; i < genotype.size(); i++)
            if (!isValidPath(genotype.get(i-1), genotype.get(i)))
                return false;

        return true;
    }

    private boolean isValidPath(int src, int dst) {
        return this.instance.getCost(src, dst) != 0;
    }

    private ArrayList<ArrayList<Integer>> parentSelectionProcess(ArrayList<ArrayList<Integer>> population) {

        ArrayList<ArrayList<Integer>> pairParents = new ArrayList<>();
        ArrayList<Integer> individuals = new ArrayList<>();
        for (int i = 0; i < N; i++)
            individuals.add(0);

        for (int j = 0; j < N / (2 * 2); j++) {

            ArrayList<Integer> last = new ArrayList<>();
            pairParents.add(last);

            int parent = selectParentByTournament(individuals, population);
            last.add(parent);
            individuals.set(parent, 1);

            parent = selectParentByTournament(individuals, population);
            last.add(parent);
            individuals.set(parent, 1);
        }

        return pairParents;
    }

    private int selectParentByTournament(ArrayList<Integer> individuals, ArrayList<ArrayList<Integer>> population) {
        int parentWinner = -1;
        double parentFitness = -1;
        ArrayList<Integer> candidates = new ArrayList<>();

        for (int i = 0; i < k; i++) {
            int posIndividual;
            do
                posIndividual = (int) (Math.random() * N);
            while (individuals.get(posIndividual) != 0);

            candidates.add(posIndividual);
            individuals.set(posIndividual, 2);
        }

        for (int c:candidates) {
            double candidateFitness = funcFitness(population.get(c));
            if (candidateFitness > parentFitness) {
                parentWinner = c;
                parentFitness = candidateFitness;
            }
            individuals.set(c, 0);
        }

        return parentWinner;
    }

    private ArrayList<ArrayList<Integer>> crossoverOperation(ArrayList<ArrayList<Integer>> pairParents, ArrayList<ArrayList<Integer>> population) {
        ArrayList<ArrayList<Integer>> newSons = new ArrayList<>();

        for (ArrayList<Integer> pp: pairParents)
            if (Math.random() < crossoverProbability) {
                ArrayList<ArrayList<Integer>> sons = this.crossover.applyOperator(pp, population);
                for (ArrayList<Integer> son: sons) {
                    if (isAValidGenotype(son)) {
                        newSons.add(son);
                        exitos++;
                    }
                    total++;
                }
            }

        return newSons;
    }

    private void mutationOperation(ArrayList<ArrayList<Integer>> pop) {
        for (ArrayList<Integer> p : pop)
            if (Math.random() < mutationProbability) {
                ArrayList<Integer> pm = new ArrayList<>(p);
                mutation.applyOperator(pm);
                if (isAValidGenotype(pm)) {
                    p = pm;
                    exitos_m++;
                }
                total_m++;
            }
    }

    private void selectionOfSurvivors(ArrayList<ArrayList<Integer>> population,
                                                               ArrayList<ArrayList<Integer>> sons) {

        int s = n;
        if ((sons.size() < n))
            s = sons.size();

        survivors.applyOperator(population, sons, s);
    }

    public static boolean hasThatGenotype(ArrayList<ArrayList<Integer>> population, ArrayList<Integer> candidate) {

        for (ArrayList<Integer> individual: population) {

            int i = 0;
            int valueI = individual.get(i);
            int j = candidate.indexOf(valueI);
            int valueJ = candidate.get(j);
            int first = valueI;

            do {
                if (valueI != valueJ)
                    break;
                i++;
                j++;
                i = i % individual.size();
                j = j % candidate.size();
                valueI = individual.get(i);
                valueJ = candidate.get(j);

            } while (first != valueI);

            if (first == valueI)
                return true;

        }

        return false;
    }

    /*
     * 3) Ejecutar al algoritmo evolutivo en base a la configuración definida.
     */
    public ArrayList<Integer> iterateGeneration(ArrayList<ArrayList<Integer>> population) {

        ArrayList<ArrayList<Integer>> pairParents = parentSelectionProcess(population);
        ArrayList<ArrayList<Integer>> sons = crossoverOperation(pairParents, population);
        mutationOperation(sons);
        selectionOfSurvivors(population, sons);

        population.sort(Comparator.comparingDouble(this::funcFitness).reversed());

        return population.get(0);
    }
}
