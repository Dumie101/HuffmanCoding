//package D101;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;

public class Huffman {
    public static void encode()throws IOException{
        // initialize Scanner to capture user input
        Scanner sc = new Scanner(System.in);

        // capture file information from user and read file
        System.out.print("Enter the filename to read from/encode: ");
        String f = sc.nextLine();

        // create File object and build text String
        File file = new File(f);
        Scanner input = new Scanner(file).useDelimiter("\\z");
        String text = input.next();

        // close input file
        input.close();

        // initialize Array to hold frequencies (indices correspond to
        // ASCII values)
        int[] freq = new int[256];
        // concatenate/sanitize text String and create character Array
        // nice that \\s also consumes \n and \r
        // we can add the whitespace back in during the encoding phase
        char[] chars = text.replaceAll("\\s", "").toCharArray();
        // a char array to be used when printing the encoded version of the string//
        char[] charsForPrinting = text.toCharArray();

        // count character frequencies
        for(char c: chars)
            freq[c]++;

        // Adds the symbol and prob into an Arraylist of pairs//
        ArrayList<Pair> pairs = new ArrayList<>();
        for(int i = 0; i<256; i++){
            if(freq[i]!=0){
                // this method of rounding is good enough
                Pair p = new Pair((char)i, Math.round(freq[i]*10000d/chars.length)/10000d);
                pairs.add(p);
            }
        }
        //Sorts the ArrayList from least to greatest//
        Collections.sort(pairs);
        // S and T "queue" (ArrayList that does the function of the queue"//
        ArrayList< BinaryTree<Pair> > S = new ArrayList<>();
        ArrayList< BinaryTree<Pair> > T = new ArrayList<>();

        //Makes each pair into a node//
        for (Pair pair : pairs) {
            BinaryTree<Pair> val = new BinaryTree<>();
            val.makeRoot(pair);
            S.add(val);
        }

        /* Logic to determine node A and B (1): If S empty ---> then, remove the first two from T queue becoming node A and B
        * If S not empty --> then, find the node with the lowest prob between the first element of S and T that will become A
        * now depending on if A was chosen from T or S, B is either the first element of T or S because S or T became empty or the first element of S or T
        * has a lower probability
        *
        * Logic to create huffman Tree (2): Then the probablity of A and B becomes the root value of A and B and is added to the binary tree to create the huffman code
        * */

        /*(1)*/
        while ( ! S.isEmpty() ) {
            BinaryTree<Pair> A;
            BinaryTree<Pair> B;
            if ( T.isEmpty() ) {
                A = S.remove(0);
                B = S.remove(0);
            } else {
                if (S.get(0).getData().getProb() < T.get(0).getData().getProb() ) {
                    A = S.remove(0);

                    if ( S.isEmpty() || S.get(0).getData().getProb() > T.get(0).getData().getProb()    ){
                        B = T.remove(0);
                    } else {
                        B = S.remove(0);
                    }
                } else {
                    A = T.remove(0);
                    if ( T.isEmpty() || S.get(0).getData().getProb() < T.get(0).getData().getProb()    ){
                        B = S.remove(0);
                    } else {
                        B = T.remove(0);
                    }
                }
            }
            /*(2)*/
            BinaryTree<Pair> P = new BinaryTree<>();
            Pair pp = new Pair('⁂', A.getData().getProb()+ B.getData().getProb() );
            P.makeRoot(pp);
            P.attachLeft(A);
            P.attachRight(B);
            T.add(P);
        }
        // Logic: See lab4.pdf //
        while ( T.size() > 1 ){
            BinaryTree<Pair> A;
            BinaryTree<Pair> B;
            A = T.remove(0);
            B = T.remove(0);
            BinaryTree<Pair> P = new BinaryTree<>();
            Pair pp = new Pair('⁂', A.getData().getProb()+ B.getData().getProb() );
            P.makeRoot(pp);
            P.attachLeft(A);
            P.attachRight(B);
            T.add(P);
        }

        // Logic: See lab4.pdf //
        String[] codes = findEncoding(T.get(0));

        //PrintWriter did not work for me so I used printstream which I found on stack over flow//
        PrintStream fileOut = new PrintStream("Encode.txt");

        // Prints the encoded txt to the text file Encode.txt //
        for ( char item : charsForPrinting  ){
            if( item != ' ' ){
                fileOut.print(codes[item]);
            } else {
                fileOut.print(" ");
            }
        }

        // Creates the huffman.txt and addes the huffman symbol, prob, and codes onto the text file //
        StringBuilder huffmanResult = new StringBuilder();

        huffmanResult.append(String.format("%-8s%-8s%-12s\n", "Symbol", "Prob.", "Code"));
        huffmanResult.append("\n");
        for(int i = 0; i < codes.length; i++ ){
            if ( codes[i] != null ){
                huffmanResult.append(String.format("%-8s%-8s%-12s\n", (char) i, Math.round(freq[i] * 10000d / chars.length) / 10000d, codes[i]));
            }
        }
        PrintStream output = new PrintStream("Huffman.txt");
        output.println(huffmanResult);

        System.out.println("Codes generated. Printing codes to Huffman.txt");
        System.out.println("Printing encoded text to Encoded.txt");

    }


    public static void decode()throws IOException{
        // initialize Scanner to capture user input
        Scanner sc = new Scanner(System.in);

        // capture file information from user and read file
        System.out.print("Enter the filename to read from/decode: ");
        String f = sc.nextLine();

        // create File object and build text String
        File file = new File(f);
        Scanner input = new Scanner(file).useDelimiter("\\Z");
        String text = input.next();
        // ensure all text is consumed, avoiding false positive end of
        // input String
        input.useDelimiter("\\z");

        // close input file
        input.close();

        // capture file information from user and read file
        System.out.print("Enter the filename of document containing Huffman codes: ");
        f = sc.nextLine();
        // create File object and build text String
        file = new File(f);
        input = new Scanner(file).useDelimiter("\\z");
        String codes = input.next();
        // close input file
        input.close();

        // Logic: Creates a map using the huffman codes as the key and the symbols as the value//

        Map<String, String> map = new HashMap<>();
        Scanner ls = new Scanner(codes);
        ls.nextLine();

        while(ls.hasNext()){
            String symbol = ls.next();
            ls.next();
            String huffmanCodes = ls.next();
            map.put(huffmanCodes,symbol);
        }

        String huffmanResult = "";
        StringBuilder huffmanResult2 = new StringBuilder();

        //Logic: goes through the text and appends to the string builder (huffmanResult),
        // if the string builder in key of map add the value with the key to another string builder (huffmanResult2) //
        for ( int i = 0; i < text.length(); i++ ){
            if (text.charAt(i) == ' ' ){
                huffmanResult2.append(" ");
            } else {
                huffmanResult += text.charAt(i);
                if (map.containsKey(huffmanResult)) {

                    huffmanResult2.append(map.get(huffmanResult));
                    huffmanResult = "";
                }
            }
        }

        PrintStream fileOut = new PrintStream("Decoded.txt");

        System.out.println("Printing decoded text to Decoded.txt");
        fileOut.print(huffmanResult2);
    }

    private static String[] findEncoding(BinaryTree<Pair> bt){
        String[] result = new String[256];
        findEncoding(bt, result, "");
        return result;
    }
    private static void findEncoding(BinaryTree<Pair> bt, String[] a, String prefix){
        // test is node/tree is a leaf
        if (bt.getLeft()==null && bt.getRight()==null){
            a[bt.getData().getValue()] = prefix;
        }
        // recursive calls
        else{
            findEncoding(bt.getLeft(), a, prefix+"0");
            findEncoding(bt.getRight(), a, prefix+"1");
        }
    }
}
