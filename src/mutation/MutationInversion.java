package mutation;

import tsp.Individual;

public class MutationInversion implements MutationOperator {

    public static final String NAME = "inversion";

    /**
     *
     * • Se eligen al azar dos posiciones de la solución y se invierten los valores existentes entres dichas posiciones
     * • Preserva la mayor parte de la información sobre adyacencias pero perturba significativamente la información
     * sobre el orden
     *
     * @param son
     */
    @Override
    public void applyOperator(Individual son) {

        int p1 = (int) (Math.random() * son.instance.DIMENSION);
        int p2 = (int) (Math.random() * son.instance.DIMENSION);

        if (p1 > p2) {
            int tmp = p1;
            p1 = p2;
            p2 = tmp;
        }

        int j = p2;
        for (int i = p1; i <= p2 / 2; i++) {
            int tmp = son.genotype[i];
            son.genotype[i] = son.genotype[j];
            son.genotype[j] = tmp;
            j--;
        }
    }
}
