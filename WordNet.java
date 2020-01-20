/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//package wordnet;
import edu.princeton.cs.algs4.*;
import java.util.*;
//import sap.SAP;
/**
 *
 * @author 14342
 */
public final class WordNet {
    private Map<String,Bag<Integer>> nounMap = new HashMap<String,Bag<Integer>>();
    private Map<Integer,String> idNounMap = new HashMap<Integer,String>();
    private Map<Integer,List<Integer>> idMap = new HashMap<Integer, List<Integer>>();
    private Digraph G;
    private SAP sap_inst;
       
    public WordNet(String synsets, String hypernyms){
        if(synsets==null || hypernyms==null) throw new IllegalArgumentException("Constructor argument is null");
        In in1 = new In(synsets);
        In in2 = new In(hypernyms);

        while(!in1.isEmpty()){
            String line = in1.readLine();
            String[] nounSet = line.split(",");
            int id = Integer.parseInt(nounSet[0]);
            String[] nounList = nounSet[1].split(" ");
            if(!idNounMap.containsKey(id)){
                StringBuilder sb = new StringBuilder(nounList[0]);
                for(int i=1; i<nounList.length; i++){
                    sb.append(" ");
                    sb.append(nounList[i]);
                }
                idNounMap.put(id,sb.toString());
            }
            for(int i=0; i<nounList.length; i++){
                if(!nounMap.containsKey(nounList[i])){
                    Bag<Integer> nouns = new Bag<Integer>();
                    nouns.add(id);
                    nounMap.put(nounList[i],nouns);
                }
                else nounMap.get(nounList[i]).add(id);
            }
        }
        while(!in2.isEmpty()){
            String line = in2.readLine();
            String[] idList = line.split(",");
            int id0 = Integer.parseInt(idList[0]);
            if(!idMap.containsKey(id0)) {idMap.put(id0, new ArrayList<Integer>());}
            for(int i=1; i<idList.length; i++){
                int idx = Integer.parseInt(idList[i]);
                if(!idMap.containsKey(idx)) {idMap.put(idx, new ArrayList<Integer>());}
                List<Integer> b1 = idMap.get(id0);
                b1.add(idx);
                idMap.put(id0,b1);
            }
        }

        G = new Digraph(idMap.size());
        for(int v = 0; v<idMap.size(); v++){
            List<Integer> adj = idMap.get(v);
            for(int i=0; i<adj.size(); i++){
                G.addEdge(v,adj.get(i));
            }            
        }
        sap_inst = new SAP(G);
    }
    
    public Iterable<String> nouns(){
        if(nounMap==null) throw new IllegalArgumentException("noun Map is null");
        return nounMap.keySet();
    }
    
    public boolean isNoun(String word){
        if(word==null) throw new IllegalArgumentException("Method argument is null");
        return nounMap.containsKey(word);
    }
    
    public int distance(String nounA, String nounB){
        validateNoun(nounA);
        validateNoun(nounB);
        return sap_inst.length(nounMap.get(nounA),nounMap.get(nounB));          
    }
    
    public String sap(String nounA, String nounB){
        validateNoun(nounA);
        validateNoun(nounB);
        return idNounMap.get(sap_inst.ancestor(nounMap.get(nounA),nounMap.get(nounB)));        
    }
    
    private void validateNoun(String nounA) {
        if (nounA==null || !nounMap.containsKey(nounA))
            throw new IllegalArgumentException("noun " + nounA + " is not a valid noun");
    }
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String synsets = args[0];
        String hypernyms = args[1];
        WordNet wn = new WordNet(synsets , hypernyms);
        StdOut.printf("distance is = %d\n", wn.distance("a","b"));        
        //System.out.println( wn.sap("sincerity","journal"));
    }
    
}
