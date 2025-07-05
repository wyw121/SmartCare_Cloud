/**
 * JWT密钥长度测试
 */
public class JwtKeyTest {
    public static void main(String[] args) {
        String secret = "smartcare-cloud-jwt-secret-key-2024-secure-enough-for-hs512-algorithm-minimum-64-bytes-required";
        byte[] keyBytes = secret.getBytes();
        
        System.out.println("JWT密钥: " + secret);
        System.out.println("密钥长度: " + keyBytes.length + " 字节 (" + (keyBytes.length * 8) + " 位)");
        System.out.println("HS512要求: 至少64字节 (512位)");
        
        if (keyBytes.length >= 64) {
            System.out.println("✓ 符合HS512算法安全要求");
        } else {
            System.out.println("✗ 不符合HS512算法安全要求");
        }
    }
}
