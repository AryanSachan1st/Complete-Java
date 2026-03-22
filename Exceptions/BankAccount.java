package Exceptions;

public class BankAccount {
    private double balance;

    public BankAccount(double balance) {
        this.balance = balance;
    }

    public double getBalance() {
        return this.balance;
    }

    public boolean withdraw(double amount) throws CustomException {
        if (amount > this.balance) {
            throw new CustomException("Insufficient Balance");
        }
        balance -= amount;
        return true;
    }
}
