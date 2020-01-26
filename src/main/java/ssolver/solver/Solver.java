package ssolver.solver;

import ssolver.solver.model.Board;
import ssolver.solver.model.Cell;

/**
 * Class that solves sudoku puzzles.
 * The whole structure of this class is slightly half-assed.
 * It's decoupled but the whole thing could be replaced by one static method. Depends on where you need to
 * use it. Also depends a lot on performance if there are puzzles it can't/won't solve.
 *
 * Threading might be an idea to apply but it might turn out to cost more then a linear approach
 * No point now since execution time is generally < 1sec.
 * The usage of constraints creates a sufficient "cut-away" heuristic to make a single threaded approach
 * sufficiently fast.
 */
public class Solver {

    public int[][] solve(int[][] numbers) {
        Board board = new Board(numbers);
        try {
            // Apply obvious numbers
            var changes = true;
            while (changes) {
                changes = board.checkUniqueOptions();
                changes = board.setObviousChoices() | changes;
            }

            // Check if we are done...
            if (board.isComplete()) {
                return board.getNumbers();
            }

            // ..or recurse
            return recursionSolve(board);

        } catch (Exception e) {
            throw e;
        }
    }

    private int[][] recursionSolve(Board board) {
        /*
            We operate on the int matrix during recursion because java is all references and no clones :D
            We could have made logic to clone the actual board but that's a balance between processor
            cycles and memory consumption. And let's be honest, it's java, who's going to notice the difference?
         */
        var numbers = board.getNumbers();

        Cell cell = board.getCellWithLowestOptionCount();
        if (cell != null) {
            for (var opt : cell.getOptions()) {
                numbers[cell.getCol()][cell.getRow()] = opt;

                var result = solve(numbers);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }
}
