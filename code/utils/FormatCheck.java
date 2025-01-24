package utils;

import java.util.Arrays;
import java.util.List;

public class FormatCheck {
    public final static String[] fullStopAbbreviation={"mr.","mrs.","ms.","dr.","prof."};
    public final static String[] apostropheAbbreviation={
            "isn't", "aren't",
            "don't","doesn't","didn't",
             "won't", "can't", "haven't", "wouldn't","shouldn't",
            "she's", "he's","it's",
            "you're","i'm", "i've", "they're","we're",
            "o'clock","'em"
            };

    public static boolean isWord(String str) {
        return str.matches("[a-z]+");
    }
    public static boolean isNumber(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");
    }

    public static boolean isMoney(String str){


        return str.matches("^[￥$€?]?\\d{1,3}(,\\d{3})*(\\.\\d+)?[KkMmBbTt]?$");
    }

    public static boolean isAbbreviation(String word){
//        System.out.println(word);
        List<String> list1 = Arrays.asList(apostropheAbbreviation);
        List<String> list2 = Arrays.asList(fullStopAbbreviation);
        return list1.contains(word)||list2.contains(word)||isPossessiveCase(word)||isFutureTense(word)||isPresentTense(word)||isNOT(word)||isNameAbbrev(word);
    }

    public static boolean isNameAbbrev(String word){//o'neill, o'leary
        return word.contains("'");
    }
    public static boolean isPossessiveCase(String word){
        String prefix = word.substring(word.length() - 2);
        return prefix.equals("'s")||prefix.equals("s'");
    }
    public static boolean isFutureTense(String word){
        if (word.length()<3)
            return false;
        String prefix = word.substring(word.length() - 3);
        return prefix.equals("'ll");
    }
    public static boolean isPresentTense(String word){
        if (word.length()<3)
            return false;
        String prefix1 = word.substring(word.length() - 3);
        String prefix2 = word.substring(word.length() - 2);
        return prefix1.equals("'ve")||prefix2.equals("'d");
    }

    public static boolean isNOT(String word){
        if (word.length()<3)
            return false;
        String prefix = word.substring(word.length() - 3);
        return prefix.equals("n't");
    }

    public static void main(String[] args) {
//        String num1 = "1900";
//        String num2 = "-123.45";
//        String num3 = "abc";
//        System.out.println(isNumber(num1)); // 输出 true
//        System.out.println(isNumber(num2)); // 输出 true
//        System.out.println(isNumber(num3)); // 输出 false

//        String money1 = "￥2.5M";
//        String money2 = "$10k";
//        String money3 = "€100";
//        String notMoney = "12345";
//
//        System.out.println(isMoney(money1)); // 输出 true
//        System.out.println(isMoney(money2)); // 输出 true
//        System.out.println(isMoney(money3)); // 输出 true
//        System.out.println(isMoney(notMoney)); // 输出 false

        System.out.println(isFutureTense("he'll"));
    }

    //the last word in a sentence with a full stop
    //except "c. e. o.s"
    public static boolean isLastWord(String word) {
        if (word.length()>2&&word.charAt(word.length() - 1) == '.'){
            return true;
        }
        return false;
    }

    public static boolean isSomethingElse(String word) {//a. a.b ...those strange abbreviation
        return word.matches("^[a-zA-Z]\\.[a-zA-Z]?$");
    }
}

