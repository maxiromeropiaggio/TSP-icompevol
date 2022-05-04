package survivors;

import tsp.Individual;

import java.util.ArrayList;
import java.util.Comparator;

public class SteadyState implements SurvivorsOperator {

    public static final String NAME = "steady-state";
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
    public void applyOperator(ArrayList<Individual> population, ArrayList<Individual> sons, int s) {

        population.sort(Individual::compareTo);
        sons.sort(Individual::compareTo);

        int i = 0;
        int replaces = 0;
        for (int j = sons.size()-1; j >= 0 && replaces < s; j--) {
            Individual son = sons.get(j);
            if (!hasThatGenotype(population, son)) {
                population.set(i, son);
                replaces++;
                i++;
            }
        }

    }


    public boolean hasThatGenotype(ArrayList<Individual> population, Individual candidate) {

        for (Individual individual: population) {
            if (individual.equals(candidate))
                return true;
        }

        return false;
    }
}
