import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
	
	//Pizza variables
	public static Pizza pizza;
	public static int min;
	public static int max;
	public static int rows;
	public static int columns;
	
	//Handling of random
	static int[] randomArray;
	static int randomsCommited = 0;
	static ArrayList<Point> tmpRandomList;
	
	public static void main(String[] args) {
		
		//Handle the input
		File file = new File(System.getProperty("user.home") + "/Desktop/hashcode/d_big.in");
		Scanner scan = null;
		try {
			scan = new Scanner(file);
		} catch (FileNotFoundException e) {}
		
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
		
		//tmpRandomList = new ArrayList<>();
		randomArray = new int[rows*columns];
		tmpRandomList = new ArrayList<>();
		for(int i=0;i<randomArray.length;i++)
			randomArray[i] = i;
		
		solve();
	}

	public static void solve(){
		while(randomsCommited+1 < rows*columns)  {
			createNewSlice();
			//printPizzaStatus();
		}
		
		Writer writer = null;
		int score = 0;
		System.out.println(pizza.slices.size()+"");
		File file = new File(System.getProperty("user.home") + "/Desktop/D.txt");
		
		try {
            FileOutputStream is = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(is);    
            Writer w = new BufferedWriter(osw);
            w.write((pizza.slices.size()+"\n"));
    		for (Slice slice : pizza.slices) {
    			score+=slice.M+slice.T;
    			w.write(""+slice.start.x+" "+slice.start.y+" "+slice.end.x+" "+slice.end.y+"\n");
    		}
    		w.close();
    		osw.close();
    		is.close();
        } catch (IOException e) {
            System.err.println("Problem writing to the file statsTest.txt");
        }
		

		System.out.println("Total score is: " + score);
	}
	
	public static void createNewSlice(){

		Slice slice = new Slice();
		slice.start = getRandomFreePointEfficient();
		slice.end = new Point(slice.start);
		if(pizza.arr[slice.start.x][slice.start.y].substring(0, 1).equals("M"))
			slice.M++;
		else 
			slice.T++;
		pizza.arr[slice.start.x][slice.start.y] = pizza.arr[slice.start.x][slice.start.y] + "X";
		
		//Try make slice bigger
		boolean shouldContinue = true;
		while(shouldContinue) {

			boolean canAddPiece = false;
			
			if(false){}
			else if(addRowDown(slice)){}
			else if(addRowUp(slice)){}
			else if(addColumnRight(slice)){}
			else if(addColumnLeft(slice)){}


			else {
			//Cannot make slice any bigger
			if(slice.M >= min && slice.T >= min) {
				for(Point p : tmpRandomList)
					removeIndexFromRandom(findIndex(twoToOne(p)));
				pizza.slices.add(slice);
			} else {
				clearSlice(slice);
				slice = null;
			}
			tmpRandomList.clear();
			shouldContinue = false;
		}
			
//			canAddPiece = addRowUp(slice) || canAddPiece ;
//			canAddPiece = addRowDown(slice) || canAddPiece;
//			canAddPiece = addColumnRight(slice) || canAddPiece;
//			canAddPiece = addColumnLeft(slice) || canAddPiece;
//			
//			if(!canAddPiece) {
//				//Cannot make slice any bigger
//				if(slice.M >= min && slice.T >= min) {
//					for(Point p : tmpRandomList)
//						removeIndexFromRandom(findIndex(twoToOne(p)));
//					pizza.slices.add(slice);
//				} else {
//					clearSlice(slice);
//					slice = null;
//				}
//				tmpRandomList.clear();
//				shouldContinue = false;
//			}
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
			//removeIndexFromRandom(findIndex(twoToOne(new Point(i,start.y))));
			tmpRandomList.add(tmpRandomList.size(),new Point(i,start.y));
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
			//removeIndexFromRandom(findIndex(twoToOne(new Point(i,end.y))));
			tmpRandomList.add(tmpRandomList.size(),new Point(i,end.y));
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
			//removeIndexFromRandom(findIndex(twoToOne(new Point(start.x,i))));
			tmpRandomList.add(tmpRandomList.size(),new Point(start.x,i));
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
			//removeIndexFromRandom(findIndex(twoToOne(new Point(end.x,i))));
			tmpRandomList.add(tmpRandomList.size(),new Point(end.x,i));
			if(pizza.arr[end.x][i].substring(0, 1).equals("T"))
				slice.T++;
			else 
				slice.M++;
		}
		
		slice.end.x = end.x;
		
		return true;
	}
	
	public static int findIndex(int val){
		for(int i=0;i<randomArray.length;i++){
			if(randomArray[i]==val)
				return i;
		}
		return 0;
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
		int index = r.nextInt(randomArray.length - randomsCommited);
		int returnVal = randomArray[index];
		removeIndexFromRandom(index);
		
		return oneToTwo(returnVal);
	}
	
	static void removeIndexFromRandom(int index){
		int tmp = randomArray[index];
		//if(index >=0 && randomArray.length-1-randomsCommited>=0) {
		randomArray[index] = randomArray[randomArray.length-1-randomsCommited];
		randomArray[randomArray.length-1-randomsCommited] = tmp;
		randomsCommited++;
		//}
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
	
}
