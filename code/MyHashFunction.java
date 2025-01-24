

abstract class MyHashFunction {
    abstract public int hashCode(String word);
}

//using the first letter's ASCII of the word
class HashFun0 extends MyHashFunction {

    @Override
    public int hashCode(String word) {
        return word.charAt(0);
    }
}
//Accumulate the ASCII value for each character of the first word(used for bigram)
class HashFunForOne extends MyHashFunction {
    @Override
    public int hashCode(String word) {
        word=word.split(" ")[0];
        int hashcode = 0;
        for (int i = 0; i < word.length(); i++) {
            hashcode += word.charAt(i);
        }
        return hashcode;
    }
}
//Accumulate the ASCII value for each character of the first two word(used for trigram)
class HashFunForTwo extends MyHashFunction {
    @Override
    public int hashCode(String word) {
        word=word.split(" ")[0]+word.split(" ")[1];
        int hashcode = 0;
        for (int i = 0; i < word.length(); i++) {
            hashcode += word.charAt(i);
        }
        return hashcode;
    }
}
//Accumulate the ASCII value for each character
class HashFunForAll extends MyHashFunction {
    @Override
    public int hashCode(String word) {
        int hashcode = 0;
        for (int i = 0; i < word.length(); i++) {
            hashcode += word.charAt(i);
        }
        return hashcode;
    }

    public static void main(String[] args) {
        MyHashFunction myHashFunction;
        myHashFunction = new HashFunForOne();
        System.out.println(myHashFunction.hashCode("president")%70);
//        System.out.println(myHashFunction.hashCode("the apple"));
    }
}

//multiplicationHash
class HashFun5 extends MyHashFunction {
    HashFunForOne hashFun1 = new HashFunForOne();

    @Override
    public int hashCode(String word) {
        double k = 0.618;
        double temp1 = hashFun1.hashCode(word) * k;//shrink
        double temp2 = temp1 % 1;//Take the decimal part
        double res = temp2 * 100;//magnify
        return (int) res;
    }
}

//Bitwise arithmetic hashing
class HashFun6 extends MyHashFunction {
    @Override
    public int hashCode(String word) {
        long hash = 0;
//        ArrayIndexOutOfBoundsException------maybe due to integer overflow
        for (int i = 0; i < word.length(); i++) {
            hash ^= (hash << 2) + (hash >> 1) + word.charAt(i);
        }
        return (int) hash;
    }
}

class HashFun7 extends MyHashFunction {
    //    also ArrayIndexOutOfBoundsException------maybe due to integer overflow
    public int djb2(String str) {
        int hash = 5381;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            hash = ((hash << 5) + hash) + c; // hash * 33 + c
        }
        return hash;
    }

    @Override
    public int hashCode(String word) {
        long hash = 5381;
        for (int i = 0; i < word.length(); i++) {
            System.out.println("左移结果"+(hash << 5));
            hash = ((hash << 5) + hash) + word.charAt(i);
        }
        System.out.println("--------------");
        return (int) hash;
    }
}