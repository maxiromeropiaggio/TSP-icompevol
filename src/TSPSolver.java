import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class TSPSolver {

    private ArrayList<Integer> solution;
    private final TSPInstance instance;

    private final CrossoverOperator crossover;
    private final int N;
    private final int m;
    private final double crossoverProbability;
    private final double mutationProbability;
    private final int k;
    private final int n;
    private final int maxGenerations;


    /*
     * 2) Configurar los distintos parámetros y componentes del algoritmo evolutivo (método
     * de generación de la población inicial, método de selección de padres, operador de cruce,
     * operador de mutación, método de selección de sobrevivientes, tamaño de la población
     * inicial, probabilidad de cruce, probabilidad de mutación, condición de corte, etc.
     */

    public TSPSolver(TSPInstance instance, CrossoverOperator crossover, int N, int m, double crossoverProbability, double mutationProbability,
                     int k, int n, int maxGenerations) {
        this.instance = instance;
        this.crossover = crossover;
        this.N = N;
        this.m = m;
        this.crossoverProbability = crossoverProbability;
        this.mutationProbability = mutationProbability;
        this.k = k;
        this.n = n;
        this.maxGenerations = maxGenerations;
    }

    public double funcFitness(ArrayList<Integer> s) {
        int r = 0;
        int last = s.size()-1;
        for (int i = 0; i < last; i++)
            r += instance.getCost(i, i+1);
        r += instance.getCost(last, 0);
        return 1/r;
    }

    public ArrayList<ArrayList<Integer>> generateInitialPoblation() {
        ArrayList<ArrayList<Integer>> initialPoblation = new ArrayList<>();
        for (int i = 0; i < N - m; i++)
            initialPoblation.add(generateRandomGenotype(this.instance.getDIMENSION()));

        for (int j = N - m; j < N; j++)
            initialPoblation.add(getGenotypeBySelectiveInitialization());

        return initialPoblation;
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
        if (isAValidPath(genotype.get(genotype.size()-1), genotype.get(0)))
            return false;
        for (int i = 1; i < genotype.size(); i++)
            if (isAValidPath(genotype.get(i-1), genotype.get(i)))
                return false;

        return true;
    }

    private boolean isAValidPath(int src, int dst) {
        return this.instance.getCost(src, dst) == 0;
    }

    public ArrayList<ArrayList<Integer>> parentSelectionProcess(ArrayList<ArrayList<Integer>> poblation) {

        ArrayList<ArrayList<Integer>> parParents = new ArrayList<>();
        ArrayList<Integer> individuals = new ArrayList<>();
        for (int i = 0; i < N; i++)
            individuals.add(0);

        for (int j = 0; j < N / (2 * 2); j++) {

            ArrayList<Integer> last = new ArrayList<>();
            parParents.add(last);

            int parent = selectParentByTournament(individuals, poblation);
            last.add(parent);
            individuals.set(parent, 1);

            parent = selectParentByTournament(individuals, poblation);
            last.add(parent);
            individuals.set(parent, 1);
        }

        return parParents;
    }

    private int selectParentByTournament(ArrayList<Integer> individuals, ArrayList<ArrayList<Integer>> poblation) {
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
            double candidateFitness = funcFitness(poblation.get(c));
            if (candidateFitness > parentFitness) {
                parentWinner = c;
                parentFitness = candidateFitness;
            }
            individuals.set(c, 0);
        }

        return parentWinner;
    }

    public ArrayList<ArrayList<Integer>> crossoverOperation(ArrayList<ArrayList<Integer>> parParents, ArrayList<ArrayList<Integer>> poblation) {
        ArrayList<ArrayList<Integer>> parSons = new ArrayList<>();

        for(ArrayList<Integer> pp: parParents)
            if (Math.random() < crossoverProbability)
                parSons.add(this.crossover.applyOperator(pp));

        return parSons;
    }

    /*
     * 3) Ejecutar al algoritmo evolutivo en base a la configuración definida.
     */
    public ArrayList<Integer> run() {
        ArrayList<ArrayList<Integer>> ip = generateInitialPoblation();
        int generation = 1;

        while (generation <= 1 /*maxGenerations*/) {
            ArrayList<ArrayList<Integer>> parParents = parentSelectionProcess(ip);
            System.out.println(parParents);
            //ArrayList<ArrayList<Integer>> parSons = crossoverOperation(parParents, ip);

            generation++;
        }

        return solution;
    }

    /*
     * 4) Registrar en un archivo los resultados de cada ejecución realizada. Específicamente,
     * luego de realizar una ejecución, la aplicación debería permitir que se registre en un
     * archivo la siguiente información: configuración del algoritmo evolutivo (detallar qué
     * componentes y qué valores de parámetros fueron utilizados para ejecutar al algoritmo),
     * evolución del fitness a lo largo del tiempo (registrar el mejor fitness obtenido en cada
     * generación o iteración), mejor solución lograda al finalizar la ejecución (describir la
     * composición de la mejor solución lograda y su valor de fitness), y tiempo requerido por
     * la ejecución.
     */

}
