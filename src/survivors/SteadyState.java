package survivors;

import tsp.Individual;

import java.util.ArrayList;

public class SteadyState implements SurvivorsOperator {

    public static final String NAME = "steady-state";

    /**
     * los n peores individuos de la población actual son reemplazados por los n mejores hijos de dicha población.
     * Con este mecanismo se incrementa muy rápido (en pocas generaciones) el nivel de fitness de la población pero
     * puede llevar a una convergencia prematura. Por este motivo, generalmente es utilizado en combinación con
     * poblaciones grandes y/o con una política de no permitir duplicados.
     *
     * @param population
     * @param sons
     */
    @Override
    public void applyOperator(ArrayList<Individual> population, ArrayList<Individual> sons, int n) {

        population.sort(Individual::compareTo);
        sons.sort(Individual::compareTo);

        int i = 0;
        int j = sons.size()-1;
        for (int replaces = 0; replaces < n; replaces++) {
            population.set(i, sons.get(j));
            i++;
            j--;
        }

    }
}
