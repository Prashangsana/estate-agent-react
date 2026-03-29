// Student ID: 20232986
// Name: Prashangsana Dissanayake

import java.io.IOException;
import java.util.List;

/**
 * Main entry point. Parses the graph from a file, runs the acyclicity check,
 * and prints out the results or cycles if found.
 */
public class Main {

    public static void main(String[] args) {

        String filePath = "benchmarks/acyclic/a_40_4.txt";

        DirectedGraph graph;
        try {
            graph = GraphParser.parse(filePath);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            System.exit(1);
            return;
        }

        System.out.println("Loaded graph from: " + filePath);
        System.out.println("Vertices: " + graph.vertexCount());
        System.out.println();

        long start = System.nanoTime();

        boolean acyclic = AcyclicityChecker.isAcyclic(graph, true);

        long end = System.nanoTime();

        // Calculate elapsed time in seconds
        double elapsedSeconds = (end - start) / 1_000_000_000.0;

        if (!acyclic) {
            List<Integer> cycle = AcyclicityChecker.findCycle(graph);
            if (cycle != null) {
                StringBuilder sb = new StringBuilder("Cycle found: ");
                for (int i = 0; i < cycle.size(); i++) {
                    if (i > 0) {
                        sb.append(" -> ");
                    }
                    sb.append(cycle.get(i));
                }
                System.out.println(sb);
            } else {
                System.out.println("(Cycle detection inconclusive)");
            }
        }

        System.out.println("\nAnswer: " + (acyclic ? "YES — the graph is acyclic."
                : "NO  — the graph contains a cycle."));
        System.out.println("\nElapsed time = " + elapsedSeconds + " seconds");
    }
}