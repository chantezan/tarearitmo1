/**
 * Created by X455LJ on 28-10-2016.
 */
public class Main {
    public static void main (String [ ] args) {




        RTree nuevo = new RTree(5,5,7);
        RTree raiz = new RTree(nuevo);
        nuevo = new RTree(5,6,8);
        raiz = raiz.insert(nuevo);
        System.out.println("insertado 2");
        System.out.print(raiz.toString());

        nuevo = new RTree(5,8,4);
        raiz = raiz.insert(nuevo);
        System.out.println("insertado 3");
        System.out.print(raiz.toString());
        nuevo = new RTree(5,11,3);
        raiz = raiz.insert(nuevo);
        System.out.println("insertado 4");
        System.out.print(raiz.toString());
        nuevo = new RTree(5,13,7);
        raiz = raiz.insert(nuevo);
        System.out.println("insertado 5");
        System.out.print(raiz.toString());
        nuevo = new RTree(5,15,2);
        raiz = raiz.insert(nuevo);
        System.out.println("insertado 6");
        System.out.print(raiz.toString());




    }
}
