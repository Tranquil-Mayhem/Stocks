//Jamie Gashler
//JAVA Programming I
//HW 4 - Trades

import java.util.Scanner;
import java.util.ArrayList;

public class Stocks {
    ArrayList<String> stockName;
    ArrayList<ArrayList<Integer>> quantity;
    ArrayList<ArrayList<Double>> price;
    public static final int NOTININDEX = -1;
    public static final double TAX = 0.15;

    /**Prompts the user to input a string, and if invalid, display an error message, until a correct string is input
     *
     * @param prompt the prompt that the user recieves when required to input a string
     * @return the next line after the user input
     */
    public static String getString(String prompt) {
        System.out.printf(prompt);
        Scanner scanner = new Scanner(System.in);

        while (!scanner.hasNextLine()) {
            scanner.nextLine();
            System.out.println("Invalid, try again");
            System.out.print(prompt);
        }
        return scanner.nextLine();
    }


    /**Displays the Main Menu options to the user, and gets the user response.
     *
     * @return the user's response to the menu options
     */
    public static String getMenuResponse() {
        return getString("Buy (B or b) %nSale (S or s) %nPrint (P or p) %nExit (E or e)%n");
    }


    /**Prompts the user to input a 3-character long string, and if invalid, display an error message, until a correct
     * 3-letter string is input
     *
     * @param prompt the prompt that the user recieves when required to input a 3-letter string
     * @return the 3-letter symbol that the user input
     */
    public static String getShortString(String prompt){
        System.out.printf(prompt);
        Scanner scanner = new Scanner(System.in);

        String userInputSymbol = scanner.nextLine();
        int symbolLength = userInputSymbol.length();

        while (symbolLength != 3) {
            System.out.println("Invalid, try again");
            System.out.print(prompt);
            userInputSymbol = scanner.nextLine();
            symbolLength = userInputSymbol.length();

        }
        return userInputSymbol;
    }


    /**Prompts the user to input a double value, and if invalid, display an error message, until a correct double is input
     *
     * @param prompt the prompt that the user recieves when required to input a double value
     * @return the double value that the user input
     */
    public static double getDouble(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);

        while(!scanner.hasNextDouble()) {
            scanner.nextLine();
            System.out.println("Invalid, try again");
            System.out.print(prompt);
        }

        return scanner.nextDouble();
    }


    /**Prompts the user to input an integer value, and if invalid, display an error message, until a correct double is
     * input
     *
     * @param prompt the prompt that the user recieves when required to input an integer value
     * @return the integer value that the user input
     */
    public static int getInt(String prompt) {
        System.out.print(prompt);
        Scanner scanner = new Scanner(System.in);

        while(!scanner.hasNextInt()) {
            scanner.nextLine();
            System.out.println("Invalid, try again");
            System.out.print(prompt);
        }

        return scanner.nextInt();
    }


    /** Determines if the 3-letter symbol exists within the stocks, and if so, signify that there are duplicates,
     * otherwise, return notInIndex
     *
     * @param threeLetterSymbol the 3-letter symbol that the user input, representing the stock name
     * @return NOTININDEX an integer that signifies that this stock name has not been specified before and is not
     * currently in the index of stock names.
     */
    public int findStockName(String threeLetterSymbol) {
        for(int i = 0; i < stockName.size(); i++) {
            if (stockName.get(i).equals(threeLetterSymbol)) {
                return i;
            }

        }
        return NOTININDEX;
    }


    /**Allows the user to buy stocks. The user is prompted to input the stock name of three character length, the
     * quantity of the stock that they wish to purchase, and the stock price of their stock. Adds each of the user
     * inputs to the index of the specified arraylist. Passes no parameters.
     */
    public void buy() {
         String threeLetterSymbol = getShortString("Please input a three-letter symbol > ");

         int index = findStockName(threeLetterSymbol); // returns -1 if not found
         if (index == NOTININDEX) {

             index = stockName.size();
             stockName.add(threeLetterSymbol);
             quantity.add(new ArrayList<Integer>());
             price.add(new ArrayList<Double>());
         }

         int q = getInt("Please input the stock quantity > ");
         quantity.get(index).add(q);

         double p = getDouble("Please input the stock price > ");
         price.get(index).add(p);
    }


    /**Determines the total amount of stock specified for one stock name. Even if the user has multiple shares with
     * different share prices of this stock, this method combines them to determine how much stock from that company
     * the user has total
     *
     * @param stockAmount the amount of stock per specified share
     * @return the total quantity of stock the user owns for their specified stock name
     */
     public int howmuchdoihave(int stockAmount) {
        ArrayList<Integer> specifiedShares = quantity.get(stockAmount);
        int sum = 0;
        for(int i = 0; i < specifiedShares.size(); i++)
            sum += specifiedShares.get(i).intValue();
        return sum;
    }


    /**Allows the user to sell stocks. The user is prompted to input the stock name of three character length, the
     * quantity of the stock that they wish to sell, and the current price of their stock. Determines if the input is
     * valid, and passes the user input to processGains so that the user can determine their profits.
     */
    public void sell() {
        Scanner scanner = new Scanner(System.in);
        String threeLetterSymbol = getShortString("Please input the three-letter symbol of the stock > ");

        int index = findStockName(threeLetterSymbol);
        if (index == NOTININDEX) {
            System.out.println("Invalid input, please try again.");
            sell();
            return;
        }

        int stockQuantity = getInt("Please input the stock quantity you would like to sell > ");
        int howmuchihave = howmuchdoihave(index);
        if (stockQuantity > howmuchihave) {
            System.out.println("The quantity you requested of that stock is more than you own!");
            sell();
            return;
         }

        double currentPrice = getDouble("Please input the stock's current price > ");

         processGains(index, stockQuantity, currentPrice, threeLetterSymbol);
    }


    /**Calculates the shares being sold, in order of when purchased, the total sale amount, tax, take home and number
     * of shares remaining for the company / stock name that the user specified.
     *
     * @param specifiedIndex which specific company/ stock name that is being referred to as an integer
     * @param quantityToSell the amount of stocks that are being sold by the user
     * @param sellPrice the price that the user is selling the stocks for
     * @param symbol the name of the stock / company as a string
     */
    public void processGains(int specifiedIndex, int quantityToSell, double sellPrice, String symbol) {
        double totalGains = 0.0;
        ArrayList<Integer> specifiedShares = quantity.get(specifiedIndex);
        ArrayList<Double> purchasePrices = price.get(specifiedIndex);

        System.out.printf("Selling stock (%s) %n", symbol);
        for(int i = 0; quantityToSell > 0 && i < specifiedShares.size(); i++) {

            int amountImSellingNow = Math.min(quantityToSell, specifiedShares.get(i));
            double gain = ((sellPrice - purchasePrices.get(i)) * amountImSellingNow);
            totalGains += gain;
            System.out.printf("Selling %d shares at     $%.2f ($%.2f profit) :          $%.2f %n", amountImSellingNow, sellPrice,(sellPrice - purchasePrices.get(i)), gain);
            specifiedShares.set(i, specifiedShares.get(i) - amountImSellingNow);
            quantityToSell -= amountImSellingNow;
        }
        System.out.printf("Total sale amount                                         $%.2f %n", totalGains);
        System.out.printf("Tax(15%%)                                                  $%.2f %n", (totalGains * TAX));
        System.out.printf("Take Home                                                 $%.2f %n", (totalGains-(totalGains * TAX)));
        System.out.printf("%d share of %s remaining.                                              ", howmuchdoihave(specifiedIndex), symbol);

    }

    /**Determines the different stocks that the user has purchased. Displays the stock name, the quantity of stocks
     * owned and prices that the stock was bought for, in each transaction. Passes no parameters.
     */
    public void print() {
        for(int j = 0; j < stockName.size(); j++) {
            System.out.printf("Stock Name: %s %n", stockName.get(j));

            ArrayList<Integer> quantityOfThisStock = quantity.get(j);
            for (int i = 0; i < quantityOfThisStock.size(); i++) {

                System.out.printf("Stock Quantity: %d %n", quantityOfThisStock.get(i).intValue());
            }
            ArrayList<Double> pricesOfThisStock = price.get(j);

            for (int x = 0; x < pricesOfThisStock.size(); x++) {

                System.out.printf("Stock Price: $%.2f %n %n", pricesOfThisStock.get(x).doubleValue());
            }
        }

    }

    /**This is the mainMenu method. It passes no parameters. It prompts the user to input a character that corresponds
     * with the question in the prompt, and if their input is invalid, repeat the prompt.
     */
    public void mainMenu() {
        stockName = new ArrayList<String>();
        quantity = new ArrayList<ArrayList<Integer>>();
        price = new ArrayList<ArrayList<Double>>();

        while(true) {
            System.out.printf("%n%n");
            String response = getMenuResponse();
            if (response.equalsIgnoreCase("b")) {
                buy();

            } else if(response.equalsIgnoreCase("s")) {
                sell();

            } else if(response.equalsIgnoreCase("p")) {
                print();

            } else if(response.equalsIgnoreCase("e")) {
                break;

            } else {
                System.out.printf("%n%n");
                System.out.println("Invalid Input, try again.");
            }

        }
    }

    /**This is the main method of the program. It calls the mainMenu method to begin the program.
     *
     * @param args contain the supplied command-line arguments as an array of String objects
     */
    public static void main(String[] args) {
        Stocks stocks = new Stocks();
        stocks.mainMenu();
    }
}