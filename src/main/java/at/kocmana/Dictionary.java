package at.kocmana;

import at.kocmana.model.Result;
import at.kocmana.wordpicker.WordPicker;
import java.util.List;
import java.util.stream.Collectors;

public class Dictionary {

  private WordPicker wordPicker;
  private List<String> words;

  public static Dictionary withPicker(WordPicker wordPicker) {
    return new Dictionary(wordPicker);
  }

  private Dictionary(WordPicker wordPicker) {
    this.wordPicker = wordPicker;
    this.words = DictionaryRepository.prepareDictionary().and().parseWordList();
  }

  private Dictionary() {
    this.words = DictionaryRepository.prepareDictionary().and().parseWordList();
  }

  public void filterBasedOnResult(Result result) {
    words = words.stream()
        .filter(result::checkAdherence)
        .collect(Collectors.toList());
  }

  public boolean hasWordsLeft() {
    return !words.isEmpty();
  }

  public String pickWordAndRemove() {
    if (!this.hasWordsLeft()) {
      throw new IllegalStateException("Dictionary has no more words for the combination of results");
    }
    var pick = wordPicker.pickWord(words);
    words.remove(pick);
    return pick;
  }

}
