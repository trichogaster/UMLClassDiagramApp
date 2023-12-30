package raf.dsw.classycraft.app.controller;

import org.w3c.dom.views.AbstractView;
import raf.dsw.classycraft.app.classyCraftRepository.composite.ClassyNode;
import raf.dsw.classycraft.app.classyCraftRepository.composite.ClassyNodeComposite;
import raf.dsw.classycraft.app.classyCraftRepository.composite.classContent.Atributi;
import raf.dsw.classycraft.app.classyCraftRepository.composite.classContent.ClassContent;
import raf.dsw.classycraft.app.classyCraftRepository.composite.classContent.Metode;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.Connection;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.Interclass;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.connection.Generalizacija;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.interclass.EnumM;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.interclass.Interfejs;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.interclass.Klasa;
import raf.dsw.classycraft.app.classyCraftRepository.implementation.Dijagram;
import raf.dsw.classycraft.app.classyCraftRepository.implementation.Package;
import raf.dsw.classycraft.app.classyCraftRepository.implementation.Project;
import raf.dsw.classycraft.app.core.ApplicationFramework;
import raf.dsw.classycraft.app.errorHandler.MessageType;
import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ExportProjectToCode extends AbstractClassyAction {
    public ExportProjectToCode() {
        putValue(SMALL_ICON, loadIcon("/images/exportProjectToCode.png"));
        putValue(NAME, "Export as code");
        putValue(SHORT_DESCRIPTION, "Export as code");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        ClassyTreeItem selectedTree = MainFrame.getInstance().getClassyTree().getSelectedNode();
        if (selectedTree == null || !(selectedTree.getClassyNode() instanceof Project)) {
            if (selectedTree != null)
                ApplicationFramework.getInstance().getMessageGenerator().GenerateMessage("Samo celokupan projekat moze da se exportuje kao kod", MessageType.ERROR);
            return;
        }
        ClassyNode selectedNode = selectedTree.getClassyNode();

        File file = null;
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Odaberite folder u kom zelite sa exportujete projekat");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (jfc.showSaveDialog(MainFrame.getInstance()) == JFileChooser.APPROVE_OPTION)
            file = jfc.getCurrentDirectory();

        if (file == null)
            return;

        file = new File(file.getPath() + "/" + selectedNode.getName());

        if (file.mkdir()) {

            kreirajDir(file, (ClassyNodeComposite) selectedNode);

        } else {
            ApplicationFramework.getInstance().getMessageGenerator().GenerateMessage("Folder pod nazivom vaseg projekta vec postoji na zeljenoj lokaciji", MessageType.ERROR);
            return;
        }
    }

    public void kreirajDir(File file, ClassyNodeComposite node) {

        for (ClassyNode c : node.getChildren()) {
            if (c instanceof Package) {
                File f = new File(file.getPath() + "/" + c.getName());
                f.mkdir();
                kreirajDir(f, (ClassyNodeComposite) c);
            } else if (c instanceof Dijagram) {
                System.out.println(file.getPath());
                File f = new File(file.getPath() + "/" + c.getName() + ".java");
                System.out.println(f.getPath());
                generateCode(f, (Dijagram) c);
            }
        }

    }

    public void generateCode(File file, Dijagram d) {

        for (ClassyNode c : d.getChildren()) {
            if (c instanceof Connection) {
                Connection cn = (Connection) c;
                for (ClassyNode node : d.getChildren()) {
                    if (node instanceof Interclass) {
                        Interclass in = (Interclass) node;
                        if (cn.getFrom().poredjenje(in))
                            cn.setFrom(in);
                        if (cn.getTo().poredjenje(in))
                            cn.setTo(in);
                    }
                }
            }
        }


        try {
            FileWriter fw = new FileWriter(file);
            fw.append("package " + file.getPath() + "\n\n");

            String string = "";

            for (ClassyNode c : d.getChildren()) {
                if (c instanceof Klasa) {
                    Klasa k = (Klasa) c;
                    ///dodavanje extends
                    for (ClassyNode cn : d.getChildren()) {
                        if (cn instanceof Connection) {
                            if (cn instanceof Generalizacija) {
                                Connection cnc = (Connection) cn;
                                if (cnc.getTo().poredjenje(k)) {
                                    System.out.println(cnc.getFrom().getNaziv());
                                    System.out.println(k.getNaziv());
                                    if (cnc.getFrom() instanceof Klasa) {
                                        if (string.contains("extends"))
                                            string = string + " " + ((Connection) cn).getFrom().getNaziv();
                                        else string = string + " extends " + ((Connection) cn).getFrom().getNaziv();
                                    }
                                }
                            }
                        }
                    }
                    ///sredjivanje implements
                    for (ClassyNode cn : d.getChildren()) {
                        if (cn instanceof Connection) {
                            if (cn instanceof Generalizacija) {
                                Connection cnc = (Connection) cn;
                                if (cnc.getTo().poredjenje(k)) {
                                    if (cnc.getFrom() instanceof Interfejs) {
                                        if (string.contains("implements"))
                                            string = string + "," + ((Connection) cn).getFrom().getNaziv();
                                        else string = string + " implements " + ((Connection) cn).getFrom().getNaziv();
                                    }
                                }
                            }
                        }
                    }

                    fw.append(k.getVidljivost().toString().toLowerCase() + " class " + k.getNaziv() + string + " {" + "\n\n");
                    string = "";
                    ///ubacivanje svih atributa
                    for (ClassContent cc : k.getClassContentList()) {
                        if (cc instanceof Atributi) {
                            fw.append("\t" + vidljivostToString(cc.getVidljivost()) + " " + cc.getTip() + " " + cc.getNaziv() + ";\n");
                        }
                    }
                    ///sredjivanje veza atribut
                    for (ClassyNode v : d.getChildren()) {
                        ///From - parent, To - child  -> child extends parent
                        if (v instanceof Generalizacija) {
                            Generalizacija g = (Generalizacija) v;
                            Interclass parent = g.getFrom();
                            Interclass child = g.getTo();
                            if (child.poredjenje(k)) {
                                ubaciVezuA(fw, parent);
                            }
                        }
                    }

                    ///ubacivanje svih metoda
                    for (ClassContent cc : k.getClassContentList()) {
                        if (cc instanceof Metode) {
                            fw.append("\t" + vidljivostToString(cc.getVidljivost()) + " " + cc.getTip() + " " + cc.getNaziv() + "() {\n\n\t}\n");
                        }
                    }
                    ///sredjivanje veza metode
                    for (ClassyNode v : d.getChildren()) {
                        ///From - parent, To - child  -> child extends parent
                        if (v instanceof Generalizacija) {
                            Generalizacija g = (Generalizacija) v;
                            Interclass parent = g.getFrom();
                            Interclass child = g.getTo();
                            if (child.poredjenje(k)) {
                                ubaciVezuM(fw, parent);
                            }
                        }
                    }
                    fw.append("}");
                } else if (c instanceof Interfejs) {
                    Interfejs i = (Interfejs) c;

                    ///dodavanje extends
                    for (ClassyNode cn : d.getChildren()) {
                        if (cn instanceof Connection) {
                            if (cn instanceof Generalizacija) {
                                Connection cnc = (Connection) cn;
                                if (cnc.getTo().poredjenje(i)) {
                                    if (string.contains("extends"))
                                        string = string + ", " + ((Connection) cn).getFrom().getNaziv();
                                    else string = string + " extends " + ((Connection) cn).getFrom().getNaziv();
                                }
                            }
                        }
                    }

                    fw.append(i.getVidljivost().toString().toLowerCase() + " interface " + i.getNaziv() + string + " {" + "\n\n");
                    string="";
                    ///ubacivanje svih metoda
                    for (Metode cc : i.getMetodeList()) {
                        fw.append("\t" + vidljivostToString(cc.getVidljivost()) + " " + cc.getTip() + " " + cc.getNaziv() + "() {\n\n\t}\n");
                    }
                    ///ovde treba da dodam dodate metode iz extendova!!!!!!!!!!!!!!!!!!!!!


                    fw.append("}");
                } else if (c instanceof EnumM) {
                    EnumM e = (EnumM) c;
                    fw.append(e.getVidljivost().toString().toLowerCase() + " enum " + e.getNaziv() + " {" + "\n\n");
                    int broj = e.getListEnuma().size();
                    int brojac = 1;
                    for (String s : e.getListEnuma()) {
                        if (brojac == broj)
                            fw.append("\t" + s.toUpperCase() + ";\n");
                        else {
                            fw.append("\t" + s.toUpperCase() + ",\n");
                            brojac++;
                        }
                    }
                    fw.append("}");
                }
                fw.append("\n\n");
            }
            fw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    ///child extends parent
    public void ubaciVezuA(FileWriter fw, Interclass parent) {
        if (parent instanceof Klasa) {
            Klasa klasa = (Klasa) parent;
            ///dodavanje atributa
            for (ClassContent c : klasa.getClassContentList()) {
                if (c instanceof Atributi) {
                    try {
                        fw.append("\t" + vidljivostToString(c.getVidljivost()) + " " + c.getTip() + " " + c.getNaziv() + ";\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void ubaciVezuM(FileWriter fw, Interclass parent) {
        if (parent instanceof Klasa) {
            Klasa klasa = (Klasa) parent;
            ///dodavanje metoda
            for (ClassContent c : klasa.getClassContentList()) {
                if (c instanceof Metode) {
                    try {
                        fw.append("\t" + vidljivostToString(c.getVidljivost()) + " " + c.getTip() + " " + c.getNaziv() + "() {\n\n\t}\n");
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } else if (parent instanceof Interfejs) {
            Interfejs interfejs = (Interfejs) parent;
            for (Metode m : interfejs.getMetodeList()) {
                System.out.println(m.getNaziv());
                try {
                    fw.append("\t" + vidljivostToString(m.getVidljivost()) + " " + m.getTip() + " " + m.getNaziv() + "() {\n\n\t}\n");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String vidljivostToString(String veza) {
        switch (veza) {
            case "-":
                return "private";
            case "+":
                return "public";
            case "#":
                return "protected";
            default:
                return "?";
        }
    }
}
