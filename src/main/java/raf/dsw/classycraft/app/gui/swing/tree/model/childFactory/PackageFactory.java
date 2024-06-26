package raf.dsw.classycraft.app.gui.swing.tree.model.childFactory;

import raf.dsw.classycraft.app.classyCraftRepository.composite.ClassyNode;
import raf.dsw.classycraft.app.classyCraftRepository.composite.dijagramElementi.DijagramElement;
import raf.dsw.classycraft.app.classyCraftRepository.implementation.Package;

public class PackageFactory extends ChildFactory {

    public PackageFactory() {

    }

    @Override
    public ClassyNode makeChild(String name, ClassyNode parent, DijagramElement dijagramElement) {

        return new Package(name,parent);

    }
}
