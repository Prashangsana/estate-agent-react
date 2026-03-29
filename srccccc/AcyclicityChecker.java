// Student ID: 20232986
// Name: Prashangsana Dissanayake

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Contains the logic for determining acyclicity using sink elimination
 * and finding cycles using Depth-First Search (DFS).
 */
public class AcyclicityChecker {

    /**
     * Checks if a graph is acyclic by repeatedly removing sinks.
     * Time Complexity: O(V^2) in the worst case (calls O(V) findSink inside a loop of V iterations).
     */
    public static boolean isAcyclic(DirectedGraph graph, boolean verbose) {
        DirectedGraph working = graph.deepCopy();

        if (verbose) {
            System.out.println("— Sink Elimination Algorithm —");
            System.out.println("Starting vertex count: " + working.vertexCount());
        }

        int step = 1;
        while (!working.isEmpty()) {
            int sink = working.findSink();

            if (sink == -1) {
                if (verbose) {
                    System.out.println("\nStep " + step + ": No sink found in remaining graph.");
                    System.out.println("Remaining vertices (all have outgoing edges): "
                            + sortedList(working.getVertices()));
                    System.out.println("\nResult: NOT ACYCLIC (cycle exists)");
                }
                return false;
            }

            if (verbose) {
                System.out.println("Step " + step + ": Found sink " + sink
                        + " — removing it. Vertices remaining: "
                        + (working.vertexCount() - 1));
            }
            working.removeVertex(sink);
            step++;
        }

        if (verbose) {
            System.out.println("\nResult: ACYCLIC (graph was reduced to empty successfully)");
        }
        return true;
    }

    /**
     * Initiates a DFS to detect and return a cycle if one exists.
     * Time Complexity: O(V + E) overall for DFS traversal.
     */
    public static List<Integer> findCycle(DirectedGraph graph) {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> onStack = new HashSet<>();
        Map<Integer, Integer> parent = new HashMap<>();

        for (int start : graph.getVertices()) {
            if (!visited.contains(start)) {
                List<Integer> cycle = dfsCycle(graph, start, visited, onStack, parent);
                if (cycle != null) return cycle;
            }
        }
        return null;
    }

    /**
     * Recursive DFS helper that tracks the current path stack to find back-edges.
     */
    private static List<Integer> dfsCycle(
            DirectedGraph graph,
            int v,
            Set<Integer> visited,
            Set<Integer> onStack,
            Map<Integer, Integer> parent) {

        visited.add(v);
        onStack.add(v);

        for (int w : graph.getSuccessors(v)) {
            if (!visited.contains(w)) {
                parent.put(w, v);
                List<Integer> cycle = dfsCycle(graph, w, visited, onStack, parent);
                if (cycle != null) return cycle;
            } else if (onStack.contains(w)) {
                return reconstructCycle(parent, v, w);
            }
        }

        onStack.remove(v);
        return null;
    }

    /**
     * Reconstructs the exact path of the cycle using the parent map once a back-edge is found.
     * Time Complexity: O(V) to trace back the path.
     */
    private static List<Integer> reconstructCycle(
            Map<Integer, Integer> parent, int cycleEnd, int cycleStart) {

        List<Integer> cycle = new ArrayList<>();
        cycle.add(cycleStart);

        int cur = cycleEnd;
        while (cur != cycleStart) {
            cycle.add(0, cur);
            cur = parent.get(cur);
        }
        cycle.add(0, cycleStart);
        cycle.add(cycleStart);

        int minIdx = 0;
        for (int i = 1; i < cycle.size() - 1; i++) {
            if (cycle.get(i) < cycle.get(minIdx)) minIdx = i;
        }
        List<Integer> rotated = new ArrayList<>();
        for (int i = minIdx; i < cycle.size() - 1; i++) rotated.add(cycle.get(i));
        rotated.add(cycle.get(minIdx));
        return rotated;
    }

    private static List<Integer> sortedList(Set<Integer> set) {
        List<Integer> list = new ArrayList<>(set);
        java.util.Collections.sort(list);
        return list;
    }
}