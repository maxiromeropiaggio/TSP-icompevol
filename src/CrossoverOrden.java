import java.util.ArrayList;

public class CrossoverOrden implements CrossoverOperator {


    private ArrayList<Integer> crossAndGetSon(ArrayList<Integer> parent1, ArrayList<Integer> parent2, int crossPoint1, int crossPoint2) {

        ArrayList<Integer> son = new ArrayList<>();

        for (int i = 0; i < parent1.size(); i++)
            son.add(-1);

        for (int i = crossPoint1; i < crossPoint2; i++)
            son.set(i, parent1.get(i));

        int posSon = crossPoint2;
        for (int posParent = crossPoint2; posParent < parent2.size(); posParent++){
            int n = parent2.get(posParent);

            if (!son.contains(n)) {
                son.set(posSon, n);
                posSon++;
            }
        }

        for (int posParent = 0; posParent < crossPoint2; posParent++){
            int n = parent2.get(posParent);

            if (!(posSon < son.size()))
                posSon = 0;
            if (!son.contains(n)) {
                son.set(posSon, n);
                posSon++;
            }
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
     *
     *
     * @param parParents
     * @param poblation
     * @return
     */

    @Override
    public ArrayList<ArrayList<Integer>> applyOperator(ArrayList<Integer> parParents, ArrayList<ArrayList<Integer>> poblation) {

        int dim = poblation.get(0).size();
        int crossPoint1 = (int) (Math.random() * dim);
        int crossPoint2 = (int) (Math.random() * dim);

        ArrayList<ArrayList<Integer>> sons = new ArrayList<>();

        ArrayList<Integer> parentA = poblation.get(parParents.get(0));
        ArrayList<Integer> parentB = poblation.get(parParents.get(1));

        if (crossPoint1 > crossPoint2) {
            int tmp = crossPoint2;
            crossPoint2 = crossPoint1;
            crossPoint1 = tmp;
        }

        sons.add(crossAndGetSon(parentA, parentB, crossPoint1, crossPoint2));
        sons.add(crossAndGetSon(parentB, parentA, crossPoint1, crossPoint2));

        return sons;
    }
}
