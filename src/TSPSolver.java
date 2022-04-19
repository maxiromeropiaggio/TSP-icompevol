import java.util.ArrayList;

public class TSPSolver {

    private int[] solution;
    private final TSPInstance instance;
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

    public TSPSolver(TSPInstance instance, int N, int m, double crossoverProbability, double mutationProbability,
                     int k, int n, int maxGenerations) {
        this.instance = instance;
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
        for (int i = 0; i < s.length-1; i++) {
            r += instance.getCost(i, i+1);
        }
        r += instance.getCost(s.length-1, 0);
        return r;
    }

    public int[][] generateInitialPoblation() {
        int[][] initialPoblation = new int[N][];
        for (int i = 0; i < initialPoblation.length - m; i++) {
            initialPoblation[i] = generateRandomGenotype(this.instance.getDIMENSION());
        }
        for (int j = initialPoblation.length - m; j < initialPoblation.length; j++) {
            initialPoblation[j] = getGenotypeBySelectiveInitialization();
        }

        return initialPoblation;
    }

    private int[] generateRandomGenotype(int dim) {
        int[] r = new int[dim];
        int[] candidates = new int[dim];
        for (int i = 0; i < candidates.length; i++)
            candidates[i] = i;

        int remaining = dim;
        int pos = (int) (Math.random() * remaining);
        r[0] = candidates[pos];
        int lastPos = remaining - 1;
        candidates[pos] = candidates[lastPos];
        candidates[lastPos] = 0;
        remaining--;

        for (int i = 1; i < r.length; i++) {
            do {
                pos = (int) (Math.random() * remaining);
            } while (this.instance.getCost(r[i-1], candidates[pos]) == 0);

            r[i] = candidates[pos];
            lastPos = remaining - 1;
            candidates[pos] = candidates[lastPos];
            candidates[lastPos] = 0;
            remaining--;
        }

        return r;
    }

    private int[] getGenotypeBySelectiveInitialization() {
        return null;
    }



    /*
     * 3) Ejecutar al algoritmo evolutivo en base a la configuración definida.
     */

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
