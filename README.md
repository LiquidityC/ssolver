# A soduko app and solver

What the title says.

There's a "naive" javafx gui because I wanted to be able to edit/save/load puzzles easily.
I'm not great at design and not super experienced with JavaFX, but it works which helps a lot.

The solver part works through a constraints algorithm. Finding options for each cell and applying that
option when there's only one. Also it checks if an option is unique for a cell within it's row/col/box and
applies that if it finds any such case.

I haven't found any puzzles that didn't get solved but I didn't try them all. Neither did I do the math proof to confirm
if the logic is sound for every case or not.

## Compiling
It's Gradle, just google it or do what you usually do :D
