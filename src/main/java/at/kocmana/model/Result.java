package at.kocmana.model;

import static java.util.stream.Collectors.groupingBy;

import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Result {

  private final List<LetterResult> letterResults;
  private final Predicate<String> wordAdheresToResult;

  public Result(List<LetterResult> letterResultResults) {
    this.letterResults = letterResultResults;
    this.wordAdheresToResult = generatePresentPredicate().and(generateCorrectPredicate());
  }

  public boolean checkAdherence(String word) {
    return wordAdheresToResult.test(word);
  }

  public boolean isVictory() {
    return letterResults.stream()
        .allMatch(letterResult -> letterResult.state().equals(LetterState.CORRECT));
  }

  @Override
  public String toString() {
    return letterResults.stream()
        .map(LetterResult::toString)
        .collect(Collectors.joining("\r\n\t"));
  }

  private Predicate<String> generatePresentPredicate() {
    var groupedResult = letterResults.stream()
        .collect(groupingBy(LetterResult::letter));
    return groupedResult.entrySet()
        .stream()
        .map(entry -> generateCharPresentPredicate(entry.getKey(), entry.getValue()))
        .reduce(Predicate::and)
        .orElseThrow();
  }

  private Predicate<String> generateCharPresentPredicate(char character, List<LetterResult> results) {
    var bound = results.stream()
        .filter(resultingLetters -> resultingLetters.state().isCorrect())
        .count();

    var isAbsoluteBound = results.stream()
        .anyMatch(resultingLetters -> resultingLetters.state().equals(LetterState.ABSENT));

    if (isAbsoluteBound) {
      return s -> s.codePoints().filter(ch -> ch == character).count() == bound;
    } else {
      return s -> s.codePoints().filter(ch -> ch == character).count() >= bound;
    }
  }

  private Predicate<String> generateCorrectPredicate() {
    var sb = new StringBuilder(".....");
    letterResults.stream()
        .filter(letterResult -> letterResult.state().equals(LetterState.CORRECT))
        .forEach(letterResult -> sb.setCharAt(letterResult.position(), letterResult.letter()));

    return Pattern.compile(sb.toString()).asMatchPredicate();
  }


}
