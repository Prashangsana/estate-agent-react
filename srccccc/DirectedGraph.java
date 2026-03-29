// Student ID: 20232986
// Name: Prashangsana Dissanayake

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

/**
 * A directed graph represented using an adjacency list.
 * Utilises HashMaps and HashSets for efficient average-case operations.
 */
public class DirectedGraph {

    private final HashMap<Integer, HashSet<Integer>> adjacencyList;
    private final HashMap<Integer, Integer> inDegree;

    public DirectedGraph() {
        adjacencyList = new HashMap<>();
        inDegree = new HashMap<>();
    }

    /**
     * Adds a vertex to the graph.
     * Time Complexity: O(1) average.
     */
    public void addVertex(int v) {
        adjacencyList.putIfAbsent(v, new HashSet<>());
        inDegree.putIfAbsent(v, 0);
    }

    /**
     * Adds a directed edge between two vertices.
     * Time Complexity: O(1) average.
     */
    public void addEdge(int from, int to) {
        addVertex(from);
        addVertex(to);
        if (adjacencyList.get(from).add(to)) {
            inDegree.merge(to, 1, Integer::sum);
        }
    }

    public Set<Integer> getVertices() {
        return new HashSet<>(adjacencyList.keySet());
    }

    public Set<Integer> getSuccessors(int v) {
        return adjacencyList.getOrDefault(v, new HashSet<>());
    }

    public int vertexCount() {
        return adjacencyList.size();
    }

    public boolean isEmpty() {
        return adjacencyList.isEmpty();
    }

    /**
     * Finds a vertex with an out-degree of 0 (a sink).
     * Time Complexity: O(V) where V is the number of vertices in the remaining graph.
     */
    public int findSink() {
        for (Map.Entry<Integer, HashSet<Integer>> entry : adjacencyList.entrySet()) {
            if (entry.getValue().isEmpty()) {
                return entry.getKey();
            }
        }
        return -1;
    }

    /**
     * Removes a vertex and all its incoming and outgoing edges.
     * Time Complexity: O(V + out-degree(v)) as it scans all vertices to remove incoming edges.
     */
    public void removeVertex(int v) {
        if (!adjacencyList.containsKey(v)) return;

        for (int successor : adjacencyList.get(v)) {
            inDegree.merge(successor, -1, Integer::sum);
        }

        adjacencyList.remove(v);
        inDegree.remove(v);

        for (HashSet<Integer> neighbours : adjacencyList.values()) {
            neighbours.remove(v);
        }
    }

    /**
     * Creates a deep copy of the graph to preserve original state during processing.
     * Time Complexity: O(V + E)
     */
    public DirectedGraph deepCopy() {
        DirectedGraph copy = new DirectedGraph();
        for (Map.Entry<Integer, HashSet<Integer>> entry : adjacencyList.entrySet()) {
            int v = entry.getKey();
            copy.adjacencyList.put(v, new HashSet<>(entry.getValue()));
            copy.inDegree.put(v, inDegree.get(v));
        }
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Integer> sorted = new ArrayList<>(adjacencyList.keySet());
        java.util.Collections.sort(sorted);
        for (int v : sorted) {
            List<Integer> succs = new ArrayList<>(adjacencyList.get(v));
            java.util.Collections.sort(succs);
            sb.append(v).append(" -> ").append(succs).append("\n");
        }
        return sb.toString();
    }
}