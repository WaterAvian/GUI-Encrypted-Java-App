import java.util.*;
import javax.crypto.SecretKey;

/*string , int HashMap
 * 
 */

public class KeyMap {

    private ArrayList[] array;
    
    KeyMap(){
        array = new ArrayList[16];
        for(int i = 0; i<array.length; i++){
            array[i] = new ArrayList<Pair>();
        }

    }

    KeyMap(int arraySize){
        array = new ArrayList[arraySize];
    }

    public void put(SecretKey val, Long key){
        Pair pair = new Pair(key, val0);        
        this.array[getHash(key)].add(pair);
    }
    
   
    public SecretKey get(Long key){
        ArrayList<Pair> temp = array[getHash(key)];
        for(int i = 0; i<temp.size();i++){
            if(temp.get(i).getKey().equals(key)){
                return (SecretKey)temp.get(i).getVal();
            }
        }
        return null;
    }


    /*
    public String[] getKeys(){}
    public int[] getVals(){}
    public boolean isEmpty(){}
    */

    private int getHash(Long key){
        int hash = key.intValue() % this.array.length;
        return hash;
    }

    public class Pair {
        public Object[] pair;
        Pair(Object a, Object b){
            pair = new Object[2];
            pair[0] = a;
            pair[1] = b;
        }
        public Object getVal(){
            return pair[1];
        }
        public Object getKey(){
            return pair[0];
        }
    }

}
