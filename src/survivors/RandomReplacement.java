package survivors;

import tsp.Individual;

import java.util.ArrayList;

public class RandomReplacement implements SurvivorsOperator {

    public static final String NAME = "random";

    /**
     *
     * n individuos de la población actual (elegidos al azar) son reemplazados por n hijos de dicha población (elegidos
     * al azar)
     *
     * @param population
     * @param sons
     * @param n
     */
    @Override
    public void applyOperator(ArrayList<Individual> population, ArrayList<Individual> sons, int n) {

        int replaces = 0;
        ArrayList<Integer> valuesUsed = new ArrayList<>();
        for (int j = 0; j < sons.size() && replaces < n; j++) {
            Individual son = sons.get(j);
            int i;

            do {
                i = (int) (Math.random() * population.size());
            } while (valuesUsed.contains(i));

            population.set(i, son);
            valuesUsed.add(i);
            replaces++;
        }
    }
}
