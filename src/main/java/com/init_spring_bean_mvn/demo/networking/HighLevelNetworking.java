package com.init_spring_bean_mvn.demo.networking;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

public class HighLevelNetworking {
    public static void main(String[] args) {
        // URI, { URL, URN }- subsets of uri

        //url - more specific type of url - identifies and locates resources on anet work - includes - protocol and address needed to access it
        // - pointer to resource on www - simple file or directory
        // can be query to db or search engine
        // [scheme:]shceme-specific-par[#fragment]

        //[authority]][path]

        URI baseSite = URI.create("https://learnprogramming.academy"); // can resolve to absoulte using base
        // URL not subclass to URI in Java - can use toURI on URL instance - not all uri's are urlsl
        // URI timsSite = URI.create("https://learnprogramming.academy/courses/complete-java-masterclass");
        URI timsSite = URI.create("courses/complete-java-masterclass");
        print(timsSite);

        try {
            URI uri = new URI("http://user:pw@store.com:5000/products/phones?os=android#samsung"); // doesn't have to be valid identifier
            // has checked exception
            print(uri);

            URI masterClass = baseSite.resolve(timsSite);
            URL url = masterClass.toURL();
            //URL url = uri.toURL();
            // URL url = timsSite.toURL(); // get error - URI is not absolute - means it's relative
            // reference to resource , webpage, file or image that's relative to current context
            // web dev - specify resources relative to location of specific resource - similar to file system path
            // root is implied if have / --- if ommitted that means it's relative to current directory
            // Can't get a relative url from uri

             // acces sresource you need to use a URL  -- needs to be absolute
            // Accesses lots of pages on websites - need baseURI that contains base info - if host location changes, only need to update uri
            // ex hhtp:example.com than changes to http:// example.org -- used relative uri's with base url -
            System.out.println(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
    // print component sparts in hierarchy
    private static void print(URI uri) {
        System.out.printf("""
                ------------------
                [scheme:]scheme-specific-par[#fragment]
                -----------------
                Scheme: %s
                Scheme-specific par: %s
                Authority: %s
                User infor: %s
                Host: %s
                Port: %d
               Path: %s
               Query: %s
              Fragment: %s
                """,
                uri.getScheme(),
                uri.getSchemeSpecificPart(),
                uri.getAuthority(),
                uri.getUserInfo(),
                uri.getHost(),
                uri.getPort(),
                uri.getPath(),
                uri.getQuery(),
                uri.getFragment());
    }

    // Absolute url
    private static void print(URL uri) {
        System.out.printf("""
                ------------------
                [scheme:]scheme-specific-par[#fragment]
                -----------------
              
                Authority: %s
                User infor: %s
                Host: %s
                Port: %d
               Path: %s
               Query: %s
              Fragment: %s
                """,
               // uri.getScheme(), // not available on url
                //uri.getSchemeSpecificPart(),
                uri.getAuthority(),
                uri.getUserInfo(),
                uri.getHost(),
                uri.getPort(),
                uri.getPath(),
                uri.getQuery() );
                // uri.getFragment());
    }
}
