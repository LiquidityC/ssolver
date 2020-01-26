# A soduko app and solver

What the title says.

There's a "naive" javafx gui because I wanted to be able to edit/save/load puzzles easily.
I'm not great at design and not super experienced with JavaFX, but it works which helps a lot.

The solver part works through a constraints algorithm. Finding options for each cell and applying that
option when there's only one. Also it checks if an option is unique for a cell within it's row/col/box and
applies that if it finds any such case.

If the constraint logic doesn't reach all the way the algorithm then takes a guess on the cell that currently
has the lowest amount of options and recurses from there.

I haven't found any puzzles that didn't get solved but I didn't try them all. Neither did I do the math proof to confirm
if the logic is sound for every case or not.

If a puzzle doesn't result in a solution the user will get a notification mentioning this. There is no
check for puzzles that might have more then one solution wich I'm told is not a valid puzzle either. The algorithm
will just solve it using the first solution found.

## Compiling
It's Gradle, just google it or do what you usually do :D
