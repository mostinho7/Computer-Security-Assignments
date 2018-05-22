package mflaifel;

import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static void main(String[] args) {

        String encryptionKey = (args.length >= 1) ? args[0] : null;
        Cypher cypher = new Cypher(encryptionKey);

        String deCypheredKey = "";

        if (encryptionKey == null) {
            //we weren't given an encryption key
            //so we are decrypting the cipherText and finding the key

            int foundKeyLength = 0;

            outerLoop:
            for (int keyLength = 1; keyLength < cypher.fileContents.length(); keyLength++) {
                ArrayList<Double> frequencyArray = cypher.getLetterFrequencies(keyLength, 0);
                System.out.println("**********");
                //System.out.println("Frequency array for key of length " + keyLength);

                for (int index = 0; index < frequencyArray.size(); index++) {
                    if (frequencyArray.get(index) > 10) {
                        System.out.println("MOS: found keyLength " + keyLength);
                        foundKeyLength = keyLength;
                        break outerLoop;
                    }
                }
                System.out.println("**********");
            }

            //once we have the keylength, we can start figuring out what the key is
            //find the monoalphabetic key at each index
            for (int startingIndex = 0; startingIndex < foundKeyLength; startingIndex++) {
                ArrayList<Double> frequencyArray = cypher.getLetterFrequencies(foundKeyLength, startingIndex);
                //the frequency array is the mapping for key character at index startingIndex
                //determine which letters in plain (english) text was mapped to what letter
                //we know most common letter in english alphabet is e with frequency of ~ 12%
                int indexOfLargestFrequency = -1;
                double largestFreq = Collections.max(frequencyArray);
                //find the index of the largest value in frequency array
                for (int freqIndex = 0; freqIndex < frequencyArray.size(); freqIndex++) {
                    indexOfLargestFrequency = (frequencyArray.get(freqIndex) >= largestFreq) ? freqIndex : indexOfLargestFrequency;
                }

                //System.out.println("MOS: e was mapped to " + (char) (indexOfLargestFrequency + 97));
                //calculate key at current index
                int keyCharAtCurrentIndex = ((indexOfLargestFrequency + 97 - 101 + 26) % 26) + 97;
                //System.out.println("MOS: keyCharAtCurrentIndex " + (char) keyCharAtCurrentIndex);
                deCypheredKey = deCypheredKey.concat(String.valueOf((char) keyCharAtCurrentIndex));
            }

            System.out.println("MOS: key is " + deCypheredKey);
        }

        cypher.cypherTextWithKey((encryptionKey == null) ? deCypheredKey : encryptionKey);
    }
}
