package server.utils;

/**
 * @author sowhile
 * <p>
 * 2022/12/1 0:35
 */
public class TestUtils {
    public static void main(String[] args) {
        String password = "wd0531";
        long start = System.currentTimeMillis();
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(5));
        System.out.println(hashed);
        String candidate = "wd0531";
        if (BCrypt.checkpw(candidate, hashed))
            System.out.println("It matches");
        else
            System.out.println("It does not match");
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
