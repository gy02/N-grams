

public class MyLinkedObject {
    private String word;
    private int count;
    private MyLinkedObject next;

    public MyLinkedObject(String w) {
        this.word = w;
        this.count = 1;
        this.next = null;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public MyLinkedObject getNext() {
        return next;
    }

    public void setNext(MyLinkedObject next) {
        this.next = next;
    }

    public boolean setWord(String w) {
        if (w.equalsIgnoreCase(this.word))
            this.count++;
        else {
            int compareToThis = w.compareToIgnoreCase(this.word);
            //w<current
            if (compareToThis < 0) {//w is alphabetically smaller than current word
                //insert unsuccessfully
                return false;
            } else {
                MyLinkedObject newLinkedObject = new MyLinkedObject(w);
                if (this.next == null) {
                    this.next = newLinkedObject;
                }
                else {
                    int compareToNext = w.compareToIgnoreCase(this.next.word);
                    //current<w<next
                    if (compareToNext < 0) {//w is alphabetically smaller than the next word
                        //insert w between this and next
                        newLinkedObject.next = this.next;
                        this.next = newLinkedObject;
                    } else {//w>next
                        this.next.setWord(w);//recursive
                    }
                }
            }
        }
        return true;
    }

    public void printAll() {
        MyLinkedObject current = this;
        while (current != null) {
            System.out.print(current.word + ":" + current.count + " -> ");
            current = current.next;
        }
        System.out.println();
    }

    public int nodesCount() {
        MyLinkedObject current = this;
        int num = 0;
        while (current != null) {
            num++;
            current = current.next;
        }
        return num;
    }
}
