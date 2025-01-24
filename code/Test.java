import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Yuan Gao
 * @date 10/12/2023 11:58
 * @email 2581677302@qq.com
 */
public class Test {
    MyLanguageModel myLanguageModel;
    MyHashTable hashTable;
    MyHashFunction myHashFunction;

    public void testForFun(){
        myHashFunction=new HashFun7();
        hashTable = new MyHashTable(100, myHashFunction);
        MyHashTable myHashTable = MyLanguageModel.inputWords("code/news.txt", hashTable);
        float stdDeviation = myHashTable.getStdDeviation();
        System.out.println(myHashFunction.getClass()+"   "+stdDeviation);
    }
    public void testForSize() {
        myHashFunction=new HashFun5();
        for (int i = 100; i < 200; i++) {
            hashTable = new MyHashTable(i, myHashFunction);
//            myLanguageModel = new MyLanguageModel(myHashTable);
//            myLanguageModel.initGUI();
//            myLanguageModel.makeStatistics();

//            MyHashTable myHashTable = inputWords("code/news.txt", hashTable);
            MyHashTable myHashTable = MyLanguageModel.inputBigrams("code/news.txt", hashTable);
            float stdDeviation = myHashTable.getStdDeviation();
            System.out.println(i+"   "+stdDeviation);
        }
    }

    public static void main(String[] args) {
//        CopyOnWriteArrayList
        Test test = new Test();
        test.testForSize();
        HashFunForOne hashFun1=new HashFunForOne();
//        System.out.println(hashFun2.hashCode("assume that")%70);
//        System.out.println(hashFun2.hashCode("a balance"));
//        System.out.println(hashFun2.hashCode("a best"));

//        System.out.println(hashFun1.hashCode("a lot")%70);
//        System.out.println(hashFun1.hashCode("and it")%70);

//        MyLanguageModel.analysisWords("a b c ");

//        HashFun7 hashFun7=new HashFun7();
//        System.out.println(hashFun7.hashCode("introduction presentation")%70);
//        System.out.println(hashFun7.hashCode("assume that")%70);
//        System.out.println(hashFun7.hashCode("a balance")%70);
//        System.out.println(hashFun7.hashCode("assume that a")%70);
    }
}
