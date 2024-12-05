package ch.zkb.t632.java;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.annotation.PostConstruct;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Day5 {

  @Value("input/puzzle5_test.csv")
  private Path puzzle5_test;

  @Value("input/puzzle5_testprint.csv")
  private Path puzzle5_testprint;

  @Value("input/puzzle5.csv")
  private Path puzzle5;

  @Value("input/puzzle5_print.csv")
  private Path puzzle5_print;

  @PostConstruct
  public void solve() throws IOException {
    System.out.println("Test 1:   " + task1(puzzle5_test, puzzle5_testprint));
    System.out.println("Result 1: " + task1(puzzle5, puzzle5_print));
    System.out.println("Test 2:   " + task2(puzzle5_test, puzzle5_testprint));
    System.out.println("Result 2: " + task2(puzzle5, puzzle5_print));
  }

  public long task1(Path path, Path print) throws IOException {
    var orderMap = createOrderMap(path);

    var printInstructions = Files.readAllLines(print).stream().map(line -> Arrays.stream(line.split(",")).map(Integer::parseInt).collect(
        Collectors.toList())).toList();
    var filtered = printInstructions.stream().filter(instrution -> checkInstruction(instrution, orderMap)).map(this::middle).toList();
    return filtered.stream().mapToInt(Integer::intValue).sum();
  }

  public long task2(Path path, Path print) throws IOException {
    var orderMap = createOrderMap(path);

    var printInstructions = Files.readAllLines(print).stream().map(line -> Arrays.stream(line.split(",")).map(Integer::parseInt).collect(
        Collectors.toList())).toList();
    var filtered =
        printInstructions.stream().filter(instrution -> !checkInstruction(instrution, orderMap)).map(instruction -> order(instruction, orderMap))
            .map(this::middle).toList();
    return filtered.stream().mapToInt(Integer::intValue).sum();
  }

  @NotNull
  private static HashMap<Integer, Set<Integer>> createOrderMap(Path path) throws IOException {
    var order = Files.readAllLines(path);
    var orderMap = new HashMap<Integer, Set<Integer>>();
    order.forEach(line -> {
      var splitted = line.split("\\|");
      var before = Integer.parseInt(splitted[0]);
      var after = Integer.parseInt(splitted[1]);
      var befors = orderMap.getOrDefault(after, new HashSet<>());
      befors.add(before);
      orderMap.put(after, befors);
    });
    return orderMap;
  }

  private boolean checkInstruction(List<Integer> instruction, HashMap<Integer, Set<Integer>> orderMap) {
    for (int i = 0; i < instruction.size(); i++) {
      var current = instruction.get(i);
      var befors = orderMap.get(current);
      if (befors != null) {
        for (int r = i + 1; r < instruction.size(); r++) {
          var rest = instruction.get(r);
          if (befors.contains(rest)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  private Integer middle(List<Integer> instructions) {
    return instructions.get((instructions.size() - 1) / 2);
  }

  private List<Integer> order(List<Integer> instruction, HashMap<Integer, Set<Integer>> orderMap) {
    instruction.sort((o1, o2) -> {
      var afters = orderMap.get(o1);
      if (afters != null && afters.contains(o2)) {
        return -1;
      }
      var afters2 = orderMap.get(o2);
      if (afters2 != null && afters2.contains(o1)) {
        return 1;
      }
      return 0;
    });
    return instruction;
  }

}
