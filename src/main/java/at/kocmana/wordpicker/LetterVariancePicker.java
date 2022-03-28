package at.kocmana.wordpicker;

import java.util.Comparator;
import java.util.List;

public class LetterVariancePicker implements WordPicker {

  @Override
  public String pickWord(List<String> options) {

    return options.stream()
        .max(Comparator.comparing(this::evaluateWord))
        .orElseThrow(() -> new IllegalArgumentException("Empty list provided"));
  }

  private long evaluateWord(String word) {
    return word.chars().distinct().count();
  }
}
