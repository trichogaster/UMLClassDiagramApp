package raf.dsw.classycraft.app.classyCraftRepository.composite;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.connection.Agregacija;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.connection.Generalizacija;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.connection.Kompozicija;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.connection.Zavisnost;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.interclass.EnumM;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.interclass.Interfejs;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.interclass.Klasa;
import raf.dsw.classycraft.app.classyCraftRepository.implementation.Dijagram;
import raf.dsw.classycraft.app.classyCraftRepository.implementation.Project;

@JsonIgnoreProperties({ "parent"})
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Dijagram.class, name = "dijagram"),
        @JsonSubTypes.Type(value = Package.class, name = "paket"),
        @JsonSubTypes.Type(value = Project.class, name = "projekat"),
        @JsonSubTypes.Type(value = Klasa.class, name = "klasa"),
        @JsonSubTypes.Type(value = Interfejs.class, name = "interfejs"),
        @JsonSubTypes.Type(value = EnumM.class, name = "enum"),
        @JsonSubTypes.Type(value = Agregacija.class, name = "agregacija"),
        @JsonSubTypes.Type(value = Kompozicija.class, name = "kompozicija"),
        @JsonSubTypes.Type(value = Zavisnost.class, name = "zavisnost"),
        @JsonSubTypes.Type(value = Generalizacija.class, name = "generalizacija")
})
public abstract class ClassyNode {

    private String name;
    private ClassyNode parent;
    public ClassyNode(String name, ClassyNode parent) {
        this.name = name;
        this.parent = parent;
        projectChanged();
    }

    @Override
    public boolean equals(Object obj) {
        if((obj != null) && (obj instanceof ClassyNode)) {
            ClassyNode otherObj = (ClassyNode) obj;
            return this.getName().equals(otherObj.getName());
        }
        return false;
    }

    private void projectChanged(){
        ClassyNode c = this;
        while(c!=null && !(c instanceof Project)){
            c = c.getParent();
        }
        if(c != null)
            ((Project) c).setChanged(true);
    }

    public String getName() {
        return name;
    }

    public ClassyNode getParent() {
        return parent;
    }

    public void setName(String name) {
        projectChanged();
        this.name = name;
    }

    public void setParent(ClassyNode parent) {
        projectChanged();
        this.parent = parent;
    }

}
