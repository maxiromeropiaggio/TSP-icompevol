import crossover.CrossoverOperator;
import mutation.MutationOperator;

import java.util.ArrayList;
import java.util.Comparator;

public class TSPSolver {

    private final TSPInstance instance;

    private final CrossoverOperator crossover;
    private final MutationOperator mutation;
    private final int N;
    private final int m;
    private final double crossoverProbability;
    private final double mutationProbability;
    private final int k;
    private final int n;

    /*
    For each iteration
    private int numberFailsValidSons
    private int numberFailsInitialGenotype
    private int numberFailsValidMutations
     */


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

    public double funcFitness(ArrayList<Integer> s) {
        int r = 0;
        int last = s.size()-1;
        for (int i = 0; i < last; i++)
            r += instance.getCost(i, i+1);
        r += instance.getCost(last, 0);
        return (double) 1/r;
    }

    public ArrayList<ArrayList<Integer>> generateInitialPopulation() {
        ArrayList<ArrayList<Integer>> initialPopulation = new ArrayList<>();
        for (int i = 0; i < N - m; i++)
            initialPopulation.add(generateRandomGenotype(this.instance.getDIMENSION()));

        for (int j = N - m; j < N; j++)
            initialPopulation.add(getGenotypeBySelectiveInitialization());

        return initialPopulation;
    }

    private ArrayList<Integer> generateRandomGenotype(int dim) {
        ArrayList<Integer> r;
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
        if (isNotValidPath(genotype.get(genotype.size()-1), genotype.get(0)))
            return false;
        for (int i = 1; i < genotype.size(); i++)
            if (isNotValidPath(genotype.get(i-1), genotype.get(i)))
                return false;

        return true;
    }

    private boolean isNotValidPath(int src, int dst) {
        return this.instance.getCost(src, dst) == 0;
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
                for (ArrayList<Integer> son: sons)
                    if (isAValidGenotype(son))
                        newSons.add(son);
            }

        return newSons;
    }

    private void mutationOperation(ArrayList<ArrayList<Integer>> pop) {
        for (ArrayList<Integer> p : pop)
            if (Math.random() < mutationProbability) {
                ArrayList<Integer> pm = new ArrayList<>(p);
                mutation.applyOperator(pm);
                if (isAValidGenotype(pm))
                    p = pm;
            }
    }

    /**
     *
     * Steady-State: los n peores individuos de la población actual son reemplazados por los n mejores hijos de dicha
     * población. Con este mecanismo se incrementa muy rápido (en pocas generaciones) el nivel de fitness de
     * la población pero puede llevar a una convergencia prematura. Por este motivo, generalmente es utilizado en
     * combinación con poblaciones grandes y/o con una política de no permitir duplicados.
     *
     * @param population
     * @param sons
     * @return
     */

    private void selectionOfSurvivors(ArrayList<ArrayList<Integer>> population,
                                                               ArrayList<ArrayList<Integer>> sons) {

        population.sort(Comparator.comparingDouble(this::funcFitness));
        sons.sort(Comparator.comparingDouble(this::funcFitness));

        int s = n;
        if ((sons.size() < n)) {
            s = sons.size();
        }

        int i = population.size()-1;
        int replaces = 0;
        for (int j = 0; j < sons.size() && replaces < s; j++) {
            ArrayList<Integer> son = sons.get(j);
            if (!hasThatGenotype(population, son)) {
                population.set(i, son);
                replaces++;
                i--;
            }
        }
    }

    private boolean hasThatGenotype(ArrayList<ArrayList<Integer>> population, ArrayList<Integer> son) {

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

        population.sort(Comparator.comparingDouble(this::funcFitness));

        return population.get(0);
    }
}
