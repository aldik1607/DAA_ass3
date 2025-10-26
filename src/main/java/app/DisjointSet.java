package app;
import java.util.*;

public class DisjointSet {
    private final Map<String,String> parent = new HashMap<>();
    private final Map<String,Integer> rank = new HashMap<>();

    public void makeSet(Collection<String> elems){
        parent.clear();
        rank.clear();
        for(String e: elems){ parent.put(e,e); rank.put(e,0); }
    }

    public String find(String x){
        String p = parent.get(x);
        if (p == null) return null;
        if(!p.equals(x)){
            String r = find(p);
            parent.put(x, r);
            return r;
        }
        return p;
    }


    public boolean unionRoots(String ra, String rb){
        if(ra == null || rb == null) return false;
        if(ra.equals(rb)) return false;
        int rka = rank.getOrDefault(ra, 0);
        int rkb = rank.getOrDefault(rb, 0);
        if(rka < rkb) parent.put(ra, rb);
        else if(rka > rkb) parent.put(rb, ra);
        else { parent.put(rb, ra); rank.put(ra, rka + 1); }
        return true;
    }


    public boolean union(String a, String b){
        String ra = find(a);
        String rb = find(b);
        return unionRoots(ra, rb);
    }
}
