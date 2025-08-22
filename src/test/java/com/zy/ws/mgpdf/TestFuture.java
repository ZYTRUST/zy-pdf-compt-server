package com.zy.ws.mgpdf;

import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.concurrent.*;

@Slf4j
public class TestFuture {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        //testFuture();
        //testCompletable();
        //testFutureAsync();
        //testCompletableAsync();
        //testFutureCompletion();
        testCompletableFuture();
    }

    /**
     * This method waits if necessary for the computation to complete, and then retrieves its result.
     */
    public static void testFuture() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> stringFuture = executor.submit(TestFuture::neverEndingComputation);
        System.out.println("The result is: " + stringFuture.get());
    }
    public static String neverEndingComputation(){
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "ending computation";
    }

    /**
     * we are testing if stringCompletableFuture really has a value by using the method isDone()
     * which returns true if completed in any fashion: normally, exceptionally, or via cancellation.
     */
    public static void testCompletable() {
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.supplyAsync(TestFuture::neverEndingComputation);
        stringCompletableFuture.complete("Completed");
        System.out.println("Is the stringCompletableFuture done ? " + stringCompletableFuture.isDone());
    }

    /**
     *  the only way to retrieve the value is by using the get() of the Future method
     *  and by using it we block the main thread.
     */
    public static void testFutureAsync() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> firstApiCallResult = executor.submit(
                () -> firstApiCall("someValue")
        );

        String stringFirstApi = firstApiCallResult.get();
        Future<String> secondApiCallResult = executor.submit(
                () -> secondApiCall(stringFirstApi)
        );
        String stringSecondApi = secondApiCallResult.get();
    }
    public static String firstApiCall(String value){
        log.info("stringFirstApi {}",value);
        return "firstApi:"+value;
    }
    public static String secondApiCall(String value){
        log.info("stringSecondApi {}",value);
        return "SecondApi:"+value;
    }

    /**
     * By using the CompletableFuture class we don’t need to block the main thread anymore,
     * but we can asynchronously combine more operations:
     */
    public static void testCompletableAsync() {
        var finalResult = CompletableFuture.supplyAsync(
                        () -> firstApiCall("someValue")
                )
                .thenApply(TestFuture::secondApiCall);
    }

    /**
     * The only way to get the value is by using the get() method which blocks the thread until the result is returned:
     */
    public static void testFutureCompletion() throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> stringFuture = executor.submit(() -> "hello future");
        String uppercase = stringFuture.get().toUpperCase();
        System.out.println("The result is: " + uppercase);
    }

    /**
     * Using CompletableFuture we can now create a pipeline of asynchronous operations.
     * we realize that working with CompletableFuture is very similar to Java Streams.
     */
    public static void testCompletableFuture() {
        CompletableFuture.supplyAsync(() -> "hello completable future")
                .thenApply(String::toUpperCase)
                .thenAccept(System.out::println);
    }

}
