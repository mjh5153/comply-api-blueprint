package com.init_spring_bean_mvn.demo.account;

public class ScanAccount {
    private final Object lockName = new Object();

    private final Object lockBalance = new Object();
    public String getName() {
        return name;
    }

    public void setName(String name) {

        synchronized (lockName) {

            this.name = name;
            System.out.println("Updated name: " + name); // confirm pudated name properly set
        }
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    private String name;
    public ScanAccount(String name, double balance) {
        this.balance = balance;
    }



    public double getBalance() {
        return balance;
    }

    // private volatile double balance; // will not fix mem inconsistency
    private volatile double balance; // synchronized fixes mem inconsistency and thread interference - add to deposit and withdraw methods

    // diff invocations of synch methods on same object - no interleave
    // one thread is executing a synch method for object,
    // all threads incoke synch methds for same object, block and suspen their execution,
    //  until first thread is done with object
    // when a synchronized method exits it ensures that the state of object is visisble to all threads
    // similar to volitile keyword
    // Circuventing threading and multithreading ? Yes for updating balance code
    // example three synched methods: can only run one at time
    // limit code that has access to shared object

    // Critical Section - critical section is code referencing a shared resource
    // Only one thread at time should be able to execute critical section
    // When all critical sections are synchronized = the class is thread safe

    public void deposit(double amount) {
        try {
            System.out.println("Deposit = Taling to teller at bank");
            Thread.sleep(7000);
            // most times you don't want to synch on current instance
        } catch (InterruptedException e) {
            throw new RuntimeException();
        }
        Double boxedBalance = this.balance; // could create lock objects where sole purpose to locking on different fields
        synchronized (lockBalance) { // Warning: Synchronization on instance of value-based class  // pass object instance - required: this - current instance of ScanAccount
            // intrinsict lock onLY AVAILABLE ON OBJECS not primitives
            // local varialbes stored on thread stack - each stack has copy and only on thread stack instance only
            // not shared so will be useless - String may only work - Solution: don't use local variable on synchronized statement

            // current instance of object is implicitely locked -
            // every object instance has built-in intrinsic lock - monitor lock
            // thread acquires a lock by executing a synchronized method on the instance or by using the instance as parameter to a synchronized statement.
            //         releases lock when it exits from synchronized block or method even if it throws an excetpion
            // Only one thread at a time can acquire this lock, which prevents all other threads from accessing instances's state, until lock is released
            // All other threads, which want acess to instances state through synchronized code will;
            //      block, wait until lock is acquireable
            // Synchronized statement better option : limits scope of synch, to critical wsection of code
            //      - Gives more granular control, over when you want other threads to block
            //      - Gives even more control - can use a different object to acquire its lock, accessing this ScanAccount instance won't have to block entirely
            double origBalance = balance;
            balance += amount;

            System.out.printf("STARTING BALANCE: %.0f, DEPOSIT (%.0f)" +
                    " : NEW BALANCE = %.0f%n", origBalance, amount, balance);

            addPromoDollars(amount); // gets lock on lockBalance field - same thread calling different method within thread trying to acquire lock
            // methods executed in same thread - any nested calls which try to acuire a lock, won't block, because the current thread exists already
            // REENTRANT SYNCHRONIZATION
            // Without - threads could block indefinitely
            // Based on Monitor Mechanism - Concept Build into Java language
        }
    }

    public synchronized void withdrawal(double amount) {


        try {
            Thread.sleep(100);
        } catch(InterruptedException e) {
            throw new RuntimeException();
        }

        double origBalance = balance;
        if (amount <= balance) {
            balance -= amount;

            System.out.printf("STARTING BALANCE: %.0f, WITHDRAWAL (%.0f)" +
                    " : NEW BALANCE = %.0f%n", origBalance, amount, balance);
        } else
            System.out.printf("STARTING BALANCE: %.0f, WITHDRAWAL (%.0f)" +
                    " : INFSUFFICIENT FUNDS!", origBalance, amount);

    }

    // give 25 to 2500 deposit
    private void addPromoDollars(double amount) {
        if(amount >= 5000) {
            synchronized(lockBalance) {
                System.out.println(" Congrats");
                this.balance += 25;
            }
        }
    }
}
