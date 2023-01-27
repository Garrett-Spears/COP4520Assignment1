import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Primes {

    private static boolean executeThreads(CalcPrimesThread[] threadArray, int numThreads) {
        int i;

        if (threadArray == null) {
            return false;
        }

        for (i = 0; i < numThreads; i++) {
            if (threadArray[i] != null) {
                threadArray[i].start();
            }
        }

        try {
            for (i = 0; i < numThreads; i++) {
                if (threadArray[i] != null) {
                    threadArray[i].join();
                }
            }
        }
        catch (InterruptedException e) {
            System.out.println("Error joining thread: " + e.toString());
            return false;
        }
        
        return true;
    }

    public static void main(String[] args) {
        int i, maxNum, numThreads;
        CalcPrimesThread[] threadArray;
        FileWriter fileWriter;

        maxNum = (int) Math.pow(10, 8);
        numThreads = 8;
        threadArray = new CalcPrimesThread[numThreads];

        for (i = 0; i < numThreads; i++) {
            threadArray[i] = new CalcPrimesThread(maxNum);
        }

        long startTime = System.currentTimeMillis();

        if (!executeThreads(threadArray, numThreads)) {
            System.out.println("Failed to execute threads properly so execution of program will be terminated");
            return;
        }

        long endTime = System.currentTimeMillis();

        // Calculate Execution Time
        double elapsedSeconds = (endTime - startTime) / 1000.0;

        // Get Total Number of Primes Found
        int totalPrimesFound = CalcPrimesThread.primesList.size();
        
        long sumOfAllPrimesFound = 0l;
        List<Integer> topTenPrimesFound = new ArrayList<>();

        // Iterate Through and Sum up All Prime Integers in List
        for (int prime : CalcPrimesThread.primesList) {
            sumOfAllPrimesFound += prime;
        }

        // Sort List of Primes then Get Last 10 Largest Primes in the Sorted List 
        Collections.sort(CalcPrimesThread.primesList);
        for (int j = Math.max(CalcPrimesThread.primesList.size() - 10, 0); j < CalcPrimesThread.primesList.size(); j++) {
            topTenPrimesFound.add(CalcPrimesThread.primesList.get(j));
        }

        try {
            fileWriter = new FileWriter("primes.txt");

            fileWriter.write("Execution Time: " + elapsedSeconds + " seconds\n");
            fileWriter.write("Total Number of Primes Found: " + totalPrimesFound + "\n");
            fileWriter.write("Sum of All Primes Found: " + sumOfAllPrimesFound + "\n");
            fileWriter.write("Top Ten Maximum Primes Found: ");

            for (i = 0; i < topTenPrimesFound.size(); i++) {
                fileWriter.write(topTenPrimesFound.get(i) + ((i == topTenPrimesFound.size() - 1) ? "\n" : ", "));
            }
            
            fileWriter.close();
        }
        catch (IOException e) {
            System.out.println("Error writing to file: " + e.toString());
        }
     }
}

class CalcPrimesThread extends Thread {
    private static AtomicInteger counter = new AtomicInteger(1);
    public static List<Integer> primesList = Collections.synchronizedList(new ArrayList<>());

    private int maxNum;

    public CalcPrimesThread(int maxNum) {
        this.maxNum = maxNum;
    }

    private boolean isPrime(int num) {
        int i;

        if (num <= 1)
            return false;
        else if (num == 2)
            return true;
        else if (num % 2 == 0)
            return false;

        for (i = 3; i * i <= num; i += 2) {
            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }

    public void run() {
        int current;

        while ((current = counter.getAndIncrement()) <= this.maxNum) {
            if (isPrime(current)) {
                synchronized(primesList) {
                    primesList.add(current);
                }
            }
        }
    }
}
