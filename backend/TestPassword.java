import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String rawPassword = "123456";
        String hash1 = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH";
        String hash2 = "$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iufvOpHm";
        
        System.out.println("Testing password: " + rawPassword);
        System.out.println("\nHash1 (admin2): " + hash1);
        System.out.println("Match result: " + encoder.matches(rawPassword, hash1));
        
        System.out.println("\nHash2 (old admin): " + hash2);
        System.out.println("Match result: " + encoder.matches(rawPassword, hash2));
        
        System.out.println("\nGenerate new hash:");
        String newHash = encoder.encode(rawPassword);
        System.out.println(newHash);
        System.out.println("New hash match: " + encoder.matches(rawPassword, newHash));
    }
}
