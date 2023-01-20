import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Primes {
    private static boolean executeThreads(CalcPrimesThread[] threadArray) {
        if (threadArray == null || threadArray.length != 8) {
            return false;
        }

        threadArray[0].start();
        threadArray[1].start();
        threadArray[2].start();
        threadArray[3].start();
        threadArray[4].start();
        threadArray[5].start();
        threadArray[6].start();
        threadArray[7].start();

        try {
            threadArray[0].join();
            threadArray[1].join();
            threadArray[2].join();
            threadArray[3].join();
            threadArray[4].join();
            threadArray[5].join();
            threadArray[6].join();
            threadArray[7].join();
        }
        catch (InterruptedException e) {
            System.out.println("Error joining thread: " + e.toString());
            return false;
        }
        
        return true;
    }

    public static void main(String[] args) {
        int i, j, calcRange;
        List<List<Integer>>primeLists;
        List<Long> sumPrimesList;
        CalcPrimesThread[] threadArray;
        FileWriter fileWriter;

        primeLists = new ArrayList<>();
        sumPrimesList = new ArrayList<>();
        calcRange = (int) Math.pow(10, 8) / 8;
        threadArray = new CalcPrimesThread[8];

        for (i = 0; i < 8; i++) {
            primeLists.add(new ArrayList<>());
            threadArray[i] = new CalcPrimesThread(i * calcRange + 1, i * calcRange + calcRange, primeLists.get(i), sumPrimesList);
        }

        long startTime = System.currentTimeMillis();

        if (!executeThreads(threadArray)) {
            System.out.println("Failed to execute threads properly so execution of program will be terminated");
            return;
        }

        long endTime = System.currentTimeMillis();

        // Calculate Execution Time
        double elapsedSeconds = (endTime - startTime) / 1000.0; 

        // Calculate Total Number of Primes Found
        int totalPrimesFound = 0;
        for (List<Integer> primeList : primeLists) {
            totalPrimesFound += primeList.size();
        }

        // Calculate Sum of All Primes Found
        long sumOfAllPrimesFound = 0L;
        for (long sum : sumPrimesList) {
            sumOfAllPrimesFound += sum;
        }

        // Pick out Top Ten Maximum Primes Found
        List<Integer> topTenPrimesFound = new ArrayList<>();
        for (i = primeLists.size() - 1; topTenPrimesFound.size() < 10 && i >= 0; i--) {
            List<Integer> currPrimeList = primeLists.get(i);
            for (j = currPrimeList.size() - 1; topTenPrimesFound.size() < 10 && j >= 0; j--) {
                topTenPrimesFound.add(currPrimeList.get(j));
            }
        }
        Collections.reverse(topTenPrimesFound);

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
    private int start, end;
    private List<Integer> primeList;
    private List<Long> sumList;

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

    public CalcPrimesThread(int start, int end, List<Integer> primeList, List<Long> sumList) {
        this.start = start;
        this.end = end;
        this.primeList = primeList;
        this.sumList = sumList;
    }

    public void run() {
        int i;
        long sumPrimes = 0;

        for (i = start; i <= end; i++) {
            if (isPrime(i)) {
                primeList.add(i);
                sumPrimes += i;
            }
        }

        sumList.add(sumPrimes);
    }
}