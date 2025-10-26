package app;

import java.util.*;

public class DisjointSet {
    private final Map<String,String> parent = new HashMap<>();
    private final Map<String,Integer> rank = new HashMap<>();
    public void makeSet(Collection<String> elems){
        for(String e: elems){ parent.put(e,e); rank.put(e,0); }
    }
    public String find(String x){
        if(!parent.get(x).equals(x)) parent.put(x, find(parent.get(x)));
        return parent.get(x);
    }
    public boolean union(String a, String b){
        String ra = find(a), rb = find(b);
        if(ra.equals(rb)) return false;
        int rka = rank.get(ra), rkb = rank.get(rb);
        if(rka < rkb) parent.put(ra, rb);
        else if(rka > rkb) parent.put(rb, ra);
        else { parent.put(rb, ra); rank.put(ra, rka+1); }
        return true;
    }
}
