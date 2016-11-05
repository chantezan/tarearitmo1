import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by X455LJ on 28-10-2016.
 */
public class Main {
    public static void main (String [ ] args) throws IOException {
        int i = 2;

        BufferedReader in = new BufferedReader(new FileReader("datos.txt"));
        String str = in.readLine();
        String str1[] = str.split(" ");
        RTree nuevo = new RTree(1,Double.parseDouble(str1[0]),Double.parseDouble(str1[1]),Double.parseDouble(str1[2]),Double.parseDouble(str1[3]));
        RTree raiz = new RTree(nuevo);
        while(in.readLine()!=null){
            nuevo = new RTree(i++,Double.parseDouble(str1[0]),Double.parseDouble(str1[1]),Double.parseDouble(str1[2]),Double.parseDouble(str1[3]));
            raiz = (RTree) raiz.insert(nuevo);
        }
        System.out.println("FIN");
        System.out.println(raiz.toString());
        System.out.println(raiz.buscar(4.5,8,1,5));




    }
}
