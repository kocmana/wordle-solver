package at.kocmana.wordpicker;

import java.util.List;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

public class ProbabilisticLetterVariancePicker implements WordPicker {

  public static final double WEIGHT_FACTOR = 1.0;

  @Override
  public String pickWord(List<String> options) {

    var weightedWords = options.parallelStream()
        .map(word -> new Pair<>(word, calculateWeight(word) * WEIGHT_FACTOR))
        .toList();

    var enumeratedDistribution = new EnumeratedDistribution<>(weightedWords);

    return enumeratedDistribution.sample();
  }

  double calculateWeight(String word) {
    return word.chars().distinct().count();
  }

}
