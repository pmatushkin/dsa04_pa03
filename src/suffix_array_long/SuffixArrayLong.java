import java.util.*;
import java.io.*;
import java.util.zip.CheckedInputStream;

public class SuffixArrayLong {
    final int ALPHABET_SIZE = 5;

    class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    public class Suffix implements Comparable {
        String suffix;
        int start;

        Suffix(String suffix, int start) {
            this.suffix = suffix;
            this.start = start;
        }

        @Override
        public int compareTo(Object o) {
            Suffix other = (Suffix) o;
            return suffix.compareTo(other.suffix);
        }
    }

    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.
    public int[] computeSuffixArray(String text) {
        int textLength = text.length();

//        int[] result = new int[textLength];

        // write your code here
        int[] result = SortCharacters(text);
        int[] class_ = ComputeCharClasses(text, result);
        int L = 1;

        while (L < textLength) {
            result = SortDoubled(text, L, result, class_);
            class_ = UpdateClasses(result, class_, L);

            L = 2 * L;
        }

        return result;
    }

    private int[] SortCharacters(String text) {
        int textLength = text.length();

        int[] result = new int[textLength];
        int[] count = new int[ALPHABET_SIZE];

        for (int i = 0; i < textLength; i++) {
            char symbolAt = text.charAt(i);
            int symbolIndex = letterToIndex(symbolAt);
            count[symbolIndex] = count[symbolIndex] + 1;
        }

        for (int i = 1; i < ALPHABET_SIZE; i++) {
            count[i] = count[i] + count[i - 1];
        }

        for (int i = textLength - 1; i >= 0; i--) {
            char symbolAt = text.charAt(i);
            int symbolIndex = letterToIndex(symbolAt);
            count[symbolIndex] = count[symbolIndex] - 1;
            result[count[symbolIndex]] = i;
        }

        return result;
    }

    private int[] ComputeCharClasses(String text, int[] order) {
        int textLength = text.length();

        int[] result = new int[textLength];
        for (int i = 1; i < textLength; i++) {
            if (text.charAt(order[i]) != text.charAt(order[i - 1])) {
                result[order[i]] = result[order[i - 1]] + 1;
            } else {
                result[order[i]] = result[order[i - 1]];
            }
        }

        return result;
    }

    private int[] SortDoubled(String text, int L, int[] order, int[] class_) {
        int textLength = text.length();

        int[] count = new int[textLength];
        int[] result = new int[textLength];

        for (int i = 0; i < textLength; i++) {
            count[class_[i]] = count[class_[i]] + 1;
        }

        for (int i = 1; i < textLength; i++) {
            count[i] = count[i] + count[i - 1];
        }

        for (int i = textLength - 1; i >= 0; i--) {
            int start = (order[i] - L + textLength) % textLength;
            int cl = class_[start];
            count[cl] = count[cl] - 1;
            result[count[cl]] = start;
        }

        return result;
    }

    private int[] UpdateClasses(int[] order, int[] class_, int L) {
        int textLength = order.length;

        int[] result = new int[textLength];

        for (int i = 1; i < textLength; i++) {
            int cur = order[i];
            int prev = order[i - 1];
            int mid = (cur + L) % textLength;
            int midPrev = (prev + L) % textLength;

            if ((class_[cur] != class_[prev]) || (class_[mid] != class_[midPrev])) {
                result[cur] = result[prev] + 1;
            } else {
                result[cur] = result[prev];
            }
        }

        return result;
    }

    int letterToIndex(char letter) {
        switch (letter) {
            case '$':
                return 0;
            case 'A':
                return 1;
            case 'C':
                return 2;
            case 'G':
                return 3;
            case 'T':
                return 4;
            default:
                assert (false);
                return -1;
        }
    }

    static public void main(String[] args) throws IOException {
        new SuffixArrayLong().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] suffix_array = computeSuffixArray(text);
        print(suffix_array);
    }
}
