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
            if (!hasThatGenotype(population, son)) {
                population.set(i, son);
                replaces++;
                i--;
            }
        }

    }


    public boolean hasThatGenotype(ArrayList<ArrayList<Integer>> population, ArrayList<Integer> candidate) {

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
}
