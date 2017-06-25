/**
 * Created by Rick on 25-6-2017.
 */
public class Counter {
    public static void main(String[] args) {
        long sum = 0;
        for(int i = 0; i <= 160000; i++) {
            sum += i;
        }
        System.out.println(sum);
    }
}
