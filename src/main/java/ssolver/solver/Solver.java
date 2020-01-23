package ssolver.solver;

import ssolver.solver.model.Board;

import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Solver {

    private Consumer<int[][]> changeObserver;
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
                var changes = true;
                while (changes) {
                    board.reduceAllOptions();
                    changes = board.setObviousChoices();
                    changeObserver.accept(board.getNumbers());
                }
            }
        };
        thread.start();
    }

    public void setChangeObserver(Consumer<int[][]> changeObserver) {
        this.changeObserver = changeObserver;
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
