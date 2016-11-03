import java.util.Arrays;

/**
 * Created by X455LJ on 02-11-2016.
 */
public class RTree2 extends RTree {

    int[] orderH;
    int[] orderV;

    public RTree2(int m) {
        super(m);

    }

    public RTree2(int m, double x1, double x2, double y1, double y2) {
        super(m, x1, x2, y1, y2);
    }

    public RTree2(RTree tree) {
        super(tree);
    }

    public void ordenar(int i){
        if(orderH==null) {
            orderH = new int[m+1];
            orderV = new int[m+1];
            Arrays.fill(orderH, -1);
            Arrays.fill(orderH, -1);
            orderH[0] = i;
            orderV[0] = i;
            return;
        }
        int hor = i;
        int ver = i;
        for(int j = 0;j <= m; j++) {
            if(orderH[j]!=-1) {
                if(hijos[orderH[j]].mbr[0] > hijos[hor].mbr[0]) {
                    int aux1 = orderH[j];
                    orderH[j] = hor;
                    hor = aux1;
                }
                if(hijos[orderV[j]].mbr[2] > hijos[ver].mbr[2]) {
                    int aux1 = orderV[j];
                    orderV[j] = ver;
                    ver = aux1;
                }
            }
            else {
                orderH[j] = hor;
                orderV[j] = ver;
                return;
            }
        }
    }

    @Override
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
        int[] indice;

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
            ordenar(j);//ordena de mayor a menos en ejeX e Y
        }

        if((rangoD - rangoI)/(hor2-hor1) > (rangoAr - rangoAb)/(ver2-ver1)){
            indice = orderH;; //revisara el lado izquierdo
            tree1 = izquierda;
            tree2 = derecha;
        }
        else {
            indice = orderV;; // revisara el lado derecho
            tree1 = abajo;
            tree2 = arriba;
        }
        int i = 0;
        for(int j = 0;j < hijos.length; j++) {
            if(indice[j]!=tree1 && indice[j]!=tree2) {
                if( i<minimo ){
                    hijos[tree1].insert(hijos[indice[j]]);
                }
                else{
                    hijos[tree2].insert(hijos[indice[j]]);
                }
                i++;
            }

        }

        if(padre==null){
            RTree raiznueva = new RTree2(hijos[tree1]);
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
}
