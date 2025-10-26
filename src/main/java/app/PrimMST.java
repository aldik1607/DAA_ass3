package app;

import java.util.*;

public class PrimMST {
    public static class Result {
        public final List<Edge> mst; public final double totalCost;
        public final long ops; public final long timeMs;
        public Result(List<Edge> mst, double cost, long ops, long timeMs){
            this.mst=mst; this.totalCost=cost; this.ops=ops; this.timeMs=timeMs;
        }
    }
    public static Result run(Graph g, String start){
        long ops = 0;
        long t0 = System.currentTimeMillis();

        Map<String, Double> key = new HashMap<>();
        Map<String, String> parent = new HashMap<>();
        Set<String> inMST = new HashSet<>();
        for(String v: g.vertices){ key.put(v, Double.POSITIVE_INFINITY); parent.put(v, null); }

        key.put(start, 0.0);
        PriorityQueue<String> pq = new PriorityQueue<>(Comparator.comparingDouble(key::get));
        pq.addAll(g.vertices);

        // adjacency list
        Map<String, List<Edge>> adj = new HashMap<>();
        for(String v: g.vertices) adj.put(v, new ArrayList<>());
        for(Edge e: g.edges){
            adj.get(e.u).add(e); adj.get(e.v).add(new Edge(e.v,e.u,e.w));
        }

        List<Edge> mst = new ArrayList<>();
        while(!pq.isEmpty()){
            String u = pq.poll();
            if(inMST.contains(u)) continue;
            inMST.add(u);
            ops++; // extract
            String p = parent.get(u);
            if(p!=null) mst.add(new Edge(p,u,key.get(u)));

            for(Edge e: adj.get(u)){
                ops++; // adjacency consideration as operation
                if(!inMST.contains(e.v) && e.w < key.get(e.v)){
                    key.put(e.v, e.w);
                    parent.put(e.v, u);
                    // decrease-key: reinsert
                    pq.remove(e.v);
                    pq.add(e.v);
                    ops++; // decrease-key op
                }
            }
        }
        double total = mst.stream().mapToDouble(x->x.w).sum();
        long t1 = System.currentTimeMillis();
        return new Result(mst, total, ops, t1-t0);
    }
}
