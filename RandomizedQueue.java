import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] a;
    private int N;

   public RandomizedQueue(){
       a = (Item[]) new Object[2];
       N = 0;       
   }
   public boolean isEmpty(){
       return N==0;       
   }
   public int size(){
       return N;       
   }
   public void enqueue(Item item){
       if(item==null) throw new java.lang.IllegalArgumentException();
       if(a.length==N) resize(2*N);
       a[N++]=item;       
   }
   public Item dequeue(){
       if(N==0) throw new java.util.NoSuchElementException();
       int i = StdRandom.uniform(N);
       Item temp = a[i];
       a[i]=a[N-1];
       N--;
       if (N > 0 && N == a.length/4) resize(a.length/2);
       return temp;         
   }
   public Item sample(){
       if(N==0) throw new java.util.NoSuchElementException();  
       int i = StdRandom.uniform(N);
       Item temp = a[i];
       return temp;
   }
   private void resize(int capacity){
       assert capacity >= N;
       Item[] temp = (Item[]) new Object[capacity];
       for (int i = 0; i < N; i++) {
           temp[i] = a[i];
        }
        a = temp;       
   }
   public Iterator<Item> iterator(){
       return new randomIterator();       
   }
   
   private class randomIterator implements Iterator<Item>{
       private int temp;
       private Item[] b; 
       
       public boolean hasNext(){
           return temp !=0;
       }
       public randomIterator(){
           temp = N;
           b = (Item[]) new Object[N];
           for(int i=0; i<N; i++)
               b[i] = a[i];
       }
       public Item next(){
           if (!hasNext()) throw new NoSuchElementException();
           int i = StdRandom.uniform(temp);
           Item item = b[i];
           b[i] = b[temp-1];
           temp=temp-1;
           return item;           
       }
   }
   public static void main(String[] args){
       RandomizedQueue<Integer> rq = new RandomizedQueue<>();
       for(int i=0; i<10; i++){
           rq.enqueue(i);
       }
       for(int i : rq){
           for(int j : rq){
               System.out.println("i is : " + i + "j is : " + j);
           }           
       }       
   }    
}
