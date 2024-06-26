package raf.dsw.classycraft.app.controller;

import raf.dsw.classycraft.app.classyCraftRepository.implementation.Dijagram;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.errorHandler.MessageType;
import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

public class NewChildAction extends AbstractClassyAction{

    public NewChildAction() {
        putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        putValue(SMALL_ICON, loadIcon("/images/plus.png"));
        putValue(NAME, "New child");
        putValue(SHORT_DESCRIPTION, "New child");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ClassyTreeItem selected = (ClassyTreeItem) MainFrame.getInstance().getClassyTree().getSelectedNode();
        if(selected != null && !(selected.getClassyNode() instanceof Dijagram))
            MainFrame.getInstance().getClassyTree().addChild(selected, null);
        else if(selected != null && selected.getClassyNode() instanceof Dijagram){

        }
        else
            ApplicationFramework.getInstance().getMessageGenerator().GenerateMessage("Nije selektovan roditelj", MessageType.ERROR);
    }
}
