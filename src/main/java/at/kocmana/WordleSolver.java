package at.kocmana;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordleSolver {

  private static final Logger LOG = LoggerFactory.getLogger(WordleSolver.class);

  private Dictionary dictionary;
  private Wordle wordle;

  public static void main(String[] args) {
    new WordleSolver().solve();
  }

  private WordleSolver() {
    LOG.info("Setting up...");
    var dictionaryFuture = CompletableFuture.supplyAsync(Dictionary::getInstance);
    var wordleFuture = CompletableFuture.supplyAsync(Wordle::new);

    CompletableFuture<Void> requiredResources = CompletableFuture.allOf(dictionaryFuture, wordleFuture);
    try {
      requiredResources.get();
      this.dictionary = dictionaryFuture.get();
      this.wordle = wordleFuture.get();
    } catch (Exception e) {
      LOG.error("Could not setup Wordle Solver: {}", e.getMessage());
    }
  }

  public void solve() {
    wordle.removeBanner();

    LOG.info("Starting to guess...");
    while (wordle.hasGuessesLeft() && dictionary.hasWordsLeft()) {

      var guess = dictionary.pickWordAtRandomAndRemove();
      LOG.info("Trying {}", guess);
      wordle.type(guess);
      try {
        Thread.sleep(2000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

      if (!wordle.entryWasAccepted()) {
        LOG.info("Entry was not accepted (incorrect entry in dictionary?), removing entry...");
        wordle.cleanEntry();
        continue;
      }

      var result = wordle.analyzeResultsFor(guess);
      LOG.debug("Result was:\r\n\t{}", result);

      if(result.isVictory()){
        LOG.info("Victory!");
        break;
      }

      LOG.info("Guess was not correct.");
      dictionary.filterBasedOnResult(result);
    }

    LOG.info("Done.");
    wordle.close();
  }

}
