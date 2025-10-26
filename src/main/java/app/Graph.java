package app;

import java.util.*;

public class Graph {
    public final Set<String> vertices = new LinkedHashSet<>();
    public final List<Edge> edges = new ArrayList<>();
    public void addVertex(String v){ vertices.add(v); }
    public void addEdge(String u, String v, double w){
        vertices.add(u); vertices.add(v);
        edges.add(new Edge(u,v,w));
    }
}
