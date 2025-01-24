

import utils.FormatCheck;
import utils.ToWordConverter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

public class MyLanguageModel {
    MyHashTable myHashTable, myHashTable2, myHashTable3;
    TreeMap<String, Integer> treeMap;
    int totalWords = 0, bigramsNum = 0, trigramsNum = 0;
    static List<String> errorWords = new ArrayList<String>() {
        @Override
        public String toString() {
            StringBuilder res = new StringBuilder();
            for (String word : errorWords) {
                res.append(word).append(", ");
            }
            if (res.length() >= 2)
                return res.substring(0, res.length() - 2);
            else
                return null;
        }
    };

    public MyLanguageModel(MyHashTable myHashTable, MyHashTable myHashTable2, MyHashTable myHashTable3) {
        this.myHashTable = myHashTable;
        this.myHashTable2 = myHashTable2;
        this.myHashTable3 = myHashTable3;
    }

    public static void main(String[] args) {
//        MyHashTable hashTable = inputWords("code/news.txt");
//        MyHashTable hashTable = inputWords("C:\\Users\\25816\\Desktop\\news.txt");
//        DisplayGUI.init(hashTable);
//        FileDisplayGUI.initGUI();
        MyHashTable myHashTable = new MyHashTable(70, new HashFunForOne());
        MyHashTable myHashTable2 = new MyHashTable(700, new HashFunForOne());
        MyHashTable myHashTable3 = new MyHashTable(800, new HashFunForTwo());
        MyLanguageModel myLanguageModel = new MyLanguageModel(myHashTable, myHashTable2, myHashTable3);
        myLanguageModel.initGUI();
    }

    //probability of bigram
    public double getProbability(String w1, String w2) {
        return (double) myHashTable2.getWordCount(w1 + w2) / myHashTable.getWordCount(w1);
    }

    //bigram
    public String getNextWord(String w1) {
        System.out.println("getNextWord("+w1+")");
        w1 = w1.toLowerCase();
        String w2 = null;
        double probability = 0;
        int i = myHashTable2.hashFunction.hashCode(w1) % myHashTable2.size;
        MyLinkedObject linkedObject = myHashTable2.table[i];
        while (linkedObject != null) {
//            System.out.println("****************");
//            System.out.println(linkedObject.getWord());
            String firstWord = linkedObject.getWord().split(" ")[0];
            if (firstWord.equals(w1) && probability < linkedObject.getCount()) {
                probability = linkedObject.getCount();
                w2 = linkedObject.getWord().split(" ")[1];
                System.out.println(probability + "-->" + w2);
            }
            linkedObject = linkedObject.getNext();
        }
        System.out.println("result of getNextWord1: " + w2);
        return w2;
    }

    //trigram
    public String getNextWord(String w1, String w2) {
        System.out.println("getNextWord("+w1+","+w2+")");
        w1 = w1.toLowerCase();
        w2 = w2.toLowerCase();
        String w3 = null;
        double probability = 0;
        int i = myHashTable3.hashFunction.hashCode(w1 + " " + w2) % myHashTable3.size;
        MyLinkedObject linkedObject = myHashTable3.table[i];
        while (linkedObject != null) {
//            System.out.println("--------------------");
//            System.out.println(linkedObject.getWord());
            String preWord = linkedObject.getWord().split(" ")[0] + linkedObject.getWord().split(" ")[1];
            if (preWord.equals(w1 + w2) && probability < linkedObject.getCount()) {
                probability = linkedObject.getCount();
                w3 = linkedObject.getWord().split(" ")[2];
//                System.out.println(probability + "-->" + w3);
            }
            linkedObject = linkedObject.getNext();
        }
        System.out.println("result of getNextWord2: " + w3);
        return w3;
    }

    public String getWordsSeq(String[] words) {
        StringBuilder res = new StringBuilder();
        String temp, word, nextWord;
        word = words[0].toLowerCase();
        if (words.length == 1) {
//            word = words[0].toLowerCase();
            nextWord = getNextWord(word);
            if (nextWord!=null)
                res.append(" ").append(nextWord);
            else
                return null;
            System.out.println("getWordsSeq" + "(" + word + ")");
        } else {//length==2
//            word = words[1].toLowerCase();
            nextWord = words[1].toLowerCase();
            System.out.println("getWordsSeq" + "(" + word + "," + nextWord + ")");
        }
        //bigrams
//        for (int i = 0; i < 20; i++) {
//            nextWord=getNextWord(word);
//            if (nextWord == null) {
//                System.out.println("cannot find next word of '" + word);
//                return null;
//            }
//            res.append(" ").append(nextWord);
//            word = nextWord;
//        }
        //trigrams
        for (int i = 0; i < 20; i++) {
            temp = nextWord;
//            System.out.println("getNextWord(" + word + ", " + nextWord + ")");
            nextWord = getNextWord(word, nextWord);
            if (nextWord == null) {
                System.out.println("cannot find next word of '" + word + "' and '" + nextWord+"'");
                //change to use bigrams
                nextWord=getNextWord(temp);
//                System.out.println("nextWord: "+nextWord);
                if (nextWord==null)
                    return null;
            }
            res.append(" ").append(nextWord);
            word = temp;
        }
        return String.valueOf(res);
    }

    public void initGUI() {
        JFrame frame = new JFrame("My Language Model");

        // Create a button and text area to show a file
        JButton selectFileButton = new JButton("Select File(txt only)");
        JTextArea textArea = new JTextArea(20, 40);
        JScrollPane scrollPane = new JScrollPane(textArea);

        // Create a set of hidden buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        buttonPanel.setVisible(false);

        JButton button1 = new JButton("text");
        JButton button2 = new JButton("unigrams");
        JButton button3 = new JButton("bigrams");
        JButton button4 = new JButton("trigrams");
        JButton button5 = new JButton("statistics");
        JButton button6 = new JButton("predict");

        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);
        buttonPanel.add(button4);
        buttonPanel.add(button5);
        buttonPanel.add(button6);

        final StringBuilder[] content = new StringBuilder[1];
        // Set the click event for the button
        selectFileButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            //forbid to modify the file type to be selected
            fileChooser.setAcceptAllFileFilterUsed(false);
            //make filter to only show txt file
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
            fileChooser.setFileFilter(filter);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                textArea.setText("please wait a moment");
                SwingUtilities.invokeLater(() -> {
                    //Reads the contents of the selected file and displays it in the text area
                    String filePath = fileChooser.getSelectedFile().getPath();
//                    System.out.println(filePath);
                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new FileReader(filePath));
                        String line, biTemp = null;
                        String triTemp1 = null, triTemp2 = null;
                        content[0] = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            content[0].append(line).append("\n");
                            //set words into MyHashTable
                            List<String> stringList = preprocessLine(line);
//                            System.out.println("stringList的size是：" + stringList.size());
                            int size = stringList.size();
                            //unigrams
                            for (String s : stringList) {
                                totalWords++;//not include error word
                                myHashTable.addLinkedObject(s);
                            }
                            //bigrams
                            if (!stringList.isEmpty() && biTemp != null) {//have word left in last line
                                myHashTable2.addLinkedObject(biTemp + " " + stringList.get(0));
                                bigramsNum++;
                                biTemp = null;
                            }
                            for (int i = 0; i < stringList.size() - 1; i++) {
                                myHashTable2.addLinkedObject(stringList.get(i) + " " + stringList.get(i + 1));
                                bigramsNum++;
                            }
                            //last word of this line
                            if (!stringList.isEmpty())
                                biTemp = stringList.get(stringList.size() - 1);

                            //trigrams
                            String firstWord;
                            String secondWord;
                            if (size == 0)
                                continue;
                                //size=1
                            else if (size == 1) {
                                firstWord = stringList.get(0);
                                if (triTemp1 != null && triTemp2 != null) {
                                    myHashTable3.addLinkedObject(triTemp1 + " " + triTemp2 + " " + firstWord);
                                    trigramsNum++;
                                    triTemp1 = triTemp2;
                                    triTemp2 = firstWord;
                                } else if (triTemp1 == null && triTemp2 != null) {
                                    triTemp1 = triTemp2;
                                    triTemp2 = firstWord;
                                } else {
                                    triTemp2 = firstWord;
                                }
                            } else {//size>=2
                                firstWord = stringList.get(0);
                                secondWord = stringList.get(1);
                                if (triTemp1 != null && triTemp2 != null) {
                                    myHashTable3.addLinkedObject(triTemp1 + " " + triTemp2 + " " + firstWord);
                                    trigramsNum++;
                                    myHashTable3.addLinkedObject(triTemp2 + " " + firstWord + " " + secondWord);
                                    trigramsNum++;
                                } else if (triTemp1 == null && triTemp2 != null) {
                                    myHashTable3.addLinkedObject(triTemp2 + " " + firstWord + " " + secondWord);
                                    trigramsNum++;
                                }
                                for (int i = 0; i < size - 2; i++) {
                                    myHashTable3.addLinkedObject(stringList.get(i) + " " + stringList.get(i + 1) + " " + stringList.get(i + 2));
                                    trigramsNum++;
                                }
                                triTemp1 = stringList.get(size - 2);
                                triTemp2 = stringList.get(size - 1);
                            }
                        }

                        textArea.setText(content[0].toString());
                        reader.close();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                });
                // set the hide button to be visible when the text is read
                buttonPanel.setVisible(true);
            }
        });

        button1.addActionListener(actionEvent -> {
            textArea.setText(content[0].toString());
            scrollPane.setViewportView(textArea);

        });
        button2.addActionListener(actionEvent -> {
            JPanel panel = showGrams(myHashTable);
            scrollPane.setViewportView(panel);
        });
        button3.addActionListener(actionEvent -> {
            JPanel panel = showGrams(myHashTable2);
            scrollPane.setViewportView(panel);
        });
        button4.addActionListener(actionEvent -> {
            JPanel panel = showGrams(myHashTable3);
            scrollPane.setViewportView(panel);
        });
        button5.addActionListener(actionEvent -> {
            System.out.println("The words that are most likely to appear " + getNextWord("president", "is"));
            //set data
//            treeMap = myHashTable.insertIntoMap();
//            treeMap2 = myHashTable2.insertIntoMap();

            JTable table1 = makeStatistics(myHashTable, 1);
            JTable table2 = makeStatistics(myHashTable2, 2);
            JTable table3 = makeStatistics(myHashTable3, 3);
            table1.setEnabled(false);
            table2.setEnabled(false);
            table3.setEnabled(false);

            // set table header
            JTableHeader header1 = table1.getTableHeader();
            JTableHeader header2 = table2.getTableHeader();
            JTableHeader header3 = table3.getTableHeader();
            //set background color of the header
            header1.setBackground(Color.YELLOW);
            header2.setBackground(Color.YELLOW);
            header3.setBackground(Color.YELLOW);
            table1.getTableHeader().getColumnModel().getColumn(0).setHeaderValue("unigrams");
            table2.getTableHeader().getColumnModel().getColumn(0).setHeaderValue("bigrams");
            table3.getTableHeader().getColumnModel().getColumn(0).setHeaderValue("trigrams");


            JPanel panel = new JPanel(new GridLayout(3, 1));

//            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            JScrollPane pane1 = new JScrollPane(table1);
            panel.add(pane1);
            panel.add(new JScrollPane(table2));
            panel.add(new JScrollPane(table3));

//            JPanel panel = new JPanel(new BorderLayout());
//
//            JScrollPane scrollPane1 = new JScrollPane(table1);
//            JScrollPane scrollPane2 = new JScrollPane(table2);
//
//            panel.add(scrollPane1, BorderLayout.NORTH);
//            panel.add(scrollPane2, BorderLayout.SOUTH);

            scrollPane.setViewportView(panel);
        });
        button6.addActionListener(actionEvent -> {
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            // create border instance
            Border border = BorderFactory.createLineBorder(Color.CYAN, 20); //LineBorder in blue
            panel.setBorder(border);

            JLabel inputLabel = new JLabel("Input:");
            inputLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            JTextField textField = new JTextField(20);
            Dimension size = textField.getMaximumSize();
            size.height = 30;
            textField.setMaximumSize(size);
            textField.setAlignmentX(Component.LEFT_ALIGNMENT);

            JLabel predictionLabel = new JLabel("Prediction:");
            predictionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Multiple lines show the text area of the forecast field
            JTextArea predictionTextArea = new JTextArea(20, 20);
            predictionTextArea.setEditable(false);
            predictionTextArea.setAlignmentX(Component.LEFT_ALIGNMENT);

            // Add a text change listener for a text box
            textField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    updateText();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    updateText();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    updateText();
                }

                // Methods for handling text changing
                private void updateText() {
                    String text = textField.getText();
                    System.out.println("current text: " + text);
                    String[] strings = analysisWords(text);
                    String preWord = getWordsSeq(strings);
                    if (preWord == null)
                        predictionTextArea.setText("no result");
//                        preWord="no result";
                    else
                        predictionTextArea.setText(text + preWord);
                    System.out.println("getWordsSeq:" + preWord);

                    // Update the forecast field display
//                    predictionTextArea.setText(""); // Cleared the previous prediction results
//                    predictionTextArea.setText(text+preWord);
//                    for (int i = 0; i < 20; i++) {
//                        predictionTextArea.append((i + 1) + ": " + text + "\n");
//                    }
                }
            });

            panel.add(inputLabel);
            panel.add(textField);
            panel.add(predictionLabel);
            panel.add(predictionTextArea);

            scrollPane.setViewportView(panel);
        });

        // Add buttons and text areas to JFrame
        frame.add(selectFileButton, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Set the size and shutdown of JFrame
        frame.setSize(1200, 900);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public JPanel showGrams(MyHashTable myHashTable) {
        final boolean[] sortByWord = {true};
        treeMap = myHashTable.insertIntoMap();
        TreeMap<String, Integer> treeMap1 = sortByWordCountsDes(treeMap);
        JTable table = makeTable(treeMap);//sorted by words alphabetically
        JTable table1 = makeTable(treeMap1);//sorted by the decreasing word counts

        // Create a panel containing the table and a button
        JPanel panel = new JPanel(new BorderLayout());
        JScrollPane pane = new JScrollPane(table);
        JScrollPane pane1 = new JScrollPane(table1);
        panel.add(pane, BorderLayout.CENTER);

        JButton button = new JButton("sorted by the decreasing word counts");
        button.addActionListener(e -> {
            sortByWord[0] = !sortByWord[0];
            if (sortByWord[0]) {
                button.setText("sorted by the decreasing word counts");
                panel.remove(pane1);
                panel.add(pane, BorderLayout.CENTER);
//                    panel.repaint();
            } else {
                button.setText("sorted by words alphabetically");
                panel.remove(pane);
                panel.add(pane1, BorderLayout.CENTER);
//                    panel.repaint();
            }
        });
        panel.add(button, BorderLayout.SOUTH);
        return panel;
    }

    //get the last two words of the text
    public static String[] analysisWords(String text) {
        String[] strings = text.split(" ");
//        System.out.println(strings.length);
        int start = Math.max(0, strings.length - 2); // Calculate the starting index
        return Arrays.copyOfRange(strings, start, strings.length);
    }

    private TreeMap<String, Integer> sortByWordCountsDes(TreeMap<String, Integer> treeMap) {
        Comparator<String> valueComparator = new Comparator<String>() {
            @Override
            public int compare(String key1, String key2) {
                Integer value1 = treeMap.get(key1);
                Integer value2 = treeMap.get(key2);

                int compareByValue = value2.compareTo(value1); // Sort by values in descending order
                if (compareByValue != 0) {
                    return compareByValue; // If values are not equal, return comparison result based on values
                } else {
                    return key1.compareTo(key2); // If values are equal, sort by keys in natural order
                }
            }
        };
        // Use the valueComparator to sort the key-value of the TreeMap
        TreeMap<String, Integer> sortedByValue = new TreeMap<>(valueComparator);
        sortedByValue.putAll(treeMap);
        return sortedByValue;
    }

    public static MyHashTable inputWords(String filePath, MyHashTable myHashTable) {
//        MyHashTable hashTable = new MyHashTable(10, new HashFun2());
        List<String> res = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
//                    System.out.println(line);
                res.addAll(preprocessLine(line));
                for (String s : preprocessLine(line)) {
                    myHashTable.addLinkedObject(s);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return myHashTable;
    }

    public static MyHashTable inputBigrams(String filePath,MyHashTable myHashTable){
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line, biTemp = null;
            while ((line = reader.readLine()) != null) {
                for (String s : preprocessLine(line)) {
                    myHashTable.addLinkedObject(s);
                }
                List<String> stringList = preprocessLine(line);
                if (!stringList.isEmpty() && biTemp != null) {//have word left in last line
                    myHashTable.addLinkedObject(biTemp + " " + stringList.get(0));
//                    bigramsNum++;
                    biTemp = null;
                }
                for (int i = 0; i < stringList.size() - 1; i++) {
                    myHashTable.addLinkedObject(stringList.get(i) + " " + stringList.get(i + 1));
//                    bigramsNum++;
                }
                //last word of this line
                if (!stringList.isEmpty())
                    biTemp = stringList.get(stringList.size() - 1);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return myHashTable;
    }

    public static List<String> preprocessLine(String line) {
        List<String> res = new ArrayList<>();
        for (String word : line.toLowerCase().trim().split(" ")) {
//            System.out.println("---------"+word);
            if (FormatCheck.isWord(word)) {
                res.add(word);
            } else if (FormatCheck.isAbbreviation(word)) {
                res.add(word);
            } else if (FormatCheck.isNumber(word)) {
                res.addAll(Arrays.asList(ToWordConverter.numberToWords(word).trim().split(" ")));
            } else if (FormatCheck.isMoney(word)) {
                res.addAll(Arrays.asList(ToWordConverter.moneyToWords(word).trim().split(" ")));
            } else if (FormatCheck.isLastWord(word)) {
                res.add(word.substring(0, word.length() - 1));
            } else if (FormatCheck.isSomethingElse(word)) {
                res.add(word);
            } else {
                errorWords.add(word);
//                System.out.println("error word:" + word);
            }
        }
        return res;
    }

    public JTable makeStatistics(MyHashTable myHashTable, int i) {
        TreeMap<String, Integer> treeMap = myHashTable.insertIntoMap();
        DefaultTableModel model = new DefaultTableModel();
        int nodesCount = myHashTable.getNodesCount();
        Map.Entry<String, Integer> maxValueEntry = findMaxValueEntry(treeMap);
        Map.Entry<String, Integer> minValueEntry = findMinValueKey(treeMap);
        float averageLength = (float) nodesCount / myHashTable.size;
        float stdDeviation = myHashTable.getStdDeviation();
        int maxNodesCount = myHashTable.getMaxNodesCount();
        int minNodesCount = myHashTable.getMinNodesCount();

        JTable jTable = new JTable(model);
        model.addColumn("");
        model.addColumn("");

        model.addRow(new Object[]{"size of hashtable", myHashTable.size});
        model.addRow(new Object[]{"total number of different words(number of MyLinkedObject)", nodesCount});
        model.addRow(new Object[]{"word appears the most", maxValueEntry.getKey() + " : " + maxValueEntry.getValue()});
        model.addRow(new Object[]{"word appears the least", minValueEntry.getKey() + " : " + minValueEntry.getValue()});
        if (i == 1)
            model.addRow(new Object[]{"Total number of unigram(not include error words)", totalWords});
        else if (i == 2) {
            model.addRow(new Object[]{"Total number of bigrams(not include error words)", bigramsNum});
        } else {
            model.addRow(new Object[]{"Total number of trigrams(not include error words)", trigramsNum});
        }
        model.addRow(new Object[]{"max nodes count in one list", maxNodesCount});
        model.addRow(new Object[]{"min nodes count in one list", minNodesCount});
        model.addRow(new Object[]{"average length of linked list", averageLength});
        model.addRow(new Object[]{"standard deviation of linked list length", stdDeviation});
        if (i == 1)
            model.addRow(new Object[]{"error words", errorWords});

        // Get the column renderer
        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                           boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int lastTow = table.getModel().getRowCount() - 1;
                if (row == lastTow) {// set the text in last row in red
                    c.setForeground(Color.RED);
                }
                return c;
            }
        };

        // Set the renderer to a specific column
        jTable.setDefaultRenderer(Object.class, renderer);
//        jTable.getColumnModel().getColumn().setCellRenderer(renderer);

        Dimension size = jTable.getSize();
//        System.out.println(size.width + "*" + size.height);

//        System.out.println(model.getRowCount()+"---"+model.getColumnCount());
        return jTable;
    }

    public JTable makeTable(TreeMap<String, Integer> treeMap) {
        // creat table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Word"); // The first column shows the word
        model.addColumn("Count"); // The first column shows the count

        // add data to table
//        System.out.println(treeMap);
        for (Map.Entry<String, Integer> entry : treeMap.entrySet()) {
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});
        }
//            model.addRow(new Object[]{"Hello", 5});
//            model.addRow(new Object[]{"World", 3});
        // Create JTable and use the model
        return new JTable(model);
    }

    public static <K, V extends Comparable<V>> Map.Entry<K, V> findMaxValueEntry(TreeMap<K, V> map) {
        if (map.isEmpty()) {
            return null;
        }
        return Collections.max(map.entrySet(), Comparator.comparing(Map.Entry::getValue));
    }

    public static <K, V extends Comparable<V>> Map.Entry<K, V> findMinValueKey(TreeMap<K, V> map) {
        if (map.isEmpty()) {
            return null;
        }
        return Collections.min(map.entrySet(), Comparator.comparing(Map.Entry::getValue));
    }
}