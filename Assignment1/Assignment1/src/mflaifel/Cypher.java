package mflaifel;

import com.sun.org.apache.xpath.internal.operations.Bool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.DoubleStream;

public class Cypher {

    String fileContents = "";
    private ArrayList<Double> letterFrequencyArray = new ArrayList<Double>(Collections.nCopies(26, 0.0));
    private final String cypherTextFileName = "../cipherText.txt";
    private final String plainTextFileName = "../plainTextToDecrypt.txt";

    private Boolean decrypt = true;

    public Cypher(String encryptionKey) {
        //Read the file
        try {

            this.decrypt = (encryptionKey == null);
            FileReader fileReader = new FileReader(this.decrypt ? this.cypherTextFileName : this.plainTextFileName);

            //BufferedReader wraps around readers, like FileReader to buffer input and improve efficiency
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            String lineOfText = null;
            while((lineOfText = bufferedReader.readLine()) != null) {
                this.fileContents = this.fileContents.concat(lineOfText);
            }

            bufferedReader.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        System.out.println("MOS: Cypher initialized with text " + this.fileContents);
    }

    //calculate the frequencies for given keyLength
    //for keyLength of 1, it's a cesar cypher
    public ArrayList<Double> getLetterFrequencies(int keyLength, int startingIndex) {

        for (int charArrayIndex = startingIndex; charArrayIndex < this.fileContents.length(); charArrayIndex = charArrayIndex + keyLength) {
            int ascii = (int) this.fileContents.charAt(charArrayIndex);
            //System.out.println(ascii);
            int index = ascii - 97;
            //System.out.println("mos: index is " + index);
            Double frequency = (this.letterFrequencyArray.get(index) + 1);
            this.letterFrequencyArray.set(index, frequency);
        }

        double frequencyArraySum = 0;
        for (double freq : this.letterFrequencyArray) {
            frequencyArraySum += freq;
        }

        for (int freqArrayIndex = 0; freqArrayIndex < this.letterFrequencyArray.size(); freqArrayIndex++) {
            double percentageValue = this.letterFrequencyArray.get(freqArrayIndex) * 100 / frequencyArraySum;
            this.letterFrequencyArray.set(freqArrayIndex, percentageValue);
        }

        double frequencySum = 0;
        for (int freqArrayIndex = 0; freqArrayIndex < this.letterFrequencyArray.size(); freqArrayIndex++) {
            frequencySum += this.letterFrequencyArray.get(freqArrayIndex);
            char freqArrayChar = (char) (freqArrayIndex + 97);
            System.out.println(
                    "MOS: with keyLength " +  keyLength + " starting at index " +
                    startingIndex + " letter " + freqArrayChar + " has freq: " +
                    this.letterFrequencyArray.get(freqArrayIndex)
            );
        }
        System.out.println("mos: sum of frequencies is " + frequencySum);

        return this.letterFrequencyArray;
    }

    public Boolean cypherTextWithKey(String key) {
        String outPutText = "";
        for (int cipherIndex = 0; cipherIndex < this.fileContents.length(); cipherIndex++) {
            //get character to shift
            int toShift = (int) this.fileContents.charAt(cipherIndex);
            //System.out.println("mos: shifting with key char " + key.charAt(cipherIndex % key.length()));
            int toShiftBy = ((int) key.charAt(cipherIndex % key.length())) - 97;
            if (this.decrypt) {
                toShiftBy = toShiftBy * -1;
            }
            int shifted = toShift + toShiftBy;
            if (shifted < 97) {
                //went too far back, must loop around
                shifted += 26;
            }
            if (shifted > 122) {
                shifted -= 26;
            }
            //System.out.println("mos: appending char " + (char) shifted);
            outPutText = outPutText.concat(((char) shifted) + "");
        }
        System.out.println(
                "MOS: " + ((this.decrypt) ? "decrypted text: ": "encrypted text")
                + " is " + outPutText
        );
        return true;
    }
}
