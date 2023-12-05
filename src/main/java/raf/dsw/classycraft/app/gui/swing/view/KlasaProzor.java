package raf.dsw.classycraft.app.gui.swing.view;



import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class KlasaProzor extends JFrame {

    private JRadioButton atribut;
    private JRadioButton metoda;

    private JRadioButton jbPrivate;
    private JRadioButton jbPublic;
    private JRadioButton jbProtected;

    private JRadioButton jbInt;
    private JRadioButton jbFloat;
    private JRadioButton jbDouble;
    private JRadioButton jbString;
    private JRadioButton jbBoolean;
    private JTextField tfNaziv;

    private JButton jbDodaj;

    public KlasaProzor(){

        //namestanje prozora
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screenSize = kit.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth/3, screenHeight/3);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("Dodavanje u klasu");

        // atribut ili metoda
        ButtonGroup bg = new ButtonGroup();
        atribut = new JRadioButton("Atribut");
        metoda = new JRadioButton("Metoda");
        bg.add(atribut); bg.add(metoda);

        //vidljivost : + - #
        JLabel lbVidljivost = new JLabel("Vidljivost:");
        ButtonGroup bgVidljivost = new ButtonGroup();
        jbPrivate = new JRadioButton("private");
        jbPublic = new JRadioButton("public");
        jbProtected = new JRadioButton("protected");
        bgVidljivost.add(jbPrivate); bgVidljivost.add(jbPublic); bgVidljivost.add(jbProtected);

        // tip
        JLabel lbTip = new JLabel("Tip:");
        ButtonGroup bgTip = new ButtonGroup();
        jbInt = new JRadioButton("int");
        jbFloat = new JRadioButton("float");
        jbDouble = new JRadioButton("double");
        jbString = new JRadioButton("string");
        jbBoolean = new JRadioButton("boolean");
        bgTip.add(jbInt); bgTip.add(jbFloat); bgTip.add(jbDouble); bgTip.add(jbString); bgTip.add(jbBoolean);

        //naziv
        JLabel lbNaziv = new JLabel("Naziv:");
        tfNaziv = new JTextField();

        JPanel jpAtrMet = new JPanel();
        jpAtrMet.setAlignmentX(Component.LEFT_ALIGNMENT);
        jpAtrMet.setLayout(new BoxLayout(jpAtrMet, BoxLayout.X_AXIS));
        jpAtrMet.add(atribut); jpAtrMet.add(metoda);

        JPanel jpVidljivost = new JPanel();
        jpVidljivost.setAlignmentX(Component.LEFT_ALIGNMENT);
        jpVidljivost.setLayout(new BoxLayout(jpVidljivost, BoxLayout.X_AXIS));
        jpVidljivost.add(jbPrivate); jpVidljivost.add(jbPublic); jpVidljivost.add(jbProtected);

        JPanel jpTip = new JPanel();
        jpTip.setAlignmentX(Component.LEFT_ALIGNMENT);
        jpTip.setLayout(new BoxLayout(jpTip, BoxLayout.X_AXIS));
        jpTip.add(jbInt); jpTip.add(jbFloat); jpTip.add(jbDouble); jpTip.add(jbString); jpTip.add(jbBoolean);

        jbDodaj = new JButton("Dodaj");

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        jPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        jPanel.add(jpAtrMet); jPanel.add(lbVidljivost); jPanel.add(jpVidljivost);jPanel.add(lbTip);
        jPanel.add(jpTip); jPanel.add(lbNaziv); jPanel.add(tfNaziv); jPanel.add(jbDodaj);

        jPanel.setBorder(new EmptyBorder(new Insets(15, 10, 15, 10)));
        this.add(jPanel);
    }

    public JRadioButton getAtribut() {
        return atribut;
    }

    public void setAtribut(JRadioButton atribut) {
        this.atribut = atribut;
    }

    public JRadioButton getMetoda() {
        return metoda;
    }

    public void setMetoda(JRadioButton metoda) {
        this.metoda = metoda;
    }

    public JRadioButton getJbPrivate() {
        return jbPrivate;
    }

    public void setJbPrivate(JRadioButton jbPrivate) {
        this.jbPrivate = jbPrivate;
    }

    public JRadioButton getJbPublic() {
        return jbPublic;
    }

    public void setJbPublic(JRadioButton jbPublic) {
        this.jbPublic = jbPublic;
    }

    public JRadioButton getJbProtected() {
        return jbProtected;
    }

    public void setJbProtected(JRadioButton jbProtected) {
        this.jbProtected = jbProtected;
    }

    public JRadioButton getJbInt() {
        return jbInt;
    }

    public void setJbInt(JRadioButton jbInt) {
        this.jbInt = jbInt;
    }

    public JRadioButton getJbFloat() {
        return jbFloat;
    }

    public void setJbFloat(JRadioButton jbFloat) {
        this.jbFloat = jbFloat;
    }

    public JRadioButton getJbDouble() {
        return jbDouble;
    }

    public void setJbDouble(JRadioButton jbDouble) {
        this.jbDouble = jbDouble;
    }

    public JRadioButton getJbString() {
        return jbString;
    }

    public void setJbString(JRadioButton jbString) {
        this.jbString = jbString;
    }

    public JRadioButton getJbBoolean() {
        return jbBoolean;
    }

    public void setJbBoolean(JRadioButton jbBoolean) {
        this.jbBoolean = jbBoolean;
    }

    public JTextField getTfNaziv() {
        return tfNaziv;
    }

    public void setTfNaziv(JTextField tfNaziv) {
        this.tfNaziv = tfNaziv;
    }

    public JButton getJbDodaj() {
        return jbDodaj;
    }

    public void setJbDodaj(JButton jbDodaj) {
        this.jbDodaj = jbDodaj;
    }
}
