package ch.zkb.t632.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Day2 {
  @Value("input/puzzle2.csv")
  private Path puzzle2;

  public void solveDay1() throws IOException {
    task1a();
  }

  public void task1a() throws IOException {
    var puzzles = new ArrayList<List<Integer>>();
    var lines = Files.readAllLines(puzzle2);

    lines.stream().map(line -> line.split(" ")).forEach(line -> {
      var p1 = new ArrayList<Integer>();
      for (String s : line) {
        p1.add(Integer.parseInt(s));
      }
      puzzles.add(p1);
    });

    System.out.println("Result: " + puzzles.stream().filter(this::isSave).count());
  }

  private boolean isSave(List<Integer> p) {
    for (int i = 0; i < p.size(); i++) {
      var copy = new ArrayList<>(p);
      copy.remove(i);
      if (isInnerSave(copy)) {
        return true;
      }
    }
    return false;
  }

  private boolean isInnerSave(List<Integer> p) {
    Integer last = p.get(0);
    boolean increasing = last < p.get(1);
    for (int i = 1; i < p.size(); i++) {
      var diff = p.get(i) - last;
      last = p.get(i);
      if (increasing) {
        if (diff <= 0 || diff > 3) {
          return false;
        }
      } else {
        if (diff >= 0 || diff < -3) {
          return false;
        }
      }
    }
    return true;
  }
}
