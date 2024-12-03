package ch.zkb.t632;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Day4 {

  @Value("input/puzzle4_test.csv")
  private Path puzzle4_test;

  @Value("input/puzzle4.csv")
  private Path puzzle4;

  @PostConstruct
  public void solve() throws IOException {
    System.out.println("Test:   " + task(puzzle4_test));
    System.out.println("Result: " + task(puzzle4));
  }

  public long task(Path path) throws IOException {
    var lines = Files.readAllLines(path);
    var input = String.join("", lines);
    var splitted = input.split("don't\\(\\)");
    var edited = splitted[0];
    for (int i = 1; i < splitted.length; i++) {
      var index = splitted[i].indexOf("do()");
      if (index > -1) {
        edited += splitted[i].substring(index);
      }
    }
    var pattern = Pattern.compile("mul\\((\\d{0,3}),(\\d{0,3})\\)");
    var matcher = pattern.matcher(edited);
    long result = 0;
    while (matcher.find()) {
      result += Long.parseLong(matcher.group(1)) * Long.parseLong(matcher.group(2));
    }

    return result;
  }

}
