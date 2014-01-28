package com.quixey.median;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Class TestDriver performs a test of class SmartMedianData functionality.
 * A number of random integer values are generated and stored both into sorted ArrayList
 * and into an  instance of SmartMedianData class.
 * After that median value of arrayList is compared to the calculated value of the median of
 * class SmartMedianData instance.
 * 
 * @author Zhenya Kusner
 *
 */
public class TestDriver {

	private static final int MILLION = 1000000;
	private static final int DEFAULT_TEST_DATA_MAX_SIZE = MILLION;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss"); 
	
	private int testDataSize = DEFAULT_TEST_DATA_MAX_SIZE;
	private int randomRange  = testDataSize; // it's not a must, it's just easier
	
	private ArrayList<Integer> testData;
	private SmartMedianData    smartMedianData;

	public TestDriver() {
		testData = new ArrayList<Integer>(testDataSize);
		smartMedianData = new SmartMedianData();
	}

	public TestDriver(int size) {
		testDataSize = size;
		randomRange = testDataSize;
		testData = new ArrayList<Integer>(testDataSize);
		smartMedianData = new SmartMedianData();
	}

	// *******************************************************
	//         Private methods
	// *******************************************************

	// Method returns median value for a sorted array of integers;
	// Median value is defined as follows:
	// if an array size is an odd number then
	//       median = array[(size-1)/2];
	// if an array size is an even number then 
	//       median is defined as an average of values
	//       array[(size/2) - 1] and array[size/2]
	private double calculateTestDataMedian() {
		System.out.println("Method calculateMedian().");
		double medianValue;
		boolean isSizeEven = ((testData.size() % 2) == 0);

		if (isSizeEven){
			int index1 = (testData.size() / 2) - 1;
			int index2 = testData.size() / 2;
			medianValue = (testData.get(index1) + testData.get(index2)) / 2.0;
		} else {
			int index = (testData.size() - 1) / 2;
			medianValue = testData.get(index);
		}
		return medianValue;
	}


	private boolean compareMedians() {
		double testDataMedian  = calculateTestDataMedian();
		double smartDataMedian = smartMedianData.getMedian();
		boolean result;
		result = (testDataMedian == smartDataMedian);
		System.out.println("Median of test data = " + testDataMedian + "." + "\n" 
				         + "Median of smart data (calculated by algorithm) = " + smartDataMedian + ".");
		
		return result;
	}


	private void sortTestData() {
		System.out.println("\n" + "Method sortData() starts......");
		Collections.sort(testData);
		System.out.println("Method sortData() completed.");
	}


	private void generateRandomTestData() {
		int value;
		System.out.println("\n" + "Method generateRandomTestData(), test data size = " + testDataSize + ".");
		for (int i = 0; i < testDataSize; i++){
			if ((i % 1000) == 0) {
				System.out.print("\n" + "At time: " + dateFormat.format(new Date(System.currentTimeMillis())) + " generated index = " + i + ".");
			}

			value = (int)(Math.random() * randomRange);
			// add new value to smartData structure
			smartMedianData.insert(value);
			// add new value to testData structure
			testData.add(value);
		}
		System.out.println("\n" + "All test data successfully generated.");
	}

	// *******************************************************
	//         Public methods
	// *******************************************************
	public void test(){
		System.out.println("Starting median calculation test for " + testDataSize + " integer values.");
		long startTime;
		long endTime;
		boolean result;
		
		System.out.println("Started at " + new Date(System.currentTimeMillis()));
		startTime = System.currentTimeMillis();
		generateRandomTestData();
		sortTestData();
		result = compareMedians();
		endTime = System.currentTimeMillis() - startTime;
		
		System.out.println("\n" + "Results: "
				+ ((result)?"medians are equal.\n" + "TEST SUCCEEDED."
		                         :"medians are not equal.\n" + "TEST FAILED.")
				+ "\n" + "The whole process took " + (endTime / 1000.0) + " seconds"
					+ ((endTime <= 60000)?"":", which are " + (endTime / 60000.0) + " minutes.")
				+ "\n" + "Test completed."
		);
	}
	
	
	public static void main(String[] args){
		int runningSize = MILLION;
		TestDriver driver = new TestDriver(runningSize);
		driver.test();
	}
}
