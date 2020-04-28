package com.company;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class Main {

  public static void main(String[] args) throws IOException {
    Path messagePath =
        Paths.get(
            "C:\\Users\\feesh\\Desktop\\Fall 2019\\COP3003\\MapBasedBiGram\\src\\com\\company\\messages.txt");

    // Read all lines (except empty) into a String Stream for later processing
    Stream<String> messageLines = Files.lines(messagePath).filter(line -> !line.isEmpty());
    // Then amend our earlier example by adding a split & flatMapping our Arrays
    List<String> messageWords =
        messageLines
            .map(String::toLowerCase)
            .map(line -> line.split("\\s+"))
            .flatMap(Arrays::stream)
            .collect(Collectors.toList());

    // messageWords.forEach(System.out::println);

    /*
     * Flat map puts the list into a single dimension data structure Take the stream of arrays that
     * we have, and turn it into a single flatMap string String.
     */

    // messageLines.forEach(line->System.out.println("Your Word is: " + line));
    // messageWords.forEach(System.out::println);

    /* Now we should go onto using MapReduce using the Apache Hadoop library
     *     but we are not doing this on a distributed system:
     *     You see Hadoop allows for duplicate keys in its Maps!
     *      They have DataFrames that allow for duplicates
     *        The reason? Because the maps are not on the same computer system
     *           The shuffle step is it actually putting them together on the same system
     *           For us the shuffle & reduce step are combined
     *
     * Instead we will modify and combine the shuffle and reduce steps
     */

    /*
     * Instead of using flatMap, we would use a reduce command instead
     *Pushes all the data through using non-unique keys
     * Puts all the words into their own hashmap, then reduces them by that key.
     * List<String> messageWords = messageLines
     * .map(String::toLowerCase)
     * .map(line -> line.split("\\s+"))
     * .flatMap(Arrays::stream)
     * .ReduceByKey()
     * .collect(Collectors.toList());
     */

    Map<Set<String>, Integer> bgrams = new HashMap<>();




      // System.out.println(sizeOfWordList);

      for (int i = 1; i < messageWords.size(); i++) {
        bgrams.merge(
            new HashSet<>(Arrays.asList(messageWords.get(i - 1), messageWords.get(i))),
            1,
            Integer::sum);
        // See we were using a version of MapReduce
        // We just combined the shuffle & reduce in the same step
      }
      double sizeOfWordList = (double) bgrams.size()/4;
      int sizeWOrds = bgrams.size()/2;

     // System.out.println(sizeOfWordList);
      // double sizeOfWordList = (double) messageWords.size()/2;
      // bgrams.forEach((key, value) -> System.out.println(key + ", " + value));
      ArrayList<Integer> valList = new ArrayList();
      ArrayList<String>kk = new ArrayList();


     // bgrams.forEach((key, value) -> value = (value/sizeWOrds));
     // bgrams.forEach((key, value) -> System.out.println(key + ", " + value));
      bgrams.forEach((key, value) -> valList.add(value));

      System.out.println("Enter word: ");
      Scanner in = new Scanner(System.in);
     String input = in.nextLine();

    for (int j = 0; j < messageWords.size(); j++) {
      for (int i = 0; i < valList.size(); i++) {
        double a = valList.get(i) / sizeOfWordList;
        if (a > 0.65) {
         // System.out.println("[" + messageWords.get(j) + " , " + messageWords.get(j + 1) + "]");

        }
      }
        if(input.equals(messageWords.get(j))){
            if(!kk.contains(messageWords.get(j+1))){
                kk.add(messageWords.get(j+1));
            }

        }else{
            kk.add("the");
            kk.add("this");
            kk.add("of");
        }
      }
      System.out.println(kk.get(0));
      System.out.println(kk.get(1));
      System.out.println(kk.get(2));





      // Affinity analysis
      String SFName =
          "C:\\Users\\feesh\\Desktop\\Fall 2019\\COP3003\\MapBasedBiGram\\src\\com\\company\\sampleFile.csv";

      // Create string variables for each line in the txt file.
      String line = null;
      String splitHere = ",";

      // Create file object with the name of the file that we need to open.
      File sampleFile = new File(SFName);
      BufferedReader buffR = null;

      // If the file does not exist, then print a message saying so, and exit.
      if (!sampleFile.exists()) {
        System.out.println("File does not exist");
        System.exit(1);
      }

      // Create Buffered Reader object with the file.
      buffR = new BufferedReader(new FileReader(sampleFile));

      // Initialize number of transactions.
      int totalTransactions = 0;

      // Assert that the buffered reader is not null.
      assert buffR != null;
      // Create an array of strings of the file read, and split where there is a comma exist.
      String[] features = buffR.readLine().split(splitHere);
      // Create an integer variable that holds the number of times it splits at a comma.
      int numOfFeatures = features.length;
      int numOfFeature2 = messageWords.size();


      // Create a list of the result.
      List<int[]> sampleResults = new ArrayList<>();



       //While loop that will read each line of the file and split at a comma.
      while ((line = buffR.readLine()) != null) {
            Arrays.stream(line.split(splitHere)).mapToInt(Integer::parseInt).toArray();
        totalTransactions++;
      }

      // Initialize Map of a String, Integer and a HashSet String and Integer
      Map<String, Integer> fullResults = new HashMap<>();
      Map<HashSet<String>, Integer> validResults = new HashMap<>();

      for (int[] sample : sampleResults) {

        for (int premise = 0; premise < numOfFeatures; premise++) {
          if (sample[premise] == 1) {
            fullResults.put(features[premise], fullResults.getOrDefault(features[premise], 0) + 1);
          }
          for (int conclusion = 0; conclusion < numOfFeatures; conclusion++) {
            if (conclusion == premise) {
              continue;
            }
            if (sample[conclusion] == 1) {
              validResults.put(
                  new HashSet<String>(Arrays.asList(features[premise], features[conclusion])),
                  validResults.getOrDefault(
                          new HashSet<String>(
                              Arrays.asList(features[premise], features[conclusion])),
                          0)
                      + 1);
            }
          }
        }
      }
      for (HashSet<String> featureSet : validResults.keySet()) {
        List<String> featureList = featureSet.stream().collect(Collectors.toList());

        double confidence =
            (double) fullResults.get(featureList.get(0)) / validResults.get(featureSet);
        double support = (double) validResults.get(featureSet) / totalTransactions;

        System.out.printf(
            "We show a confidence of %f that a person who "
                + "bought %s will also buy %s %n  and a support of %f that "
                + "a person will buy these items together at all.%n",
            confidence, featureList.get(0), featureList.get(1), support);

        //   System.out.println("We show a confidence of "+ confidence +
        //     " that a person who bought " + featureList.get(0) +" will also buy
        // "+featureList.get(1) +
        //   "  and a support of that "+support+
        // " a person will buy these items together at all.");

      }


      buffR.close();
    }
  }

