package com.quixey.median;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
/**
 * Class SmartMedianData provides a data structure
 * that facilitates calculation of a median for a list of integer values.
 * 
 * The algorithm:
 * ==============
 * It is based in keeping a ordered double-linked list of values
 * and a reference point to a median (a place in the list) which facilitates
 * reaching the median.
 * The median reference point is updated at each insert and
 * the median value itself is recalculated at each insert, but these
 * two operations take O(constant)=O(1) running time complexity.
 * The main running time is consumed by inserting a new integer value into a ordered list.
 * The method Collections.binarySort() is used for this implementation.
 * 
 * Run-time analysis:
 * ==================
 *  - inserting a single value into your data structure:
 *       Complexity O(log(n)) (as a complexity of Collections.binarySearch());
 *  - doing a lookup for a median: 
 *       Complexity O(1).
 * 
 * @author Zhenya Kusner
 *
 */
public class SmartMedianData {

	// list of nodes that stores all inserted integer values; 
	// it is kept always ordered
	private LinkedList<Node> dataList;

	// the median value of the data list
	private double median;
	
	// place in a dataList STARTING FROM WHICH median value can be obtained; 
	// the reference node doesn't necessarily points TO exact median value
	private Node medianReferenceNode;

	public SmartMedianData() {
		dataList = new LinkedList<Node>();
	}

	// *******************************************************
	//         Private methods
	// *******************************************************
	
	/**
	 * calculate current median value for ordered data list 
	 * based on data list size, use median reference node for performance
	 */
	private static double calculateMedian(int size, Node referenceNode) {
		if (size == 0) {
			// median is not defined for an empty data list
			throw new RuntimeException("Data is empty, median is not defined.");
		}
		
		double result;
		if ((size % 2) == 1) {
			// size is an odd number
			result = referenceNode.getValue();
		} else {
			// size is an even number
			// in this case median value is defined as a mean value 
			// of two "middle" values
			result = (referenceNode.getValue() + referenceNode.getNext().getValue()) / 2.0;
		}
		
		return result;
	}

	// update median reference (node)
	// based on new inserted value, previous median reference and data list size.
	//
	// This is done for performance reasons:
	// algorithm can't be based on index returned by binary search
	// because if the dataList contains multiple elements equal to the 
	// value, there is no guarantee which one will be found
	// and which index will be returned;
	// hence actual values have to be compared
	//
	// Median reference node is always correct at this point for the PREVIOUS step.
	private static Node updateMedianReferenceNode(int value, 
			                              Node referenceNode,
			                              LinkedList<Node> list) {
		int size = list.size();
		// treat special case of a list of one node
		if (size == 1) {
			referenceNode = list.getFirst();
			return referenceNode;
		}

		double referenceValue = referenceNode.getValue();
		if (value <= referenceValue) {
			// new value is inserted at lower half of the dataList
			if ((size % 2) == 0) { 
				// move median reference one position toward lowest end
				referenceNode = referenceNode.getPrev();
			}
		} else {
			// new value is inserted at higher half of the dataList
			if ((size % 2) != 0) {
				// move median reference one position toward highest end
				referenceNode = referenceNode.getNext();
			}
		}

		return referenceNode;
	}


	// insert value in list at certain index
	// take care of prev and next references of nodes in the list
	private void insertValueIntoStorage(int index, int value) {
		Node node = new Node(value);

		// special case - dataList is empty
		if (dataList.isEmpty()) {
			dataList.add(index, node);
			node.setPrev(null);
			node.setNext(null);
			return;
		}
		
		// take care about "next" and "prev" references
		// of newly inserted node
		// and a node before it (prev) 
		// and a node after it (next)
		Node prevNode;
		Node nextNode;
		if (index == dataList.size()) {
			prevNode = dataList.getLast();
			nextNode = null;
		} else {
			// standard case
			nextNode = dataList.get(index);
			prevNode = nextNode.getPrev();
		}
		
		// actually insert value into the list
		dataList.add(index, node);
		node.setNext(nextNode);
		node.setPrev(prevNode);
		
		// take care about prevNode and nextNode
		if (prevNode != null) {
			prevNode.setNext(node);
		}
		if (nextNode != null) {
			nextNode.setPrev(node);
		}
	}


	// calculate where to insert new value into the ordered data list
	private int calculateInsertIndex(int value) {
		int index;
		index = Collections.binarySearch(dataList, new Node(value), new Comparator<Node>(){
			@Override
			public int compare(Node n1, Node n2) {
				if (n1.getValue() < n2.getValue()){
					return -1;
				} else if (n1.getValue() > n2.getValue()) {
					return 1;
				} else {
					return 0;
				}
			}
		});
		
		
		if (index < 0) {
			// according to definition of binary search this means that
			// index = (-(insertionPoint) - 1)
			index = 0 - index - 1;
		}
		return index;

	}


	// store value in an ordered list and keep the list ordered
	private void addValue(int value) {
		int index = 0;
		if (!dataList.isEmpty()) {
			index = calculateInsertIndex(value);
		}
		insertValueIntoStorage(index, value);
	}


	// *******************************************************
	//         Public methods
	// *******************************************************
	
	/**
	 * @param value - new value to be inserted into data 
	 */
	public void insert(int value) {
		// store value in the data structure
		addValue(value);
		// update median reference 
		medianReferenceNode = updateMedianReferenceNode(value, medianReferenceNode, dataList);
		// recalculate median based on new medianReference and list size
		// yes, median is calculated at each insert, but performance wise
		// it is a very small overhead
		median = calculateMedian(dataList.size(), medianReferenceNode);
	}

	/**
	 * 
	 * @return median value for data
	 */
	
	public double getMedian() {
		return median;
	}

}
