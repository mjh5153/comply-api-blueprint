package com.init_spring_bean_mvn.demo.lpa;

import com.init_spring_bean_mvn.demo.account.ScanAccount;

public class Main {
    public static void main(String[] args) {
        ScanAccount companyAccount = new ScanAccount("Medical", 10000);
// order change but math correct for deposit and withdrawal
        //
        Thread thread1 = new Thread(() -> companyAccount.withdrawal(2500));
        Thread thread2 = new Thread(() -> companyAccount.deposit(5000));
        Thread thread3 = new Thread(() -> companyAccount.setName("Legal"));
        Thread thread4 = new Thread(() -> companyAccount.withdrawal(5000));

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        // can join all three threads to main thread

        try {
            thread1.join();
            thread3.join();
            try {
                Thread.sleep(500);
            } catch(InterruptedException e) {
                throw new RuntimeException(e);
            }
            thread2.join();
            thread4.join();
            // Making customer go through whole balance process - fix : remove shynch from deposit and create specialized code block
            // Next video - Synchronized  blocks and meaning
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Final Balance: " + companyAccount.getBalance());
        // this code will show inconsistent results - time slicing happening
        // caching of balance by each thread - mem inconsitency, thread interferenec or both -
    }
}
