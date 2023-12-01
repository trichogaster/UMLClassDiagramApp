package raf.dsw.classycraft.app.stateSablon.stateActions;

import com.sun.tools.javac.Main;
import raf.dsw.classycraft.app.controller.AbstractClassyAction;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class AddInterclassAction extends AbstractClassyAction {

    public AddInterclassAction() {
        //putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        putValue(SMALL_ICON, loadIcon("/images/add.png"));
        putValue(NAME, "");
        putValue(SHORT_DESCRIPTION, "");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object[] options = {"Class", "Interface", "Enum"};
        int n = JOptionPane.showOptionDialog(MainFrame.getInstance(), "Odaberite opciju:", "Interclass", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if(n == 0)
            MainFrame.getInstance().getPackageView().getStateManager().getAddInterclass().setInterclass("class");
        else if(n == 1)
            MainFrame.getInstance().getPackageView().getStateManager().getAddInterclass().setInterclass("interface");
        else if(n == 2)
            MainFrame.getInstance().getPackageView().getStateManager().getAddInterclass().setInterclass("enum");

        MainFrame.getInstance().getPackageView().startAddInterclassState();
    }
}