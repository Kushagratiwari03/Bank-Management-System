import java.util.*;


// Enum for Account types
enum AccountType{
    SAVINGS, CURRENT;
}

// Interface for banking operations
interface BankOperations{
    void deposit(double amount);
    void withdraw(double amount);
    void showDetails();
}

// Abstract User class
abstract class User{
    protected String username;
    protected String password;

    public User(String username, String password)
    {
        this.username = username;
        this.password = password;
    }
    public String getUsername()
    {
        return username;
    }
    public boolean login(String password)
    {
        return this.password.equals(password);
    }

    abstract void showMenu();
}

// Transaction class
class Transaction
{
    String type;
    double amount;
    Date date;

    public Transaction(String type, double amount)
    {
        this.type = type;
        this.amount = amount;
        this.date = new Date();
    }

    public String getType(){
        return type;
    }

    public double getAmount(){
        return amount;
    }

    public Date getDate(){
        return date;
    }

    public String toString() {
        return type + ": ₹" + amount + " on " + date;
    }
}

// Bank Account class
class BankAccount implements BankOperations
{
    private static final double MIN_BALANCE = 500;
    private static int accCounter = 1000;

    private int accNo;
    private double balance;
    private AccountType type;
    private List<Transaction> transactions;


    public BankAccount(AccountType type, double balance)
    {
        this.accNo = accCounter++;
        this.type = type;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    public int getAccNo(){
        return accNo;
    }
    public double getBalance(){
        return balance;
    }
    public AccountType getType(){
        return type;
    }
    public List<Transaction> geTransactions(){
        return transactions;
    }

    public void deposit(double amount)
    {
        if(amount > 0){
            balance += amount;
            transactions.add(new Transaction("Deposit", amount));
        }
    }

    public void withdraw(double amount)
    {
        if(amount > 0 && balance - amount > MIN_BALANCE){
            balance -= amount;
            transactions.add(new Transaction("Withdraw", amount));
        }
        else{
            System.out.println("Insufficient balance or minimum balance policy violated!");
        }
    }

    public void showDetails()
    {
        System.out.println("Acc No: " + accNo + " | Type: " + type + " | Balance: ₹" + balance);
        System.out.println("Transactions: ");
        transactions.forEach(t -> System.out.println(" - " + t));
    }

    public void showFilteredTransactions(String typeFilter)
    {
        transactions.stream()
            .filter(t -> t.getType().equalsIgnoreCase(typeFilter))
            .forEach(t -> System.out.println(" - " + t));
    }

    public void showSortedTransactionsByAmount()
    {
        transactions.stream()
            .sorted((t1, t2) -> Double.compare(t2.getAmount(), t1.getAmount()))
            .forEach(t -> System.out.println(" - "   + t));
    }

    public void showSortedTransactionsByDate()
    {
        transactions.stream()
            .sorted((t1, t2) -> t2.getDate().compareTo(t1.getDate()))
            .forEach(t -> System.out.println(" - " + t));
    }
}

// Customer class
class Customer extends User
{
    private BankAccount account;

    public Customer(String username, String password, AccountType type, double balance){
        super(username, password);
        this.account = new BankAccount(type, balance);
    }

    public BankAccount getAccount(){
        return account;
    }
 
    void showMenu() {
        Scanner sc = new Scanner(System.in);
        while (true)
        {
            System.out.println("1.Deposit 2.Withdraw 3.View Account 4.Filter Transactions 5.Sort by Amount 6.Sort by Date 7.Logout");
            int choice = sc.nextInt();
            if(choice == 1){
                System.out.println("Amount");
                account.deposit(sc.nextDouble());
            }
            else if(choice == 2){
                System.out.println("Amount");
                account.withdraw(sc.nextDouble());
            }
            else if(choice == 3){
                account.showDetails();
            }
            else if(choice == 4){
                System.out.println("Enter type to filter (Deposit/Withdraw): ");
                String type = sc.next();
                account.showFilteredTransactions(type);
            }
            else if(choice == 5){
                System.out.println("Transactions sorted by amount (high to low): ");
                account.showSortedTransactionsByAmount();
            }
            else if(choice == 6){
                System.out.println("Transactions sorted by date (latest first): ");
            }
            else break;
        }
        
    }
}

// Admin class
class Admin extends User {
    public Admin(String username, String password) {
        super(username, password);
    }

    void showMenu() {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- Admin Menu ---");
            System.out.println("1. View All Users");
            System.out.println("2. Total Bank Balance");
            System.out.println("3. View User Details");
            System.out.println("4. Logout");
            int ch = sc.nextInt(); sc.nextLine();

            if (ch == 1) {
                System.out.println("All Users:");
                BankManagementSystem.users.forEach((k, v) -> {
                    if (v instanceof Customer) {
                        Customer c = (Customer) v;
                        System.out.println(" - " + c.getUsername() + " (" + c.getAccount().getType() + ")");
                    }
                });
            } 
            else if (ch == 2) {
                double total = BankManagementSystem.users.values().stream()
                    .filter(u -> u instanceof Customer)
                    .mapToDouble(u -> ((Customer) u).getAccount().getBalance())
                    .sum();
                System.out.println("Total Bank Balance: ₹" + total);
            } 
            else if (ch == 3) {
                System.out.print("Enter username to view: ");
                String uname = sc.nextLine();
                User u = BankManagementSystem.users.get(uname);
                if (u instanceof Customer) {
                    ((Customer) u).getAccount().showDetails();
                } else {
                    System.out.println("Customer not found!");
                }
            } 
            else break;
        }
    }
}

// Main driver class
public class BankManagementSystem{
    public static Map<String, User> users = new HashMap<>();

    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);

        users.put("admin", new Admin("admin", "admin123"));

        while (true)
        {
            System.out.println("\n1.Register 2.Login 3.Exit");
            int choice = sc.nextInt(); sc.nextLine();

            if(choice == 1){
                System.out.println("Username: ");
                String username = sc.nextLine();
                System.out.println("Password");
                String password = sc.nextLine();
                System.out.println("Account Type (1.Savings 2.Current): ");
                int type = sc.nextInt();
                System.out.println("Initial Balance");
                double deposit = sc.nextDouble();
                users.put(username, new Customer(username, password, type == 1 ? AccountType.SAVINGS : AccountType.CURRENT, deposit));
                System.out.println("User registered successfully!");
            }

            else if (choice == 2) {
                System.out.print("Username: ");
                String username = sc.nextLine();
                System.out.print("Password: ");
                String password = sc.nextLine();

                User user = users.get(username);
                if (user != null && user.login(password)) {
                    user.showMenu();
                } else {
                    System.out.println("Invalid credentials!");
                }
            } else break;
        
        }
        sc.close();
    }
}