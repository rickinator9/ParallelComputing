import java.util.concurrent.ThreadLocalRandom;

class Utils {

    // source: http://stackoverflow.com/questions/1519736/random-shuffling-of-an-array
    // Implementing Fisher�Yates shuffle
    public static void shuffleArray(int[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        ThreadLocalRandom rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    public static void printArray(int[] anArray) {
        System.out.print("Array: ");
        for (int i=0; i< anArray.length; i++){
            System.out.print(anArray[i]+" ");
        }
        System.out.println();
    }


    public static int[] fillArray(int amount) {
        int[] result = new int[amount];
        for (int i=0; i<amount; i++){
            result[i] = i;
        }
        return result;
    }

    public static void addValue(int[] anArray, int value) {
        for (int i=0; i<anArray.length; i++){
            anArray[i] += value;
        }
    }
}