package com.init_spring_bean_mvn.demo.classobjectstaticinstancefieldsmethods;

public class BankAccount {
    public Integer getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(Integer accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    private Integer accountNumber;
    private Double balance;
    private String customerName;


    public void depositFunds(Double amount) {
        balance += amount;
    }

    public Double withdrawFunds(Double amount) {
        if (balance >= amount) {
            balance -= amount;
            return amount;
        } else {
            System.out.println("Insufficient funds");
            return 0.0;
        }
    }

     public BankAccount(Integer accountNumber, Double balance, String customerName) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.customerName = customerName;
    }

}
