package ch.zkb.t632.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Day4 {

  @Value("input/puzzle4_test.csv")
  private Path puzzle4_test;

  @Value("input/puzzle4_test2.csv")
  private Path puzzle4_test2;

  @Value("input/puzzle4.csv")
  private Path puzzle4;

  private static final String XMAS = "XMAS";

  private static final String XMAS_REV = "SAMX";

  private static final String MAS = "MAS";

  private static final String MAS_REV = "SAM";

  @PostConstruct
  public void solve() throws IOException {
    System.out.println("Test 1:   " + task1(puzzle4_test));
    System.out.println("Result 1: " + task1(puzzle4));
    System.out.println("Test 2:   " + task2(puzzle4_test));
    System.out.println("Result 1: " + task2(puzzle4));
  }

  public long task1(Path path) throws IOException {
    var lines = Files.readAllLines(path);
    var result = findHorizontally(lines);
    result += findVertically(lines);
    result += findOther(lines);
    return result;
  }

  public long task2(Path path) throws IOException {
    var lines = Files.readAllLines(path);
    return findXMAS(lines);
  }

  private int findHorizontally(List<String> lines) {
    var matches = 0;
    for (String line : lines) {
      for (int l = 0; l < line.length() - 3; l++) {
        var sub = line.substring(l, l + 4);
        if (sub.equals(XMAS) || sub.equals(XMAS_REV)) {
          matches++;
        }
      }
    }
    return matches;
  }

  private int findVertically(List<String> lines) {
    var matches = 0;
    for (int i = 0; i < lines.size() - 3; i++) {
      for (int l = 0; l < lines.getFirst().length(); l++) {
        var sub = "" + lines.get(i).charAt(l) + lines.get(i + 1).charAt(l) + lines.get(i + 2).charAt(l) + lines.get(i + 3).charAt(l);
        if (sub.equals(XMAS) || sub.equals(XMAS_REV)) {
          matches++;
        }
      }
    }
    return matches;
  }

  private int findOther(List<String> lines) {
    var matches = 0;
    for (int i = 0; i < lines.size() - 3; i++) {
      for (int l = 0; l < lines.getFirst().length() - 3; l++) {
        var sub = "" + lines.get(i).charAt(l) + lines.get(i + 1).charAt(l + 1) + lines.get(i + 2).charAt(l + 2) + lines.get(i + 3).charAt(l + 3);
        if (sub.equals(XMAS) || sub.equals(XMAS_REV)) {
          matches++;
        }
      }
    }

    for (int i = 0; i < lines.size() - 3; i++) {
      for (int l = 3; l < lines.getFirst().length(); l++) {
        var sub = "" + lines.get(i).charAt(l) + lines.get(i + 1).charAt(l - 1) + lines.get(i + 2).charAt(l - 2) + lines.get(i + 3).charAt(l - 3);
        if (sub.equals(XMAS) || sub.equals(XMAS_REV)) {
          matches++;
        }
      }
    }
    return matches;
  }

  private int findXMAS(List<String> lines) {
    var matches = 0;
    for (int i = 0; i < lines.size() - 2; i++) {
      for (int l = 0; l < lines.getFirst().length() - 2; l++) {
        var sub1 = "" + lines.get(i).charAt(l) + lines.get(i + 1).charAt(l + 1) + lines.get(i + 2).charAt(l + 2);
        var sub2 = "" + lines.get(i + 2).charAt(l) + lines.get(i + 1).charAt(l + 1) + lines.get(i).charAt(l + 2);
        if ((sub1.equals(MAS) || sub1.equals(MAS_REV)) && (sub2.equals(MAS) || sub2.equals(MAS_REV))) {
          matches++;
        }
      }
    }
    return matches;
  }
}
