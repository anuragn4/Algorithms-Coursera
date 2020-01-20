import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
public class Permutation {
   //private static RandomizedQueue<String> rq = new RandomizedQueue<>();
   public static void main(String[] args){
       RandomizedQueue<String> rq = new RandomizedQueue<>();
       int k = Integer.parseInt(args[0]);
       while(!StdIn.isEmpty()){
           String input = StdIn.readString();
           rq.enqueue(input);    
       }
       for(int i=0; i<k; i++){
           String temp = rq.dequeue();
           StdOut.println(temp);
       }
   }    
}
