package com.init_spring_bean_mvn.demo;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.springframework.boot.SpringApplication.*;


//@Configuration
//@EnableAutoConfiguration
//@ComponentScan

// Problem Spring mvc, Hibernate, Spring Security dependencies
// Solution - spring-boot-starter-* // frameworks for each; web, jpa ( hibernate ) and security - don't need to maintain all versions of compatible versions
// most common: webmvc, jackson-json, validation-api, tomcat

@SpringBootApplication // combo of enableautoconfiguration and @ComponentScan - specifiy package for component scanning and @SpringBootConfiguration annotation
public class DemoApplication {

    interface DelegateI {
        String name();
    }

    @Bean
    public HelloWorld helloWorld() {
        return new HelloWorld();
    }

    public static void printData(String header, Collection<Contact> contacts) {
        System.out.println(header);
        contacts.forEach(System.out::println);
    }

    private static Map<String, Purchase> purchases = new LinkedHashMap<>();
    private static NavigableMap<String, Delegate> delegates = new TreeMap<>(); //similar to treeset and navigableSet
    // String or LocalDate as Key,
    // Next use TreeMap to make sense of purchase data

    enum WeekDay {MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY};
    private static int[] getRandomArray(int len) {
        Random random = new Random();
        int[] randomArray = new int[len];
        for(int i=0; i<len; i++) {
            randomArray[i] = random.nextInt(100); // exclusive upperbound: 1 - 99
        }
        return randomArray;
    }
    public static void main(String[] args) {
        ApplicationContext appContext = run(DemoApplication.class, args);
        HelloWorld helloWorld = appContext.getBean(HelloWorld.class);
        System.out.println(helloWorld.helloWorld());

        try {
            URL url = null;
            try {
                url = new URL("http://localhost:8080");
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            // new http 2 - faster - multiplexing and header , priority of critical resources to load it first
            // http url connection only with http1.1
            // HttpClient suppoerts both
            HttpRequest request = HttpRequest.newBuilder().GET().uri(url.toURI())
                    .header("User-Agent", "Chrome")
                    .headers("Accept", "application/json")
                    .timeout(Duration.ofSeconds(30))
                    .build();
            // get headers
            // headers.entrySet().forEach()

            TimeUnit.SECONDS.sleep(5);
            // send is synchronous
            /*  HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString()); */
            // threading could be used OR client.sendAsync
            // non blocking - returns with Completable future - method parms same
            // CompletableFuture<HttpResponse<T>> - result of async computation
            // implements Future  and extends Future - actions trigger upon completion - example promises and callbacks in javascript
            // methods support callback - pass function to other function in future such as in lambdas
            //
            HttpResponse<Stream<String>> asyncresponse;
            // CompletableFuture<HttpResponse<Stream<String>>> responseFuture = client.sendAsync(request, HttpResponse.BodyHandlers.ofLines());
            // use Future get method
            // asyncresponse = responseFuture.get();
            // responseFuture.join();
            // join method in Threads - blocking if used without timeout for curr thread to wait for spawned thread timed out
            // join async doesn't throw checked exception
            int result = -1;
            // while((result = response.statusCode()) == -1) {

//            }
//            HttpResponse.BodyHandlers.ofLines();
//            if(response.statusCode() != HTTP_OK) {
//                System.out.println("error");
//                return;
//            }
//        } catch (IOException e) {
//
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (ExecutionException e) {
//            throw new RuntimeException(e);
//        }
            /* String operations
             * length(), isEmpty(), charAt(int index),
             * Manipulation: toLowerCase, toUpperCase, trim(), contact(String str, replace(String oldChar, String newChar)
             * substring(int beginIndex, int endIndex
             * Creating New Strings from existing- join(), repeat(int count), valueOf(Object obj)
             * */

            String myString = "Hello World!";
            // String methods; equals, equalsIgnoreCase
            String[] myarray = myString.split(" "); //

            // use StringBuilder to concatenate strings - avoids creating unnecessary temp string objects
            StringBuilder sb = new StringBuilder(); //initial capacity 16
            sb.append(myString);
            System.out.println(sb.toString()); // can insert at specific position (5, "," )
            //sb // chars(), insert(), indexOf(), codePoint() - unicode , replace, reverse, capacity, subsequence, substring, capacity, size

            /* multiple values of same type - Array */
            int[] integerArray = new int[10];
            integerArray[4] = 50;


            double[] myDoubleArray = new double[10];
            myDoubleArray[2] = 3.5;
            System.out.println(myDoubleArray[2]);

            // Now shortcut Array initializer
            int[] myIntArray = new int[]{1, 2, 3, 4, 5}; // comma delimited list in curly braces
            // java allows to drop new int[], called anonymouse array, declaration statement

            int[] firstTen = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
            System.out.println("first = " + firstTen[0]);
            int arrayLength = firstTen.length;
            System.out.println("last = " + firstTen[arrayLength - 1]);
            // cant use {} for initializing in statements - get Array initializer is not aloud here; Can use; new int[]{}

            int[] newArray = new int[5]; // No array initializer ? all array elements get initiazed to default value for type,
            //primites ? zero - int, double , short, booleans ? false, class ? null

            // populating array dynamically
            for (int index = 0; index < newArray.length; index++) {
                newArray[index] = newArray.length - index;
                System.out.println("index = " + index + ", value = " + newArray[index]);
            }

            for (int element : newArray) { // don't have index, simpler less error prone
                System.out.print(element + " ");
            }
            System.out.println(newArray);
            // prints Object Address [I@hashcode] - can't use toString()
            // Solution java.util.Arrays class; propert or field - length
            // Inherits from java.util.objects, class methods not instance methods - static
            // Arrays.toString(new Array)

            System.out.println(Arrays.toString(newArray)); // prints in square brackets and comma delimited
            // can assign any array to object variable
            Object objVar = newArray;
            if (objVar instanceof int[]) {
                System.out.println("objVar is an int[]");
            }

            Object[] objArr = new Object[3];
            objArr[0] = "New";
            objArr[1] = new StringBuilder("Scan");
            objArr[2] = newArray; // nested array!!!!!!

            /** Sorting, Filling Copying Arrays helper java.util.Arrays*/

            // manage items of same type
            // look at oracle api docs; overloaded methods - binarySearch()
            // most common

            // enhanced for loop or for each loop
            // for array or other collection types - for (declaration : collection)

            for (int index = 0; index < newArray.length; index++) {
                System.out.println("index = " + index + ", value = " + newArray[index]);
            }
            // array gets printed in order of indexes

            // Arrays Part II:: Array Populating, Looping, searching
            // What is an array - special class, inherits from java.lang.Object

            int[] firstArray = getRandomArray(10);
            System.out.println(Arrays.toString(firstArray));

            // Sorting
            Arrays.sort(firstArray); // return type void
            System.out.println(Arrays.toString(firstArray)); // low to high : natural order

            int[] secondArray = new int[10];
            Arrays.fill(secondArray, 5); // sets every element to 5
            // create copy of array and apply to copy

            int[] thirdArray = getRandomArray(10);
            int[] fourthArray = Arrays.copyOf(thirdArray, thirdArray.length);

            System.out.println(Arrays.toString(fourthArray));
            // array copy creates a new instance of array, no impact to original array
            // when copyOf length is less - copies first 5
            // greater than - 0's added to additional elements indexes

            /*** Binary Search and Equality checks */
            // search sequentially - linear or sequential search
            // sorted ? unnecessarily inefficient - pick a spot close to where word might be
            // intervals to search - split to section up, testing boundaries, narrowing search each time - interval searching
            // intervals continually split into two - binary
            // Requirements: sorted, comparable, duplicates? no guarentee of accuracy

            String[] sArray = {"Emily", "John", "Michael", "Sophia"};
            Arrays.sort(sArray);
            if (Arrays.binarySearch(sArray, "Emily") >= 0) {
                System.out.println("Found emily ");
            }

            // two arrays equal
            int[] s1 = {1, 2, 3};
            int[] s2 = {1, 2, 3};
            if (Arrays.equals(s1, s2)) {
                System.out.println("Arrays are equal");
            }
            // switching numbers indexes causes array to not be equal and if lengths aren't equals
            // ArrayIndexOUtOfBoundsException - index is out of bounds;

            /**** Nested Arrays **/
            // two: int[][] array = {{1,2,3},{}} - can be different sizes
            int[][] nestedArray = new int[20][20]; // defaults to 0
            // second set can be empty - limited to assigning ints
            // easiest way is [][]
            // Multiple arrays
            // Arrays.deepToString

            nestedArray[1] = new int[]{10, 20, 30};
            System.out.println(Arrays.deepToString(nestedArray));

            // nested arrays can have nested array of any type or length
            Object[] nestedObjArray = new Object[20];
            nestedObjArray[0] = new String[]{"10", "20", "30"};
            // Runtime Error, No Compile error - assign Object array to anything else than array it will fail at runtime

            // Best Practice: Stay to stricly typed arrays!!! nestedObjArray[0] = "String"

            /*Linked List*/
            // Why - Array of primitive values - memory allocated contiguously - java can use simple math to get address of arrays
            // Object References - Object not contiguous, references are, remove ? addresses re-indexed or shifted, expensive if elements size large
            // ArrayList capacity - initial capacity
            ArrayList<Integer> list = new ArrayList<>(10);

            // elements exceed ? java reindex array and copy existing elements; add costs more in time and memory
            // java sets new capacity to greater capacity, can't get to size() from ArrayList
            // adding an element is constant amortized time
            // How to determine cost? Big O
            // time and mem usage as scales - approximate cost estimate n - number of elements,
            // Amortized Time Cost - O(1) * close to O(!) somewhere between 1 and n, max cost O(n) in reallocation
            // arrayList operations - add:o(1)*, add ( index, e): O(n), contains: O(n), get: O(1), indexOf: O(n), remove: O(n), set: O(1)

            // not indexed, forms chain and links to prev and next or doubly linked list, head or tail is start and end, or queue
            // start at head or tail, check matched: Expensive in cpmuter currency, inserting and removing much simpler
            // break two links and reestablish new links, no new array shifting or reallocation of memory - cheap
            // Debate on if LinkedList ever preferred

            // Array list MAX_VALUE is integer size - Use linkedList large amounts of data

            // scans - alpha order - simple types here implement comprable
            Scan tech = new Scan("tech1", "MIT License", "open source");


            // purchases: LinkedHashMap - insertion order
            addPurchase("MIT License", tech, .10);

            purchases.forEach((k, v) -> System.out.println("key=" + k + ", value= " + v));

            NavigableMap<LocalDate, List<Purchase>> datePurchases = new TreeMap<>();
            for (Purchase p : purchases.values()) {
                // Attempts to compute a mapping for the specified key and its current mapped value, or null if there is no current mapping (optional operation). For example, to either create or append a String msg to a value mapping:
                datePurchases.compute(p.purchaseDate(), (pdate, plist) -> {
                    List<Purchase> cclist = (plist == null) ? new ArrayList<>() : plist;
                    cclist.add(p);
                    return cclist;
                });

                datePurchases.forEach((key, value) -> System.out.println("key=" + key + ", value= " + value));

                /*Treemap - headmap and tailmap*/
                int currentYear = LocalDate.now().getYear();
                LocalDate firstDay = LocalDate.ofYearDay(currentYear, 1);
                LocalDate week1 = firstDay.plusDays(7);
                // LocalDate methods - plusWeeks, plusYears
                Map<LocalDate, List<Purchase>> week1Purchases = datePurchases.headMap(week1); //excludes value you pass unless overload with inclusive flag true
                Map<LocalDate, List<Purchase>> week2Purchases = datePurchases.tailMap(week1); // inclusive by default
                week1Purchases.forEach((key, value) -> System.out.println("key=" + key + ", value= " + value));

                displayStats(1, week1Purchases);
                displayStats(2, week2Purchases);
                // prints purchases by date and totals

                //pollfirstKey pollLastKey, lastEntry, lowerKey, lowerEntry, floorEntry, floorKey ( makes below while infinite loop )
                LocalDate lastDate = datePurchases.lastKey();
                var previousEntry = datePurchases.lastEntry();
                // to get entry's need to call getValue();

                while (previousEntry != null) {
                    List<Purchase> lastDaysData = previousEntry.getValue();
                    System.out.println(lastDate + " purchases: " + lastDaysData.size());

                    LocalDate prevDate = datePurchases.lowerKey(lastDate);
                    previousEntry = datePurchases.lowerEntry(lastDate);
                    lastDate = prevDate; //continue until lowerEntry return a null
                }


                // descending map - return same data above but with higher instead of lower methods
                var reversed = datePurchases.descendingMap();

                LocalDate firstDate = reversed.firstKey();
                System.out.println("First date: " + firstDate);
                // var nextEntry = reversed.firstEntry();
                var nextEntry = reversed.pollFirstEntry(); // manipulates source map
                // subMap(fromKey, toKey
                // subMap(fromKey, bool inclusive, toKey, bool inclusive)

                while (nextEntry != null) {
                    List<Purchase> lastDaysData = nextEntry.getValue();

                    LocalDate nextDate = reversed.higherKey(firstDate);
                    //nextEntry = reversed.higherEntry(firstDate);
                    nextEntry = reversed.pollFirstEntry();
                    firstDate = nextDate;

                }
                System.out.println("-------------------------");
                datePurchases.forEach((k, v) -> System.out.println("key=" + k + ", value= " + v));

                /* EnumSet and EnumMap - List, Set or Map with enum Constant
                 * Both have impl differs from HashSet or HashMap
                 * - compact and efficient - no special list
                 * EnumSet - elements come from single enum type
                 * EnumSet abstract, many factory methods
                 * better perf than HashSet
                 * Bulk operations O(1)
                 *
                 * EnumMap - enum type keys - ordered by ordinal value of enum constants
                 * same as HashMap - O(1) basic operations
                 * better perf than hashMap with enum type*/


                // now different purchases for day - but Map ordered in chrono order
                List<Contact> treePhones = ContactData.getData("phone");
                List<Contact> treeEmails = ContactData.getData("email");

                // elements must implement comprable

//        NavigableSet<Contact> sorted = new TreeSet<>(treePhones);
                Comparator<Contact> mySort = Comparator.comparing(Contact::getName);
                NavigableSet<Contact> sorted = new TreeSet<>(mySort);
                sorted.addAll(treePhones);
                sorted.forEach(System.out::println);

                NavigableSet<String> justNames = new TreeSet<>();
                treePhones.forEach(c -> justNames.add(c.getName()));
                System.out.println(justNames);
                // !Sorted output [Emily James, John Doe, Michael Torres, Sophia Lane]

                NavigableSet<Contact> fullSet = new TreeSet<>(sorted);
                fullSet.addAll(treeEmails);
                fullSet.forEach(System.out::println);
                // combined list of contacts in alphabetical order

                // method in sorted set that returns comparator on sorted set

                Contact min = Collections.min(fullSet, fullSet.comparator());
                Contact max = Collections.max(fullSet, fullSet.comparator());
                // overloaded version to pass comparator
                //first last, pollfirst: remove first or last sorted element from set, polllast
                Contact first = fullSet.first();
                Contact last = fullSet.last();
                //Preferred way to get data

                System.out.println("First contact: " + first);
                System.out.printf("min=%s, max=%s%n", min, max);

                // create copy of set
                NavigableSet<Contact> copiedSet = new TreeSet<>(fullSet);
                System.out.println("First element: = " + copiedSet.pollFirst()); // remove element from set
                System.out.println("Last element: = " + copiedSet.pollLast());
                copiedSet.forEach(System.out::println);
                System.out.println("-------------------------");

                /*TreeSet continued Closet Match and Subset Methods*/
                // to a value you pass
                Contact sofia = new Contact("Sophia Lane");
                Contact emily = new Contact("Emily James");
                Contact joel = new Contact("Joel");
                Contact karen = new Contact("Karen");

                // higher, lower, ceiling, floor
                for (Contact c : List.of(sofia, emily, joel)) {
                    System.out.printf("Ceiling(%s)=%s%n", c.getName(), fullSet.ceiling(c)); // returning element >= element passed
                    System.out.printf("Higher(%s)=%s%n", c.getName(), fullSet.higher(c)); // returns next greater element
                }
                System.out.println("-------------------------");

                for (Contact c : List.of(sofia, emily, karen)) {
                    System.out.printf("Floor(%s)=%s%n", c.getName(), fullSet.floor(c)); // returning element >= element passed
                    System.out.printf("Lower(%s)=%s%n", c.getName(), fullSet.lower(c)); // returns next greater element
                }
                System.out.println("-------------------------");

                // NavigableSet methods to get closest matches
                NavigableSet<Contact> descendingSet = fullSet.descendingSet();
                descendingSet.forEach(System.out::println);
                System.out.println("-------------------------");
                fullSet.forEach(System.out::println);
                System.out.println("-------------------------");
                // can get subsets from the head or beginning of full descendingSet
                //headSet method

                Contact marion = new Contact("Marion");
                var headSet = fullSet.headSet(marion); // can be overloaded with false to make inclusive
                headSet.forEach(System.out::println);
                System.out.println("-------------------------");
                //exclusive
                var tailSet = fullSet.tailSet(marion); // same as headSet but exclusive
                tailSet.forEach(System.out::println);
                System.out.println("-------------------------");
                //inclusive

                //Can get subset in the middle: subset method on navigableset to
                Contact linus = new Contact("Linus Torvalds");
                var subset = fullSet.subSet(linus, false, marion, true); //exclude linus and inclue marion
                subset.forEach(System.out::println); // has overloaded method
                // see getting subsets from a TreeSet slide for table and inclusivity

                // TreeSet - higher performance cost
                // ensure no duplicate elements stored - alternavite to ArrayList for sorting and uniqueness

                /*Map interface of Java Collections framework - complements List and Sets
                 * Map doesn't inherit from Collections - different data structure
                 * two type arguments - interface Map<K,V> - reference types not primitives
                 * replaces Dictionary
                 * java map can't have duplicate keys as english language words can have many meaning for same word
                 * HashMap, LinkedHashMap, TreeMap
                 * */

                List<WeekDay> annsWorkDays = new ArrayList<>(List.of(WeekDay.MONDAY, WeekDay.TUESDAY, WeekDay.WEDNESDAY, WeekDay.THURSDAY, WeekDay.FRIDAY));

                var annsDaysSet = EnumSet.copyOf(annsWorkDays);
                // Prints RegularEnumSet as class
                System.out.println(annsDaysSet.getClass().getSimpleName());

                annsDaysSet.forEach(System.out::println);

                var allDaysSet = EnumSet.allOf(WeekDay.class);
                System.out.println("--------------------");

                allDaysSet.forEach(System.out::println);

                //complementOf

                Set<WeekDay> anotherWay = EnumSet.copyOf(annsDaysSet);
                anotherWay.removeAll(annsDaysSet);
                anotherWay.forEach(System.out::println);

                // range method - EnumSet.range(WeekDay.MONDAY, WeekDay.FRIDAY) - inclusive

                // EnumMap - no no args constructor

                Map<WeekDay, String[]> employeeMap = new EnumMap<>(WeekDay.class);
                employeeMap.put(WeekDay.MONDAY, new String[]{"Ann", "Mary", "Bob"});

                employeeMap.forEach((k, v) -> System.out.printf("%s: %s%n", k, Arrays.toString(v)));
                // ordered by weekday without using sorted map
                // has all methods has hashmap
                // More efficient and ordered than

                // Challenge - Create program using Conceptual Model - Product, InventoryItem, Cart, Store

                // list of phone and email contacts creating combined lists
                List<Contact> mapphones = ContactData.getData("phone");
                List<Contact> mapemails = ContactData.getData("email");
                List<Contact> fullList = new ArrayList<>(mapphones);
                fullList.addAll(mapemails);
                fullList.forEach(System.out::println);

                Map<String, Contact> map = new HashMap<>(); // don't have addAll(), can't pass collections type
                for (Contact contact : fullList) {
                    map.put(contact.getName(), contact);
                }
                // lambda requires two args; key and value
                map.forEach((k, v) -> System.out.println("key=" + k + ", value= " + v));
                // use key: value to get entry
                // returns null if doesn't default - jdk 8 can replace null with default

                System.out.println(map.get("Charlie Brown"));

                Contact defaultContact = new Contact("Peter Griffin");
                System.out.println(map.getOrDefault("Peter Griffin", defaultContact));
                System.out.println("-------------------------");
                map.clear();

                for (Contact contact : fullList) {
                    Contact duplicate = map.put(contact.getName(), contact);
                    if (duplicate != null) {
//                System.out.println("Duplicate = " + duplicate);
//                System.out.println("Duplicate = " + contact);
                        map.put(contact.getName(), contact.mergeContactData(duplicate)); //new contact with current and duplicate data merged
                    }
                }
                map.forEach((k, v) -> System.out.println("key=" + k + ", value= " + v));
                // don't want to replace value every put

                System.out.println("-------------------------");
                map.clear();
                for (Contact contact : fullList) {
                    map.putIfAbsent(contact.getName(), contact); // default on map inteface
                }
                // lambda requires two args; key and value
                map.forEach((k, v) -> System.out.println("key=" + k + ", value= " + v));
                // diff of put and putIfAbsent - won't put updated element in map if exists, returns null if 1st time entry being added.

                // jdk 8 - merge method, key, value, target for lambda
                System.out.println("-------------------------");
                map.clear();
                fullList.forEach(contact -> map.merge(contact.getName(), contact, (prev, curr) -> {

                    Contact merged = prev.mergeContactData(curr); // can be replaced with lambda method reference Contact::mergeContactData
                    return merged;
                }));
                map.forEach((k, v) -> System.out.println("key=" + k + ", value= " + v));

                //jdk 8 : compute, computeIfAbsent, replace, remove
                System.out.println("-------------------------");

                for (String contactName : new String[]{"Sofia Lane", "Emily James", "Michael Torres"}) {
                    //map.compute(contactName, (k, v) -> new Contact(k));
                    map.computeIfAbsent(contactName, (k) -> new Contact(k)); // only takes key parameter
                    // if emtry exists doesn't get overwritten


                }


                // replace what's in the map with what in lambda expression
                map.forEach((k, v) -> System.out.println("key=" + k + ", value= " + v));

                for (String contactName : new String[]{"Sofia Lane", "Emily James", "Michael Torres"}) {
                    //map.compute(contactName, (k, v) -> new Contact(k));
                    map.computeIfPresent(contactName, (k, v) -> {
                        v.addEmail("Fun Place");
                        return v;
                    });
                    ; // only takes key parameter
                    // if emtry exists doesn't get overwritten


                }

                // next replaceAll - similar to Set but takes bi function (lambda)
//        map.replaceAll((k,v) -> {
//            String newEmail = k.replaceAll(" ", "") + "@funplace.com";
//            v.replaceEmailIfExists("DDuck@funplace.com", newEmail);
//            return v;
//        } ))

                // replace on key and value

                // map.replace("Daisy Duck", daisy); overloaded method
//        Contact updated = replacedContact.mergeContactData(daisy)
                // boolean success = contacts.replace("Daisy Duck", replacedContact, updatedDaisy)
                // if(success) {
//            System.out.println("Map replaced successfully");

                // else {
//            System.out.println("Map replace failed");
                //}
                // contact considered equal if have same name

                // remove method also overloaded// won't remove unless equal to element passed in second arg

                /* Mastering Map View Collections: keySet, values, entrySet
                 * view doesn't store elements - depends on backing collection : headSet, tailSet or subSet methods
                 * Arrays.asList method - gets array in form of a list
                 * changes to list, changes in array and List - liited to features supporting by backing storage
                 * Database views: hide details to underlying ds
                 * view collections let us manupulate collections w/o knowing storage details
                 * can get collection view of data ? get access to methods to manipulate data
                 *
                 * Concreate classes implement Map and Map.Entry interfaces
                 *
                 * HashMap impls map and has static nested class, Node, implements Map.Entry interface
                 *
                 * HashMap maintains an array of Nodes, in field called table, size managed by java, indices by hashing functions
                 * i.e why HashMap is not ordered
                 *
                 * 3 view collections- keyset, entrySet, Values, map has keys - no dups, retrieved as Set view by calling keySet, keyvalue pair stored as instance of entry - comb of key and value will be unique
                 *
                 * Entry can be retrieved by method entrySet
                 *
                 * Values are stored, refed by key and values can have duplicates, keys can have reference to same values
                 *
                 * */

                Map<String, Contact> map2 = new HashMap<>();

                ContactData.getData("phone").forEach(c -> map2.put(c.getName(), c));
                ContactData.getData("email").forEach(c -> map2.put(c.getName(), c));

                Set<String> keysView = map2.keySet(); // need only one type in key map - String
                System.out.println(keysView);

                // if use TreeSet - get copy of keys, not a view

                if (map2.containsKey("John Doe")) {
                    System.out.println("Found John Doe"); // much easier than creating a contact instance
                }

                keysView.remove("John Doe");
                System.out.println(keysView); // won't remove from original

                keysView.retainAll(List.of("John Doe", "Sofia Lane", "Emily James"));

                // Set returned from keySet is backed by Map
                // Changes to Map are reflected in Set and vice-versa
                // Set supports: element removal, removes corresponding mapping from map
                // use remove, removeall, retailAll and clear not add or addAll operations

                keysView.clear();
                System.out.println(map2);

                ContactData.getData("email").forEach(c -> map2.put(c.getName(), c)); // adds each contact to set
                ContactData.getData("phone").forEach(c -> map2.put(c.getName(), c));
                System.out.println(keysView); //set
                // all contacs in keys view set, view refreshed automatically - powerful way to manipulate elements in map

                var values = map2.values();
                values.forEach(System.out::println);

                values.retainAll(ContactData.getData("email"));
                map2.forEach((k, v) -> System.out.println(v));
                // get unsupported operation exception - add CANNOT BE USED

                // adding duplicate value using new key
                // HashSet takes collection as arg of constructor
                HashSet<Contact> contacts = new HashSet<>(values);
                // if set size < map2.keySet().size() ? duplicates are in map
                /* entrySet: used to check if names not in sync with Contact name */
                var nodeSet = map2.entrySet();
                for (var node : nodeSet) {
                    System.out.println(nodeSet.getClass().getName()); // shows underlying Objects
                    if (!node.getKey().equals(node.getValue().getName())) {
                        System.out.println("Key and value names do not match: " + node.getKey() + " " + node.getValue());
                    }
                }

                /* LinkedHashMap and TreeMap classes
                 * key value entry collections: ordered by insertion order
                 * treemap - needs to implement comparable b/c ordered by keys
                 * */


                List<Contact> emails = ContactData.getData("email");
                List<Contact> phones = ContactData.getData("phone");
                printData("Phone List", phones);
                printData("Email List", emails);
// combine contacts, merging any dups with multiple email and phone numbers into single record
                Set<Contact> emailContacts = new HashSet<>(emails);
                Set<Contact> phoneContacts = new HashSet<>(phones);
                // most collections constructors accept collection
                printData("Phone contacts", phoneContacts);
                printData("Email contacts", emailContacts);
                // Duplicates in hashset: haschode , equals method : using object implementation - why duplicate values show up
                // need to add rule  in Contact Class to make name unique
                // change default java, select all fields : first two screens, select name for non-null fields
                int index = emails.indexOf(new Contact("John Doe"));
                System.out.println(index);
                Contact john = emails.get(index);
                john.addEmail("jim do");
                System.out.println(john);
                // collections
                //List<String> list = new ArrayList<>();
                // Collection<String> list = new ArrayList<>();

                // Collection<String> list = new TreeSet<>();
                // code run s and output same
                Set<Contact> unionAB = new HashSet<>();
                unionAB.addAll(emailContacts);
                unionAB.addAll(phoneContacts);
                printData("(A  B) Union of emails (A)  with phones (B)", unionAB);

                Set<Contact> intersectAB = new HashSet<>(emailContacts);
                intersectAB.retainAll(phoneContacts);
                printData("(A ∩ B) Intersection of emails (A)  with phones (B)", intersectAB);

                // Union Sets - unique items returned from all sets

                // Intersect Sets - 2 or more sets - items occurring in both returned
                // interset - Symettric Set: A B and B A equal

                /* use retailAll method */

                Set<Contact> intersectBA = new HashSet<>(phoneContacts);
                intersectAB.retainAll(emailContacts);
                printData("(B ∩ A) Intersection of phones (B)  with emails (A)", intersectBA);

                // Set Difference - Leave only elements from 1st set as result - asymmetric
                // use removeAll method
                // code test AMinusB and BMinusA: reverse direction

                Set<Contact> BMinusA = new HashSet<>(emailContacts);
                BMinusA.removeAll(phoneContacts);
                // Symmetric are items not occurring in both sets


                // Symmetric Diff: hones and emails - unionAB - elements in one set but not other
                // only distinct Elements

                Collection<String> clist = new HashSet<>();
                // not ordered as lists
                //how to sort?


                String[] names = {"John", "Mary", "Bob"};
                clist.addAll(Arrays.asList(names));
                System.out.println(clist);

                clist.add("Fred");
                clist.addAll(Arrays.asList("Tom", "Jerry")); // takes collection of elements to be added: second paramter is array

                System.out.println("Tom in the list? " + clist.contains("Tom"));

                clist.removeIf(s -> s.charAt(0) == 'T');
                System.out.println(clist);
                System.out.println("Tom in the list? " + clist.contains("Tom"));

                // abstract variable type to collections
                // list.sort() //doesn't compile List does
                // concrete classes like lists and set implement classes
                // Example List interface extending Collection
                // list can be indexed as arraylist or not, like LinkedList
                // add, remove get and sort elements for specific collection - Derived interfaces

                /* Collections: Versatile Deck of Cards */
                // record, nested enum on record and ASCII char of suit
                // getNumericCard, getFaceCard, Static:getStandardDeck, printDeck, overloaded printDeck
                /* Collections Essential Methods and List operations */
                // java.util.Collections is not Collections Framework
                // one example of interfaces and implemented classes

                //populate a list; fill
                List<String> delegateList = new ArrayList<>(13);
                Collections.fill(delegateList, "Tech"); // if list is emty it stays empty
                System.out.println(Arrays.toString(delegateList.toArray()));

                // nCopies
                List<String> delegates = new ArrayList<>(Collections.nCopies(13, "Tech"));
                System.out.println("Delegates added " + delegates);

                List<String> medicalDelegate = new ArrayList<>(Collections.nCopies(13, "Medical"));

                List<String> legalDelegate = new ArrayList<>(Collections.nCopies(13, "Legal"));

                List<String> financialDelegate = new ArrayList<>(Collections.nCopies(13, "Financial"));

                // Collections.addAll(delegateList, "Marketing", "Sales");
                //Exception in thread "main" java.lang.IndexOutOfBoundsException: Source does not fit in dest
                //	at java.base/java.util.Collections.copy(Collections.java:585)
                //	at com.init_spring_bean_mvn.demo.DemoApplication.main(DemoApplication.java:88)

                // Collections.copy(delegateList, delegates); // cant use copy if element in list is < source list
                // doesn't return a copy - use list.copyOf method

                Collections.shuffle(delegates);

                Collections.reverse(delegates);

                System.out.println(Arrays.toString(delegates.toArray()));

                // sorting - legacy code

                // Implement comparable or pass a comparator

                var sortingAlgo = Comparator.comparing(DelegateI::name)
                        .thenComparing(DelegateI::name);

                Collections.sort(delegates);

                System.out.println(delegates + " sorted " + 13);

                // Collections.indexOfSubList
                int subListIndex = Collections.indexOfSubList(delegates, delegates);

                System.out.println("subListIndex " + subListIndex);

                System.out.println("delegates.containsAll(delegates) " + delegates.containsAll(delegates)); // might have poor perf but wrap in HashSet constructor

                // disjoint - check if lists dont share elements

                boolean disjoint = Collections.disjoint(delegates, delegates);
                System.out.println("disjoint " + disjoint);

                // Collections.binarySearch: Lists, elements sorted, no guarenteed index of duplicates, overloaded: pass elements or comparator, elements being searched for === elements searching
                // indexOf : list is unsorted and small
                // binarySearch : List is large and sorted

                // Collections.replaceAll()

                Collections.replaceAll(delegates, "Tech", "Marketing");

                // Collections.frequency

                System.out.println("Frequency of Tech: " + Collections.frequency(delegates, "Tech"));

                // Collections.min max

                // Collections.rotate

                // List<Delegate> delegates = Arrays.asList(medicalDelegate.toArray(new Delegate[0]));
                // order of rotated elements remains the same - positive number: End to start of list
                //  try with negative number in rotate - start to end of list


                // Collections.swap: Only need to do half for index: index < length/ 2 : index++

                // Collections.reverse - full reversal
                // System.out.println(delegates);

                // Java Hash Codes and Mastering Basics of Effective Collections
                // HashSet and HashMap based on hash codes: distribute elements to buckets- lookup time roughly halved
                // hashed collection creates limited set of buckets - any valid integer
                //  hasing mechansim takes integer hash code and capacity declaration with number of buckets to distro objects
                // maps hash codes to manageable range of bucket identifiers
                // combine hash code with other techniques to create efficient bucketing system

                // Hashing and Equality of Objects
                // Object.equals and .hashcode
                // == two variables have same reference to single object in memory
                // attribute values are equals - String overrides this - compares all strings

                String aText = "Hello";
                String bText = "Hello";
                String cText = String.join("l", "He", "lo");
                String dText = "He".concat("llo");
                String eText = "hello";

                List<String> hellos = Arrays.asList(aText, bText, cText, dText, eText); // takes variable argu list
                hellos.forEach(s -> System.out.println(s + ": " + s.hashCode()));

                Set<String> myset = new HashSet<>(hellos); // tracks duplicates by hashcode, any instance implementing collections to be passed to it
                System.out.println("mySet = " + myset);
                System.out.println("mySet.size(eText) " + myset.size());

                // Loop through List using classic for loop
                for (String setValue : myset) {
                    for (int i = 0; i < hellos.size(); i++) {
                        if (setValue == hellos.get(i)) {
                            System.out.println("Found " + setValue + " at index " + i);
                        }
                    }
                    System.out.println("=========================");
                }

                ScanningDelegate legalPrivacyPolicy = new ScanningDelegate("legal", "privacy policy");
                ScanningDelegate medicalCookiePolicy = new ScanningDelegate("medical", "cookie policy");
                ScanningDelegate financialTermsService = new ScanningDelegate("financial", "terms of service");

                List<ScanningDelegate> scanningDelegates = Arrays.asList(legalPrivacyPolicy, medicalCookiePolicy, financialTermsService);
                scanningDelegates.forEach(s -> System.out.println(s + ": " + s.hashCode()));
                Set<ScanningDelegate> group = new HashSet<>(scanningDelegates);
                for (ScanningDelegate sd : scanningDelegates) {
                    if (!group.add(sd)) {
                        System.out.println("Duplicate " + sd);

                    }
                }


                // Transforming collections into other collection types; viewable, immutable, checkable and empty collections

                String myString1 = "10-25-2025";

                String[] strings = {"One ", "Two", "Three"};
                var firstStream = Arrays.stream(strings).sorted(Comparator.reverseOrder());
                // forEach(System.out::println);

                var secondStream = Stream.of("Six", "Five", "four").map(String::toUpperCase);

                Stream.concat(firstStream, secondStream).map(s -> s.charAt(0) + "-" + s)
                        .forEach(System.out::println);

                Random rand = new Random();
                Stream.generate(() -> rand.nextInt(2)).limit(10).forEach(s -> System.out.print(s + " "));

                IntStream.iterate(1, n -> n + 1)
                        .filter(DemoApplication::isPrime) // need to implement
                        .limit(10)
                        .forEach(System.out::println);

                // overloaded intStream has finite parameter n -> n <=1000
                // range IntStream.range(1, 1000); exclusive 1 - 999
                // rangeClosed - inclusive 1 - 1000
                // infinite real number between two and double numbers

                // Filtering and Transforming data
                // distinct, limit, skip -> elements won't be in outgoing stream, takewhile, dropwhile -> dropped from stream not true
                IntStream.iterate((int) 'A', i -> i <= (int) 'Z', i -> i + 1)
                        .forEach(d -> System.out.printf("%c ", d));
                // arrays for any primitive types
                // type[] example int[ integerArray; or String courseList[]

                //GENERICS - arraylist populated with integers and not
                // Check warnings to not write bad java code
                System.out.println("Generics intro Doubled");
                ArrayList<Integer> items = new ArrayList<>();
                // raw type, old way for backwards compatibility
                // never use these raw types anymore because and
                // Use <> typed parameterized and fixes warnings
                items.add(1);
                items.add(2);
                // adding a string for example would cause a compile error
                items.add(3);

                printDoubled(items);
                // Generics - Useful to ensure program and mitigate runtime errors
                // Adding players to sporting teams

                MedicalDelegate md = new MedicalDelegate("medical");
                LegalDelegate legal = new LegalDelegate("legal");
                //FinancialDelegate financial = new FinancialDelegate("financial");

                Group<MedicalDelegate> scanGroup = new Group<MedicalDelegate>("scan group");
                scanGroup.addDelegate(md); // implemented with raw use of class, Type parameter,

                Group<LegalDelegate> scanLegalGroup = new Group<LegalDelegate>("scan legal group");
                scanLegalGroup.addDelegate(legal); // Generics allow compiler time type checking,

//        Group<FinancialDelegate> scanfinancialGroup = new Group<FinancialDelegate>("scan financial group");

                // scanfinancialGroup.addDelegate(financial); // Generics simply code, don'e have to do our own type checking

                System.out.println(scanGroup.numDelegates());

                // ensure particular team accepts correct delegate.
                // Java Generics: methods, classes, records, interfaces

                // class <T> type identifer
                // formatted message
                System.out.printf("%s %s %n", md, legal);
                /* Algorithms Practice
                 *  Greedy Example
                 * target - V cents n coins denominations
                 * coinValue[i] (in cents) for coin types i from [0..n-1]
                 * min number of cons we must use to represent V
                 * - unlimited supply of coins
                 * - coins penny, nickel, dime, quarter
                 * - V = 41
                 * - Solutions - continuously select largest coin denomination <= V, V - n, loop
                 * V=41 | 0
                 * V = 16 | 1 ( 41 - 25 = 16)
                 * V = 6 | 2 (16 - 10 = 6 )
                 * V = 1 | 3  (6 - 5 = 1
                 * V = 0 | 4  (1 - 1 = 0)
                 * */


                int[] numbers = {1, 2, 3, 4, 5};

                int[] arrayNumbers = new int[10]; //int[10]() compile error

                // reversing arrays swapping in place
                reverseArray(numbers);
            }


        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

        private static void displayStats(int period, Map<LocalDate, List<Purchase>> periodData) {
        System.out.println("-------------");
        Map<String, Integer> weeklyCounts = new TreeMap<>();
        periodData.forEach((key, value) -> {
            System.out.println(key + ": " + value);
            for (Purchase p : value) {
                weeklyCounts.merge(p.scanId(), 1, (prev, current) -> { // If the specified key is not already associated with a value or is associated with null, associates it with the given non-null value (optional operation). Otherwise, replaces the associated value with the results of the given remapping function, or removes if the result is null. This method may be of use when combining multiple mapped values for a key. For example, to either create or append a String msg to a value mapping:
                    return prev + current; // statement lambda could be replaced with expression lambda
                });
            }
        });
        System.out.printf("Week %d Purchases = %s%n", period, weeklyCounts);
    }
    private static void addPurchase(String name, Scan scan, double price) {
        Delegate existingDelegate = delegates.get(name);
        if(existingDelegate == null) {
            existingDelegate = new Delegate(name, scan);

        } else {
            existingDelegate.addScan(scan);
        }
        int day = new Random().nextInt(1) + 15; // increase + when increasing data
        String key = scan.scanId() + "_" + existingDelegate.getId();
        int year = LocalDate.now().getYear();
        Purchase purchase = new Purchase(scan.scanId(), existingDelegate.getId(), price, year, day);

        purchases.put(key, purchase);
        System.out.println("Purchase added " + purchase);
    }
    // gap in compiler type validation causing runtime classCastException between items and multiplying values
    private static void printDoubled(ArrayList<Integer> items) {
        for(int item : items) { // java autoboxing and unboxing
            System.out.println(item * 2); //need to tell java what item is in array list
        }
    }
    public static void reverseArray(int[] array) {
        int maxIndex = array.length -1;
        int halfLength = array.length / 2;
        for (int index = 0; index < halfLength; index++) {

            // set temp to current value of curr index
            int temp = array[index];
            System.out.println(temp);
            array[index] = array[maxIndex - index];  // set current array index value to array maxIndex-index (next value from end of array )
            System.out.println(array[index]);

            // set current index from end of array to current value of current index in loop
            array[maxIndex - index] = temp;
            System.out.println(array[maxIndex - index]);
        }

        System.out.println(Arrays.toString(array));

    }

    private static  int sumDigits(int number) {
        if(number < 10 ) {
            return -1;
        }
        int sum = 0;
        // 125 -> 125 / 10 = 12 -> 12 * 10 = 120 -> 125 - 120 = 5
        while (number>0) {
            int digit = number % 10;
            sum += digit;

            number /= 10;
        }

        return sum;
     }


    private static boolean isPrime(int i) {
        if (i <= 1) {
            return false;
        }
        for (int j = 2; j < i; j++) {
            if (i % j == 0) {
                return false;
            }
        }
        return true;
    }

}

/*
doubleStream, intStream, longStream,
function types
map collection set views, keyset, entryset: allows change of stream type
- map.entry to stream.string instances - regular practice
    - finite streams
Infinite Streams: Can limit [ intermediate op: filter, sorted, map and limit ]
- suppliers functional method returns a value but doesn't tke any args
-
terminal operations in pipeline - required for pipeline to be executed and complete
source elements consumed as needed
    input
terminal operation
    output
Pipeline - optimized for performance, evaluation performed from inputs to give projected outputs
- avoid side effects in intermediate operations

Errors - Stream has already been operated upon or closed

Seed - Intstream or stream [ java autobox int - less efficient than intstream ]
- unary operator - seed fed [ lambda expression ]

DS - arraylist, linkedlist, HashMap, TreeMap, HashSet, TreeSet,
 */
/*
mvnw
mvnw.cmd // used to run maven without installing on local machine
*/

/*
Problem - spring based apps = lot of configs
- 1. example mvc web - component scan, dispatcher servlet, view resolver, web jars ( static content )
- 2. hibernate/jpa - data source, entity manager factory/session factory, transaction manager, etc.
- example: dependencies - java/config/AppInitializer - dispacterServlet
webmvcConfig - Resolver
AppContext - dataSource, sessionFactory, TransactionManager

More config:
- 3. cache
- 4. database
- 5. message queue

Solution: Spring Auto Configuration: spring starter - autoconfig - configures Appcontext, AppInitializer, etc. based on pom configured from initializer
- JPA / hibernate example: no need for data source, entity manager factory, session factory and transaction manager
spring-boot-autoconfigure.jar

 -- corresponding autoconfigure classes enabled;
-- configure debug -
============================
CONDITIONS EVALUATION REPORT
============================

positive
    -aop enabled
    DispatcherServlet AutoConfigurations matched

     - @ConditionalOnClass({DispaccherServlet}) autoconfigure finds this class
     3. @ConditionalOnClass
The @ConditionalOnClass annotation creates the bean only if the specified class is present on the classpath. It is useful for creating beans that depend on the presence of a particular library or class. It helps in writing modular code that can be adapted based on the available classpath.

    EmbeddedWebServerFacotry
    - ErrorMvcAutoConfiguration -- provides default error page

    JacksonAutoConfiguration - converts - java to jsons
negative

- BatchAutoConfiguratin - did not match
- CacheAutoConfiguration
- CassandraAutoConfiguration


* */

/*
* EnableAutoConfiguration - aop classes
* @SpringBootApplication - allows spring bean definitions
* */

/*

Spring Rest, RestController, GetMapping, PostMapping, RequestMapping, ResponseMapping, etc..
 */