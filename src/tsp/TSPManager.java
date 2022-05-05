package tsp;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import crossover.*;
import mutation.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import survivors.*;

public class TSPManager implements Runnable {

    private TSPInstance instance;
    private TSPSolver solver;
    public String name;
    private String crossover;
    private String mutation;
    private String survivors;
    private int N;
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
            survivors = (String) tsp.get("survivors");
            N = Math.toIntExact((long) tsp.get("N"));
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

            SurvivorsOperator survivorsOperator;
            if (survivors.equals(SteadyState.NAME)) {
                survivorsOperator = new SteadyState();
            }
            else
                survivorsOperator = new RandomReplacement();

            solver = new TSPSolver(instance, crossoverOperator, mutationOperator, survivorsOperator, N,
                    crossoverProbability, mutationProbability, k, n);


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

        JSONObject register = new JSONObject();
        JSONArray bestSolutions = new JSONArray();
        Individual best = null;

        register.put("name", name);
        register.put("instance", instance.NAME);
        register.put("crossover", crossover);
        register.put("mutation", mutation);
        register.put("survivors", survivors);
        register.put("N", N);
        register.put("crossoverProbability", crossoverProbability);
        register.put("mutationProbability", mutationProbability);
        register.put("k", k);
        register.put("n", n);
        register.put("maxGenerations", maxGenerations);
        register.put("initialPopulation", "random");
        register.put("selectionParentProcess", "tournament");

        long startTime = System.nanoTime();
        ArrayList<Individual> population = solver.generateInitialPopulation();

        System.out.println();
        System.out.println(" -------------- GENERACION " + 0 + " --------------");
        System.out.println();
        population.sort(Individual::compareTo);
        for (int i = 0; i < population.size(); i++) {
            System.out.println(population.get(i).toString());
            System.out.println();
        }

        int generation = 1;

        while (generation <= maxGenerations) {
            JSONArray bestSolutionPerInteration = new JSONArray();
            best = solver.iterateGeneration(population);

            bestSolutionPerInteration.add(Arrays.toString(best.genotype));
            bestSolutionPerInteration.add(best.getFitness());

            bestSolutions.add(bestSolutionPerInteration);


            System.out.println();
            System.out.println(" -------------- GENERACION " + generation + " --------------");
            System.out.println();

            for (int i = 0; i < population.size(); i++) {
                System.out.println(population.get(i).toString());
                System.out.println();
            }

            generation++;
        }

        long endTime = System.nanoTime();

        register.put("bestSolutions", bestSolutions);
        //register.put("best", Arrays.toString(best.genotype));
//        register.put("bestFitness", best.getFitness());
        register.put("time", (endTime - startTime)/1e6); //ms

        try (FileWriter fw = new FileWriter("resultOf-" + name)) {
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(register.toJSONString());
            bw.close();

        } catch (IOException e) {
            System.err.println("tsp.TSPManager: file not found. Please re-try and be sure of input path.");
        }

    }
}