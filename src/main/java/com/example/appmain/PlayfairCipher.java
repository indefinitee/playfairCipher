package com.example.appmain;

import java.util.*;

public class PlayfairCipher {
    private String keyWord;
    private String plaintext;
    private String cipheredText;
    private String Language;

    // j -> i, FF -> FXF, АА -> АЯА

    // обработать ключ: убрал пробелы + повторяющиеся символы + замена символа
    static String cleanKey(String key) {
        StringBuilder tmp = new StringBuilder();
        key = key.replaceAll("\\s", "").toUpperCase();
        // в ключе заменили символы
        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) == 'J') {
                tmp.append("I");
            }
            else {
                tmp.append(key.charAt(i));
            }
        }

        LinkedHashSet<Character> set = new LinkedHashSet<>();
        // добавили в контейнер ключ
        for (int i = 0; i < tmp.length(); i++) {
            set.add(tmp.charAt(i));
        }

        StringBuilder cleanKey = new StringBuilder();
        Iterator<Character> it = set.iterator();
        while (it.hasNext()) {
            cleanKey.append((Character) it.next());
        }
        // новый ключ без повторений символов
        return cleanKey.toString();
    }

    // обработать слово
    static String cleanWord(String word) {
        String msg = "";
        word = word.replaceAll("\\s", "").toUpperCase();

        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == 'J') {
                msg += 'I';
            }
            else {
                msg += word.charAt(i);
            }
        }

        for (int i = 0; i < msg.length() - 1; i += 2) {
            if (msg.charAt(i) == msg.charAt(i + 1)) {
                if (msg.charAt(i) == 'X') {
                    msg = msg.substring(0, i + 1) + 'Z' + msg.substring(i + 1);
                }
                else {
                    msg = msg.substring(0, i + 1) + 'X' + msg.substring(i + 1 );
                }
            }
        }


        if (msg.length() % 2 != 0) {
            if (msg.charAt(msg.length() - 1) == 'X') {
                msg += 'Z';
            }
            else {
                msg += 'X';
            };
        }

        return msg;
    }

    static String cleanWordRu(String plaintext) {
        plaintext = plaintext.replaceAll("\\s", "").toUpperCase();
        String msg = plaintext;

        for (int i = 0; i < msg.length() - 1; i += 2) {
            if (msg.charAt(i) == msg.charAt(i + 1)) {
                if (msg.charAt(i) == 'Я') {
                    msg = msg.substring(0, i + 1) + 'Ъ' + msg.substring(i + 1);
                }
                else {
                    msg = msg.substring(0, i + 1) + 'Я' + msg.substring(i + 1 );
                }
            }
        }


        if (msg.length() % 2 != 0) {
            if (msg.charAt(msg.length() - 1) == 'Я') {
                msg += 'Ъ';
            }
            else {
                msg += 'Я';
            };
        }

        return msg;
    }

    static ArrayList<String> createBigramms(String word) {
        word = cleanWord(word);

        char[] k = word.toCharArray();

        ArrayList<String> bigramm = new ArrayList<>();

        for (int i = 0; i < word.length(); i += 2) {
            bigramm.add(word.substring(i, i + 2));
        }

        return bigramm;
    }


    static ArrayList<String> createBigrammsRu(String word) {
        word = cleanWordRu(word);

        char[] k = word.toCharArray();

        ArrayList<String> bigramm = new ArrayList<>();

        for (int i = 0; i < word.length(); i += 2) {
            bigramm.add(word.substring(i, i + 2));
        }

        return bigramm;
    }

    public static char[][] generateMatrixEn(String key){
        String alphabets = "ABCDEFGHIKLMNOPQRSTUVWXYZ";
        key = cleanKey(key);
        char[] a = alphabets.toCharArray();
        char[] k = key.toCharArray();
        String letters = "";

        for (char letter: k) {
            letters = letters.concat(String.valueOf(letter));
        }
        for (char letter: a) {
            if(!letters.contains(String.valueOf(letter))) {
                letters = letters.concat(String.valueOf(letter));
            }

        }
        char[] l = letters.toCharArray();
        char[][] matrix = new char[5][5];
        for (int i = 0; i < 5; i++) {
            System.arraycopy(l, 5 * i, matrix[i], 0, 5);
        }
        return matrix;
    }

    public static char[][] generateMatrixRu(String key){
        String alphabets = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ12345678910";
        key = cleanKey(key);
        char[] a = alphabets.toCharArray();
        char[] k = key.toCharArray();
        String letters =  "";

        for (char letter: k) {
            letters = letters.concat(String.valueOf(letter));
        }
        for (char letter: a) {
            if (!letters.contains(String.valueOf(letter))) {
                letters = letters.concat(String.valueOf(letter));
            }
        }

        char[] l = letters.toCharArray();
        char[][] matrix = new char[6][6];
        for (int i = 0; i < 6; i++) {
            System.arraycopy(l, 6 * i, matrix[i], 0, 6);
        }
        return matrix;
    }

    public static char[][] generateMatrixGeneral(String key, String lang) {
        if (lang.equals("RU")) {
            return generateMatrixRu(key);
        }
        else {
            return generateMatrixEn(key);
        }
    }


    public static String encrypt(String plaintext, char[][] matrix) {
        ArrayList<String> bigramms = createBigramms(plaintext);
        String encryptedText = "";
        int[][] pos = new int[2][2];
        for (String bigramm : bigramms) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    for (int k = 0; k < 2; k++) {
                        if (matrix[i][j] == bigramm.charAt(k)) {
                            pos[k][0] = i;
                            pos[k][1] = j;
                        }
                    }
                }
            }
        // смещение букв
            if (pos[0][0] == pos[1][0]) {
                pos[0][1] = (pos[0][1] + 1) % 5;
                pos[1][1] = (pos[1][1] + 1) % 5;
            } else if (pos[0][1] == pos[1][1]) {
                pos[0][0] = (pos[0][0] + 1) % 5;
                pos[1][0] = (pos[1][0] + 1) % 5;
            } else {
                int temp;
                temp = pos[0][1];
                pos[0][1] = pos[1][1];
                pos[1][1] = temp;
            }
            encryptedText = encryptedText.concat(String.valueOf(matrix[pos[0][0]][pos[0][1]]) + String.valueOf(matrix[pos[1][0]][pos[1][1]]));
        }
        return encryptedText;
    }

    public static String encryptRu(String plaintext, char[][] matrix) {
        ArrayList<String> bigramms = createBigrammsRu(plaintext);
        String encryptedText = "";
        int[][] pos = new int[2][2];
        for (String bigramm : bigramms) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    for (int k = 0; k < 2; k++) {
                        if (matrix[i][j] == bigramm.charAt(k)) {
                            pos[k][0] = i;
                            pos[k][1] = j;
                        }
                    }
                }
            }
            // смещение букв
            if (pos[0][0] == pos[1][0]) {
                pos[0][1] = (pos[0][1] + 1) % 6;
                pos[1][1] = (pos[1][1] + 1) % 6;
            } else if (pos[0][1] == pos[1][1]) {
                pos[0][0] = (pos[0][0] + 1) % 6;
                pos[1][0] = (pos[1][0] + 1) % 6;
            } else {
                int temp;
                temp = pos[0][1];
                pos[0][1] = pos[1][1];
                pos[1][1] = temp;
            }
            encryptedText = encryptedText.concat(String.valueOf(matrix[pos[0][0]][pos[0][1]]) + String.valueOf(matrix[pos[1][0]][pos[1][1]]));
        }
        return encryptedText;
    }


    public static String decrypt(String encryptedText, char[][] matrix) {
        ArrayList<String> bigramms = createBigramms(encryptedText);

        String plainText = "";

        int[][] pos = new int[2][2];
        for (String bigramm : bigramms) {
            for (int i = 0; i < 5; i++) {
                for (int j = 0; j < 5; j++) {
                    for (int k = 0; k < 2; k++) {
                        if (matrix[i][j] == bigramm.charAt(k)) {
                            pos[k][0] = i;
                            pos[k][1] = j;
                        }
                    }
                }
            }
            if (pos[0][0] == pos[1][0]) {
                pos[0][1] = (pos[0][1] - 1) % 5;
                if (pos[0][1] < 0) {
                    // если позиция -1
                    pos[0][1] += 5;
                }
                pos[1][1] = (pos[1][1] - 1) % 5;
                if (pos[1][1] < 0) {
                    // если позиция -1
                    pos[1][1] += 5;
                }
            } else if (pos[0][1] == pos[1][1]) {
                // буквы в одном столбце
                pos[0][0] = (pos[0][0] - 1) % 5;
                if (pos[0][0] < 0) {
                    // если позиция -1
                    pos[0][0] += 5;
                }
                pos[1][0] = (pos[1][0] - 1) % 5;
                if (pos[1][0] < 0) {
                    // If pos = -1
                    pos[1][0] += 5;
                }
            } else {
                int temp;
                temp = pos[0][1];
                pos[0][1] = pos[1][1];
                pos[1][1] = temp;
            }
            plainText = plainText.concat(String.valueOf(matrix[pos[0][0]][pos[0][1]]) + String.valueOf(matrix[pos[1][0]][pos[1][1]]));
        }
        return removeX(plainText);
    }

    public static String decryptRu(String encryptedText, char[][] matrix) {
        ArrayList<String> bigramms = createBigrammsRu(encryptedText);

        String plainText = "";

        int[][] pos = new int[2][2];
        for (String bigramm : bigramms) {
            for (int i = 0; i < 6; i++) {
                for (int j = 0; j < 6; j++) {
                    for (int k = 0; k < 2; k++) {
                        if (matrix[i][j] == bigramm.charAt(k)) {
                            pos[k][0] = i;
                            pos[k][1] = j;
                        }
                    }
                }
            }
            if (pos[0][0] == pos[1][0]) {
                pos[0][1] = (pos[0][1] - 1) % 6;
                if (pos[0][1] < 0) {
                    // если позиция -1
                    pos[0][1] += 6;
                }
                pos[1][1] = (pos[1][1] - 1) % 6;
                if (pos[1][1] < 0) {
                    // если позиция -1
                    pos[1][1] += 6;
                }
            } else if (pos[0][1] == pos[1][1]) {
                // буквы в одном столбце
                pos[0][0] = (pos[0][0] - 1) % 6;
                if (pos[0][0] < 0) {
                    // если позиция -1
                    pos[0][0] += 6;
                }
                pos[1][0] = (pos[1][0] - 1) % 6;
                if (pos[1][0] < 0) {
                    // If pos = -1
                    pos[1][0] += 6;
                }
            } else {
                int temp;
                temp = pos[0][1];
                pos[0][1] = pos[1][1];
                pos[1][1] = temp;
            }
            plainText = plainText.concat(String.valueOf(matrix[pos[0][0]][pos[0][1]]) + String.valueOf(matrix[pos[1][0]][pos[1][1]]));
        }
        return removeYA(plainText);
    }

    public static String removeX(String word) {
        String msg = word;

        for (int i = 1; i < word.length() - 1; i ++) {
            if (word.charAt(i) == 'X') {
                if (word.charAt(i - 1) == word.charAt(i + 1)) {
                    msg = word.substring(0, i) + ' ' + word.substring(i + 1);
                    i++;
                }
            }
        }
        if (msg.charAt(msg.length() - 1) == 'X') {
            msg = msg.substring(0, msg.length() - 1);
        }
        return msg.replaceAll("\\s", "");
    }

    public static String removeYA(String word){
        String msg = word;

        for (int i = 1; i < msg.length() - 1; i ++) {
            if (msg.charAt(i) == 'Я') {
                if (msg.charAt(i - 1) == msg.charAt(i + 1)) {
                    msg = msg.substring(0, i) + ' ' + msg.substring(i + 1);
                    i++;
                }
            }
        }
        if (msg.charAt(msg.length() - 1) == 'Я') {
            msg = msg.substring(0, msg.length() - 1);
        }
        return msg.replaceAll("\\s", "");
    }

    public static String encryptFinal(String plaintext, String language, String keyWord) {
        if (language.equals("RU")) {
            char[][] key = generateMatrixRu(keyWord);
            return encryptRu(plaintext, key);
        }
        else {
            char[][] key = generateMatrixEn(keyWord);
            return encrypt(plaintext, key);
        }
    }

    public static String decryptFinal(String plaintext, String language, String keyWord) {
        if (language.equals("RU")) {
            char[][] key = generateMatrixRu(keyWord);
            return decryptRu(plaintext, key);
        }
        else {
            char[][] key = generateMatrixEn(keyWord);
            return decrypt(plaintext, key);
        }
    }

}

