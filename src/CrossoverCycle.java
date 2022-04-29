import java.util.ArrayList;
import java.util.Collections;

public class CrossoverCycle implements CrossoverOperator {

    /**
     *
     * Procedimiento:
     * 1. Definir un ciclo de valores a partir de P1 en la siguiente forma
     *  1. Comenzar con el primer valor no usado de P1
     *  2. Revisar el valor ubicado en la misma posición en P2
     *  3. Ir a la posición que contiene el mismo valor en P1
     *  4. Sumar este valor al ciclo
     *  5. Repetir los pasos 2-4 hasta que se arribe al primer valor de P1
     * 2. Ubicar los valores del ciclo en el hijo 1 (hijo 2) respetando las
     * posiciones que ellos tienen en el P1 (P2)
     * 3. Definir el siguiente ciclo. Ubicar a los valores de este ciclo en el
     * hijo 1 (hijo 2) respetando las posiciones que ellos tienen en el P2
     * (P1)
     *
     * @param pairParents
     * @param population
     * @return
     */
    @Override
    public ArrayList<ArrayList<Integer>> applyOperator(ArrayList<Integer> pairParents,
                                                       ArrayList<ArrayList<Integer>> population) {

        int dim = population.get(0).size();

        ArrayList<ArrayList<Integer>> sons = new ArrayList<>();
        sons.add(new ArrayList<>());
        sons.add(new ArrayList<>());

        ArrayList<Integer> son1 = sons.get(0);
        ArrayList<Integer> son2 = sons.get(1);
        for (int i = 0; i < dim; i++) {
            son1.add(-1);
            son2.add(-1);
        }

        ArrayList<Integer> parent1 = population.get(pairParents.get(0));
        ArrayList<Integer> parent2 = population.get(pairParents.get(1));

        ArrayList<ArrayList<Integer>> posAllCycles = new ArrayList<>();

        ArrayList<Integer> valuesUsed = new ArrayList<>();
        for (int i = 0; i < dim; i++)
            valuesUsed.add(0);

        do {
            ArrayList<Integer> posCycle = new ArrayList<>();
            int firstValue = -1;

            for (int j = 0; j < dim && firstValue == -1; j++) {
                int v = parent1.get(j);
                if (valuesUsed.get(v) == 0){
                    firstValue = v;
                    valuesUsed.set(firstValue, 1);
                    posCycle.add(j);
                }
            }

            int nextValue;
            int i = posCycle.get(0);
            do {
                nextValue = parent2.get(i);
                i = parent1.indexOf(nextValue);
                posCycle.add(i);
                valuesUsed.set(nextValue, 1);

            } while (nextValue != firstValue);

            posCycle.remove(posCycle.size()-1);

            posAllCycles.add(posCycle);

        } while (Collections.min(valuesUsed) == 0);

        for (ArrayList<Integer> posCycle : posAllCycles) {
            for (Integer posValue : posCycle) {

                int valueSon1 = parent1.get(posValue);
                int valueSon2 = parent2.get(posValue);

                son1.set(posValue, valueSon1);
                son2.set(posValue, valueSon2);
            }

            ArrayList<Integer> tmp = son1;
            son1 = son2;
            son2 = tmp;
        }

        return sons;
    }
}
