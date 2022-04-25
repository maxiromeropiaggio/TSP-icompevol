import java.util.ArrayList;
import java.util.Arrays;

public class TSPSolver {

    private int[] solution;
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

    public int funcFitness(int[] s) {
        int r = 0;
        for (int i = 0; i < s.length-1; i++)
            r += instance.getCost(i, i+1);
        r += instance.getCost(s.length-1, 0);
        return r;
    }

    public int[][] generateInitialPoblation() {
        int[][] initialPoblation = new int[N][];
        for (int i = 0; i < initialPoblation.length - m; i++)
            initialPoblation[i] = generateRandomGenotype(this.instance.getDIMENSION());

        for (int j = initialPoblation.length - m; j < initialPoblation.length; j++)
            initialPoblation[j] = getGenotypeBySelectiveInitialization();

        return initialPoblation;
    }

    private int[] generateRandomGenotype(int dim) {
        int[] r = new int[dim];
        do {
            int[] candidates = new int[dim];
            for (int i = 0; i < candidates.length; i++)
                candidates[i] = i;
            int remaining = dim;

            for (int i = 0; i < r.length; i++) {
                int pos = (int) (Math.random() * remaining);
                r[i] = candidates[pos];

                int lastPos = remaining - 1;
                candidates[pos] = candidates[lastPos];
                candidates[lastPos] = 0;
                remaining--;
            }
        } while (!isAValidGenotype(r));

        return r;
    }

    private int[] getGenotypeBySelectiveInitialization() {
        return null;
    }

    private boolean isAValidGenotype(int[] genotype) {
        if (isAValidPath(genotype[genotype.length-1], genotype[0]))
            return false;
        for (int i = 1; i < genotype.length; i++) {
            if (isAValidPath(genotype[i-1], genotype[i]))
                return false;
        }
        return true;
    }

    private boolean isAValidPath(int src, int dst) {
        return this.instance.getCost(src, dst) == 0;
    }

    public int[][] parentSelectionProcess(int[][] poblation) {
        int numberPar = N / (2 * 2);
        int[][] parParents = new int[numberPar][2];

        int[] individuals = new int[N];

        for (int j = 0; j < parParents.length; j++) {

            int parent = selectParentByTournament(individuals, poblation);
            parParents[j][0] = parent;
            individuals[parent] = 1;

            parent = selectParentByTournament(individuals, poblation);
            parParents[j][1] = parent;
            individuals[parent] = 1;
        }

        return parParents;
    }

    private int selectParentByTournament(int[] individuals, int[][] poblation) {
        int parentWinner = -1;
        int parentFitness = Integer.MAX_VALUE;
        int[] candidates = new int[k];

        for (int i = 0; i < k; i++) {
            int c;
            do {
                c = (int) (Math.random() * N);
            }
            while (individuals[c] != 0);
            candidates[i] = c;
            individuals[c] = 2;
        }

        for (int c:candidates) {
            int candidateFitness = funcFitness(poblation[c]);
            if (candidateFitness < parentFitness) {
                parentWinner = c;
                parentFitness = candidateFitness;
            }
            individuals[c] = 0;
        }

        return parentWinner;
    }

    public int[][] crossoverOperation(int [][] parParents, int[][] poblation) {
        int[][] parSons = new int[parParents.length][2];

        Vector<>

        for (int = 0; i < parSons.length; i++) {
            if (Math.random() < crossoverProbability)

        }

        return parSons;
    }

    /*
     * 3) Ejecutar al algoritmo evolutivo en base a la configuración definida.
     */
    public int[] run() {
        int[][] ip = generateInitialPoblation();
        int generation = 1;

        while (generation <= maxGenerations) {
            int[][] parParents = parentSelectionProcess(ip);
            int[][] parSons = crossoverOperation(parParents, ip);

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
