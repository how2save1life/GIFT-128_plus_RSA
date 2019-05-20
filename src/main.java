/**
 * 系统安全课设
 * gift-128
 */
public class main {
    public static void main(String[] args) {
        String p = "fe dc ba 98 76 54 32 10 fe dc ba 98 76 54 32 10";
        String k = "fe dc ba 98 76 54 32 10 fe dc ba 98 76 54 32 10";
        gift g = new gift();
        String E = g.giftEncrypt(p, k);
        System.out.println("加密结果:" + E+"\n");
        System.out.println("-------------------------------------------------------------------------------");
        String D = g.giftDecrypt(E, k);
        System.out.println("解密结果:" + D+"\n");

    }
}
