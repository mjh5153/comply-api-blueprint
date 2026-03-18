package com.init_spring_bean_mvn.demo.codeblocks;

public class CodeBlocks {
    public static void main(String[] args) {
        boolean gameOver = true;
        int score = 5000;
        int levelCompleted = 5;
        int bonus = 100;

        // replace below conditional with method
//        if(score == 5000 && score > 1000) {
//            System.out.println("Your score was 5000");
//        } else if(score > 1000) {
//            System.out.println("Your score was greater than 1000 but less than 5000");
//        } else {
//            System.out.println("Your score was less than 1000");
//        }

        int highScore = calculateScoreInt(true, score, levelCompleted, bonus);
        System.out.println("Your final score was " + calculateScoreInt(true, score, levelCompleted, bonus));
        // passing method call to println statement
        score = 2000;
        levelCompleted = 7;
        bonus = 200;
        String month = "January";
        getQuarter(month);

        //calculateScore(true, score, levelCompleted, bonus);
    }

    private static String getQuarter(String month) {

        return switch(month) {
            case  "JANUARY", "FEBRUARY", "MARCH" -> "1st";
            case "February" -> "2nd";
            // default ->  "bad";
            default -> {
                String response = "this is bad " + month;
                System.out.println("month: " + month);
                yield response;
            }
        };
    }

    public static void calculateScore(boolean gameOver, int score, int levelCompleted, int bonus) {

        int finalScore = score;
        if(gameOver) {
            finalScore += (levelCompleted * bonus);
            finalScore += 1000;
            System.out.println("Your final score was " + finalScore);
        }
    }

    public static int calculateScoreInt(boolean gameOver, int score, int levelCompleted, int bonus) {

        int finalScore = score;
        if(gameOver) {
            finalScore += (levelCompleted * bonus);
            finalScore += 1000;
            // System.out.println("Your final score was " + finalScore);
        }
        return finalScore;
    }

    public static void doSomething(int a) {
        a = 10;
    }

    public static void doSomething(int[] a) {
        a[0] = 10;
    }

    // switch statement - condition testing single variable state
    // Advanced switch - can be replaced with enhanced switch



}
