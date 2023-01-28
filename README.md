# COP4520Assignment1

Approach: 
    Since I am most familiar with Java, I decided to use this language for the assignment. In order to implement a concurrent program, I first created a custom thread class called "CalcPrimesThread" that inherits from the base Thread class in Java. This custom thread class contains two static member fields accessible by all "CalcPrimesThread" objects. The first of these static members is a synchronized ArrayList that is used to store all primes that all the threads find during execution. The second static member is an AtomicInteger which is used as a shared counter across all threads to keep track of what number needs to be processed next.
    Each "CalcPrimesThread" is assigned a maximum value in its constructor. In this program, all threads are assigned the same max value of 10^8. In the run() method of each thread, each thread gets the next number in line by using the AtomicInteger counter's getAndIncrement() method. This ensures that only one thread can access the value of the counter at a time and that this value is incremented before the thread releases control of the counter. The thread then checks if this number is prime by using an efficient isPrime() method that immediately returns false for all even numbers besides 2, skips over all even numbers when checking for divisibility, and only checks for possible divisors up to the square root of the number being checked for primality. If a thread finds a valid prime number, it enters a synchronized block and adds this prime number to the static synchronized ArrayList in the "CalcPrimesThread" class.
    To execute 8 concurrent threads in this program, I created an array of 8 "CalcPrimesThread" objects. I then call start() on all these 8 threads simulatenously and join() on all threads immediately after. This causes the Main thread to wait before continuing until all 8 threads finish execution.

Generating Output:
    I calculate the execution time of the program by recording the time right before all threads are started and the time right after all threads finish executing. To get the number of primes found in the program, I simply just access the size() of the list of primes generated by the concurrent threads. To get the sum of all primes found, I just iterate through all numbers in the list of primes found and add each prime integer to a running sum. To get the maximum 10 primes found, I sorted the list of all primes found and just accessed the last 10 prime numbers in this sorted list.
    
Design Correctness/Efficiency:
    On average, it takes about 4.7 seconds for all threads to start and finish execution on my laptop. Originally, I divided 10^8 into 8 separate ranges and assigned each thread a discrete range of numbers to process. Although the execution time of this approach was relatively low, it was still ineffecient since each thread assigned a range with higher numbers does more work than any thread assigned with lower number range. For example, it takes longer to determine whether or not 1,000,000 is prime in comparison to testing a smaller number like 11. 
    To address this inefficiency, I created a shared counter that each thread continues to access until the counter eventually reaches 10^8. This ensures that each thread instead does relatively the same amount of work, which improves the program's overall execution time.
    To prevent conflicting threads from accessing a shared resource at the same time, I used an AtomicInteger for the shared counter between threads and a synchronized ArrayList for the list of primes generated by all the threads. AtomicInteger is a thread safe class, so it is safe to use across multiple threads. To ensure that only one thread can add to the list of primes at a time, I have each add operation to this list contained inside a synchronized block. These thread safe resources may slow down the execution of the program by a few seconds, but they ensure that no resources are corrupted and that the correct output is generated.
    
Experimental Evaluation:
    To verify that my program generates correct output, I ran my program with some max values other than 10^8 and verified these outputs against online calculator outputs. Some of these test outputs generated by my program are listed below and can be verfied online.
        
        1 to 100:
            Primes Found = 25
            Sum Primes = 1060 
            Max 10 Primes = 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
        
        1 to 1,000:
            Primes Found = 168
            Sum Primes = 76127
            Max 10 Primes = 937, 941, 947, 953, 967, 971, 977, 983, 991, 997
        
        1 to 10,000:
            Primes Found = 1229
            Sum Primes = 5736396
            Max 10 Primes = 9887, 9901, 9907, 9923, 9929, 9931, 9941, 9949, 9967, 9973

To run this program: 
    1. Use the command prompt to navigate to the directory where the Primes.java file is located.
    2. Enter the command "javac Primes.java" on the command line to compile the java source code.
    3. Enter the command "java Primes" on the command line to execute the code.
    4. Open the primes.txt file generated by the program's execution. This text file contains all the required output for the program and is placed in the same directory as the "Primes.java" file.

