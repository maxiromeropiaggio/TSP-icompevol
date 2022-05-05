package crossover;

import tsp.Individual;

public class CrossoverOrden implements CrossoverOperator {

    public static final String NAME = "orden";


    private Individual crossAndGetSon(Individual parent1, Individual parent2, int crossPoint1,
                                              int crossPoint2) {
        int dim = parent1.instance.DIMENSION;
        Individual son = new Individual(parent1.instance, new int[dim]);

        for (int i = 0; i < parent1.instance.DIMENSION; i++)
            son.genotype[i] = -1;

        for (int i = crossPoint1; i <= crossPoint2; i++)
            son.genotype[i] = parent1.genotype[i];

        int posSon = crossPoint2+1;
        int posParent = crossPoint2+1;

        for (int i = 0; i < dim; i++){
            int n = parent2.genotype[posParent % dim];

            if (son.getIndexOf(n) == -1) {
                son.genotype[posSon % dim] = n;
                posSon = (posSon + 1) % dim;
            }
            posParent = (posParent + 1) % dim;
        }

        return son;
    }

    /**
     *
     * La idea central es preservar el orden relativo en el cual ocurren los elementos
     * • Procedimiento:
     * 1. Elegir dos puntos de cruce al azar
     * 2. Copiar los valores del padre 1 que se encuentran entre los dos puntos de cruce en el hijo 1
     * 3. Copiar en el hijo 1 los valores que aún no se han incluido en dicho hijo
     *  1. Comenzar a partir del segundo punto de cruce del padre 2
     *  2. Copiar los valores no incluidos en el hijo 1 respetando el orden en el cual dichos valores aparecen en el padre 2
     *  3. Al terminar la lista del padre 2, continuar con los primeros valores de la misma
     * 4. El segundo hijo es creado de manera análoga (pasos 2 y 3) invirtiendo el rol de los padres
     *
     * @param parents
     */

    public Individual[] applyOperator(Individual[] parents) {

        int dim = parents[0].instance.DIMENSION;

        int crossPoint1, crossPoint2;

        do {
            crossPoint1 = (int) (Math.random() * dim);
            crossPoint2 = (int) (Math.random() * dim);
        } while (crossPoint1 == crossPoint2);

        Individual[] sons = new Individual[2];

        Individual parentA = parents[0];
        Individual parentB = parents[1];

        if (crossPoint1 > crossPoint2) {
            int tmp = crossPoint2;
            crossPoint2 = crossPoint1;
            crossPoint1 = tmp;
        }

        /*System.out.println(" -------------- PADRES --------------");
        System.out.println(parentA.toString());
        System.out.println(parentB.toString());
        System.out.println("punto1: " + crossPoint1);
        System.out.println("punto2: " + crossPoint2);*/

        sons[0] = crossAndGetSon(parentA, parentB, crossPoint1, crossPoint2);
        sons[1] = crossAndGetSon(parentB, parentA, crossPoint1, crossPoint2);

        return sons;
    }
}
