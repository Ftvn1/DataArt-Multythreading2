/*  and the 2nd task is:
+ ≈сть два массива целых чисел по 1000 элементов.
+ «аполните массивы произвольными числами, 
+ затем отсортируйте массивы:
+ в первом потоке сортировкой сли€нием,
+ во втором потоке быстрой сортировкой.
+ ¬ третьем потоке найдите 
+ среднее арифметическое наибольших 10 элементов из первого потока, 
+ и наименьших 100 элементов из второго потока. 
+ “ак же в результе выполнени€ программы выведите 
+ врем€ работы быстрой сортировкой, 
+ и сортировкой сли€нием, 
и в отдельные файлы запишите отсортированные массивы. */
package dataArtsMultyThreading2;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
public class Main {

	public static int[] array1 = new int [1000];
	public static int[] array2 = new int [1000];
	
	public static void main(String[] args) {
		long start1;
		long start2;
		MyThread1 myThread1 = new MyThread1();
		MyThread2 myThread2 = new MyThread2();
		MyThread3 myThread3 = new MyThread3();
		Thread thread1 = new Thread(myThread1);
		Thread thread2 = new Thread(myThread2);
		Thread thread3 = new Thread(myThread3);
		start1 = System.currentTimeMillis();
		thread1.start();
		System.out.println("Merge sorting took " + (System.currentTimeMillis() - start1) + " ms,");
		start2 = System.currentTimeMillis();
		thread2.start();
		System.out.println("and fast sorting took " + (System.currentTimeMillis() - start2) + " ms");
		try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } 
		thread3.start();
		try {
            thread3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
		try(FileWriter writer = new FileWriter("result1.txt", false)) {
            String text = "And the first array after sorting looks like:";
            writer.write(text);
            for (int e : Main.array1) {
            	writer.write(e + " ");
            }
            text = "And the second array after sorting seems like: ";
            writer.write(text);
            for (int e : Main.array2) {
            	writer.write(e + " ");
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
	}
	public static void sortFast(int[] array2, int low, int high) {
        int [] array = array2;
		if (array.length == 0)
            return;
        if (low >= high)
            return;
        int middle = low + (high - low) / 2;
        int central = array[middle];
        int i = low, j = high;
        while (i <= j) {
            while (array[i] < central) {
                i++;
            }
            while (array[j] > central) {
                j--;
            }
            if (i <= j) {
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
                i++;
                j--;
            }
        }
        if (low < j)
            sortFast(array, low, j);
        if (high > i)
            sortFast(array, i, high);
    }

	public static boolean doesArrayNeedToBeSorted(int[] arrayInt) {
		boolean res = false;
		int transit = 0; 
		for (int k = 0; k < arrayInt.length-1; k++) {
				if (arrayInt[k] > arrayInt[k+1]) {
					transit = arrayInt[k];
					arrayInt[k] = arrayInt[k+1];
					arrayInt[k+1] = transit;
					res = true;
					break;
			}
		}
		return res;
	}

	public static int[] sortSliyaniem(int[] arrayInt) {
		int part1counter = 0;
		int part2counter = arrayInt.length/2;
		int resCounter = 0;
		int [] res = new int [arrayInt.length];
		while(part1counter < res.length/2 && part2counter < res.length) {
res[resCounter++] = arrayInt[part1counter] < arrayInt[part2counter] ? arrayInt[part1counter++] : arrayInt[part2counter++];
			}
		if (part1counter < res.length/2) {
		  for (int t = (part1counter+part2counter-arrayInt.length/2); t < res.length; t++) {
			  res[t] = arrayInt[part1counter++];
		  }
		} else if(part2counter < res.length) {
				for (int q = part1counter+part2counter-arrayInt.length/2; q < res.length; q++) {
				  res[q] = arrayInt[part2counter++];
				}			  
			}
		return res;
	}

	private static void printArray(int[] arrayInt) {
		for (int i = 0; i < arrayInt.length; i++) {
			if ( (i % 40)!= 39) System.out.print(arrayInt[i] + " ");
			else System.out.println (arrayInt[i] + " ");
		}
	}
	public static int [] fillIn(int[] arrayInt) {
		for (int i = 0; i < arrayInt.length; i++) {
			arrayInt[i] = (int)(Math.random()*1000); 
		}
		return arrayInt;
	}
}
class MyThread1 extends Thread {
	 boolean repeatingFlag = true;
	 @Override
	 public void run () {
		Main.fillIn (Main.array1);
		while (repeatingFlag) {
			Main.sortSliyaniem (Main.array1);
			repeatingFlag = Main.doesArrayNeedToBeSorted (Main.array1);
		}
    }
}
class MyThread2 extends Thread {
	int low = 0;
    int high = 999;
    @Override
    public void run() {
    	Main.fillIn (Main.array2);
    	Main.sortFast (Main.array2, low, high);
    }
}
class MyThread3 extends Thread {
	 int averageOfHighest10fromArray1 = 0;
	 int averageOfLowest100fromArray2 = 0;
	 @Override
     public void run() {
		for (int i = 990; i < 1000; i++) {
			averageOfHighest10fromArray1 += Main.array1[i]; 
		} 
		averageOfHighest10fromArray1 /= 10;
		for (int j = 0; j < 100; j++) {
			averageOfLowest100fromArray2 += Main.array2[j];
		} 
		averageOfLowest100fromArray2 /= 100;
		System.out.print ("Average of highest 10 from Array1 is " + averageOfHighest10fromArray1 + ", ");
		System.out.println ("and average of lowest 100 from Array2 is " + averageOfLowest100fromArray2 + ".");
		System.out.println ("Have a nice day!");
   	}
}