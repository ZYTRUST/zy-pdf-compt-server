package com.zy.ws.mgpdf;

import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
public class TestCompletable {

    public static void main(String[] args) {
        //testSequence();
        //testParalell();
        testCompletableFuture();
    }

    /**
     * we are categorizing each transaction in sequence
     * and the time needed to categorize one transaction is one second.
     */
    public static void testSequence(){
        long start = System.currentTimeMillis();
        var categories = Stream.of(
                        new Transaction("1", "description 1"),
                        new Transaction("2", "description 2"),
                        new Transaction("3", "description 3"),
                        new Transaction("4", "description 4"),
                        new Transaction("5", "description 5"),
                        new Transaction("6", "description 6"),
                        new Transaction("7", "description 7"),
                        new Transaction("8", "description 8"),
                        new Transaction("9", "description 9"),
                        new Transaction("10", "description 10"))
                .map(CategorizationService::categorizeTransaction)
                .collect(toList());
        long end = System.currentTimeMillis();

        System.out.printf("The operation took %s ms%n", end - start);
        System.out.println("Categories are: " + categories);
    }

    /**
     * here we are using the parallel() method to parallelize the computation.
     * This solution can scale until we reach the limit of the number of processors.
     */
    public static void testParalell(){
        log.info("#procesadores {}",Runtime.getRuntime().availableProcessors());
        long start = System.currentTimeMillis();
        var categories = Stream.of(
                        new Transaction("1", "description 1"),
                        new Transaction("2", "description 2"),
                        new Transaction("3", "description 3"),
                        new Transaction("4", "description 4"),
                        new Transaction("5", "description 5"),
                        new Transaction("6", "description 6"),
                        new Transaction("7", "description 7"),
                        new Transaction("8", "description 8"),
                        new Transaction("9", "description 9"),
                        new Transaction("10", "description 10"),
                        new Transaction("11", "description 11"),
                        new Transaction("12", "description 12"),
                        new Transaction("13", "description 13"),
                        new Transaction("14", "description 14"),
                        new Transaction("15", "description 15"),
                        new Transaction("16", "description 16"),
                        new Transaction("17", "description 17"))
                .parallel()
                .map(CategorizationService::categorizeTransaction)
                .collect(toList());
        long end = System.currentTimeMillis();

        System.out.printf("The operation took %s ms%n", end - start);
        System.out.println("Categories are: " + categories);
    }

    public static void testCompletableFuture(){
        Executor executor = Executors.newFixedThreadPool(20);
        log.info("#procesadores {}",Runtime.getRuntime().availableProcessors());
        long start = System.currentTimeMillis();
        var futureCategories = Stream.of(
                        new Transaction("1", "description 1"),
                        new Transaction("2", "description 2"),
                        new Transaction("3", "description 3"),
                        new Transaction("4", "description 4"),
                        new Transaction("5", "description 5"),
                        new Transaction("6", "description 6"),
                        new Transaction("7", "description 7"),
                        new Transaction("8", "description 8"),
                        new Transaction("9", "description 9"),
                        new Transaction("10", "description 10"),
                        new Transaction("11", "description 11"),
                        new Transaction("12", "description 12"),
                        new Transaction("13", "description 13"),
                        new Transaction("14", "description 14"),
                        new Transaction("15", "description 15"),
                        new Transaction("16", "description 16"),
                        new Transaction("17", "description 17"))
                .map(transaction -> CompletableFuture.supplyAsync(
                        () -> CategorizationService.categorizeTransaction(transaction), executor)
                )
                .collect(toList());
        var categories = futureCategories.stream()
                .map(CompletableFuture::join)
                .collect(toList());

        long end = System.currentTimeMillis();

        System.out.printf("The operation took %s ms%n", end - start);
        System.out.println("Categories are: " + categories);
    }
}



/********************************************************************/
class CategorizationService {

    public static Category categorizeTransaction(Transaction transaction) {
        delay();
        return new Category("Category_" + transaction.getId());
    }

    public static void delay() {
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

class Category {
    private final String category;

    public Category(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "Category{" +
                "category='" + category + '\'' +
                '}';
    }
}

class Transaction {
    private String id;
    private String description;

    public Transaction(String id, String description) {
        this.id = id;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}