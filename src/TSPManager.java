import java.util.ArrayList;

public class TSPManager {
    private TSPInstance instance;
    private String name;

    /*
     * Realizar inciso A
     */

    /*
     * B) Desarrollar experimentos computacionales para evaluar la efectividad y eficiencia
     * del algoritmo evolutivo implementado.
     * Para el desarrollo de los experimentos, se requiere utilizar alguna de las instancias del
     * problema provistas por la cátedra.
     * Luego, se requiere ejecutar diferentes configuraciones posibles para el algoritmo
     * evolutivo y, posteriormente, realizar un análisis comparativo de los resultados obtenidos
     * mediante las distintas configuraciones ejecutadas. El objetivo de este análisis es detectar
     * a la mejor configuración de las configuraciones ejecutadas.
     */

    public TSPManager(String name, TSPInstance instance) {
    }

    public TSPInstance getInstance() {
        return instance;
    }

    public void setInstance(TSPInstance instance) {
        this.instance = instance;
    }

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        TSPInstance br17 = new TSPInstance("br17.atsp");
        CrossoverOrden crossoverOrden = new CrossoverOrden();
        CrossoverCycle crossoverCycle = new CrossoverCycle();
        MutationInsertion mutationInsertion = new MutationInsertion();
        MutationInversion mutationInversion = new MutationInversion();

        TSPSolver br17Solver = new TSPSolver(br17, crossoverCycle, mutationInsertion,100, 0, 0.84, 0.12, 10, 20, 120);
        ArrayList<Integer> s = br17Solver.run();

        TSPInstance p43 = new TSPInstance("p43.atsp");
    }

}
