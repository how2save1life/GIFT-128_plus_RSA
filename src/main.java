/**
 * 系统安全课设
 * gift-128
 */
public class main {
    public static void main(String[] args) {
        String p = "fe dc ba 98 76 54 32 10 fe dc ba 98 76 54 32 10";
        String k = "fe dc ba 98 76 54 32 10 fe dc ba 98 76 54 32 10";
        gift g = new gift();
        g.giftEncrypt(p,k);

    }
}
