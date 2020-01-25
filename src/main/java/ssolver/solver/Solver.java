package ssolver.solver;

import ssolver.solver.model.Board;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class that solves soduko puzzles.
 * The whole structure of this class is slightly half-assed.
 * It's decoupled but the whole thing could be replaced by one static method. Depends on where you need to
 * use it. Also depends a lot on performance if there are puzzles it can't/won't solve.
 *
 * Threading might be possible to apply but it might turn out to cost more then a linear approach
 * No point now since execution time is generally < 1sec
 */
public class Solver {

    private int[][] numbers;
    private final Board board = new Board();

    public Solver(int[][] numbers) {
        this.numbers = numbers;
        board.setNumbers(numbers);
    }

    public int[][] solve() {
        try {
            var changes = true;
            while (changes) {
                changes = board.checkUniqueOptions();
                changes = board.setObviousChoices() | changes;
            }
        } catch (Exception e) {
            throw e;
        }
        return board.getNumbers();
    }
}
