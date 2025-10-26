package app;

import java.util.*;

public class KruskalMST {
    public static class Result {
        public final List<Edge> mst; public final double totalCost;
        public final long ops; public final long timeMs;
        public Result(List<Edge> mst, double cost, long ops, long timeMs){
            this.mst=mst; this.totalCost=cost; this.ops=ops; this.timeMs=timeMs;
        }
    }
    public static Result run(Graph g){
        long ops = 0;
        long t0 = System.currentTimeMillis();

        List<Edge> edges = new ArrayList<>(g.edges);
        Collections.sort(edges);
        ops += edges.size() * (long)Math.log(Math.max(2, edges.size())); // rough for sort (for reporting)
        DisjointSet ds = new DisjointSet(); ds.makeSet(g.vertices);

        List<Edge> mst = new ArrayList<>();
        for(Edge e: edges){
            ops++; // checking union/find
            String ru = ds.find(e.u);
            String rv = ds.find(e.v);
            if(!ru.equals(rv)){
                ds.union(ru, rv);
                mst.add(e);
                ops++; // successful union
            }
            if(mst.size() == g.vertices.size() - 1) break;
        }
        double total = mst.stream().mapToDouble(x->x.w).sum();
        long t1 = System.currentTimeMillis();
        return new Result(mst, total, ops, t1-t0);
    }
}
