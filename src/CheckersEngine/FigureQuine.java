package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;

public class FigureQuine extends FigureParent {

    public FigureQuine(ETypeColor colorFig) {
        super(ETypeFigure.QUINE, colorFig);
    }

    public FigureQuine(ETypeColor colorFig, short x, short y) {
        super(ETypeFigure.QUINE, colorFig, x, y);
    }

    @Override
    public void searchMove() {

    }

    @Override
    public void searchAttack() {

    }

}
