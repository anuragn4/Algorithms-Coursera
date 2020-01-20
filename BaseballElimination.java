/**
 *
 * @author 14342
 */
import java.util.List;
import java.util.ArrayList;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;

public class BaseballElimination {

    private int num_Teams;
    private List<String> teams;
    private List<String> trivial_Elimination_Teams;
    private int[] w;
    private int[] l;
    private int[] r;
    private int[][] g; 
    private int max_Flow;
    private int[][] team_Map;
    
    public BaseballElimination(String filename){
        int line = 0;        
        In in = new In(filename);
        String st;
        while((st=in.readLine())!=null){
            if(line==0){
                num_Teams = Integer.parseInt(st);
                teams = new ArrayList<>();
                w = new int[num_Teams];
                l = new int[num_Teams];
                r = new int[num_Teams];
                g = new int[num_Teams][num_Teams];
                team_Map = new int[(num_Teams-1)*(num_Teams-2)/2][2];
                line++;
            }
            else{
                String[] st_team = st.trim().split("\\s+");
                teams.add(st_team[0]);
                w[line-1] = Integer.parseInt(st_team[1]);
                l[line-1] = Integer.parseInt(st_team[2]);
                r[line-1] = Integer.parseInt(st_team[3]);
                for(int i=4; i<st_team.length; i++){
                    g[line-1][i-4] = Integer.parseInt(st_team[i]);                           
                }
                line++;
            }                
        }
    }
    public int numberOfTeams(){
        return num_Teams;
    }
    public Iterable<String> teams(){
        return teams;
    } 
    public int wins(String team){
        validateTeam(team);
        return w[team_index(team)];
    }
    public int losses(String team){
        validateTeam(team);
        return l[team_index(team)];
    }
    public int remaining(String team){
        validateTeam(team);
        return r[team_index(team)]; 
    }
    public int against(String team1, String team2){
        validateTeam(team1);
        validateTeam(team2);
        int first=team_index(team1);
        int second=team_index(team2);
        return g[first][second];
    }
    private int team_index(String team){
        int index=0;
        for(int i=0; i<teams.size();i++){
            if(team.equals(teams.get(i))){
                index=i;
                return index;
            }
        }
        return index;
    }
    private FlowNetwork createNetwork(int index){
        int num_vertices1 = (num_Teams-1)*(num_Teams-2)/2;
        int num_vertices2 = (num_Teams-1);
        int total_vertices = num_vertices1 + num_vertices2 + 2;
        max_Flow=0;
        FlowNetwork fn = new FlowNetwork(total_vertices);
        
        int current_vertex = 1;
        int target1;
        int target2;
        int target3;
        
        for(int i=0; i<num_Teams;i++){
            if(i==index) continue;
            target1 = i>index ? i:i+1;
            for(int j=i+1; j<num_Teams;j++){
                if(index==j) continue;
                team_Map[current_vertex-1][0]=i;
                team_Map[current_vertex-1][1]=j;
                max_Flow = max_Flow + g[i][j];
                fn.addEdge(new FlowEdge(0,current_vertex,g[i][j]));
                target2 = j>index ? j:j+1;                 
                fn.addEdge(new FlowEdge(current_vertex,num_vertices1+target1,Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(current_vertex,num_vertices1+target2,Double.POSITIVE_INFINITY));                
                current_vertex++;                
            }
        }
        for(int i=num_vertices1+1; i<total_vertices-1;i++){
            target3=i-num_vertices1-1<index ? i-num_vertices1-1 : i-num_vertices1;
            fn.addEdge(new FlowEdge(i,total_vertices-1,w[index]+r[index]-w[target3]));
        }
        return fn;        
    }
    public Iterable<String> certificateOfElimination(String team){
        validateTeam(team);
        if(!isEliminated(team)) return null;
        int index = team_index(team);
        if(trivialElimination(index)) return trivial_Elimination_Teams;
        int vertices_S = (num_Teams-1)*(num_Teams-2)/2;
        int total_vertices = vertices_S + (num_Teams-1) + 2;
        FordFulkerson ff = new FordFulkerson(createNetwork(index),0,total_vertices-1);
        List<String> minCut = new ArrayList<>();
        for(int i=1; i<=vertices_S; i++){
            if(ff.inCut(i)){
                String team1 = teams.get(team_Map[i-1][0]);
                String team2 = teams.get(team_Map[i-1][1]);
                if(!minCut.contains(team1)) minCut.add(team1);
                if(!minCut.contains(team2)) minCut.add(team2);
            }
        }
        return minCut;
    }
    public boolean isEliminated(String team){
        validateTeam(team);
        int index = team_index(team);
        if(trivialElimination(index)) return true;
        int vertices_S = (num_Teams-1)*(num_Teams-2)/2;
        int total_vertices = vertices_S + (num_Teams-1) + 2;
        FordFulkerson ff = new FordFulkerson(createNetwork(index),0,total_vertices-1);
        return (ff.value()!=max_Flow);        
    }
    private boolean trivialElimination(int index){
        boolean flag=false;
        trivial_Elimination_Teams=new ArrayList<>();
        for(int i=0; i<num_Teams;i++){
            if(i==index)continue;
            if(w[index]+r[index]<w[i]){
                trivial_Elimination_Teams.add(teams.get(i));
                flag=true;
            }            
        }
        return flag;        
    }
    private void validateTeam(String team){
        boolean flag=false;
        for(int i=0; i<num_Teams; i++){
            if(teams.get(i).equals(team)){
                flag=true;
                return;
            }            
        }
        if(!flag) throw new java.lang.IllegalArgumentException(" Team: " + team + " is Illegal");
    }

    
    public static void main(String[] args) {
       BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
            }
                StdOut.println("}");
        }
            else {
                StdOut.println(team + " is not eliminated");
        }
    }
    }
    
}
