
public class giftUtil {
    /**
     * 明文字符串转换成十六进制字符串
     *
     * @param //String str 待转换的ASCII字符串
     * @return String 每个Byte之间无空格分隔，如: [61 6C 6B]=》[616C6B]
     */
    //明文字符串转换成十六进制字符串
    public String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        //System.out.println(str.getBytes());
        int bit;

        for (int i = 0; i < bs.length; i++) {
            //str = "a",bs[0]=97,bit=0110 0001 & 1111 0000 => 0110 =>6(hex),sb=6,bit=0110 0001 & 0000 1111  => 0001 =>1(hex),sb=61. char=>ascii=>hex
            //System.out.println(bs[i]);//bs[i] 存的相应字符的ascii码
            bit = (bs[i] & 0x0f0) >> 4;//bs[i] & 11110000 再向右移4位 相当于右移一位相当于算了16进制数中的高位。 除以2，右移n位，就相当于除以2^n。
            //System.out.println(bit);
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;////bs[i] & 11110000 算低位
            sb.append(chars[bit]);
            //sb.append(' ');
        }

        return sb.toString();
    }

    /**
     * 十六进制转换字符串
     *
     * @param //String str Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    //十六进制转换字符串
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();//转成char数组
        byte[] bytes = new byte[hexStr.length() / 2];//新建byte[], 1char=8bit=2hex=1byte
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;//16进制转10进制
            n += str.indexOf(hexs[2 * i + 1]);//16进制转10进制
            bytes[i] = (byte) (n & 0xff);//变2进制,存进byte[]
        }
        return new String(bytes);//返回string类型
    }

    //十六进制转换二进制字符串
    static String hexStr2BinStr(String hexStr) {
        hexStr = hexStr.toUpperCase();
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        StringBuilder binStr = new StringBuilder();
        for (char hex : hexs) {
            int dec = str.indexOf(hex);//ascii码
            StringBuilder temp = new StringBuilder(Integer.toBinaryString(dec));//10->2
            while (temp.length() < 4) {//补零
                temp.insert(0, "0");
            }
            binStr.append(temp);//连接
        }
        return binStr.toString();
    }

    //二进制字符串转换字符串
    public static String binStr2Str(String binStr) {
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < binStr.length() / 8; i++) {
            result.append((char) Integer.parseInt(binStr.substring(i * 8, (i + 1) * 8), 2));
        }
        return result.toString();
    }

    //二进制字符串转换十六进制字符串
    static String binStr2HexStr(String binStr) {
        String str = "0123456789ABCDEF";
        StringBuilder result = new StringBuilder("");
        for (int i = 0; i < binStr.length() / 4; i++) {
            result.append(str.charAt(Integer.parseInt(binStr.substring(i * 4, (i + 1) * 4), 2)));
        }
        return result.toString();
    }

    //循环左移位
    static String leftCyclicShift(String str, int n) {
        String a = str.substring(0, n);
        String b = str.substring(n, str.length());
        return b + a;
    }

    //异或 单个bit
    static String xor(char left, char right) {
        StringBuilder result = new StringBuilder("");
        if (left == right)
            result.append('0');
        else
            result.append('1');
        return result.toString();
    }

}
