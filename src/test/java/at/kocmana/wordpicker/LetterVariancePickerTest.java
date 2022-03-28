package at.kocmana.wordpicker;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.Test;

class LetterVariancePickerTest {

  final LetterVariancePicker underTest = new LetterVariancePicker();

  @Test
  void pickWord() {
    //given
    var wordList = List.of("aaaa", "aaab", "aabc", "abcd");
    var expected = "abcd";

    //when
    var actualResult = underTest.pickWord(wordList);

    //then
    assertThat(actualResult).isEqualTo(expected);
  }
}
