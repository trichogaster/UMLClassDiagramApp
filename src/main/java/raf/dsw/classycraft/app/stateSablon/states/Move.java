package raf.dsw.classycraft.app.stateSablon.states;

import raf.dsw.classycraft.app.gui.swing.view.DijagramView;
import raf.dsw.classycraft.app.stateSablon.State;

public class Move implements State {

    @Override
    public void misKliknut(int x, int y, DijagramView dijagramView) {

    }

    @Override
    public void misOtpusten(int x, int y, DijagramView dijagramView) {
        dijagramView.removeTranslation();
        dijagramView.setDx2(x);
        dijagramView.setDy2(y);
        dijagramView.repaint();
    }

    @Override
    public void misPrivucen(int x, int y, DijagramView dijagramView) {
        dijagramView.setTranslation();
            dijagramView.setDx1(x);
            dijagramView.setDy1(y);
        dijagramView.repaint();
    }
}
