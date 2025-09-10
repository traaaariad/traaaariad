import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // Read number of VIP MAC addresses
        int n = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        // Store VIP MAC patterns
        List<VIPPattern> vipPatterns = new ArrayList<>();
        
        // Read VIP MAC addresses with masks
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine().trim();
            VIPPattern pattern = parseVIPPattern(line);
            vipPatterns.add(pattern);
        }
        
        // Read number of MAC addresses to check
        int m = scanner.nextInt();
        scanner.nextLine(); // consume newline
        
        // Check each MAC address
        for (int i = 0; i < m; i++) {
            String macAddress = scanner.nextLine().trim();
            long macValue = parseMACAddress(macAddress);
            
            boolean isVIP = false;
            for (VIPPattern pattern : vipPatterns) {
                if (matchesPattern(macValue, pattern)) {
                    isVIP = true;
                    break;
                }
            }
            
            System.out.println(isVIP ? "YES" : "NO");
        }
        
        scanner.close();
    }
    
    /**
     * Parse a VIP pattern in format xx-xx-xx-xx-xx-xx/M
     */
    private static VIPPattern parseVIPPattern(String input) {
        String[] parts = input.split("/");
        String macStr = parts[0];
        int maskLength = Integer.parseInt(parts[1]);
        
        long macValue = parseMACAddress(macStr);
        long mask = createMask(maskLength);
        
        return new VIPPattern(macValue, mask);
    }
    
    /**
     * Parse MAC address from xx-xx-xx-xx-xx-xx format to long value
     */
    private static long parseMACAddress(String macStr) {
        String[] parts = macStr.split("-");
        long result = 0;
        
        for (int i = 0; i < parts.length; i++) {
            long byteValue = Long.parseLong(parts[i], 16);
            result = (result << 8) | byteValue;
        }
        
        return result;
    }
    
    /**
     * Create a mask with the specified number of leading 1 bits
     * For example, maskLength=24 creates mask with first 24 bits set to 1
     */
    private static long createMask(int maskLength) {
        if (maskLength == 0) {
            return 0L;
        }
        if (maskLength >= 48) {
            return 0xFFFFFFFFFFFFL; // All 48 bits set
        }
        
        return ((1L << maskLength) - 1) << (48 - maskLength);
    }
    
    /**
     * Check if a MAC address matches a VIP pattern
     */
    private static boolean matchesPattern(long macValue, VIPPattern pattern) {
        return (macValue & pattern.mask) == (pattern.macValue & pattern.mask);
    }
    
    /**
     * Class to represent a VIP pattern with MAC address and mask
     */
    private static class VIPPattern {
        long macValue;
        long mask;
        
        public VIPPattern(long macValue, long mask) {
            this.macValue = macValue;
            this.mask = mask;
        }
    }
}