package at.kocmana.wordpicker;

import java.util.List;
import java.util.Random;

public class RandomPicker implements WordPicker {

  private final Random random = new Random();

  @Override
  public String pickWord(List<String> options) {
    var index = random.nextInt(options.size());

    return options.get(index);
  }
}
