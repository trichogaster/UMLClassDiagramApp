package raf.dsw.classycraft.app.controller;

import raf.dsw.classycraft.app.classyCraftRepository.implementation.Project;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

public class SaveAsAction extends AbstractClassyAction{
    public SaveAsAction() {

        //putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, ActionEvent.CTRL_MASK));
        putValue(SMALL_ICON, loadIcon("/images/saveAs.png"));
        putValue(NAME, "Save As");
        putValue(SHORT_DESCRIPTION, "SaveAs project");

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JFileChooser jfc = new JFileChooser();

        if(MainFrame.getInstance().getClassyTree().getSelectedNode() == null || !((MainFrame.getInstance().getClassyTree().getSelectedNode().getClassyNode()) instanceof Project))
            return;

        Project project = (Project) MainFrame.getInstance().getClassyTree().getSelectedNode().getClassyNode();
        File projectFile = null;

        if(project.getFilePath() == null || project.getFilePath().isEmpty() || project.getFilePath() != null){
            if(jfc.showSaveDialog(MainFrame.getInstance()) == JFileChooser.APPROVE_OPTION){
                projectFile = jfc.getSelectedFile();
                project.setFilePath(projectFile.getPath());
            }
            else {
                return;
            }
        }

        ApplicationFramework.getInstance().getSerializer().saveProject(project);

    }
}
