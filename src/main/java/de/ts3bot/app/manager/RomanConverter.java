package de.ts3bot.app.manager;

public class RomanConverter {
    private static final int[] NUMBER_VALUES = { 1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1 }; // array containing all of the values
    private static final String[] NUMBER_LETTERS = { "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I" }; // array containing all of the numerals

    private RomanConverter() { }

    /**
     * Method used to convert a string to a integer
     * @param roman roman numeral to be converted
     * @return integer
     */
    public static int toNumerical(String roman) {
        for (int x = 0; x < NUMBER_LETTERS.length; x++) { // Loop through all the letters
            if(roman.startsWith(NUMBER_LETTERS[x])) // Check if the string starts with that letter
                return NUMBER_VALUES[x] + toNumerical(roman.replaceFirst(NUMBER_LETTERS[x], "")); // Rinse and repeats until the string is empty and return it
        }
        return 0; // If something went wrong, simply return 0
    }

    /**
     * Method used to convert a integer to a roman numeral
     * @param num number to be converted
     * @return roman numeral
     */
    public static String toRoman(int num) {
        StringBuilder roman = new StringBuilder();
        for (int x = 0; x < NUMBER_VALUES.length; x++) { // loop through all the values
            while (num >= NUMBER_VALUES[x]) { // Check if the number is greater than the current value
                roman.append(NUMBER_LETTERS[x]); // Add the letter to the String
                num -= NUMBER_VALUES[x]; // Subtract the amount from the value
            }
        }
        return roman.toString(); // Return the String
    }

    /**
     * Method used to check if a string is an integer
     * @param s string to be parsed
     * @return boolean
     */

    public static boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        }catch(NumberFormatException e) {
            return false;
        }
        return true;
    }
}
