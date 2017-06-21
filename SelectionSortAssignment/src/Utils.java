import java.io.*;
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

    // https://stackoverflow.com/questions/3736058/java-object-to-byte-and-byte-to-object-converter-for-tokyo-cabinet
    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }

    // Custom utils
    public static boolean isDataSetSorted(int[] dataSet, int expectedElementCount) {
        if (dataSet.length != expectedElementCount) return false;
        for (int i = 0; i < dataSet.length - 1; i++) {
            if (dataSet[i]+1 != dataSet[i + 1]) {
                System.out.println(dataSet[i]+1);
                System.out.println(i);
                return false;
            }
        }
        return true;
    }

    public static int[] generateRandomDataSet(int numElements) {
        int[] dataSet = Utils.fillArray(numElements);
        Utils.shuffleArray(dataSet);
        System.out.println(" ");

        return dataSet;
    }
}