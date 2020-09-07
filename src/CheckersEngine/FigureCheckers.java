package CheckersEngine;

import CheckersEngine.BaseEngine.ETypeColor;
import CheckersEngine.BaseEngine.ETypeFigure;

public class FigureCheckers extends FigureParent {

    public FigureCheckers(ETypeColor colorFig) {
        super(ETypeFigure.CHECKERS, colorFig);
    }

    public FigureCheckers(ETypeColor colorFig, short x, short y) {
        super(ETypeFigure.CHECKERS, colorFig, x, y);
    }

    @Override
    public void searchMove() {

    }

    @Override
    public void searchAttack() {

    }

}
