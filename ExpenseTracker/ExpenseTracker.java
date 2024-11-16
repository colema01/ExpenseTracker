import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class ExpenseTracker {
    private static ArrayList<Expense> expenses = new ArrayList<>(); // List to store all expenses
    private static ArrayList<Double> incomes = new ArrayList<>(); // List to store income entries
    private static final String FILE_NAME = "expenses.txt"; // File to save/load data

    public static void main(String[] args) {
        loadExpenses(); // Load previous data from file
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            // Display the menu
            System.out.println("\nChoose an option: ");
            System.out.println("1. Add Expense");
            System.out.println("2. Add Income");
            System.out.println("3. View Expenses");
            System.out.println("4. View Incomes");
            System.out.println("5. Calculate Total Expenses");
            System.out.println("6. Calculate Total Income");
            System.out.println("7. Calculate Remaining Balance (Income - Expenses)");
            System.out.println("8. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear the newline

            // Handle menu choices
            switch (choice) {
                case 1 -> addExpense(scanner);
                case 2 -> addIncome(scanner);
                case 3 -> viewExpenses();
                case 4 -> viewIncomes();
                case 5 -> calculateTotalExpenses();
                case 6 -> calculateTotalIncome();
                case 7 -> calculateRemainingBalance();
                case 8 -> {
                    saveExpenses(); // Save data before exiting
                    running = false;
                }
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
        scanner.close();
        System.out.println("Thank you for using the Expense Tracker!");
    }

    private static void addExpense(Scanner scanner) {
        System.out.print("Enter description: ");
        String description = scanner.nextLine();

        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Clear the newline

        expenses.add(new Expense(description, amount, "Expense"));
        System.out.println("Expense added successfully!\n");
    }

    private static void addIncome(Scanner scanner) {
        System.out.print("Enter your income amount: ");
        double income = scanner.nextDouble();
        scanner.nextLine(); // Clear the newline

        incomes.add(income);

        // Automatically calculate and add tithing
        double tithing = income * 0.10; // 10% of the income
        expenses.add(new Expense("Tithing", tithing, "Charity"));
        System.out.printf("Income of $%.2f added successfully! Tithing of $%.2f has been automatically added as an expense.\n\n", income, tithing);
    }

    private static void viewExpenses() {
        System.out.println("\nExpenses:");
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded yet.");
        } else {
            for (Expense expense : expenses) {
                System.out.println(expense);
            }
        }
        System.out.println();
    }

    private static void viewIncomes() {
        System.out.println("\nIncomes:");
        if (incomes.isEmpty()) {
            System.out.println("No incomes recorded yet.");
        } else {
            for (double income : incomes) {
                System.out.printf("$%.2f\n", income);
            }
        }
        System.out.println();
    }

    private static void calculateTotalExpenses() {
        double total = 0;
        for (Expense expense : expenses) {
            total += expense.getAmount();
        }
        System.out.printf("Total Expenses: $%.2f\n\n", total);
    }

    private static void calculateTotalIncome() {
        double total = 0;
        for (double income : incomes) {
            total += income;
        }
        System.out.printf("Total Income: $%.2f\n\n", total);
    }

    private static void calculateRemainingBalance() {
        double totalIncome = incomes.stream().mapToDouble(Double::doubleValue).sum();
        double totalExpenses = expenses.stream().mapToDouble(Expense::getAmount).sum();
        double balance = totalIncome - totalExpenses;

        System.out.printf("Remaining Balance (Income - Expenses): $%.2f\n\n", balance);
    }

    private static void loadExpenses() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts[0].equals("Expense")) {
                    String description = parts[1];
                    double amount = Double.parseDouble(parts[2]);
                    expenses.add(new Expense(description, amount, "Expense"));
                } else if (parts[0].equals("Income")) {
                    double income = Double.parseDouble(parts[1]);
                    incomes.add(income);
                }
            }
            System.out.println("Previous data loaded successfully.\n");
        } catch (FileNotFoundException e) {
            System.out.println("No previous data found. Starting fresh.\n");
        } catch (IOException e) {
            System.out.println("Error reading the data file.");
        }
    }

    private static void saveExpenses() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Expense expense : expenses) {
                writer.printf("Expense,%s,%.2f\n", expense.getDescription(), expense.getAmount());
            }
            for (double income : incomes) {
                writer.printf("Income,%.2f\n", income);
            }
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving data to file.");
        }
    }
}

// The Expense class represents an individual expense
class Expense {
    private String description;
    private double amount;
    private String category;

    public Expense(String description, double amount, String category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return description + " - $" + amount + " [" + category + "]";
    }
}
