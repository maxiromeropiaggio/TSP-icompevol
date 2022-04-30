package survivors;

import tsp.TSPSolver;

import java.util.ArrayList;
import java.util.Comparator;

public class SteadyState implements SurvivorsOperator {

    public static final String NAME = "steady-state";
    private TSPSolver solver;

    public void setSolver(TSPSolver solver) {
        this.solver = solver;
    }

    /**
     *
     * los n peores individuos de la población actual son reemplazados por los n mejores hijos de dicha población.
     * Con este mecanismo se incrementa muy rápido (en pocas generaciones) el nivel de fitness de la población pero
     * puede llevar a una convergencia prematura. Por este motivo, generalmente es utilizado en combinación con
     * poblaciones grandes y/o con una política de no permitir duplicados.
     *
     * @param population
     * @param sons
     */
    @Override
    public void applyOperator(ArrayList<ArrayList<Integer>> population, ArrayList<ArrayList<Integer>> sons, int s) {


        population.sort(Comparator.comparingDouble(this.solver::funcFitness).reversed());
        sons.sort(Comparator.comparingDouble(this.solver::funcFitness).reversed());

        int i = population.size()-1;
        int replaces = 0;
        for (int j = 0; j < sons.size() && replaces < s; j++) {
            ArrayList<Integer> son = sons.get(j);
            if (!TSPSolver.hasThatGenotype(population, son)) {
                population.set(i, son);
                replaces++;
                i--;
            }
        }

    }
}
