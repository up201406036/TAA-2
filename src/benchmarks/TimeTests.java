package benchmarks;

import java.util.ArrayList;

import structures.*;

/**
 * Test the execution time for structures operations calling the Benchmark class
 * for calculate the time and calling the Print2CSV to show the values.
 * 
 * @author Matheus Rosa
 *
 */
public class TimeTests {	 
	public final static boolean TO_FILE = true;
	
	public static int inputFormat = 0;
	public static int numberTests = 10;
	public static int step = 5000;
	public static int samples = 3;
	public static String directoryName = "UniformInput";
	
	public static ArrayList<ArrayList<Long>> bstResult;
	public static ArrayList<ArrayList<Long>> avlResult;
	public static ArrayList<ArrayList<Long>> redBlackResult;
	public static ArrayList<ArrayList<Long>> treapResult;
	public static ArrayList<ArrayList<Long>> splayResult;
	public static ArrayList<ArrayList<Long>> listResult;
	
	public static void main(String[] args) {
		
		if (args.length > 0 && args.length != 4) {
			System.out.println("Unexpected format! Please, use the format:\n\n"
					+ "java Benchmark <input-format (0-ascending, 1-gaussian, 2-uniform)> <number-of-tests> <step-size> <number-of-samples>\n\n"
					+ "Or just \"java benchmark\" - for default values: 2 10 1000 2");
		} else if (args.length == 4) {
			try {
				inputFormat = Integer.parseInt(args[0]);
				numberTests = Integer.parseInt(args[1]);
				step = Integer.parseInt(args[2]);
				samples = Integer.parseInt(args[3]);
			} catch (NumberFormatException e) {
				System.out.println("The input arguments are not numbers.");
				System.exit(1);
			}
		} 
		
		if (inputFormat == 0) {
			directoryName = "ascendingInput";
		} else if (inputFormat == 1) {
			directoryName = "gaussianInput";
		}
		
		Benchmark benchmark = new Benchmark(numberTests, step, samples);
		
		SimpleBST<Integer> bst = new SimpleBST<>();
		AVLTree<Integer> avl = new AVLTree<>();
		RedBlackTree<Integer> redBlack = new RedBlackTree<>();
		Treap<Integer> treap = new Treap<>();
		SplayTree<Integer> splay = new SplayTree<>();
		SkipList<Integer> list = new SkipList<>();
		
		bstResult = benchmark.timeTest(bst, inputFormat);
		avlResult = benchmark.timeTest(avl, inputFormat);
		redBlackResult = benchmark.timeTest(redBlack, inputFormat);
		treapResult = benchmark.timeTest(treap, inputFormat);
		splayResult = benchmark.timeTest(splay, inputFormat);
		listResult = benchmark.timeTest(list, inputFormat);
	
		for (int i = 0; i < 5; i++) toPrint(i);
	}
	
	public static void toPrint(int op) {
		String dir = directoryName;
		String file = "";
		
		switch (op) {
		case 0:
			file = "Find";
			break;
		case 1:
			file = "Insert";
			break;
		case 2:
			file = "Delete";
			break;
		case 3:
			file = "Max";
			break;
		case 4:
			file = "Min";
			break;
		default:
			break;
		}

		ArrayList<String> labels = new ArrayList<>();
		labels.add("SimpleBST");
		labels.add("AVLTree");
		labels.add("RedBlackTree");
		labels.add("Treap");
		labels.add("SplayTree");
		labels.add("SkipList");
		
		Print2CSV export = new Print2CSV(dir, file+".csv");
		ArrayList<ArrayList<Long>> data = new ArrayList<>();
		data.add(bstResult.get(op));
		data.add(avlResult.get(op));
		data.add(redBlackResult.get(op));
		data.add(treapResult.get(op));
		data.add(splayResult.get(op));
		data.add(listResult.get(op));
		export.data2CSVFormat(labels, data, step, TO_FILE);
	}
}
