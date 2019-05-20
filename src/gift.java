import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class gift {
    //sbox
    private static Map<String, String> sBox = new HashMap<String, String>();

    //sbox_reverse
    private static Map<String, String> sBox_reverse = new HashMap<String, String>();

    //pbox
    private static int pBox[] = new int[128];

    //轮常数
    private static String roundConstants[] = new String[48];

    private void setsBox() {
        sBox.put("0", "1");
        sBox.put("1", "A");
        sBox.put("2", "4");
        sBox.put("3", "C");
        sBox.put("4", "6");
        sBox.put("5", "F");
        sBox.put("6", "3");
        sBox.put("7", "9");
        sBox.put("8", "2");
        sBox.put("9", "D");
        sBox.put("A", "B");
        sBox.put("B", "7");
        sBox.put("C", "5");
        sBox.put("D", "0");
        sBox.put("E", "8");
        sBox.put("F", "E");
    }

    private void setpBox() {
        pBox = new int[]{
                0, 33, 66, 99, 96, 1, 34, 67, 64, 97, 2, 35, 32, 65, 98, 3,
                4, 37, 70, 103, 100, 5, 38, 71, 68, 101, 6, 39, 36, 69, 102, 7,
                8, 41, 74, 107, 104, 9, 42, 75, 72, 105, 10, 43, 40, 73, 106, 11,
                12, 45, 78, 111, 108, 13, 46, 79, 76, 109, 14, 47, 44, 77, 110, 15,
                16, 49, 82, 115, 112, 17, 50, 83, 80, 113, 18, 51, 48, 81, 114, 19,
                20, 53, 86, 119, 116, 21, 54, 87, 84, 117, 22, 55, 52, 85, 118, 23,
                24, 57, 90, 123, 120, 25, 58, 91, 88, 121, 26, 59, 56, 89, 122, 27,
                28, 61, 94, 127, 124, 29, 62, 95, 92, 125, 30, 63, 60, 93, 126, 31};
    }

    private void setRoundConstants() {
        roundConstants = new String[]{
                "01", "03", "07", "0F", "1F", "3E", "3D", "3B", "37", "2F", "1E", "3C", "39", "33", "27", "0E",
                "1D", "3A", "35", "2B", "16", "2C", "18", "30", "21", "02", "05", "0B", "17", "2E", "1C", "38",
                "31", "23", "06", "0D", "1B", "36", "2D", "1A", "34", "29", "12", "24", "08", "11", "22", "04"
        };
    }

    private void setsBox_reverse() {
        sBox_reverse.put("1", "0");
        sBox_reverse.put("A", "1");
        sBox_reverse.put("4", "2");
        sBox_reverse.put("C", "3");
        sBox_reverse.put("6", "4");
        sBox_reverse.put("F", "5");
        sBox_reverse.put("3", "6");
        sBox_reverse.put("9", "7");
        sBox_reverse.put("2", "8");
        sBox_reverse.put("D", "9");
        sBox_reverse.put("B", "A");
        sBox_reverse.put("7", "B");
        sBox_reverse.put("5", "C");
        sBox_reverse.put("0", "D");
        sBox_reverse.put("8", "E");
        sBox_reverse.put("E", "F");
    }

    gift() {
        setsBox();
        setsBox_reverse();
        setpBox();
        setRoundConstants();
    }

    //加密                       hex      bin
    String giftEncrypt(String Plain, String Key) {
        String keys[];
        String plain = Plain.replace(" ", "");
        keys = updateKey(Key.replace(" ", ""));


        System.out.println(plain);
        for (int i = 0; i < 48; i++) {
            String s = getSubCells(plain, 'E');
            System.out.println(i + ":   SubCells: " + s);
            String p = getPermBits(s, 'E');
            System.out.println(i + ":   PermBits: " + giftUtil.binStr2HexStr(p));
            plain = addRoundKey(keys[i], p, gift.roundConstants[i]);
            System.out.println(i + ":AddRoundKey: " + plain);
            System.out.println(i + ":  UpdateKey: " + giftUtil.binStr2HexStr(keys[i + 1]));
            System.out.println("\n");
        }
        return plain;
    }

    //解密                hex             bin
    String giftDecrypt(String Cipher, String Key) {
        String keys[];
        String cipher = giftUtil.hexStr2BinStr(Cipher.replace(" ", ""));
        keys = updateKey(Key.replace(" ", ""));

        for (int i = 47; i >= 0; i--) {
            String a = addRoundKey(keys[i], cipher, gift.roundConstants[i]);
            System.out.println(i + ":AddRoundKey: " + a);
            String p = getPermBits(a, 'D');
            System.out.println(i + ":   PermBits: " + giftUtil.binStr2HexStr(p));
            cipher = getSubCells(p, 'D');
            System.out.println(i + ":   SubCells: " + cipher);
            cipher = giftUtil.hexStr2BinStr(cipher);//s盒得到一个16进制string，addroundkey需要2进制
            System.out.println(i + ":  UpdateKey: " + giftUtil.binStr2HexStr(keys[i]));
            System.out.println("\n");
        }
        return giftUtil.binStr2HexStr(cipher);//hex
    }

    /**
     * @param str 16进制
     * @return 16进制
     */
    //s盒替换
    private String getSubCells(String str, char mode) {
        str = str.toUpperCase();//大写统一
        StringBuilder result = new StringBuilder(String.format("%32s", " "));
        if (mode == 'D') {//解密的话把p盒的结果（bin）转hex
            str = giftUtil.binStr2HexStr(str);
        }

        for (int i = 0; i < str.length(); i++) {//对每个16进制数
            //System.out.println("p");
            if (mode == 'E') {//加密轮
                for (String key : sBox.keySet()) {//如果该16进制数符合s盒
                    String value = sBox.get(key);//得到sbox[str[i]]
                    if (str.charAt(i) == key.charAt(0)) {//如果找到
                        //System.out.println("sbox value:" + value);
                        result.replace(i, i + 1, value);//result[i]的值
                        break;
                    }
                }
            } else if (mode == 'D') {//解密轮
                for (String key : sBox_reverse.keySet()) {//如果该16进制数符合s盒
                    String value = sBox_reverse.get(key);//得到sBox_reverse[str[i]]
                    if (str.charAt(i) == key.charAt(0)) {//如果找到
                        //System.out.println("sbox value:" + value);
                        result.replace(i, i + 1, value);//result[i]的值
                        break;
                    }
                }
            }

        }
        //System.out.println("test sbox:" + result);
        return result.toString();//得到经过s盒后的16进制string
    }

    /**
     * 需要逆置
     *
     * @param hexStr
     * @return 2进制
     */
    //p盒替换//127...0
    private String getPermBits(String hexStr, char mode) {
        StringBuilder binStr = new StringBuilder(giftUtil.hexStr2BinStr(hexStr));//16转2
        binStr.reverse();//逆置
        StringBuilder result = new StringBuilder(String.format("%128s", " "));//128长度的字符串 用于存结果
        //System.out.println(result.length());
        for (int i = 0; i < 128; i++) {//p置换
            //System.out.println(result);
            if (mode == 'E') {//加密
                result.replace(pBox[i], pBox[i] + 1, String.valueOf(binStr.charAt(i)));
            } else if (mode == 'D')//解密
            {
                result.replace(i, i + 1, String.valueOf(binStr.charAt(pBox[i])));
            }

        }
        result.reverse();//再次逆置
        //System.out.println("p box:" + result);
        //System.out.println("p box hex:" + giftUtil.binStr2HexStr(result.toString()));
        //return giftUtil.binStr2HexStr(result.toString());
        return result.toString();
    }

    /**
     * @param KEY   bin
     * @param STATE bin
     * @param C     hex
     * @return bin
     */
    //轮密钥加          bin             bin         hex
    private String addRoundKey(String KEY, String STATE, String C) {
        //处理密钥
        StringBuilder key = new StringBuilder(KEY);
        key.reverse();//逆置
        String keys[] = new String[8];
        for (int i = 0; i < 8; i++) {//k分成8组，每组16bit长
            keys[i] = key.substring(i * 16, (i + 1) * 16);//分组得到k0~k7
            //System.out.println(keys[i]);
        }

        //得到u、v
        StringBuilder u = new StringBuilder("");
        u.append(keys[4]).append(keys[5]);
        StringBuilder v = new StringBuilder("");
        v.append(keys[0]).append(keys[1]);

        //处理p盒得来的文本 逆置
        StringBuilder state = new StringBuilder(STATE);
        state.reverse();

        //处理轮常数 逆置
        StringBuilder c = new StringBuilder(giftUtil.hexStr2BinStr(C).substring(2, 8));
        c.reverse();

        //储存结果
        StringBuilder result = new StringBuilder(state);

        //进行轮密钥加
        //轮密钥加
        for (int i = 0; i < 32; i++) {
            result.replace(4 * i + 2, 4 * i + 3, giftUtil.xor(state.charAt(4 * i + 2), u.charAt(i)));
            result.replace(4 * i + 1, 4 * i + 2, giftUtil.xor(state.charAt(4 * i + 1), v.charAt(i)));
        }

        //轮常数加
        result.replace(127, 128, giftUtil.xor(state.charAt(127), '1'));
        result.replace(23, 24, giftUtil.xor(state.charAt(23), c.charAt(5)));
        result.replace(19, 20, giftUtil.xor(state.charAt(19), c.charAt(4)));
        result.replace(15, 16, giftUtil.xor(state.charAt(15), c.charAt(3)));
        result.replace(11, 12, giftUtil.xor(state.charAt(11), c.charAt(2)));
        result.replace(7, 8, giftUtil.xor(state.charAt(7), c.charAt(1)));
        result.replace(3, 4, giftUtil.xor(state.charAt(3), c.charAt(0)));

        //逆置
        result.reverse();

        //System.out.println("add round key:" + giftUtil.binStr2HexStr(result.toString()));
        return giftUtil.binStr2HexStr(result.toString());
    }

    /**
     * @param KEY 输入的原始密钥
     * @return
     */
    //密钥更新 hex bin
    private String[] updateKey(String KEY) {
        KEY = KEY.toUpperCase();
        String K[] = new String[49];//存储轮密钥 bin
        StringBuilder key = new StringBuilder(giftUtil.hexStr2BinStr(KEY));//16转2
        K[0] = key.toString();

        key.reverse();//逆置

        String keys[] = new String[8];//k分成8组，每组16bit长
        for (int i = 0; i < 8; i++) {//分组得到k0~k7
            keys[i] = key.substring(i * 16, (i + 1) * 16);
            //System.out.println("update key 第一个key分组 " + i + ":" + keys[i]);
        }

        for (int i = 1; i < 49; i++) {
            StringBuilder temp = new StringBuilder("");
            //更新密钥（逆序）
            temp.append(keys[2]).append(keys[3]).append(keys[4]).append(keys[5]).append(keys[6]).append(keys[7])
                    .append(giftUtil.leftCyclicShift(keys[0], 12)).append(giftUtil.leftCyclicShift(keys[1], 2));

            key = new StringBuilder(temp);//得到新key（逆序）

            temp.reverse();//逆序得到真实的轮密钥

            for (int j = 0; j < 8; j++) {//再次分组得到k0~k7
                keys[j] = key.substring(j * 16, (j + 1) * 16);
                //System.out.println(keys[j]);
            }

            K[i] = temp.toString();//存下该轮更新的密钥
            //System.out.println("update key 该轮更新的密钥 " + i + ":" + giftUtil.binStr2HexStr(K[i]));
        }

        return K;
    }
}
