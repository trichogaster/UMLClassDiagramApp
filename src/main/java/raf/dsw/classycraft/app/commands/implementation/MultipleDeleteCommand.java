package raf.dsw.classycraft.app.commands.implementation;

import javafx.util.Pair;
import raf.dsw.classycraft.app.classyCraftRepository.composite.ClassyNode;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.Connection;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.DijagramElement;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.Interclass;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.connection.*;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.interclass.EnumM;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.interclass.Interfejs;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.interclass.Klasa;
import raf.dsw.classycraft.app.classyCraftRepository.implementation.Dijagram;
import raf.dsw.classycraft.app.commands.AbstractCommand;
import raf.dsw.classycraft.app.gui.swing.tree.ClassyTreeImplementation;
import raf.dsw.classycraft.app.gui.swing.tree.model.ClassyTreeItem;
import raf.dsw.classycraft.app.gui.swing.view.DijagramView;
import raf.dsw.classycraft.app.gui.swing.view.MainFrame;
import raf.dsw.classycraft.app.gui.swing.view.painters.ConnectionPainter;
import raf.dsw.classycraft.app.gui.swing.view.painters.ElementPainter;
import raf.dsw.classycraft.app.gui.swing.view.painters.InterclassPainter;
import raf.dsw.classycraft.app.gui.swing.view.painters.connectionPainter.*;
import raf.dsw.classycraft.app.gui.swing.view.painters.interclassPainter.EnumPainter;
import raf.dsw.classycraft.app.gui.swing.view.painters.interclassPainter.InterfejsPainter;
import raf.dsw.classycraft.app.gui.swing.view.painters.interclassPainter.KlasaPainter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MultipleDeleteCommand extends AbstractCommand {

    private List<DijagramElement> list = new ArrayList<>();
    private DijagramView dijagramView;
    private int x;
    private int y;

    public MultipleDeleteCommand(int x, int y,DijagramView dijagramView){
        this.x = x;
        this.y = y;
        this.dijagramView = dijagramView;
    }

    @Override
    public void doCommand() {
        AbstractCommand command;
        ///pronalazenje dijagrama u stablu
        ClassyTreeItem item = null;
        ClassyTreeItem selected = MainFrame.getInstance().getPackageView().getClassyTreeItem();
        for (int i = 0; i < selected.getChildCount(); i++) {
            ClassyTreeItem c = (ClassyTreeItem) selected.getChildAt(i);
            ClassyNode cn = c.getClassyNode();
            if (cn.getName().equals(dijagramView.getClassyNode().getName()))
                item = c;
        }

        for (int j=dijagramView.getElementPainterList().size()-1; j>=0; j--) {
            ElementPainter elementPainter = dijagramView.getElementPainterList().get(j);
            for(Shape s : dijagramView.getSelectionModel()){
                ///brisanje svih klasa
                if(elementPainter instanceof InterclassPainter){
                    InterclassPainter painter = (InterclassPainter) elementPainter;
                    if (s.getBounds2D().intersectsLine(painter.getConnectionPoints().get(0).x, painter.getConnectionPoints().get(0).y,
                            painter.getConnectionPoints().get(1).x, painter.getConnectionPoints().get(1).y) && s.getBounds2D().intersectsLine(painter.getConnectionPoints().get(2).x,
                            painter.getConnectionPoints().get(2).y, painter.getConnectionPoints().get(3).x, painter.getConnectionPoints().get(3).y)) {
                        Dijagram d = (Dijagram) painter.getElement().getParent();
                        if (item != null) {
                            for (int i = 0; i < item.getChildCount(); i++) {
                                if (item.getChildAt(i) != null) {
                                    ClassyTreeItem tn = (ClassyTreeItem) item.getChildAt(i);
                                    ClassyNode cn = tn.getClassyNode();
                                    if (cn instanceof Interclass) {
                                        Interclass in = (Interclass)cn;
                                        if(in.poredjenje((Interclass) painter.getElement())) {
                                            item.remove(i);
                                            ClassyTreeImplementation classyTreeImplementation = (ClassyTreeImplementation) MainFrame.getInstance().getClassyTree();
                                            SwingUtilities.updateComponentTreeUI(classyTreeImplementation.getTreeView());
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                        list.add(painter.getElement());
                        d.removeChild(painter.getElement());
                        break;
                    }

                }
                ///brisanje pojedinacne veze
                else if (elementPainter instanceof ConnectionPainter) {
                    Connection vezaBrisanje = (Connection) elementPainter.getElement();
                    if (item != null) {
                        for (int i = 0; i < item.getChildCount(); i++) {
                            if (((ClassyTreeItem) item.getChildAt(i)).getClassyNode() instanceof Connection) {
                                Connection cn = (Connection) ((ClassyTreeItem) item.getChildAt(i)).getClassyNode();
                                if (cn.poredjenje(vezaBrisanje)) {
                                    item.remove(i);
                                    list.add(vezaBrisanje);
                                    ((Dijagram) item.getClassyNode()).removeChild(vezaBrisanje);
                                    ClassyTreeImplementation classyTreeImplementation = (ClassyTreeImplementation) MainFrame.getInstance().getClassyTree();
                                    SwingUtilities.updateComponentTreeUI(classyTreeImplementation.getTreeView());
                                    break;
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }
        dijagramView.getSelectionModel().clear();

        ///sredjivanje modela i paintera
        List<ElementPainter> novaPainterLista = new ArrayList<>();
        List<ClassyNode> novaLista = new ArrayList<>();
        for(int i=0;i< item.getChildCount();i++){
            ClassyTreeItem cti = (ClassyTreeItem) item.getChildAt(i);
            novaLista.add(cti.getClassyNode());
            if(cti.getClassyNode() instanceof Klasa)
                novaPainterLista.add(new KlasaPainter((Klasa) cti.getClassyNode()));
            else if(cti.getClassyNode() instanceof Interfejs)
                novaPainterLista.add(new InterfejsPainter((Interfejs) cti.getClassyNode()));
            else if(cti.getClassyNode() instanceof EnumM)
                novaPainterLista.add(new EnumPainter((EnumM) cti.getClassyNode()));
            else if(cti.getClassyNode() instanceof Agregacija)
                novaPainterLista.add(new AgregacijaPainter((Agregacija) cti.getClassyNode()));
            else if(cti.getClassyNode() instanceof Kompozicija)
                novaPainterLista.add(new KompozicijaPainter((Kompozicija) cti.getClassyNode()));
            else if(cti.getClassyNode() instanceof Generalizacija)
                novaPainterLista.add(new GeneralizacijaPainter((Generalizacija) cti.getClassyNode()));
            else if(cti.getClassyNode() instanceof Zavisnost)
                novaPainterLista.add(new ZavisnostPainter((Zavisnost) cti.getClassyNode()));
        }
        Dijagram d = (Dijagram) item.getClassyNode();
        d.setChildren(novaLista);
        dijagramView.getElementPainterList().clear();
        dijagramView.setElementPainterList(novaPainterLista);
        dijagramView.repaint();
    }

    @Override
    public void undoCommand() {
        ///odredjivanje dijagrama unutar stabla
        ClassyTreeItem item = null;
        ClassyTreeItem selected = MainFrame.getInstance().getPackageView().getClassyTreeItem();
        for (int i = 0; i < selected.getChildCount(); i++) {
            ClassyTreeItem c = (ClassyTreeItem) selected.getChildAt(i);
            ClassyNode cn = c.getClassyNode();
            if (cn.getName().equals(dijagramView.getClassyNode().getName()))
                item = c;
        }
        Dijagram d = (Dijagram) dijagramView.getClassyNode();

        for(int idx=0; idx<list.size(); idx++) {
            if (list.get(idx) instanceof Interclass) {
                Interclass interclass = (Interclass) list.get(idx);
                if (interclass instanceof Klasa) {
                    Klasa klasa = (Klasa)interclass;
                    klasa.addSubscriber(dijagramView);
                    KlasaPainter klasaPainter = new KlasaPainter(klasa);
                    dijagramView.getElementPainterList().add(klasaPainter);
                    d.addChild(klasa);///dodoavanje u model
                    MainFrame.getInstance().getClassyTree().addChild(item, klasa);///dodavanje u stablo
                } else if (interclass instanceof Interfejs) {
                    Interfejs interfejs = (Interfejs)interclass;
                    interfejs.addSubscriber(dijagramView);
                    InterfejsPainter interfejsPainter = new InterfejsPainter(interfejs);
                    dijagramView.getElementPainterList().add(interfejsPainter);
                    d.addChild(interfejs);
                    MainFrame.getInstance().getClassyTree().addChild(item, interfejs);
                } else if (interclass instanceof EnumM) {
                    EnumM enumM = (EnumM)interclass;
                    enumM.addSubscriber(dijagramView);
                    EnumPainter enumPainter = new EnumPainter(enumM);
                    dijagramView.getElementPainterList().add(enumPainter);
                    d.addChild(enumM);
                    MainFrame.getInstance().getClassyTree().addChild(item, enumM);
                }
            } else if(list.get(idx) instanceof Connection) {
                Connection connection = (Connection) list.get(idx);
                if(connection instanceof Agregacija){
                    for(ElementPainter e : dijagramView.getElementPainterList()){
                        if(e instanceof InterclassPainter) {
                            if (e.elementAt(new Point(x, y))){
                                connection.setTo((Interclass) e.getElement());
                            }
                        }
                    }
                    if(connection.getFrom()!= null && connection.getTo() != null && connection.getFrom() != connection.getTo()){
                        AgregacijaPainter ap = new AgregacijaPainter(connection);
                        dijagramView.getElementPainterList().add(ap);
                        d.addChild(connection);
                        MainFrame.getInstance().getClassyTree().addChild(item, connection);
                    }
                }
                else if(connection instanceof Kompozicija){
                    for(ElementPainter e : dijagramView.getElementPainterList()){
                        if(e instanceof InterclassPainter) {
                            if (e.elementAt(new Point(x, y))){
                                connection.setTo((Interclass) e.getElement());
                            }
                        }
                    }
                    if(connection.getFrom()!= null && connection.getTo() != null && connection.getFrom() != connection.getTo()){
                        KompozicijaPainter kp = new KompozicijaPainter(connection);
                        dijagramView.getElementPainterList().add(kp);
                        d.addChild(connection);
                        MainFrame.getInstance().getClassyTree().addChild(item, connection);
                    }
                }
                else if(connection instanceof Generalizacija){
                    for(ElementPainter e : dijagramView.getElementPainterList()){
                        if(e instanceof InterclassPainter) {
                            if (e.elementAt(new Point(x, y))){
                                connection.setTo((Interclass) e.getElement());
                            }
                        }
                    }
                    if(connection.getFrom()!= null && connection.getTo() != null && connection.getFrom() != connection.getTo()){
                        GeneralizacijaPainter gp = new GeneralizacijaPainter(connection);
                        dijagramView.getElementPainterList().add(gp);
                        d.addChild(connection);
                        MainFrame.getInstance().getClassyTree().addChild(item, connection);
                    }
                }
                else if(connection instanceof Zavisnost){
                    for(ElementPainter e : dijagramView.getElementPainterList()){
                        if(e instanceof InterclassPainter)
                            if(e.elementAt(new Point(x, y)))
                                connection.setTo((Interclass) e.getElement());
                    }
                    if(connection.getFrom()!= null && connection.getTo() != null && connection.getFrom() != connection.getTo()){
                        ZavisnostPainter zp = new ZavisnostPainter(connection);
                        dijagramView.getElementPainterList().add(zp);
                        d.addChild(connection);
                        MainFrame.getInstance().getClassyTree().addChild(item, connection);
                    }
                }
                else if(connection instanceof Asocijacija){
                    for(ElementPainter e : dijagramView.getElementPainterList()){
                        if(e instanceof InterclassPainter) {
                            if (e.elementAt(new Point(x, y))){
                                connection.setTo((Interclass) e.getElement());
                            }
                        }
                    }
                    if(connection.getFrom()!= null && connection.getTo() != null && connection.getFrom() != connection.getTo()){
                        AsocijacijaPainter asp = new AsocijacijaPainter(connection);
                        dijagramView.getElementPainterList().add(asp);
                        d.addChild(connection);
                        MainFrame.getInstance().getClassyTree().addChild(item, connection);
                    }
                }
                dijagramView.setLine(new Pair<>(new Point(-1,-1), new Point(0,0)));
                dijagramView.repaint();
            }
        }
        list.clear();
    }

    public Shape makeShape(int x, int y){
        Shape shape = new GeneralPath();
        ((GeneralPath) shape).moveTo(x - 10, y - 10);
        ((GeneralPath) shape).lineTo(x + 10, y - 10);
        ((GeneralPath) shape).lineTo(x + 10, y + 10);
        ((GeneralPath) shape).lineTo(x - 10, y + 10);
        ((GeneralPath) shape).lineTo(x - 10, y - 10);
        ((GeneralPath) shape).closePath();
        return shape;
    }
}
