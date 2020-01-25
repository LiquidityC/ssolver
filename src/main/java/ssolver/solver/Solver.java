package ssolver.solver;

import ssolver.solver.model.Board;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solver {

    private Consumer<int[][]> changeObserver;
    private Runnable doneObserver;

    private int[][] numbers;
    private final Board board = new Board();

    public Solver(int[][] numbers) {
        this.numbers = numbers;
        board.setNumbers(numbers);
    }

    public void run() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    var changes = true;
                    while (changes) {
                        changes = board.checkUniqueOptions();
                        changes = board.setObviousChoices() | changes;
                    }
                } catch (Exception e) {
                    throw e;
                } finally {
                    changeObserver.accept(board.getNumbers());
                    doneObserver.run();
                }
            }
        };
        thread.start();
    }

    public void setChangeObserver(Consumer<int[][]> changeObserver) {
        this.changeObserver = changeObserver;
    }

    public void setDoneObserver(Runnable doneObserver) {
        this.doneObserver = doneObserver;
    }

    private int[][] copy(int[][] numbers) {
        return (int[][]) Stream.of(numbers).collect(Collectors.toList()).toArray();
    }

    private boolean equals(int[][] n1, int[][] n2) {
        var i1 = Stream.of(n1).flatMap(Stream::of).iterator();
        var i2 = Stream.of(n2).flatMap(Stream::of).iterator();
        while (i1.hasNext() && i2.hasNext()) {
            if (i1.next() != i2.next()) {
                return false;
            }
        }
        return !i1.hasNext() && !i2.hasNext();
    }
}
