package ch.zkb.t632.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Day1 {
  @Value("input/puzzle1.csv")
  private Path input1;

  // @PostConstruct
  public void solveDay1() throws IOException {
    task1a();
    task1b();
  }


  public void task1a() throws IOException {
    var firsts = new ArrayList<Integer>();
    var seconds = new ArrayList<Integer>();
    var lines = Files.readAllLines(input1);
    lines.stream().map(line -> line.split(",")).forEach(line -> {
      firsts.add(Integer.parseInt(line[0]));
      seconds.add(Integer.parseInt(line[1]));
    });
    Collections.sort(firsts);
    Collections.sort(seconds);
    int sum = 0;
    for (int i = 0; i < firsts.size(); i++) {
      sum += Math.abs(firsts.get(i) - seconds.get(i));
    }
    System.out.println("Abstand: " + sum);
  }

  public void task1b() throws IOException {
    var lines = Files.readAllLines(input1);
    var firsts = new ArrayList<Integer>();
    var secondCount = new HashMap<Integer, Integer>();
    lines.stream().map(line -> line.split(",")).forEach(line -> {
      firsts.add(Integer.parseInt(line[0]));
      var secondNumber = Integer.parseInt(line[1]);
      var number = secondCount.getOrDefault(secondNumber, 0);
      secondCount.put(secondNumber, ++number);
    });
    int sum = 0;
    for (int i = 0; i < firsts.size(); i++) {
      var first = firsts.get(i);
      sum += first * secondCount.getOrDefault(first, 0);
    }
    System.out.println("Simularität: " + sum);
  }

}
