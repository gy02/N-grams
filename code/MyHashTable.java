

import java.util.Objects;
import java.util.TreeMap;

public class MyHashTable {
    int size;
    MyLinkedObject[] table;
    MyHashFunction hashFunction;

    public MyHashTable(int size, MyHashFunction hashFunction) {
        this.size = size;
        table = new MyLinkedObject[size];
        this.hashFunction = hashFunction;
    }

    public void addLinkedObject(String w) {
        int index = hashFunction.hashCode(w) % size;
//        System.out.println("hashCode of \""+w+"\" is "+hashFunction.hashCode(w));
//        System.out.println("index of \""+w+"\" is "+index);
        if (table[index] == null) {//create the first node
            MyLinkedObject newLinkedObject = new MyLinkedObject(w);
            table[index] = newLinkedObject;
        } else {
            boolean isSet = table[index].setWord(w);
            if (!isSet){//w is not alphabetically not smaller than the word field of this object
                //insert to the first place of current table
                MyLinkedObject current = new MyLinkedObject(w);
                current.setNext(table[index].getNext());
                table[index]=current;
            }
        }
    }

    public void printAll() {
        for (int i = 0; i < size; i++) {
            if (table[i]!=null){
                MyLinkedObject linkedObject = table[i];
                System.out.println(i+"::");
                linkedObject.printAll();
            }
        }
    }

    public TreeMap<String,Integer> insertIntoMap(){
        TreeMap<String,Integer> map=new TreeMap<>();
        for (int i = 0; i < size; i++) {
            if (table[i]!=null){
                MyLinkedObject current=table[i];
                while (current!=null){
                    map.put(current.getWord(), current.getCount());
                    current=current.getNext();
                }
            }
        }
        return map;
    }

    public int getNodesCount(){
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (table[i]!=null){
                MyLinkedObject current=table[i];
                count+=current.nodesCount();
            }
        }
        return count;
    }
    public int getMaxNodesCount(){
        int maxCount=0;
        for (int i = 0; i < size; i++) {
            if (table[i]!=null){
                MyLinkedObject current=table[i];
                if (maxCount<current.nodesCount())
                    maxCount=current.nodesCount();
            }
        }
        return maxCount;
    }

    public int getMinNodesCount() {
        int minCount=99999;
        for (int i = 0; i < size; i++) {
            if (table[i]!=null){
                MyLinkedObject current=table[i];
                if (minCount>current.nodesCount())
                    minCount=current.nodesCount();
            }
        }
        return minCount;
    }
    public float getStdDeviation(){
        float average= (float) getNodesCount() /size;
        float squareDiffSum = 0;
        for (int i = 0; i < size; i++) {
            if (table[i]!=null){
                MyLinkedObject current=table[i];
                float diff=current.nodesCount()-average;
                squareDiffSum+=diff*diff;
            }
        }
        float meanSquareDiff = squareDiffSum / size; // Calculate the mean squared difference
        float standardDeviation = (float) Math.sqrt(meanSquareDiff); // Take the square root to get the standard deviation
        return standardDeviation;
    }
    public int getWordCount(String word){
        int index = hashFunction.hashCode(word) % size;
        MyLinkedObject linkedObject = table[index];
        while (!Objects.equals(linkedObject.getWord(), word)){
            linkedObject=linkedObject.getNext();
            if (linkedObject==null)
                return 0;
        }
        return linkedObject.getCount();
    }
    //p(s)=p(w2|w1)=c(w1,w2)/c(w1)
    public double getProbability(String w1,String w2){
         return getWordCount(w1+w2)/getWordCount(w1);
    }


}
