import java.io.*;
import java.util.ArrayList;

import crossover.*;
import mutation.MutationInsertion;
import mutation.MutationInversion;
import mutation.MutationOperator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class TSPManager implements Runnable {

    private TSPInstance instance;
    private TSPSolver solver;
    public String name;
    private String crossover;
    private String mutation;
    private int N;
    private int m;
    private double crossoverProbability;
    private double mutationProbability;
    private int k;
    private int n;
    private int maxGenerations;

    public TSPManager(String file) {

        String path = System.getenv("PWD") + "/experiments/" + file;
        try (FileReader fr = new FileReader(path)) {

            JSONParser parser = new JSONParser();

            JSONObject tsp = (JSONObject) parser.parse(fr);

            name = (String) tsp.get("name");

            String fileInstance = (String) tsp.get("instance");
            instance = new TSPInstance(fileInstance);

            crossover = (String) tsp.get("crossover");
            mutation = (String) tsp.get("mutation");
            N = Math.toIntExact((long) tsp.get("N"));
            m = Math.toIntExact((long) tsp.get("m"));
            crossoverProbability = (double) tsp.get("crossoverProbability");
            mutationProbability = (double) tsp.get("mutationProbability");
            k = Math.toIntExact((long) tsp.get("k"));
            n = Math.toIntExact((long) tsp.get("n"));
            maxGenerations = Math.toIntExact((long) tsp.get("maxGenerations"));

            CrossoverOperator crossoverOperator;
            if (crossover.equals(CrossoverOrden.NAME))
                crossoverOperator = new CrossoverOrden();
            else /*if (crossover.equals(CrossoverCycle.NAME))*/
                crossoverOperator = new CrossoverCycle();

            MutationOperator mutationOperator;
            if (mutation.equals(MutationInsertion.NAME))
                mutationOperator = new MutationInsertion();
            else /*if (mutation.equals(MutationInversion.NAME))*/
                mutationOperator = new MutationInversion();

            solver = new TSPSolver(instance, crossoverOperator, mutationOperator, N, m, crossoverProbability,
                    mutationProbability, k, n);

        } catch (IOException e) {
            System.err.println("TSPManager: file not found. Please re-try and be sure of input path.");
        } catch (ParseException pe) {
            System.err.println("TSPManager: error parsing file to JSON Object. Check input file and retry.");
        }

    }

    /*
     * 4) Registrar en un archivo los resultados de cada ejecución realizada. Específicamente,
     * luego de realizar una ejecución, la aplicación debería permitir que se registre en un
     * archivo la siguiente información: configuración del algoritmo evolutivo (detallar qué
     * componentes y qué valores de parámetros fueron utilizados para ejecutar al algoritmo),
     * evolución del fitness a lo largo del tiempo (registrar el mejor fitness obtenido en cada
     * generación o iteración), mejor solución lograda al finalizar la ejecución (describir la
     * composición de la mejor solución lograda y su valor de fitness), y tiempo requerido por
     * la ejecución.
     */

    public void run() {

        long startTime = System.nanoTime();

        ArrayList<ArrayList<Integer>> population = solver.generateInitialPopulation();

        int generation = 1;

        JSONObject register = new JSONObject();

        register.put("name", name);
        register.put("instance", instance.getNAME());
        register.put("crossover", crossover);
        register.put("mutation", mutation);
        register.put("N", N);
        register.put("m", m);
        register.put("crossoverProbability", crossoverProbability);
        register.put("mutationProbability", mutationProbability);
        register.put("k", k);
        register.put("n", n);
        register.put("maxGenerations", maxGenerations);
        register.put("initialPopulation", "random");
        register.put("selectionParentProcess", "tournament");
        register.put("selectionOfSurvivors", "steady-state");

        JSONArray bestSolutions = new JSONArray();
        ArrayList<Integer> best = null;
        double bestFitness = -1;

        while (generation <= maxGenerations) {
            JSONArray bestSolutionPerInteration = new JSONArray();
            best = solver.iterateGeneration(population);
            bestFitness = solver.funcFitness(best);

            bestSolutionPerInteration.add(best);
            bestSolutionPerInteration.add(bestFitness);

            bestSolutions.add(bestSolutionPerInteration);
            generation++;
        }

        register.put("bestSolutions", bestSolutions);
        register.put("best", best);
        register.put("bestFitness", bestFitness);

        long endTime = System.nanoTime();

        register.put("time", (endTime - startTime)/1e6); //ms

        try (FileWriter fw = new FileWriter("result_" + name)) {
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(register.toJSONString());
            bw.close();

        } catch (IOException e) {
            System.err.println("TSPManager: file not found. Please re-try and be sure of input path.");
        }

    }
}