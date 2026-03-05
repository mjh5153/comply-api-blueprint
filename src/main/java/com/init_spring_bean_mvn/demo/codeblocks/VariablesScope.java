package com.init_spring_bean_mvn.demo.codeblocks;

public class VariablesScope {
    public static void main(String[] args) {
        // local scope or variable scope
        // variable available only within the method or block where it is declared
        // In scope for nested blocks or blocks container within outer block
        // true for parameters as well
        // consider replacing deeply nested varialbes with method calls
        // variable in method block is containg block is not visible outside the block
        // inside conditional a variable declared there not available outside the conditional block
        // declare vriables in narrowest scope possible, do in single statement where possible
        // only available in nested block, declare there, loop code or in conditional block
        // Harder to do in while loop
        // switch - variable declared in case block can be accessed in other block as long as block is after where variable declared
            // get error if a varialbedecalred in below block accessed in previous block
            // switch (value) { case 1: value; case 2: int value = 5}

        //
    }
}
