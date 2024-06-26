package raf.dsw.classycraft.app.stateSablon.stateActions;

import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;

import java.awt.event.ActionEvent;

public class DuplicateAction extends AbstractClassyAction {

    public DuplicateAction(){
        putValue(SMALL_ICON, loadIcon("/images/duplicate.png"));
        putValue(NAME, "Napravi kopiju");
        putValue(SHORT_DESCRIPTION, "Napravi kopiju");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MainFrame.getInstance().getPackageView().startDuplicateState();
    }
}
