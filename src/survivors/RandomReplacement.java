package survivors;

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
     * @param s
     */
    @Override
    public void applyOperator(ArrayList<ArrayList<Integer>> population, ArrayList<ArrayList<Integer>> sons, int s) {

        int replaces = 0;
        ArrayList<Integer> valuesUsed = new ArrayList<>();
        for (int j = 0; j < sons.size() && replaces < s; j++) {
            ArrayList<Integer> son = sons.get(j);
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
