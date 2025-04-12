import java.util.Scanner;

class BankAccount
{
    private int accNo;
    private String name;
    private double balance;

    public BankAccount(int accNo, String name, double balance)
    {
        this.accNo = accNo;
        this.name = name;
        this.balance = balance;
    }

    public int getAccNo()
    {
        return accNo; 
    }

    public String getName()
    {
        return name;
    }

    public double getBalance()
    {
        return balance;
    }

    public void deposit(double amount)
    {
        if(amount > 0)
        {
            balance += amount;
        }
        else
        {
            System.out.println("Invalid Deposit!");
        }
    }

    public void withdraw(double amount)
    {
        if(amount > 0 && amount <= balance)
        {
            balance -= amount;
        }
        else
        {
            System.out.println("Insufficient balance or Invalid amount!");
        }
    }

    public void showDetails()
    {
        System.out.println("Name: " + name + ", Acc No: " + accNo + ", Balance: " + balance);
    }
}

public class Bank {

    public static void main(String[] args) 
    {
        Scanner sc = new Scanner(System.in);
        BankAccount accounts[] = new BankAccount[100];
        int count = 0;

        while(true)
        {
            System.out.println("\n1.Create 2.Deposit 3.Withdraw 4.Details 5.Exit");
            int ch = sc.nextInt();
        

            if(ch == 1)
            {
                System.out.println("Acc No: ");
                int no = sc.nextInt(); sc.nextLine();
                System.out.println("Name: ");
                String name = sc.nextLine();
                System.out.println("Initial Balance: ");
                double bal = sc.nextDouble();

                accounts[count++] = new BankAccount(no, name, bal);
                System.out.println("Account created!");
            }
            else if(ch == 5){
                System.out.println("Thanks Existing!");
                break;
            }

            else
            {
                System.out.println("Enter Acc No: ");
                int no = sc.nextInt();
                boolean found = false;

                for(int i=0;i<count;i++)
                {
                    if(accounts[i].getAccNo() == no)
                    {
                        if(ch == 2)
                        {
                        System.out.println("Amount to deposit: ");
                        accounts[i].deposit(sc.nextDouble());
                        }
                        else if(ch == 3)
                        {
                        System.out.println("Amount to withdraw: ");
                        accounts[i].withdraw(sc.nextDouble());
                        }
                        else if(ch == 4)
                        {
                        accounts[i].showDetails();
                        }
                        found = true;
                        break;
                    }
                }
                if (!found) System.out.println("Account not found!");
            }
        }
        sc.close();
    }
}