/**
 * Created by X455LJ on 28-10-2016.
 */
public class Main {
    public static void main (String [ ] args) {

        RTree nuevo = new RTree(5,5,6,7,8);
        RTree raiz = new RTree(nuevo);
        nuevo = new RTree(5,6,7,8,9);
        raiz = (RTree) raiz.insert(nuevo);
        System.out.println("insertado 2");
        System.out.print(raiz.toString());

        nuevo = new RTree(5,8,9,4,5);
        raiz = (RTree) raiz.insert(nuevo);
        System.out.println("insertado 3");
        System.out.print(raiz.toString());
        nuevo = new RTree(5,11,12,3,4);
        raiz = (RTree) raiz.insert(nuevo);
        System.out.println("insertado 4");
        System.out.print(raiz.toString());
        nuevo = new RTree(5,13,14,7,8);
        raiz = (RTree) raiz.insert(nuevo);
        System.out.println("insertado 5");
        System.out.print(raiz.toString());
        nuevo = new RTree(5,15,16,2,3);
        raiz = (RTree) raiz.insert(nuevo);
        System.out.println("insertado 6");
        System.out.print(raiz.toString());




    }
}
