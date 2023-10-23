package encrypter_for_esl;
/*
Electronic Shelf Label Firebase Edition
Distributed under the MIT License
© Copyright Maxim Bortnikov 2023
For more information please visit
https://github.com/Northstrix/Electronic-Shelf-Label-Firebase-Edition
https://sourceforge.net/projects/esl-firebase-edition/
Required libraries:
https://github.com/adafruit/Adafruit-ST7735-Library
https://github.com/adafruit/Adafruit-GFX-Library
https://github.com/adafruit/Adafruit_BusIO
https://github.com/mobizt/Firebase-ESP-Client
*/
/*
Implementation of DES by David Simmons was taken from here https://github.com/simmons/desdemo

* Copyright 2011 David Simmons
* http://cafbit.com/
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.

*/
import java.io.*;
import javax.swing.*;

public class MainClass {
        public static String stf;
        public static String ck = "";
        public static String ck2 = "";
        public static String ck3 = "";
        public static String key_back = "";
        public static String key_back2 = "";
        public static String key_back3 = "";
        public static int Forward_S_Box[][] = {  
        	    {0x63, 0x7C, 0x77, 0x7B, 0xF2, 0x6B, 0x6F, 0xC5, 0x30, 0x01, 0x67, 0x2B, 0xFE, 0xD7, 0xAB, 0x76},  
        	    {0xCA, 0x82, 0xC9, 0x7D, 0xFA, 0x59, 0x47, 0xF0, 0xAD, 0xD4, 0xA2, 0xAF, 0x9C, 0xA4, 0x72, 0xC0},  
        	    {0xB7, 0xFD, 0x93, 0x26, 0x36, 0x3F, 0xF7, 0xCC, 0x34, 0xA5, 0xE5, 0xF1, 0x71, 0xD8, 0x31, 0x15},  
        	    {0x04, 0xC7, 0x23, 0xC3, 0x18, 0x96, 0x05, 0x9A, 0x07, 0x12, 0x80, 0xE2, 0xEB, 0x27, 0xB2, 0x75},  
        	    {0x09, 0x83, 0x2C, 0x1A, 0x1B, 0x6E, 0x5A, 0xA0, 0x52, 0x3B, 0xD6, 0xB3, 0x29, 0xE3, 0x2F, 0x84},  
        	    {0x53, 0xD1, 0x00, 0xED, 0x20, 0xFC, 0xB1, 0x5B, 0x6A, 0xCB, 0xBE, 0x39, 0x4A, 0x4C, 0x58, 0xCF},  
        	    {0xD0, 0xEF, 0xAA, 0xFB, 0x43, 0x4D, 0x33, 0x85, 0x45, 0xF9, 0x02, 0x7F, 0x50, 0x3C, 0x9F, 0xA8},  
        	    {0x51, 0xA3, 0x40, 0x8F, 0x92, 0x9D, 0x38, 0xF5, 0xBC, 0xB6, 0xDA, 0x21, 0x10, 0xFF, 0xF3, 0xD2},  
        	    {0xCD, 0x0C, 0x13, 0xEC, 0x5F, 0x97, 0x44, 0x17, 0xC4, 0xA7, 0x7E, 0x3D, 0x64, 0x5D, 0x19, 0x73},  
        	    {0x60, 0x81, 0x4F, 0xDC, 0x22, 0x2A, 0x90, 0x88, 0x46, 0xEE, 0xB8, 0x14, 0xDE, 0x5E, 0x0B, 0xDB},  
        	    {0xE0, 0x32, 0x3A, 0x0A, 0x49, 0x06, 0x24, 0x5C, 0xC2, 0xD3, 0xAC, 0x62, 0x91, 0x95, 0xE4, 0x79},  
        	    {0xE7, 0xC8, 0x37, 0x6D, 0x8D, 0xD5, 0x4E, 0xA9, 0x6C, 0x56, 0xF4, 0xEA, 0x65, 0x7A, 0xAE, 0x08},  
        	    {0xBA, 0x78, 0x25, 0x2E, 0x1C, 0xA6, 0xB4, 0xC6, 0xE8, 0xDD, 0x74, 0x1F, 0x4B, 0xBD, 0x8B, 0x8A},  
        	    {0x70, 0x3E, 0xB5, 0x66, 0x48, 0x03, 0xF6, 0x0E, 0x61, 0x35, 0x57, 0xB9, 0x86, 0xC1, 0x1D, 0x9E},  
        	    {0xE1, 0xF8, 0x98, 0x11, 0x69, 0xD9, 0x8E, 0x94, 0x9B, 0x1E, 0x87, 0xE9, 0xCE, 0x55, 0x28, 0xDF},  
        	    {0x8C, 0xA1, 0x89, 0x0D, 0xBF, 0xE6, 0x42, 0x68, 0x41, 0x99, 0x2D, 0x0F, 0xB0, 0x54, 0xBB, 0x16}  
        	};

    static int split(char ct[], int i){
    		int res = 0;
    	    if(ct[i] != 0 && ct[i+1] != 0)
    	    res = 16*getNum(ct[i])+getNum(ct[i+1]);
    	    if(ct[i] != 0 && ct[i+1] == 0)
    	    res = 16*getNum(ct[i]);
    	    if(ct[i] == 0 && ct[i+1] != 0)
    	    res = getNum(ct[i+1]);
    	    if(ct[i] == 0 && ct[i+1] == 0)
    	    res = 0;
    	    return res;
    	}
    
    static int getNum(char ch)
    {
        int num=0;
        if(ch>='0' && ch<='9')
        {
            num=ch-0x30;
        }
        else
        {
            switch(ch)
            {
                case 'A': case 'a': num=10; break;
                case 'B': case 'b': num=11; break;
                case 'C': case 'c': num=12; break;
                case 'D': case 'd': num=13; break;
                case 'E': case 'e': num=14; break;
                case 'F': case 'f': num=15; break;
                default: num=0;
            }
        }
        return num;
    }
    
    private static final byte[] IP = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    private static final byte[] FP = {
            40, 8, 48, 16, 56, 24, 64, 32,
            39, 7, 47, 15, 55, 23, 63, 31,
            38, 6, 46, 14, 54, 22, 62, 30,
            37, 5, 45, 13, 53, 21, 61, 29,
            36, 4, 44, 12, 52, 20, 60, 28,
            35, 3, 43, 11, 51, 19, 59, 27,
            34, 2, 42, 10, 50, 18, 58, 26,
            33, 1, 41, 9, 49, 17, 57, 25
    };

    private static final byte[] E = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    private static final byte[][] S = {{
            14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13
    }, {
            15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
            3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
            0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
            13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9
    }, {
            10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
            13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
            13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
            1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12
    }, {
            7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
            13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
            10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
            3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14
    }, {
            2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
            14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
            4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
            11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3
    }, {
            12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
            10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
            9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
            4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13
    }, {
            4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
            13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
            1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
            6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12
    }, {
            13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
            1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
            7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
            2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11
    }};

    private static final byte[] P = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };

    private static final byte[] PC1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    private static final byte[] PC2 = {
            14, 17, 11, 24, 1, 5,
            3, 28, 15, 6, 21, 10,
            23, 19, 12, 4, 26, 8,
            16, 7, 27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    private static final byte[] rotations = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };

      private static long IP(long src) {
        return permute(IP, 64, src);
      } // 64-bit output

      private static long FP(long src) {
        return permute(FP, 64, src);
      } // 64-bit output

      private static long E(int src) {
        return permute(E, 32, src & 0xFFFFFFFFL);
      } // 48-bit output

      private static int P(int src) {
        return (int) permute(P, 32, src & 0xFFFFFFFFL);
      } // 32-bit output

      private static long PC1(long src) {
        return permute(PC1, 64, src);
      } // 56-bit output

      private static long PC2(long src) {
        return permute(PC2, 56, src);
      } // 48-bit output

      private static long permute(byte[] table, int srcWidth, long src) {
        long dst = 0;
        for (int i = 0; i < table.length; i++) {
          int srcPos = srcWidth - table[i];
          dst = (dst << 1) | (src >> srcPos & 0x01);
        }
        return dst;
      }

      private static byte S(int boxNumber, byte src) {
        // The first aindex based on the following bit shuffle:
        // abcdef => afbcde
        src = (byte)(src & 0x20 | ((src & 0x01) << 4) | ((src & 0x1E) >> 1));
        return S[boxNumber - 1][src];
      }

      private static long getLongFromBytes(byte[] ba, int offset) {
        long l = 0;
        for (int i = 0; i < 8; i++) {
          byte value;
          if ((offset + i) < ba.length) {
            // and last bits determine which 16-value row to
            // reference, so we transform the 6-bit input into an
            // absolute
            value = ba[offset + i];
          } else {
            value = 0;
          }
          l = l << 8 | (value & 0xFFL);
        }
        return l;
      }

      private static void getBytesFromLong(byte[] ba, int offset, long l) {
        for (int i = 7; i > -1; i--) {
          if ((offset + i) < ba.length) {
            ba[offset + i] = (byte)(l & 0xFF);
            l = l >> 8;
          } else {
            break;
          }
        }
      }

      private static int feistel(int r, /* 48 bits */ long subkey) {
        // 1. expansion
        long e = E(r);
        // 2. key mixing
        long x = e ^ subkey;
        // 3. substitution
        int dst = 0;
        for (int i = 0; i < 8; i++) {
          dst >>>= 4;
          int s = S(8 - i, (byte)(x & 0x3F));
          dst |= s << 28;
          x >>= 6;
        }
        // 4. permutation
        return P(dst);
      }

      private static long[] createSubkeys( /* 64 bits */ long key) {
        long subkeys[] = new long[16];

        // perform the PC1 permutation
        key = PC1(key);

        // split into 28-bit left and right (c and d) pairs.
        int c = (int)(key >> 28);
        int d = (int)(key & 0x0FFFFFFF);

        // for each of the 16 needed subkeys, perform a bit
        // rotation on each 28-bit keystuff half, then join
        // the halves together and permute to generate the
        // subkey.
        for (int i = 0; i < 16; i++) {
          // rotate the 28-bit values
          if (rotations[i] == 1) {
            // rotate by 1 bit
            c = ((c << 1) & 0x0FFFFFFF) | (c >> 27);
            d = ((d << 1) & 0x0FFFFFFF) | (d >> 27);
          } else {
            // rotate by 2 bits
            c = ((c << 2) & 0x0FFFFFFF) | (c >> 26);
            d = ((d << 2) & 0x0FFFFFFF) | (d >> 26);
          }

          // join the two keystuff halves together.
          long cd = (c & 0xFFFFFFFFL) << 28 | (d & 0xFFFFFFFFL);

          // perform the PC2 permutation
          subkeys[i] = PC2(cd);
        }

        return subkeys; /* 48-bit values */
      }

      public static long encryptBlock(long m, /* 64 bits */ long key) {
        // generate the 16 subkeys
        long subkeys[] = createSubkeys(key);

        // perform the initial permutation
        long ip = IP(m);

        // split the 32-bit value into 16-bit left and right halves.
        int l = (int)(ip >> 32);
        int r = (int)(ip & 0xFFFFFFFFL);

        // perform 16 rounds
        for (int i = 0; i < 16; i++) {
          int previous_l = l;
          // the right half becomes the new left half.
          l = r;
          // the Feistel function is applied to the old left half
          // and the resulting value is stored in the right half.
          r = previous_l ^ feistel(r, subkeys[i]);
        }

        // reverse the two 32-bit segments (left to right; right to left)
        long rl = (r & 0xFFFFFFFFL) << 32 | (l & 0xFFFFFFFFL);

        // apply the final permutation
        long fp = FP(rl);

        // return the ciphertext
        return fp;
      }

      public static void encryptBlock(
        byte[] message,
        int messageOffset,
        byte[] ciphertext,
        int ciphertextOffset,
        byte[] key
      ) {
        long m = getLongFromBytes(message, messageOffset);
        long k = getLongFromBytes(key, 0);
        long c = encryptBlock(m, k);
        getBytesFromLong(ciphertext, ciphertextOffset, c);
      }

      public static byte[] encrypt(byte[] message, byte[] key) {
        byte[] ciphertext = new byte[message.length];

        // encrypt each 8-byte (64-bit) block of the message.
        for (int i = 0; i < message.length; i += 8) {
          encryptBlock(message, i, ciphertext, i, key);
        }

        return ciphertext;
      }

      public static byte[] encrypt(byte[] challenge, String password) {
        return encrypt(challenge, passwordToKey(password));
      }

      private static byte[] passwordToKey(String password) {
        byte[] pwbytes = password.getBytes();
        byte[] key = new byte[8];
        for (int i = 0; i < 8; i++) {
          if (i < pwbytes.length) {
            byte b = pwbytes[i];
            // flip the byte
            byte b2 = 0;
            for (int j = 0; j < 8; j++) {
              b2 <<= 1;
              b2 |= (b & 0x01);
              b >>>= 1;
            }
            key[i] = b2;
          } else {
            key[i] = 0;
          }
        }
        return key;
      }

      private static int charToNibble(char c) {
        if (c >= '0' && c <= '9') {
          return (c - '0');
        } else if (c >= 'a' && c <= 'f') {
          return (10 + c - 'a');
        } else if (c >= 'A' && c <= 'F') {
          return (10 + c - 'A');
        } else {
          return 0;
        }
      }

      private static byte[] parseBytes(String s) {
        s = s.replace(" ", "");
        byte[] ba = new byte[s.length() / 2];
        if (s.length() % 2 > 0) {
          s = s + '0';
        }
        for (int i = 0; i < s.length(); i += 2) {
          ba[i / 2] = (byte)(charToNibble(s.charAt(i)) << 4 | charToNibble(s.charAt(i + 1)));
        }
        return ba;
      }

      private static String hex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
          sb.append(String.format("%02X ", bytes[i]));
        }
        return sb.toString();
      }

      private static long IV;

      public static long getIv() {
        return IV;
      }

      public static void setIv(long iv) {
        IV = iv;
      }

      public static byte[] encryptCBC(byte[] message, byte[] key) {
        byte[] ciphertext = new byte[message.length];
        long k = getLongFromBytes(key, 0);
        long previousCipherBlock = IV;

        for (int i = 0; i < message.length; i += 8) {
          // get the message block to be encrypted (8bytes = 64bits)
          long messageBlock = getLongFromBytes(message, i);

          // XOR message block with previous cipherblock and encrypt
          // First previousCiphertext = Initial Vector (IV)
          long cipherBlock = encryptBlock(messageBlock ^ previousCipherBlock, k);

          // Store the cipherBlock in the correct position in ciphertext
          getBytesFromLong(ciphertext, i, cipherBlock);

          // Update previousCipherBlock
          previousCipherBlock = cipherBlock;
        }

        return ciphertext;
      }

      public static long decryptBlock(long c, /* 64 bits */ long key) {
        // generate the 16 subkeys
        long[] subkeys = createSubkeys(key);

        // perform the initial permutation
        long ip = IP(c);

        // split the 32-bit value into 16-bit left and right halves.
        int l = (int)(ip >> 32);
        int r = (int)(ip & 0xFFFFFFFFL);

        // perform 16 rounds
        // NOTE: reverse order of subkeys used!
        for (int i = 15; i > -1; i--) {
          int previous_l = l;
          // the right half becomes the new left half.
          l = r;
          // the Feistel function is applied to the old left half
          // and the resulting value is stored in the right half.
          r = previous_l ^ feistel(r, subkeys[i]);
        }

        // reverse the two 32-bit segments (left to right; right to left)
        long rl = (r & 0xFFFFFFFFL) << 32 | (l & 0xFFFFFFFFL);

        // apply the final permutation
        long fp = FP(rl);

        // return the message
        return fp;
      }

      public static void decryptBlock(
        byte[] ciphertext,
        int ciphertextOffset,
        byte[] message,
        int messageOffset,
        byte[] key
      ) {
        long c = getLongFromBytes(ciphertext, ciphertextOffset);
        long k = getLongFromBytes(key, 0);
        long m = decryptBlock(c, k);
        getBytesFromLong(message, messageOffset, m);
      }

      public static byte[] decrypt(byte[] ciphertext, byte[] key) {
        byte[] message = new byte[ciphertext.length];

        // encrypt each 8-byte (64-bit) block of the message.
        for (int i = 0; i < ciphertext.length; i += 8) {
          decryptBlock(ciphertext, i, message, i, key);
        }

        return message;
      }

      public static byte[] decryptCBC(byte[] ciphertext, byte[] key) {
        byte[] message = new byte[ciphertext.length];
        long k = getLongFromBytes(key, 0);
        long previousCipherBlock = IV;

        for (int i = 0; i < ciphertext.length; i += 8) {
          // get the cipher block to be decrypted (8bytes = 64bits)
          long cipherBlock = getLongFromBytes(ciphertext, i);

          // Decrypt the cipher block and XOR with previousCipherBlock
          // First previousCiphertext = Initial Vector (IV)
          long messageBlock = decryptBlock(cipherBlock, k);
          messageBlock = messageBlock ^ previousCipherBlock;

          // Store the messageBlock in the correct position in message
          getBytesFromLong(message, i, messageBlock);

          // Update previousCipherBlock
          previousCipherBlock = cipherBlock;
        }

        return message;
      }

      public static void ctostr(char[] vrbls, int pos, String dst) {
        String tf = "";
        for (int i = 0; i < 16; i++) {
          tf += vrbls[i + pos];
        }
        //System.out.println(tf);
        byte[] enc = encrypt(parseBytes(tf), parseBytes(ck));
        incr_des_key();
        byte[] enc2 = decrypt(enc, parseBytes(ck2));
        incr_des_key2();
        byte[] enc3 = encrypt(enc2, parseBytes(ck3));
        incr_des_key3();
        String text = null;
        for (int i = 0; i < 8; i++) {
          if (i == 0)
            text = String.format("%02x", enc3[i]);
          if (i != 0)
            text += String.format("%02x", enc3[i]);
        }
        File file = new File(dst);
        FileWriter fr = null;
        BufferedWriter br = null;
        PrintWriter pr = null;
        try {
          fr = new FileWriter(file, true);
          br = new BufferedWriter(fr);
          pr = new PrintWriter(br);
          pr.print(text);
        } catch (IOException e) {
          e.printStackTrace();
        } finally {
          try {
            pr.close();
            br.close();
            fr.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
      
      public static void incr_des_key() {
    	  char char_fifteen = ck.charAt(15);
    	  char char_fourteen = ck.charAt(14);
    	  char char_thirteen = ck.charAt(13);
    	  char char_twelve = ck.charAt(12);
    	  char char_eleven = ck.charAt(11);
    	  char char_ten = ck.charAt(10);
    	  char char_nine = ck.charAt(9);
    	  char char_eight = ck.charAt(8);
    	  char char_seven = ck.charAt(7);
    	  char char_six = ck.charAt(6);
    	  char char_five = ck.charAt(5);
    	  char char_four = ck.charAt(4);
    	  char char_three = ck.charAt(3);
    	  char char_two = ck.charAt(2);
    	  char char_one = ck.charAt(1);
    	  char char_zero = ck.charAt(0);
    	  char_fifteen = incr_char(char_fifteen);
    	  if (char_fifteen == '0') {
    		  char_fourteen = incr_char(char_fourteen);
        	  if (char_fourteen == '0') {
        		  char_thirteen = incr_char(char_thirteen);
            	  if (char_thirteen == '0') {
            		  char_twelve = incr_char(char_twelve);
                	  if (char_twelve == '0') {
                		  char_eleven = incr_char(char_eleven);
                    	  if (char_eleven == '0') {
                    		  char_ten = incr_char(char_ten);
                        	  if (char_ten == '0') {
                        		  char_nine = incr_char(char_nine);
                            	  if (char_nine == '0') {
                            		  char_eight = incr_char(char_eight);
                                	  if (char_eight == '0') {
                                		  char_seven = incr_char(char_seven);
                                    	  if (char_seven == '0') {
                                    		  char_six = incr_char(char_six);
                                        	  if (char_six == '0') {
                                        		  char_five = incr_char(char_five);
                                            	  if (char_five == '0') {
                                            		  char_four = incr_char(char_four);
                                                	  if (char_four == '0') {
                                                		  char_three = incr_char(char_three);
                                                    	  if (char_three == '0') {
                                                    		  char_two = incr_char(char_two);
                                                        	  if (char_two == '0') {
                                                        		  char_one = incr_char(char_one);
                                                            	  if (char_one == '0') {
                                                            		  char_zero = incr_char(char_zero);
                                                            	  }
                                                        	  }
                                                    	  }
                                                	  }
                                            	  }
                                        	  }
                                    	  }
                                	  }
                            	  }
                        	  }
                    	  }
                	  }
            	  }
        	  }
    	  }
    	  ck = Character.toString(char_zero);
    	  ck += char_one;
    	  ck += char_two;
    	  ck += char_three;
    	  ck += char_four;
    	  ck += char_five;
    	  ck += char_six;
    	  ck += char_seven;
    	  ck += char_eight;
    	  ck += char_nine;
    	  ck += char_ten;
    	  ck += char_eleven;
    	  ck += char_twelve;
    	  ck += char_thirteen;
    	  ck += char_fourteen;
    	  ck += char_fifteen;
      }
      
      public static void incr_des_key2() {
          char char_fifteen = ck2.charAt(15);
          char char_fourteen = ck2.charAt(14);
          char char_thirteen = ck2.charAt(13);
          char char_twelve = ck2.charAt(12);
          char char_eleven = ck2.charAt(11);
          char char_ten = ck2.charAt(10);
          char char_nine = ck2.charAt(9);
          char char_eight = ck2.charAt(8);
          char char_seven = ck2.charAt(7);
          char char_six = ck2.charAt(6);
          char char_five = ck2.charAt(5);
          char char_four = ck2.charAt(4);
          char char_three = ck2.charAt(3);
          char char_two = ck2.charAt(2);
          char char_one = ck2.charAt(1);
          char char_zero = ck2.charAt(0);
          char_fifteen = incr_char(char_fifteen);
          if (char_fifteen == '0') {
            char_fourteen = incr_char(char_fourteen);
              if (char_fourteen == '0') {
                char_thirteen = incr_char(char_thirteen);
                  if (char_thirteen == '0') {
                    char_twelve = incr_char(char_twelve);
                      if (char_twelve == '0') {
                        char_eleven = incr_char(char_eleven);
                          if (char_eleven == '0') {
                            char_ten = incr_char(char_ten);
                              if (char_ten == '0') {
                                char_nine = incr_char(char_nine);
                                  if (char_nine == '0') {
                                    char_eight = incr_char(char_eight);
                                      if (char_eight == '0') {
                                        char_seven = incr_char(char_seven);
                                          if (char_seven == '0') {
                                            char_six = incr_char(char_six);
                                              if (char_six == '0') {
                                                char_five = incr_char(char_five);
                                                  if (char_five == '0') {
                                                    char_four = incr_char(char_four);
                                                      if (char_four == '0') {
                                                        char_three = incr_char(char_three);
                                                          if (char_three == '0') {
                                                            char_two = incr_char(char_two);
                                                              if (char_two == '0') {
                                                                char_one = incr_char(char_one);
                                                                  if (char_one == '0') {
                                                                    char_zero = incr_char(char_zero);
                                                                  }
                                                              }
                                                          }
                                                      }
                                                  }
                                              }
                                          }
                                      }
                                  }
                              }
                          }
                      }
                  }
              }
          }
          ck2 = Character.toString(char_zero);
          ck2 += char_one;
          ck2 += char_two;
          ck2 += char_three;
          ck2 += char_four;
          ck2 += char_five;
          ck2 += char_six;
          ck2 += char_seven;
          ck2 += char_eight;
          ck2 += char_nine;
          ck2 += char_ten;
          ck2 += char_eleven;
          ck2 += char_twelve;
          ck2 += char_thirteen;
          ck2 += char_fourteen;
          ck2 += char_fifteen;
        }
      
      public static void incr_des_key3() {
          char char_fifteen = ck3.charAt(15);
          char char_fourteen = ck3.charAt(14);
          char char_thirteen = ck3.charAt(13);
          char char_twelve = ck3.charAt(12);
          char char_eleven = ck3.charAt(11);
          char char_ten = ck3.charAt(10);
          char char_nine = ck3.charAt(9);
          char char_eight = ck3.charAt(8);
          char char_seven = ck3.charAt(7);
          char char_six = ck3.charAt(6);
          char char_five = ck3.charAt(5);
          char char_four = ck3.charAt(4);
          char char_three = ck3.charAt(3);
          char char_two = ck3.charAt(2);
          char char_one = ck3.charAt(1);
          char char_zero = ck3.charAt(0);
          char_fifteen = incr_char(char_fifteen);
          if (char_fifteen == '0') {
            char_fourteen = incr_char(char_fourteen);
              if (char_fourteen == '0') {
                char_thirteen = incr_char(char_thirteen);
                  if (char_thirteen == '0') {
                    char_twelve = incr_char(char_twelve);
                      if (char_twelve == '0') {
                        char_eleven = incr_char(char_eleven);
                          if (char_eleven == '0') {
                            char_ten = incr_char(char_ten);
                              if (char_ten == '0') {
                                char_nine = incr_char(char_nine);
                                  if (char_nine == '0') {
                                    char_eight = incr_char(char_eight);
                                      if (char_eight == '0') {
                                        char_seven = incr_char(char_seven);
                                          if (char_seven == '0') {
                                            char_six = incr_char(char_six);
                                              if (char_six == '0') {
                                                char_five = incr_char(char_five);
                                                  if (char_five == '0') {
                                                    char_four = incr_char(char_four);
                                                      if (char_four == '0') {
                                                        char_three = incr_char(char_three);
                                                          if (char_three == '0') {
                                                            char_two = incr_char(char_two);
                                                              if (char_two == '0') {
                                                                char_one = incr_char(char_one);
                                                                  if (char_one == '0') {
                                                                    char_zero = incr_char(char_zero);
                                                                  }
                                                              }
                                                          }
                                                      }
                                                  }
                                              }
                                          }
                                      }
                                  }
                              }
                          }
                      }
                  }
              }
          }
          ck3 = Character.toString(char_zero);
          ck3 += char_one;
          ck3 += char_two;
          ck3 += char_three;
          ck3 += char_four;
          ck3 += char_five;
          ck3 += char_six;
          ck3 += char_seven;
          ck3 += char_eight;
          ck3 += char_nine;
          ck3 += char_ten;
          ck3 += char_eleven;
          ck3 += char_twelve;
          ck3 += char_thirteen;
          ck3 += char_fourteen;
          ck3 += char_fifteen;
        }
      
      public static char incr_char(char current_char) {
    	  if (current_char == '0')
    		  current_char = '1';
    	  else if (current_char == '1')
    		  current_char = '2';
    	  else if (current_char == '2')
    		  current_char = '3';
    	  else if (current_char == '3')
    		  current_char = '4';
    	  else if (current_char == '4')
    		  current_char = '5';
    	  else if (current_char == '5')
    		  current_char = '6';
    	  else if (current_char == '6')
    		  current_char = '7';
    	  else if (current_char == '7')
    		  current_char = '8';
    	  else if (current_char == '8')
    		  current_char = '9';
    	  else if (current_char == '9')
    		  current_char = 'A';
    	  else if (current_char == 'A')
    		  current_char = 'B';
    	  else if (current_char == 'B')
    		  current_char = 'C';
    	  else if (current_char == 'C')
    		  current_char = 'D';
    	  else if (current_char == 'D')
    		  current_char = 'E';
    	  else if (current_char == 'E')
    		  current_char = 'F';
    	  else if (current_char == 'F')
    		  current_char = '0';
    	  return current_char;
      }
      
      public static void encr_data() {
           	try {
            	  encrypt_data("plaintext", "ciphertext");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
      }
      
      public static void encrypt_data(String src, String dst) throws Exception {
      	key_back = ck;
      	key_back2 = ck2;
      	key_back3 = ck3;
          String st;
          StringBuffer str = new StringBuffer();
          // File path is passed as parameter
          File file = new File(src);
          BufferedReader br = new BufferedReader(new FileReader(file));
          while ((st = br.readLine()) != null)
            str.append(st);
          //System.out.println(str);
          stf = "";
          try {
            String ir = "";
            //String str = "1234";
            char[] ch = str.toString().toCharArray();
            for (int i = 0; i < ch.length; i++) {
              if ((int) ch[i] != 0) {
                int b = ((int) ch[i]) / 16;
                int s = ((int) ch[i]) % 16;
                if (b > 15)
                  b = 15;
                if (s > 15)
                  s = 15;
                Integer intObject = Integer.valueOf(Forward_S_Box[b][s]);
                ir += (String.format("%02x", intObject));
              } else {
                Integer c = Integer.valueOf(Forward_S_Box[0][0]);
                ir += (String.format("%02x", c));
              }

            }
            while (ir.length() % 16 != 0) {
              ir += "63";
            }
            //System.out.println(ir);
            //System.out.println("Length of a String is: " + ir.length());
            char[] iarr = new char[ir.length()];

            // Copy character by character into array
            for (int i = 0; i < ir.length(); i++) {
              iarr[i] = ir.charAt(i);
              //System.out.println(iarr[i]);
            }

            // Printing content of array
            /*for (char c : iarr) {
                System.out.println(c);
            }
            */
            File f= new File(dst);
            f.delete();
            int al = iarr.length;
            int curr = 0;
            while (curr < al) {
              ctostr(iarr, curr, dst);
              curr += 16;
            }
            ck = key_back;
            ck2 = key_back2;
            ck3 = key_back3;
            //System.out.println("Successfully wrote to the file.");
          } catch (Exception q) {
            System.out.println("An error occurred.");
            q.printStackTrace();
          }
      }

      public static void select_key() {
              File selectedFile = new File("key.txt");
              // Display selected file in console
              //System.out.println(selectedFile.getAbsolutePath());
              try {
                  String result = null;

                  DataInputStream reader = new DataInputStream(new FileInputStream(selectedFile.getAbsolutePath()));
                  int nBytesToRead = reader.available();
                  if(nBytesToRead > 0) {
                      byte[] bytes = new byte[nBytesToRead];
                      reader.read(bytes);
                      result = new String(bytes);
                  }
                  //System.out.println(result);
                  String rs1 = result.substring(result.indexOf("{") + 1);
                  rs1.trim();
                  rs1 = rs1.replaceAll("0x","");
                  rs1 = rs1.replaceAll("\n","");
                  rs1 = rs1.replaceAll(",","");
                  rs1 = rs1.replaceAll("}","");
                  rs1 = rs1.replaceAll(";","");
                  StringBuilder key = new StringBuilder();
                  for (int i = 0; i < rs1.length(); i++) {
                	    if(rs1.charAt(i) > '/')
                	    	key.append(rs1.charAt(i));
                	}
                  String strkey = key.toString();
                  strkey = strkey.toUpperCase();
                  StringBuilder key1 = new StringBuilder();
                  StringBuilder key2 = new StringBuilder();
                  StringBuilder key3 = new StringBuilder();
                  for (int i = 0; i < 16; i++) {
                	  key1.append(strkey.charAt(i));
                	  key2.append(strkey.charAt(i+16));
                	  key3.append(strkey.charAt(i+32));
                  }
                  //System.out.println(strkey);
                  ck = key1.toString();
                  ck2 = key2.toString();
                  ck3 = key3.toString();
                  /*
                  System.out.println(ck);
                  System.out.println(ck2);
                  System.out.println(ck3);
                  */
                } catch (IOException r) {
                    System.out.println("An error occurred.");
                    r.printStackTrace();
                  }
      }
      

      
      public static void main(String[] args) {
          select_key();
          encr_data();
          System.exit(0); 
          
      }
}