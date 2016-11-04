/**
 * Created by X455LJ on 28-10-2016.
 */
public class RTree {
    RTree padre;
    double[] mbr = new double[4];
    RTree[] hijos;
    int m;
    int elementos = 0;
    int minimo;

    public RTree(int m) {
        hijos = new RTree[m+1];
        this.m = m;
        minimo = m * 40/100;
    }

    public RTree(int m, double x1,double x2,double y1, double y2) {
        this(m);
        mbr[0] = x1;
        mbr[1] = x2;
        mbr[2] = y1;
        mbr[3] = y2;
    }

    public RTree(RTree tree){
        this(tree.m,tree.mbr[0],tree.mbr[1],tree.mbr[2],tree.mbr[3]);
        hijos[0] = tree;
        hijos[0].padre = this;
        elementos++;
    }


    public boolean esHoja(){
        return elementos == 0;
    }

    public RTree insert(RTree tree) {

        if(hijos[0] == null || hijos[0].esHoja()) {
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
        int tree1;
        int tree2;
        hijos[5] = nuevo;
        int arriba = -1;
        int abajo = -1;
        int derecha = -1;
        int izquierda = -1;
        double rangoD = 0;
        double rangoI = 99999999;
        double rangoAr = 0;
        double rangoAb = 99999999;
        double hor1 = 99999999;;
        double hor2 = 0;
        double ver1 = 99999999;;
        double ver2 = 0;

        for (int j = 0;j < hijos.length ; j++){
            RTree hijo = hijos[j];
            if (hijo.mbr[0] > hor2) hor2 = hijo.mbr[0];

            if (hijo.mbr[1] < hor1) hor1 = hijo.mbr[1];

            if (hijo.mbr[2] > ver2) ver2 = hijo.mbr[2];

            if (hijo.mbr[3] < ver1) ver1 = hijo.mbr[3];

            if (izquierda == -1 || hijo.mbr[0] < hijos[izquierda].mbr[0]) izquierda = j;
            else if (derecha == -1 || hijo.mbr[1] > hijos[derecha].mbr[1]) derecha = j;
            if (abajo == -1 || hijo.mbr[2] < hijos[abajo].mbr[2]) abajo = j;
            else if (arriba == -1 || hijo.mbr[1] > hijos[arriba].mbr[1]) arriba = j;

            if(hijo.mbr[0] < rangoI) rangoI = hijo.mbr[0];
            if(hijo.mbr[1] > rangoD) rangoD = hijo.mbr[1];
            if(hijo.mbr[2] < rangoAb) rangoAb = hijo.mbr[2];
            if(hijo.mbr[3] > rangoAr) rangoAr = hijo.mbr[3];
        }

        if((rangoD - rangoI)/(hor2-hor1) > (rangoAr - rangoAb)/(ver2-ver1)){
            tree1 = izquierda;
            tree2 = derecha;
        }
        else {
            tree1 = abajo;
            tree2 = arriba;
        }

        int i = 0;
        for (RTree hijo : hijos) {
            if(i!=tree1 && i!=tree2){
                if((m - i + 1) + hijos[tree1].elementos == minimo){
                    hijos[tree1].insert(hijo);
                }
                else {
                    if((m - i + 1) + hijos[tree2].elementos == minimo){
                        hijos[tree2].insert(hijo);
                    }
                    else {
                        double a1 = hijos[tree1].grow(hijo);
                        double a2 = hijos[tree2].grow(hijo);
                        if(hijos[tree1].grow(hijo) < hijos[tree2].grow(hijo)){
                            hijos[tree1].insert(hijo);
                        }
                        else {
                            hijos[tree2].insert(hijo);
                        }
                    }
                }
            }
            i++;
        }
        if(padre==null){
            RTree raiznueva = new RTree(hijos[tree1]);
            padre = raiznueva;
        }

        mbr = hijos[tree1].mbr;

        elementos = hijos[tree1].elementos;
        padre.actualizar(hijos[tree1]);
        padre.ponerElemento(hijos[tree2]);
        hijos = hijos[tree1].hijos;
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
        double aux1 = mbr[0];
        double aux2 = mbr[1];
        double aux3 = mbr[2];
        double aux4 = mbr[3];
        if(arbol.mbr[0]<aux1) aux1 = arbol.mbr[0];
        if(aux2<arbol.mbr[1]) aux2 = arbol.mbr[1];
        if(arbol.mbr[2]<aux3) aux3 = arbol.mbr[2];
        if (arbol.mbr[3]>aux4) aux4= arbol.mbr[3];
        return (aux2 - aux1) * (aux4 - aux3);
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
    
     
    
   

	public void convertFromString(String str) {
		// TODO Auto-generated method stub
		
	}


}