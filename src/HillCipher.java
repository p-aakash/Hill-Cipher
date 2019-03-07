/**
 * Created by p-aakash on 3/7/19.
 */

import java.util.*;
import java.util.Scanner;


public class HillCipher {

    // Variable Initializations
    static int DEBUG = 0;
    static int chosen_option;
    static String message;
    static String ciphert;
    static int blocksz;
    static int[][] keym;
    static String etext = "";
    static String dtext = "";


    // ENCRYPT and its Helper Functions ---------------------------------------------------
    public void encrypt(){
        int n = blocksz;
        for(int i=0; i<(message.length()/n); i++){
            String s;
            if(i==((message.length()/n)-1)){
                s = message.substring(i*n);
            }
            else{
                s = message.substring(i*n,((i*n)+n));
            }
            int[][] num_s = new int[n][1];
            num_s = convertToMatrix(s);
            int[][] mul_num = new int[n][1];
            mul_num = matrixMultiply(num_s);
            for(int x=0; x<mul_num.length; x++){
                for(int y=0; y<(1); y++){
                    int m = mul_num[x][y] % 26;
                    char c = (char)(m+65);
                    etext += c;
                }
            }
        }
    }

    public int[][] convertToMatrix(String s){
        int[][] vals = new int[s.length()][1];
        for(int i=0; i<s.length(); i++){
            for(int j=0; j<1; j++){
                //vals[i][j] = (int)(s.charAt(i));
                vals[i][j] = ((int)(s.charAt(i)))%65;
            }
        }
        return vals;
    }

    public int[][] matrixMultiply(int[][] vals){
        int r1 = blocksz;
        int r2 = blocksz;
        int c1 = blocksz;
        int c2 = vals[0].length;
        int[][] calc_vals = new int[blocksz][1];
        for(int i=0; i<r1; i++){
            for(int j=0; j<c2; j++){
                for(int k=0; k<c1; k++){
                    calc_vals[i][j] += keym[i][k] * vals[k][j];
                }
            }
        }
        return calc_vals;
    }

    // DECRYPT and its Helper Functions -----------------------------------------------------
    public void decrypt(){
        int n = blocksz;
        int d = determinant(keym, n);
        System.out.println(d);
    }

    public int determinant(int[][] arr, int n){
        int d = 0;
        int sign = 1;
        if(n==1){
            return arr[0][0];
        }
        int[][] cofac_arr = new int[n][n];
        for(int i=0; i<n; i++){
            cofactor(arr,cofac_arr,0,i,n);
            d += sign * cofac_arr[0][i] * determinant(arr, n-1);
            sign = -sign;
        }
        return d;
    }

    public void cofactor(int[][] arr, int[][] cofac_arr, int p, int q, int n){
        int i=0, j=0;
        for(int r=0; r<n; r++){
            for(int c=0; c<n; c++){
                if(r!=p && c!=q){
                    cofac_arr[i][j++] = arr[r][c];
                    if(j==(n-1)){
                        j=0;
                        i++;
                    }
                }
            }
        }
    }

    /*public int[][] transpose(){

    }*/


    // MAIN ---------------------------------------------------------------------------------
    public static void main(String args[]){
        HillCipher hc = new HillCipher();
        Scanner in = new Scanner(System.in);
        System.out.println("PROGRAM OUTPUT SPECIFICATION FOR VALID KEY\n");
        System.out.println("Enter 1 to ENCRYPT or 2 to DECRYPT: ");
        chosen_option = in.nextInt();
        if(chosen_option==1){
            // Get PLAIN-TEXT
            System.out.println("\nEnter plain-text:");
            message = in.next();
        }
        else if(chosen_option==2){
            // Get CIPHER-TEXT
            System.out.println("\nEnter cipher-text:");
            ciphert = in.next();
        }
        // Read Block Size
        System.out.println("\nEnter block size of matrix:");
        blocksz = in.nextInt();
        // Read Key Matrix
        keym = new int[blocksz][blocksz];
        System.out.println("\nEnter Key Matrix:");
        for(int i=0; i<blocksz; i++){
            for(int j=0; j<blocksz; j++){
                keym[i][j] = in.nextInt();
            }
        }
        if(chosen_option==1){
            // Let's ENCRYPT
            if(message.length()%blocksz != 0){
                while(message.length()%blocksz !=0 ){
                    message = message+"X";
                }
            }
            hc.encrypt();
            // OUT
            System.out.println("\nEncrypted Text is:");
            System.out.println(etext);
        }
        else if(chosen_option==2){
            // Let's DECRYPT
            hc.decrypt();
            // Inverse Matrix

            // OUT
            System.out.println("\nDecrypted Text is:");
            System.out.println(dtext);
        }

        if(DEBUG==1){
            System.out.println("\nInserted Matrix:\n");
            for(int i=0; i<blocksz; i++){
                System.out.print("[");
                for(int j=0; j<blocksz; j++){
                    System.out.print(keym[i][j]);
                    if(j<(blocksz-1)){
                        System.out.print(" ");
                    }
                }
                System.out.print("]\n");
            }
        }

        in.close();
    }
}
