package at.kocmana;

import static at.kocmana.model.LetterState.ABSENT;
import static at.kocmana.model.LetterState.CORRECT;
import static at.kocmana.model.LetterState.PRESENT;
import static org.assertj.core.api.Assertions.assertThat;

import at.kocmana.model.LetterResult;
import at.kocmana.model.LetterState;
import at.kocmana.model.Result;
import java.util.ArrayList;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class ResultTest {

  @ParameterizedTest
  @CsvSource({
      "--a--,true",
      "--b--,false",
      "a----,false"
  })
  void whenOneCorrectEntryIsAvailable_thenMappingWorks(String wordToCheck, boolean expectedResult) {
    //given
    var underTest = generateResult("__a__", ABSENT, ABSENT, CORRECT, ABSENT, ABSENT);

    //when
    var actualResult = underTest.checkAdherence(wordToCheck);

    //then
    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource({
      "--a-b,true",
      "--a-a,false",
      "ab---,false"
  })
  void whenTwoCorrectEntryAreAvailable_thenMappingWorks(String wordToCheck, boolean expectedResult) {
    //given
    var underTest = generateResult("__a_b", ABSENT, ABSENT, CORRECT, ABSENT, CORRECT);

    //when
    var actualResult = underTest.checkAdherence(wordToCheck);

    //then
    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource({
      "--a--,true", //This is actually not true, we are still missing the information that the character must not be here
      "--b--,false",
      "a----,true"
  })
  void whenOnePresentEntryIsAvailable_thenMappingWorks(String wordToCheck, boolean expectedResult) {
    //given
    var underTest = generateResult("__a__", ABSENT, ABSENT, PRESENT, ABSENT, ABSENT);

    //when
    var actualResult = underTest.checkAdherence(wordToCheck);

    //then
    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource({
      "a-a--,true", //This is actually not true, we are still missing the information that the character must not be here
      "--b--,false",
      "a----,false",
      "-a-aa,true"
  })
  void whenMultiplePresentEntriesAreAvailable_thenMappingWorks(String wordToCheck, boolean expectedResult) {
    //given
    var underTest = generateResult("a_a__", PRESENT, ABSENT, PRESENT, ABSENT, ABSENT);

    //when
    var actualResult = underTest.checkAdherence(wordToCheck);

    //then
    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource({
      "a-a--,true", //This is actually not true, we are still missing the information that the character must not be here
      "--b--,false",
      "a----,false",
      "-a-aa,false"
  })
  void whenMultiplePresentEntriesAreAvailableAndUpperLimitIsKnown_thenMappingWorks(String wordToCheck, boolean expectedResult) {
    //given
    var underTest = generateResult("a_a_a", PRESENT, ABSENT, PRESENT, ABSENT, ABSENT);

    //when
    var actualResult = underTest.checkAdherence(wordToCheck);

    //then
    assertThat(actualResult).isEqualTo(expectedResult);
  }

  @ParameterizedTest
  @CsvSource({
      "--a--,false", //This is actually not true, we are still missing the information that the character must not be here
      "--b--,true",
      "aa---,false"
  })
  void whenOneAbsentEntryIsAvailable_thenMappingWorks(String wordToCheck, boolean expectedResult) {
    //given
    var underTest = generateResult("__a__", ABSENT, ABSENT, ABSENT, ABSENT, ABSENT);

    //when
    var actualResult = underTest.checkAdherence(wordToCheck);

    //then
    assertThat(actualResult).isEqualTo(expectedResult);
  }

  public Result generateResult(String word, LetterState... states) {
    var results = new ArrayList<LetterResult>();
    for (int i = 0; i < word.toCharArray().length; i++) {
      var letter = new LetterResult(word.charAt(i), i, states[i]);
      results.add(letter);
    }
    return new Result(results);
  }

}
