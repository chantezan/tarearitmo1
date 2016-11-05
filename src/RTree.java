import java.io.*;

/**
 * Created by X455LJ on 28-10-2016.
 */
public class RTree {
    RTree padre;
    double[] mbr = new double[4];
    RTree[] hijos;
    int m = 4;
    int elementos = 0;
    int minimo;
    int identificador;

    public RTree() {
        hijos = new RTree[m+1];
        minimo = m * 40/100;

    }

    public RTree(double x1,double x2,double y1, double y2) {
        this();
        mbr[0] = x1;
        mbr[1] = x2;
        mbr[2] = y1;
        mbr[3] = y2;
    }

    public RTree(int identificador, double x1,double x2,double y1, double y2) {
        this();
        this.identificador = identificador;
        mbr[0] = x1;
        mbr[1] = x2;
        mbr[2] = y1;
        mbr[3] = y2;
    }

    public RTree(RTree tree) throws FileNotFoundException, UnsupportedEncodingException {
        this(tree.mbr[0],tree.mbr[1],tree.mbr[2],tree.mbr[3]);
        if (tree.identificador > 0) {
            PrintWriter writer = new PrintWriter("rectangulos/"+tree.identificador+".txt", "UTF-8");
            writer.println(tree.mbr[0]+" "+tree.mbr[1]+" "+tree.mbr[2]+" "+tree.mbr[3]+" ");
            writer.close();
        }

        hijos[0] = tree;
        hijos[0].padre = this;
        elementos++;
    }


    public boolean esHoja(){
        return elementos == 0;
    }

    public RTree insert(RTree tree) throws FileNotFoundException, UnsupportedEncodingException {

        if(hijos[0] == null || hijos[0].esHoja()) {
            PrintWriter writer = new PrintWriter("rectangulos/"+tree.identificador+".txt", "UTF-8");
            writer.println(tree.mbr[0]+" "+tree.mbr[1]+" "+tree.mbr[2]+" "+tree.mbr[3]+" ");
            writer.close();
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

    public RTree ponerElemento(RTree tree) throws FileNotFoundException, UnsupportedEncodingException {
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

    public RTree convertir(){
        RTree aux = new RTree(mbr[0],mbr[1],mbr[2],mbr[3]);
        aux.hijos[0] = this;
        aux.elementos++;
        return aux;
    }

    public RTree overFlow(RTree nuevo) throws FileNotFoundException, UnsupportedEncodingException {
        int tree1;
        int tree2;
        hijos[m] = nuevo;
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
            else if (arriba == -1 || hijo.mbr[3] > hijos[arriba].mbr[3]) arriba = j;

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

        hijos[tree1] = hijos[tree1].convertir();
        hijos[tree2] = hijos[tree2].convertir();

        int i = 0;
        for (RTree hijo : hijos) {
            if(i!=tree1 && i!=tree2){
                if((m - i + 1) + hijos[tree1].elementos == minimo){
                    hijos[tree1].ponerElemento(hijo);
                }
                else {
                    if((m - i + 1) + hijos[tree2].elementos == minimo){
                        hijos[tree2].ponerElemento(hijo);
                    }
                    else {
                        double a1 = hijos[tree1].grow(hijo);
                        double a2 = hijos[tree2].grow(hijo);
                        if(hijos[tree1].grow(hijo) < hijos[tree2].grow(hijo)){
                            hijos[tree1].ponerElemento(hijo);
                        }
                        else {
                            hijos[tree2].ponerElemento(hijo);
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

    public boolean inside(double x1,double x2,double y1, double y2){
        if((x1 >= mbr[0] && x1 <= mbr[1]) || x2 >= mbr[0] && x2 <= mbr[1]) {
            if(y1 >= mbr[2] && y1 <= mbr[3]) return true;
            if(y2 >= mbr[2] && y2 <= mbr[3]) return true;
        }
        return false;
    }

    public String buscar(double x1,double x2,double y1, double y2) throws IOException {
        String string = "";
        if(hijos[0].esHoja()){
            for (RTree hijo : hijos) {
                if(hijo!=null){

                    BufferedReader in = new BufferedReader(new FileReader("rectangulos/"+hijo.identificador+".txt"));
                    String str = in.readLine();
                    String str1[] = str.split(" ");
                    RTree nuevo = new RTree(Double.parseDouble(str1[0]),Double.parseDouble(str1[1]),Double.parseDouble(str1[2]),Double.parseDouble(str1[3]));
                    if (nuevo.inside(x1,x2,y1,y2)) return hijo.identificador+" "+mbr[0]+" "+mbr[1]+" "+mbr[2]+" "+mbr[3]+"\n";
                }
            }
        }
        else {
            for (RTree hijo : hijos) {
                if(hijo!=null && hijo.inside(x1,x2,y1,y2)) string = string + hijo.buscar(x1,x2,y1,y2);
            }
        }
        return string;
    }

    public String toString(){
        String print;
        if(esHoja()) print = "Hoja "+identificador+" "+mbr[0]+" "+mbr[1]+" "+mbr[2]+" "+mbr[3]+"\n";
        else {
            print = "Raiz "+mbr[0]+" "+mbr[1]+" "+mbr[2]+" "+mbr[3]+"\n";
        }
        for (RTree hijo : hijos){
            if(hijo!=null) print += hijo.toString();
        }
        return print;
    }
    
     
    
   

	public void convertFromString(String str) {
		// TODO Auto-generated method stub
		
	}


}