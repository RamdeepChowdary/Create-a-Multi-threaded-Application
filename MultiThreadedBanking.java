package createmultithreadedapplication;

	import java.util.concurrent.locks.Lock;
	import java.util.concurrent.locks.ReentrantLock;

	// Shared resource: Bank Account
	class BankAccount {
	    private double balance;
	    private final Lock lock = new ReentrantLock();

	    public BankAccount(double initialBalance) {
	        this.balance = initialBalance;
	    }

	    public void deposit(double amount) {
	        lock.lock();
	        try {
	            balance += amount;
	            System.out.println("Deposited: " + amount + ", Balance: " + balance);
	        } finally {
	            lock.unlock();
	        }
	    }

	    public void withdraw(double amount) {
	        lock.lock();
	        try {
	            if (balance >= amount) {
	                balance -= amount;
	                System.out.println("Withdrawn: " + amount + ", Balance: " + balance);
	            } else {
	                System.out.println("Insufficient balance");
	            }
	        } finally {
	            lock.unlock();
	        }
	    }

	    public double getBalance() {
	        return balance;
	    }
	}

	// Thread to deposit money
	class DepositThread extends Thread {
	    private BankAccount account;
	    private double amount;

	    public DepositThread(BankAccount account, double amount) {
	        this.account = account;
	        this.amount = amount;
	    }

	    public void run() {
	        for (int i = 0; i < 5; i++) {
	            account.deposit(amount);
	            try {
	                Thread.sleep(100);
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	            }
	        }
	    }
	}

	// Thread to withdraw money
	class WithdrawThread extends Thread {
	    private BankAccount account;
	    private double amount;

	    public WithdrawThread(BankAccount account, double amount) {
	        this.account = account;
	        this.amount = amount;
	    }

	    public void run() {
	        for (int i = 0; i < 5; i++) {
	            account.withdraw(amount);
	            try {
	                Thread.sleep(100);
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	            }
	        }
	    }
	}

	public class MultiThreadedBanking {
	    public static void main(String[] args) {
	        BankAccount account = new BankAccount(1000.0);

	        DepositThread depositThread = new DepositThread(account, 500.0);
	        WithdrawThread withdrawThread = new WithdrawThread(account, 200.0);

	        depositThread.start();
	        withdrawThread.start();

	        try {
	            depositThread.join();
	            withdrawThread.join();
	        } catch (InterruptedException e) {
	            Thread.currentThread().interrupt();
	        }

	        System.out.println("Final balance: " + account.getBalance());
	    }
	}
