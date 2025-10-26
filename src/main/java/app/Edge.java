package app;

public class Edge implements Comparable<Edge> {
    public final String u, v;
    public final double w;
    public Edge(String u, String v, double w){ this.u=u; this.v=v; this.w=w;}
    @Override
    public int compareTo(Edge o){ return Double.compare(this.w, o.w); }
    @Override
    public String toString(){ return String.format("(%s - %s : %.2f)", u, v, w); }
}
