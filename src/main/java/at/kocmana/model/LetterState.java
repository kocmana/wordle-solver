package at.kocmana.model;

import static java.util.Objects.isNull;

import java.util.Locale;
import java.util.Optional;

public enum LetterState {
  ABSENT, PRESENT, CORRECT, UNKNOWN;

  public static LetterState fromString(String state) {
    if(isNull(state)) {
      return LetterState.UNKNOWN;
    }
    return Optional.of(LetterState.valueOf(state.toUpperCase(Locale.ROOT)))
        .orElse(LetterState.UNKNOWN);
  }

  public boolean isCorrect() {
    return this.equals(PRESENT) || this.equals(CORRECT);
  }
}
