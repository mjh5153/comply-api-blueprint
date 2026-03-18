package com.init_spring_bean_mvn.demo;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/* Gets data from external source
* Create contact for each row  in data passed*/
public class Contact {
    public String getName() {
        return name;
    }

    private String name;
    Set<String> emails = new HashSet<>();
    Set<String> phones = new HashSet<>();

    public Contact(String name) {
        this(name, null);
    }

    public Contact(String name, String email) {
        this(name, email, 0);
    }

    public Contact(String name, long phone) {
        this(name, null, phone);
    }

    public Contact(String name, String email, long phone) {
        this.name = name;
        if(email != null) {
            this.emails.add(email);
        }
        if(phone> 0) {
            String p = String.valueOf(phone);
            p = "+" + p.substring(0, 3) + "-" + p.substring(3, 6) + "-" + p.substring(6);
            phones.add(p);
        }
        this.phones.add(String.valueOf(phone));
    }

    @Override
    public String toString() {
        return "%s: %s %s".formatted( name, emails, phones);
    }

    public Contact mergeContactData(Contact contact) {

        Contact newContact = new Contact(name);
        newContact.emails = new HashSet<>(emails);
        newContact.phones = new HashSet<>(phones);
        this.emails.addAll(contact.emails);
        this.phones.addAll(contact.phones);

        return newContact;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        Contact contact = (Contact) o;
        return getName().equals(contact.getName());
    }

    @Override
    public int hashCode() {
        // classes aren't same type should have different hashcodes
        return 33 * getName().hashCode();
        // research debate of 33 and 31
    }

    public void addEmail(String companyName) {
        String[] names = name.split(" ");
        String email = "%c%s@%s.com".formatted(name.charAt(0), names[names.length-1],
                companyName.replaceAll(" ", "").toLowerCase());
        emails.add(email);
    }

    //  Java hashcode impl
    //  public static int hashCode(Object[] a, int fromIndex, int length, int initialValue) {
    //        int result = initialValue;
    //        int end = fromIndex + length;
    //        for (int i = fromIndex; i < end; i++) {
    //            result = 31 * result + Objects.hashCode(a[i]);
    //        }
    //        return result;
    //    }
}
