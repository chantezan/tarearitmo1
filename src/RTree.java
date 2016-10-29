/**
 * Created by X455LJ on 28-10-2016.
 */
public class RTree {
    RTree padre;
    double[] mbr = new double[4];
    RTree[] hijos;
    int m;
    int elementos = 0;
    double largoN = 0;
    double anchoN = 0;
    int minimo;

    public RTree(int m) {
        hijos = new RTree[m];
        this.m = m;
        minimo = m*40/100 + 1;
    }

    public RTree(int m, double x, double y) {
        this(m);
        mbr[0] = x;
        mbr[1] = x;
        mbr[2] = y;
        mbr[3] = y;
    }

    public RTree(RTree tree){
        this(tree.m,tree.mbr[0],tree.mbr[2]);
        hijos[0] = tree;
        hijos[0].padre = this;
        elementos++;
    }

    public void normalizar(double largo, double ancho) {
        largoN = (mbr[3] - mbr[2])/largo;
        anchoN = (mbr[1] - mbr[0])/ancho;
    }

    public boolean esHoja(){
        if (mbr[0]==mbr[1] && mbr[2]==mbr[3]){
            return true;
        }
        return false;
    }

    public double mayorN() {
        if (largoN > anchoN) return largoN;
        return anchoN;
    }

    public void convertir() {
        elementos++;
        hijos[0] = new RTree(m,mbr[0],mbr[2]);
        hijos[0].padre = this;
    }

    public RTree insert(RTree tree) {

        if(hijos[0].area()==0) {
            return ponerElemento(tree);
        }

        if(tree.elementos>0) {
            return ponerElemento(tree);

        }
        RTree seleccionado = hijos[0];
        for (RTree hijo : hijos) {
            if (hijo != null) {

                if (hijo.grow(tree) < seleccionado.grow(tree)) seleccionado = hijo;
                else if (hijo.grow(tree) == seleccionado.grow(tree)) {
                    if (hijo.area() < seleccionado.area()) seleccionado = hijo;
                }
            }
        }
        return seleccionado.insert(tree);
    }

    public RTree ponerElemento(RTree tree){
        if(elementos == m) {
            return overFlow(tree);
        }
        tree.padre = this;
        hijos[elementos++] = tree;
        actualizar(tree);
        RTree raiz = this;
        while (raiz.padre!=null){
            raiz = raiz.padre;
        }
        return raiz;
    }

    public void actualizar(RTree tree) {
        if(mbr[0]>tree.mbr[0]) mbr[0] = tree.mbr[0];
        if(mbr[1]<tree.mbr[1]) mbr[1] = tree.mbr[1];
        if(mbr[2]>tree.mbr[2]) mbr[2] = tree.mbr[2];
        if(mbr[3]<tree.mbr[3]) mbr[3] = tree.mbr[3];
        if(padre!=null) padre.actualizar(this);

    }

    public RTree overFlow(RTree nuevo){
        RTree tree1;
        RTree tree2;

        double hor1 = 0;
        double hor2 = 99999999;
        double ver1 = 0;
        double ver2 = 99999999;

        for (RTree hijo : hijos){
            if (hijo.mbr[0] < hor2) hor2 = hijo.mbr[0];
            if (hijo.mbr[1] > hor1) hor1 = hijo.mbr[1];
            if (hijo.mbr[2] < ver2) ver2 = hijo.mbr[2];
            if (hijo.mbr[3] > ver1) ver1 = hijo.mbr[3];
        }
        for (RTree hijo : hijos){
            hijo.normalizar(hor2-hor1,ver2-ver1);
        }
        if(hijos[0].mayorN() < hijos[1].mayorN()){
            tree1 = hijos[1];
            tree2 = hijos[0];
        }
        else {
            tree1 = hijos[0];
            tree2 = hijos[1];
        }

        for (RTree hijo : hijos){
            if(tree1.mayorN() < hijo.mayorN()) {
                tree2 = tree1;
                tree1 = hijo;
            }
            else {
                if(tree2.mayorN()<hijo.mayorN()) {
                    tree2 = hijo;
                }
            }
        }
        tree1.convertir();
        tree2.convertir();
        if(tree1.grow(nuevo) < tree2.grow(nuevo)) {
            tree1.insert(nuevo);
        }
        else {
            tree2.insert(nuevo);
        }

        int i = 2;
        for (RTree hijo : hijos) {
            if(!hijo.equals(tree1) && !hijo.equals(tree2)){
                if((m - i) + tree1.elementos == minimo){
                    tree1.insert(hijo);
                }
                else {
                    if((m - i) + tree2.elementos == minimo){
                        tree2.insert(hijo);
                    }
                    else {
                        if(tree1.grow(hijo) < tree2.grow(hijo)){
                            tree1.insert(hijo);
                        }
                        else {
                            tree2.insert(hijo);
                        }
                    }
                }
            }
            i++;
        }
        if(padre==null){
            RTree raiznueva = new RTree(tree1);
            padre = raiznueva;
        }

        mbr = tree1.mbr;
        hijos = tree1.hijos;
        elementos = tree1.elementos;
        padre.actualizar(tree1);
        padre.insert(tree2);
        RTree raiz = padre;
        while (raiz.padre!=null){
            raiz = raiz.padre;
        }
        return raiz;

    }

    public double area() {
        return (mbr[1] - mbr[0]) * (mbr[3] - mbr[2]);
    }

    public double areaHip(RTree arbol) {
        return 0;
    }

    public double areaHip(double x, double y){
        double aux1 = mbr[0];
        double aux2 = mbr[1];
        double aux3 = mbr[2];
        double aux4 = mbr[3];
        if(x<aux1) aux1 = x;
        if(aux2<x) aux2 = x;
        if(y<aux3) aux3 = y;
        if (y>aux4) aux4= y;
        return (aux2 - aux1) * (aux4 - aux3);
    }

    public double grow(RTree tree) {
        return areaHip(tree)-area();

    }

    public boolean inside(double x, double y){
        if(x<mbr[0] || x>mbr[1]) return false;
        if(y<mbr[2] || y>mbr[3]) return false;
        return false;
    }

    public String toString(){
        String print = "Raiz "+mbr[0]+" "+mbr[1]+" "+mbr[2]+" "+mbr[3]+"\n";
        for (RTree hijo : hijos){
            if(hijo!=null) print += hijo.toString();
        }
        return print;
    }
}