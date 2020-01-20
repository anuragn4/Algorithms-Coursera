/**
 *
 * @author 14342
 */
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first, last;
    private int N;
    
    private class Node{
        Item item;
        Node next;    
        Node prev;
    }
    
    public Deque(){
        first=null;
        last=null;
        N=0;        
    }
    public boolean isEmpty(){
        return N==0;
    }
    public int size(){
        return N;        
    }
    public void addFirst(Item item){
        if(item==null) throw new IllegalArgumentException("argument is null");
        Node newFirst = new Node();
        newFirst.item = item;
        newFirst.next = first;
        if(first !=null) first.prev = newFirst;
        first = newFirst;
        if(last==null) last = first;
        N++;
    }
    public void addLast(Item item){
        if(item==null) throw new IllegalArgumentException("argument is null");
        Node newLast = new Node();
        newLast.item = item;
        newLast.prev = last;
        if(last !=null) last.next = newLast;
        last = newLast; 
        if(first==null) first=last;
        N++;
    }
   public Item removeFirst(){
       if(N==0) throw new NoSuchElementException();
       Node newFirst = first.next;
       Item item = first.item;
       if(last==first) last = newFirst;
       first = newFirst;
       if(newFirst !=null) first.prev=null;
       N--;
       return item;       
   }
   public Item removeLast(){
       if(N==0) throw new NoSuchElementException();
       Node newLast = last.prev;
       Item item = last.item;
       if(newLast !=null) newLast.next = null;
       if(first==last) first = newLast;
       last = newLast;
       N--;
       return item;       
   }
   public Iterator<Item> iterator(){
       return new queueIterator();
   }  
   
   private class queueIterator implements Iterator<Item>{
       private Node current = first;
       public boolean hasNext(){
           return current !=null;
       }
       public Item next(){
           if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next; 
            return item;           
       }
   }     
    public static void main(String[] args) {
        Deque<Integer> deq = new Deque<>();
        deq.addLast(0);
        int i = deq.removeLast();
        
        System.out.println(i);
        deq.addLast(3);
        int j = deq.size();
        System.out.println(j);
        j = deq.removeFirst();
        System.out.println(j);
    }
    
}
