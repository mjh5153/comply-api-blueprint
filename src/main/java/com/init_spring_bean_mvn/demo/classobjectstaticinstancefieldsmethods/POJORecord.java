package com.init_spring_bean_mvn.demo.classobjectstaticinstancefieldsmethods;

public class POJORecord {
    public static void main(String[] args) {
        // table id, name, age, email

        // create object in loop - 5 records of items - name, dob, classList, id

        // can pass result to any code that may use it such as a mailing list

        // with record the curly braces would be brackets instead
        for(int i = 0; i <= 5; i++) {
            POJO pojo = new POJO("id"+i, "name"+i, "dob"+i, "classList"+i);
            System.out.println(pojo);
            switch(i) {
                case 1 -> System.out.println("This is case 1");
                case 2 -> System.out.println("This is case 2");
                case 3 -> System.out.println("This is case 3");
                case 4 -> System.out.println("This is case 4");
                case 5 -> System.out.println("This is case 5");
            }
        }

    }
}
