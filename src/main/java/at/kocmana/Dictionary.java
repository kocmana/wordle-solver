package at.kocmana;

import static java.util.Objects.isNull;

import at.kocmana.model.Result;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Dictionary {

  private static Dictionary instance;
  private final Random random = new Random();
  private List<String> words;

  public static Dictionary getInstance() {
    if (isNull(instance)) {
      instance = new Dictionary();
    }
    return instance;
  }

  private Dictionary() {
    var wordleEligibleWords = Pattern.compile("[a-z]{5}").asMatchPredicate();

    var path = Path.of("words.txt");
    try {
      words = Files.lines(path)
          .map(String::toLowerCase)
          .filter(wordleEligibleWords)
          .collect(Collectors.toList()); // we need a mutable list here, this is however not an issue
    } catch (IOException e) {
      throw new IllegalStateException("Could not find dictionary");
    }
  }

  public void filterBasedOnResult(Result result) {
    words = words.stream()
        .filter(result::checkAdherence)
        .collect(Collectors.toList());
  }

  public boolean hasWordsLeft() {
    return !words.isEmpty();
  }

  public String pickWordAtRandomAndRemove() {
    if(!this.hasWordsLeft()) {
      throw new IllegalStateException("Dictionary has no more words for the combination of results");
    }
    var index = random.nextInt(words.size());
    var pick = words.get(index);
    words.remove(index);
    return pick;
  }

}
