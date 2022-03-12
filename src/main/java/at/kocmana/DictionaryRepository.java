package at.kocmana;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DictionaryRepository {

  private static final Logger LOG = LoggerFactory.getLogger(DictionaryRepository.class);

  private static final String DICTIONARY_FILE = "words.txt";
  private static final String REPOSITORY_URL =
      "https://raw.githubusercontent.com/dwyl/english-words/master/words_alpha.txt";

  private DictionaryRepository() {
    if (!dictionaryFileExists()) {
      try {
        LOG.info("Dictionary file not found, downloading.");
        downloadDictionaryFile();
      } catch (Exception e) {
        LOG.error("Could not retrieve dictionary: {}.", e.getMessage());
        System.exit(-1);
      }
    }
  }

  public static DictionaryRepository prepareDictionary() {
    return new DictionaryRepository();
  }

  public DictionaryRepository and() {
    return this;
  }

  public List<String> parseWordList() {
    return parseDictionaryFile();
  }

  private boolean dictionaryFileExists() {
    var path = Path.of(DICTIONARY_FILE);
    return Files.exists(path);
  }

  private void downloadDictionaryFile() throws IOException {
    URL url = new URL(REPOSITORY_URL);
    try (ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
         FileOutputStream fileOutputStream = new FileOutputStream(DICTIONARY_FILE);
         FileChannel fileChannel = fileOutputStream.getChannel()) {
      fileChannel.transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
    }
    LOG.info("Download complete.");
  }

  private List<String> parseDictionaryFile() {
    var wordleEligibleWords = Pattern.compile("[a-z]{5}").asMatchPredicate();

    var path = Path.of(DICTIONARY_FILE);
    try (var stream = Files.lines(path, StandardCharsets.UTF_8)) {
      return stream.map(String::toLowerCase)
          .filter(wordleEligibleWords)
          .collect(Collectors.toList()); // we need a mutable list here to allow to remove singular entries
    } catch (IOException e) {
      throw new IllegalStateException("Could not find dictionary");
    }
  }

}
