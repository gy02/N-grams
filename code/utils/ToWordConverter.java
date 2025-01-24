package utils;

import java.util.Objects;

public class ToWordConverter {
    private static final String[] ones = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"};
    private static final String[] teens = {"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
    private static final String[] tens = {"", "", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};

    public static String numberToWords(String num) {
        float number = Float.parseFloat(num);
        if (number == 0) {
            return "zero";
        } else if (number < 0) {
            return "minus " + floatToWords(-number);
        }
        return floatToWords(number);
    }

    private static boolean isNumPlural(float num) {
        num = Math.abs(num);
        if (num > 1)
            return true;
        return false;
    }

    public static String moneyToWords(String money) {//$2
        money=money.replace(",","");//$15,000
        String currency;
        boolean isPlural;
        char prefix = money.charAt(0);//$
        char suffix = money.charAt(money.length() - 1);//2

        if (Character.isDigit(suffix)) {//$2.5
            money = money.substring(1);
            isPlural = isNumPlural(Float.parseFloat(money));
            currency = convertCurrency(prefix, isPlural);
            return numberToWords(money).trim() + " " + currency;
        } else {//$2.5M
            String s = convertMoneyAbbreviation(suffix);
            money = money.substring(1, money.length() - 1);
            isPlural = true;
            currency = convertCurrency(prefix, isPlural);
            return numberToWords(money) + " " + s + " " + currency;
        }
    }

    private static String convertMoneyAbbreviation(char c) {
        switch (Character.toUpperCase(c)) {
            case 'K':
                return "thousand";
            case 'M':
                return "million";
            case 'B':
                return "billion";
            case 'T':
                return "trillion";
        }
        return null;
    }

    private static String convertCurrency(char c, boolean isPlural) {
        if (isPlural) {
            switch (c) {
                case 'гд':
                    return "yuan";
                case '$':
                    return "dollars";
                case 'ву':
                    return "euros";
                case '?':
                    return "pounds";
            }
        } else {
            switch (c) {
                case 'гд':
                    return "yuan";
                case '$':
                    return "dollar";
                case 'ву':
                    return "euro";
                case '?':
                    return "pound";
            }
        }
        return null;
    }

    private static String convertToWords(int num) {
        if (num < 10) {
            return ones[num];
        } else if (num < 20) {
            return teens[num - 10];
        } else if (num < 100) {
            return tens[num / 10] + " " + convertToWords(num % 10);
        } else if (num < 1000) {
            return ones[num / 100] + " hundred " + convertToWords(num % 100);
        } else if (num < 1000000) {
            return convertToWords(num / 1000) + " thousand " + convertToWords(num % 1000);
        } else if (num < 1000000000) {
            return convertToWords(num / 1000000) + " million " + convertToWords(num % 1000000);
        } else {
            return convertToWords(num / 1000000000) + " billion " + convertToWords(num % 1000000000);
        }
    }

    public static String floatToWords(float num) {
        String number = String.valueOf(num);
        String[] strings = number.split("\\.");
        if (Objects.equals(strings[1], "0")) {//is int
            return convertToWords((int) num);
        } else {//is float
//            return convertToWords(Integer.parseInt(strings[0])) + " point " + convertToWords(Integer.parseInt(strings[1]));
            return numberToWords(strings[0]) + " point " + numberToWords(strings[1]);
        }
    }


    public static void main(String[] args) {
//        System.out.println(convertToWords(15001));
        System.out.println(moneyToWords("$15,000"));
//        System.out.println(convertMoneyAbbreviation('k'));
//        floatToWords(2f);
    }
}

