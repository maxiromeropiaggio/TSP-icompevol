package crossover;

import tsp.Individual;

import java.util.ArrayList;

public class CrossoverCycle implements CrossoverOperator {

    public static final String NAME = "cycle";

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
     * @param parents
     * @return
     */
    @Override
    public Individual[] applyOperator(Individual[] parents) {

        int dim = parents[0].instance.DIMENSION;

        Individual[] sons = new Individual[2];

        int[] son1 = new int[dim];
        int[] son2 = new int[dim];

        for (int i = 0; i < dim; i++) {
            son1[i] = -1;
            son2[i] = -1;
        }

        Individual parent1 = parents[0];
        Individual parent2 = parents[1];

        ArrayList<ArrayList<Integer>> posAllCycles = new ArrayList<>();

        ArrayList<Integer> valuesUsed = new ArrayList<>();

        do {
            ArrayList<Integer> posCycle = new ArrayList<>();
            int firstValue = -1;

            for (int j = 0; j < dim && firstValue == -1; j++) {
                int v = parent1.genotype[j];
                if (!valuesUsed.contains(v)){
                    firstValue = v;
                    valuesUsed.add(firstValue);
                    posCycle.add(j);
                }
            }

            int nextValue;
            int i = posCycle.get(0);
            do {
                nextValue = parent2.genotype[i];
                i = parent1.getIndexOf(nextValue);
                posCycle.add(i);
                valuesUsed.add(nextValue);

            } while (nextValue != firstValue);

            posCycle.remove(posCycle.size()-1);
            valuesUsed.remove(valuesUsed.size()-1);

            posAllCycles.add(posCycle);

        } while (valuesUsed.size() < dim);

        for (ArrayList<Integer> posCycle : posAllCycles) {
            for (Integer posValue : posCycle) {

                int valueSon1 = parent1.genotype[posValue];
                int valueSon2 = parent2.genotype[posValue];

                son1[posValue] = valueSon1;
                son2[posValue] = valueSon2;
            }

            Individual tmp = parent1;
            parent1 = parent2;
            parent2 = tmp;
        }


        sons[0] = new Individual(parent1.instance, son1);
        sons[1] = new Individual(parent1.instance, son2);

        return sons;
    }
}
