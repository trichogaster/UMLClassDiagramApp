package raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.connection;

import com.fasterxml.jackson.annotation.JsonTypeName;
import raf.dsw.classycraft.app.classyCraftRepository.composite.ClassyNode;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.Connection;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.Interclass;
import raf.dsw.classycraft.app.classyCraftRepository.implementation.Project;

@JsonTypeName("kompozicija")
public class Kompozicija extends Connection {
    private String name2;
    private String tip;
    private String kardinalnost;
    private String vidljivost;

    public Kompozicija(){
        super("", null);
    }
    public Kompozicija(String name, ClassyNode parent) {
        super(name, parent);
        projectChanged();
    }

    public Kompozicija(String name, ClassyNode parent, int stroke, Interclass from, Interclass to) {
        super(name, parent, stroke, from, to);
        name2 = "";
        tip = "";
        kardinalnost = "-";
        vidljivost = "-";
        projectChanged();
    }
    private void projectChanged(){
        ClassyNode c = this;
        while(c!=null && !(c instanceof Project)){
            c = c.getParent();
        }
        if(c != null)
            ((Project) c).setChanged(true);
    }
    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
        projectChanged();
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
        projectChanged();
    }

    public String getKardinalnost() {
        return kardinalnost;
    }

    public void setKardinalnost(String kardinalnost) {
        this.kardinalnost = kardinalnost;
        projectChanged();
    }

    public String getVidljivost() {
        return vidljivost;
    }

    public void setVidljivost(String vidljivost) {
        this.vidljivost = vidljivost;
        projectChanged();
    }
}
