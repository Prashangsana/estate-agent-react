// Student ID: 20232986
// Name: Prashangsana Dissanayake

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Utility class to read graph data from a text file and construct a DirectedGraph.
 */
public class GraphParser {

    /**
     * Parses the file into a DirectedGraph object.
     * Time Complexity: O(N) where N is the number of lines in the file.
     */
    public static DirectedGraph parse(String filePath) throws IOException {
        DirectedGraph graph = new DirectedGraph();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] tokens = line.split("\\s+");
                if (tokens.length < 2) continue;

                try {
                    int from = Integer.parseInt(tokens[0]);
                    int to   = Integer.parseInt(tokens[1]);
                    graph.addEdge(from, to);
                } catch (NumberFormatException e) {
                }
            }
        }

        return graph;
    }
}