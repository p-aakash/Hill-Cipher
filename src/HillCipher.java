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
    static int[][] inv_key;

    // ENCRYPTION and its Helper Functions ---------------------------------------------------
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
            mul_num = matrixMultiply(num_s,keym);
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
                vals[i][j] = ((int)(s.charAt(i)))%65;
            }
        }
        return vals;
    }

    public int[][] matrixMultiply(int[][] vals, int[][]keym){
        int r1 = keym.length;
        int r2 = vals.length;
        int c1 = keym[0].length;
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

    // DECRYPTION and its Helper Functions -----------------------------------------------------
    public boolean decrypt(){
        int n = blocksz;
        int d = determinant(keym, n);
        if((d%26)==0 || ((d%2)==0) || ((d%13)==0)){
            return false;
        }
        int[][] cofac_arr = new int[n][n];
        cofac_arr = cofactor(keym, n);
        inv_key = new int[n][n];
        inv_key = transpose(cofac_arr, n);
        for(int i=0; i<(ciphert.length()/n); i++){
            String c;
            if(i==((ciphert.length()/n)-1)){
                c = ciphert.substring(i*n);
            }
            else{
                c = ciphert.substring(i*n,((i*n)+n));
            }
            int[][] num_c = new int[n][1];
            num_c = convertToMatrix(c);
            int[][] mul_num = new int[n][1];
            mul_num = matrixMultiply(num_c,inv_key);
            for(int x=0; x<mul_num.length; x++){
                for(int y=0; y<(1); y++){
                    int m = mul_num[x][y] % 26;
                    char ch = (char)(m+65);
                    dtext += ch;
                }
            }
        }
        return true;
    }

    public int determinant(int[][] arr, int n){
        int d = 0;
        int sign = 1;
        if(n==1){
            return arr[0][0];
        }
        else{
            int[][] t_arr = new int[n-1][n-1];
            int p=0, q=0;
            for(int i=0; i<n; i++){
                p=0;
                q=0;
                for(int j=1; j<n; j++){
                    for(int k=0; k<n; k++){
                        if(k!=i){
                            t_arr[p][q++] = arr[j][k];
                            if(q%(n-1)==0){
                                p++;
                                q=0;
                            }
                        }
                    }
                }
                d += arr[0][i] * determinant(t_arr,n-1) * sign;
                sign = -sign;
            }
        }
        return d;
    }

    public int[][] cofactor(int[][] arr, int n){
        int[][] t_arr = new int[n][n];
        int[][] cofac_arr = new int[n][n];
        int r=0, c=0;
        for(int k=0; k<n; k++){
            for(int l=0; l<n; l++){
                r=0;
                c=0;
                for(int i=0; i<n; i++){
                    for(int j=0; j<n; j++){
                        t_arr[i][j] = 0;
                        if(i!=k && j!=l){
                            t_arr[r][c] = arr[i][j];
                            if(c < (n-2)){
                                c++;
                            }
                            else{
                                c=0;
                                r++;
                            }
                        }
                    }
                }
                cofac_arr[k][l] = (int)(Math.pow(-1,k+l) * determinant(t_arr,(n-1)));
            }
        }
        return cofac_arr;
    }

    public int[][] transpose(int[][] arr, int n){
        int[][] inv_arr = new int[n][n];
        int[][] t_arr = new int[n][n];
        int d = determinant(arr, n);
        int m = mod_it(d%26);
        m = m%26;
        if(m<0){
            m += 26;
        }
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                t_arr[i][j] = arr[j][i];
            }
        }
        for(int i=0; i<n; i++){
            for(int j=0; j<n; j++){
                inv_arr[i][j] = t_arr[i][j]%26;
                if(inv_arr[i][j] < 0){
                    inv_arr[i][j] += 26;
                }
                inv_arr[i][j] *= m;
                inv_arr[i][j] = inv_arr[i][j]%26;
            }
        }
        return inv_arr;
    }

    public int mod_it(int d){
        int r, t, q;
        int r1=26, r2=d;
        int t1=0, t2=1;
        while(r1!=1 && r2!=0){
            q = r1/r2;
            r = r1%r2;
            t = t1-(t2 * q);
            r1 = r2;
            r2 = r;
            t1 = t2;
            t2 = t;
        }
        return (t1+t2);
    }

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
            boolean rtn = hc.decrypt();
            // OUT
            if(rtn==true){
                System.out.println("\nInverse Key Matrix is:");
                for(int i=0; i<inv_key.length; i++){
                    for(int j=0; j<inv_key[0].length; j++){
                        System.out.print(inv_key[i][j]+" ");
                    }
                    System.out.print("\n");
                }
                System.out.println("\nDecrypted Text is:");
                System.out.println(dtext);
            }
            else{
                System.out.println("\nKey Matrix is not invertible mod 26. Exiting Program.");
                in.close();
                return;
            }
        }
        // DEBUG
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
        return;
    }
}