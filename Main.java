
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;

public class Main {

    //Pizza variables
    public static Pizza pizza;
    public static int min;
    public static int max;
    public static int rows;
    public static int columns;

    public static int module4 = 0;

    static int iterations = 1000;
    static int iteration = 0;

    //Handling of random
    static ArrayList<Integer> randomArray;
    static int lastScore = 0;
    static List<Slice> lastPizza = new ArrayList<>();

    public static void main(String[] args) {

        //Handle the input
        File file = new File(System.getProperty("user.home") + "/Desktop/hashcode/d_big.in");
        Scanner scan = null;
        try {scan = new Scanner(file);}
        catch (FileNotFoundException e) {}

        String s = scan.nextLine();
        String[] values = s.split(" ");

        rows = Integer.parseInt(values[0]);
        columns = Integer.parseInt(values[1]);
        min = Integer.parseInt(values[2]);
        max = Integer.parseInt(values[3]);

        String[][] pizzaArr = new String[rows][columns];
        for(int i=0;i<rows;i++)
            pizzaArr[i] = scan.nextLine().split("");
        pizza = new Pizza();
        pizza.arr = pizzaArr;

        randomArray = new ArrayList<>();
        for(int i=0;i<rows*columns;i++)
            randomArray.add(i);

        solve();
    }


    public static void solve() {
        while(randomArray.size() > 0) {
            createNewSlice();
            //printPizzaStatus();
        }

        if(iteration < iterations) {
            module4 = (module4+1)%4;
            iteration++;
            int score = pizza.getScore();
            System.out.println("Current score is " + lastScore);
            if (score > lastScore) {
                lastScore = score;
                lastPizza = new ArrayList<>(pizza.slices);
            } else {
                pizza.slices = new ArrayList<>(lastPizza);
            }

            rearangePizza();
        }

        createOutputFile();
    }

    static void rearangePizza(){
        if(module4 == 2) {
            pizza.slices.sort(new Comparator<Slice>() {
                @Override
                public int compare(Slice o1, Slice o2) {
                    if (o1.M + o1.T < o2.M + o2.T)
                        return -1;
                    else if (o1.M + o1.T == o2.M + o2.T)
                        return 0;
                    else return 1;
                }
            });
        }

        int amount = pizza.slices.size()/100+1;
        for(int i=0;i<amount;i++){
            Random r = new Random();
            int index;
            if(module4 == 2)
                index = i;
            else
                index = r.nextInt(pizza.slices.size()); // for getting random numbers;
            Slice slice = pizza.slices.get(index);
            //Slice slice = pizza.slices.get(i);
            for(int j = slice.start.x ; j<=slice.end.x; j++) {
                for(int k=slice.start.y; k<=slice.end.y;k++) {
                    randomArray.add(twoToOne(new Point(j,k)));
                    pizza.slices.remove(slice);
                }
            }
            clearSlice(slice);
        }
        solve();
    }

    public static void createOutputFile(){
        int score = 0;
        Writer writer = null;
        File file = new File(System.getProperty("user.home") + "/Desktop/D.txt");
        try {
            FileOutputStream is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);
            Writer w = new BufferedWriter(osw);
            w.write((pizza.slices.size()+"\n"));
            for (Slice slice : pizza.slices) {
                w.write(""+slice.start.x+" "+slice.start.y+" "+slice.end.x+" "+slice.end.y+"\n");
                score += slice.M+slice.T;
            }
            w.close();
            osw.close();
            is.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        }
    }

    public static void createNewSlice(){

        Slice slice = new Slice();
        slice.start = getRandomFreePointEfficient();
        if(slice.start == null)
            return;
        slice.end = new Point(slice.start);
        if(pizza.arr[slice.start.x][slice.start.y].substring(0, 1).equals("M"))
            slice.M++;
        else
            slice.T++;
        pizza.arr[slice.start.x][slice.start.y] = pizza.arr[slice.start.x][slice.start.y] + "X";

        //Try make slice bigger
        boolean shouldContinue = true;
        while(shouldContinue) {

//            boolean canAddPiece = false;
//            canAddPiece = addColumnRight(slice) || canAddPiece;
//            canAddPiece = addColumnLeft(slice) || canAddPiece;
//            canAddPiece = addRowUp(slice) || canAddPiece ;
//            canAddPiece = addRowDown(slice) || canAddPiece;

            if(module4 == 0) {
                if (false) {
                } else if (addColumnRight(slice)) {
                } else if (addColumnLeft(slice)) {
                } else if (addRowUp(slice)) {
                } else if (addRowDown(slice)) {
                } else {
                    //Cannot make slice any bigger
                    if (slice.M >= min && slice.T >= min) {
                        pizza.slices.add(slice);
                    } else {
                        clearSlice(slice);
                        slice = null;
                    }
                    shouldContinue = false;
                }
            } else if(module4 == 1) {
                if (false) {
                } else if (addRowDown(slice)) {
                } else if (addColumnLeft(slice)) {
                } else if (addColumnRight(slice)) {
                } else if (addRowUp(slice)) {
                } else {
                    //Cannot make slice any bigger
                    if (slice.M >= min && slice.T >= min) {
                        pizza.slices.add(slice);
                    } else {
                        clearSlice(slice);
                        slice = null;
                    }
                    shouldContinue = false;
                }
            }else if(module4 == 2) {
                if (false) {
                } else if (addRowUp(slice)) {
                } else if (addRowDown(slice)) {
                } else if (addColumnLeft(slice)) {
                } else if (addColumnRight(slice)) {
                } else {
                    //Cannot make slice any bigger
                    if (slice.M >= min && slice.T >= min) {
                        pizza.slices.add(slice);
                    } else {
                        clearSlice(slice);
                        slice = null;
                    }
                    shouldContinue = false;
                }
            } else {
                if (false) {
                } else if (addRowUp(slice)) {
                } else if (addColumnRight(slice)) {
                } else if (addColumnLeft(slice)) {
                } else if (addRowDown(slice)) {
                } else {
                    //Cannot make slice any bigger
                    if (slice.M >= min && slice.T >= min) {
                        pizza.slices.add(slice);
                    } else {
                        clearSlice(slice);
                        slice = null;
                    }
                    shouldContinue = false;
                }
            }
        }
    }

    public static void clearSlice(Slice slice) {
        for(int i = slice.start.x ; i<=slice.end.x; i++) {
            for(int j=slice.start.y; j<=slice.end.y;j++) {
                pizza.arr[i][j] = pizza.arr[i][j].substring(0,1);
            }
        }
    }

    public static boolean addColumnLeft(Slice slice) {

        Point start = new Point(slice.start);
        start.y = start.y - 1;
        if(slice.size()+slice.height() > max || start.y < 0)
            return false;

        //Check if the column is free
        for(int i=start.x; i<=slice.end.x; i++)
            if(pizza.arr[i][start.y].length() > 1)
                return false;

        //row is free
        for(int i=start.x; i<=slice.end.x; i++) {
            pizza.arr[i][start.y] = pizza.arr[i][start.y] + "X";
            if(pizza.arr[i][start.y].substring(0, 1).equals("T"))
                slice.T++;
            else
                slice.M++;
        }

        slice.start.y = start.y;

        return true;
    }

    public static boolean addColumnRight(Slice slice) {

        Point end = new Point(slice.end);
        end.y = end.y + 1;
        if(slice.size()+slice.height() > max || end.y >= pizza.arr[0].length)
            return false;

        //Check if the column is free
        for(int i=slice.start.x; i<=slice.end.x; i++)
            if(pizza.arr[i][end.y].length() > 1)
                return false;

        //row is free
        for(int i=slice.start.x; i<=slice.end.x; i++) {
            pizza.arr[i][end.y] = pizza.arr[i][end.y] + "X";
            if(pizza.arr[i][end.y].substring(0, 1).equals("T"))
                slice.T++;
            else
                slice.M++;
        }

        slice.end.y = end.y;

        return true;
    }

    public static boolean addRowUp(Slice slice) {

        Point start = new Point(slice.start);
        start.x = start.x - 1 ;
        if(slice.size() + slice.width() > max || start.x < 0)
            return false;

        //Check if the row is free
        for(int i=start.y; i<=slice.end.y; i++)
            if(pizza.arr[start.x][i].length() > 1)
                return false;

        //row is free
        for(int i=start.y; i<=slice.end.y; i++) {
            pizza.arr[start.x][i] = pizza.arr[start.x][i] + "X";
            if(pizza.arr[start.x][i].substring(0, 1).equals("T"))
                slice.T++;
            else
                slice.M++;

        }

        slice.start.x = start.x;

        return true;
    }

    public static boolean addRowDown(Slice slice) {

        Point end = new Point(slice.end);
        end.x = end.x + 1;
        if(slice.size() + slice.width() > max || end.x >= pizza.arr.length)
            return false;

        //Check if the column is free
        for(int i=slice.start.y; i<=slice.end.y; i++)
            if(pizza.arr[end.x][i].length() > 1)
                return false;

        //row is free
        for(int i=slice.start.y; i<=slice.end.y; i++) {
            pizza.arr[end.x][i] = pizza.arr[end.x][i] + "X";
            if(pizza.arr[end.x][i].substring(0, 1).equals("T"))
                slice.T++;
            else
                slice.M++;
        }

        slice.end.x = end.x;

        return true;
    }


    public static void printPizzaStatus(){
        for (int i = 0; i < pizza.arr.length; i++) {
            for (int j = 0; j < pizza.arr[0].length; j++) {
                System.out.print(pizza.arr[i][j] + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }


    public static Point getRandomFreePoint() {

        Random ran = new Random();

        int randomRow = ran.nextInt(rows);
        int randomCol = ran.nextInt(columns);

        while(pizza.arr[randomRow][randomCol].length()>1) {
            randomRow = ran.nextInt(rows);
            randomCol = ran.nextInt(columns);
        }

        Point point = new Point();
        point.x = randomRow;
        point.y = randomCol;

        return point;
    }

    public static Point getRandomFreePointEfficient(){

        Random r = new Random();
        int index = r.nextInt(randomArray.size());
        Point point = oneToTwo(randomArray.get(index));
        while(pizza.arr[point.x][point.y].length() > 1) {
            randomArray.remove(index);
            if(randomArray.size()==0)
                return null;

            index = r.nextInt(randomArray.size());
            point = oneToTwo(randomArray.get(index));
        }

        int returnVal = randomArray.get(index);
        randomArray.remove(index);

        return oneToTwo(returnVal);
    }


    static Point oneToTwo(int index) {
        Point point = new Point();
        point.x = index/(pizza.arr[0].length);
        point.y = index%(pizza.arr[0].length);

        return point;
    }

    static int twoToOne(Point point) {
        return point.x*(pizza.arr[0].length) + point.y;
    }

    static class Pizza {
        String[][] arr;
        List<Slice> slices;
        public Pizza(){
            slices = new ArrayList<>();
        }
        public int getScore(){
            int score = 0;
            for(Slice slice : slices) {
                score += slice.M+slice.T;
            }
            return score;
        }
    }

    static class Slice {
        Point start, end;
        int M=0,T=0;

        int size(){
            return (end.x-start.x+1) * (end.y-start.y+1);
        }

        int height(){
            return Math.abs(end.x - start.x +1);
        }

        int width() {
            return Math.abs(end.y - start.y +1);
        }
    }

    static class Point {
        int x,y;
        public Point(){}
        public Point(Point o) {
            this.x = o.x;
            this.y = o.y;
        }
        public Point(int x,int y) {
            this.x = x;
            this.y = y;
        }
    }


    public static String get2DArrayPrint(int[][] matrix) {
        String output = new String();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                output = output + (matrix[i][j] + "\t");
            }
            output = output + "\n";
        }
        return output;
    }

}
