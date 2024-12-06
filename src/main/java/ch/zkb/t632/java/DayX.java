package ch.zkb.t632.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DayX {

  @Value("input/puzzle6_test.csv")
  private Path puzzle6_test;

  @Value("input/puzzle6.csv")
  private Path puzzle6;

  @PostConstruct
  public void solve() throws IOException {
    System.out.println("Test 1:   " + task1(puzzle6_test));
    System.out.println("Result 1: " + task1(puzzle6));
  }

  public long task1(Path path) throws IOException {
    var lines = Files.readAllLines(path);

    return lines.size();

  }

}
