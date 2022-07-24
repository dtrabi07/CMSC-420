//////////////////// ALL ASSIGNMENTS INCLUDE THIS SECTION /////////////////////
//
// Title: HashTable
// Files: HashTable.java, HashTableTest.java, results.png
// Course: CS 400 Spring 2019
//
// Author: Alex Do
// Email: ado@wisc.edu
// Lecturer's Name: Deb Deppeler, Lecture 001
// Due Data: 03/14/2019
//
//////////////////// PAIR PROGRAMMERS COMPLETE THIS SECTION ///////////////////
//
// Partner Name: none
// Partner Email: none
// Partner Lecturer's Name: none
//
// VERIFY THE FOLLOWING BY PLACING AN X NEXT TO EACH TRUE STATEMENT:
// ___ Write-up states that pair programming is allowed for this assignment.
// ___ We have both read and understand the course Pair Programming Policy.
// ___ We have registered our team prior to the team registration deadline.
//
///////////////////////////// CREDIT OUTSIDE HELP /////////////////////////////
//
// Students who get help from sources other than their partner must fully
// acknowledge and credit those sources of help here. Instructors and TAs do
// not need to be credited here, but tutors, friends, relatives, room mates,
// strangers, and others do. If you received no outside help from either type
// of source, then please explicitly indicate NONE.
//
// Persons: none
// Online Sources: none
//
/////////////////////////////// 80 COLUMNS WIDE ///////////////////////////////

// Collision Resolution Scheme: (5) My Collision Resolution Scheme is an array of Linked Nodes
// which is chained buckets scheme. Whenever there is a collision, my HashTable will add it to the
// head of the LinkedList to increase efficiency.
//
// Hashing Algorithm: My hash algorithm is the absolute value of the hashCode() of the K object
// modulus with the capacity (size) of the table. This will calculate a positive integer value
// between 0 and the size of the HashTable to serve as the HashIndex.

/**
 * Class defining a HashTable with Key Value pairs.
 * 
 * @author AlexDo
 *
 * @param <K>
 * @param <V>
 */
public class HashTable<K extends Comparable<K>, V> implements HashTableADT<K, V> {

  private int capacity; // current capacity of the HashTable
  private double loadFactorThreshold; // loadFactorThreshold of this Hashtable
  private int numKeys; // number of keys in whole HashTable
  private LinkedNode<K, V>[] array; // array containing references to head of linkedNodes
  private boolean rehashStatus; // true if currently rehashing

  /**
   * Inner class defining node storing the key value pairs and reference to next node in LinkedList.
   * 
   * @author AlexDo
   *
   * @param <K>
   * @param <V>
   */
  @SuppressWarnings("hiding")
  private class LinkedNode<K, V> {
    private K key;
    private V value;
    private LinkedNode<K, V> next; // reference to next node in linkedlist

    /**
     * Inner class constructor initializing all class fields.
     * 
     * @param key
     * @param value
     */
    private LinkedNode(K key, V value) {
      this.key = key;
      this.value = value;
      this.next = null;
    }
  }


  /**
   * Default constructor setting capacity to 11 and loadFactorThreshold.
   */
  @SuppressWarnings("unchecked")
  public HashTable() {
    this.capacity = 11;
    this.loadFactorThreshold = 0.75;
    this.numKeys = 0; // keys start out at 0 when HashTable initialized
    this.array = new LinkedNode[this.capacity]; // initialize array of LinkedNodes
    this.rehashStatus = false; // false until need to be rehashed
  }

  /**
   * Constructor initializing capacity and loadFactorThreshold to specified initial capacity and
   * loadFactorThreshold.
   * 
   * @param initialCapacity
   * @param loadFactorThreshold
   */
  @SuppressWarnings("unchecked")
  public HashTable(int initialCapacity, double loadFactorThreshold) {
    this.capacity = initialCapacity;
    this.loadFactorThreshold = loadFactorThreshold;
    this.numKeys = 0; // keys start out at 0 when HashTable initialized
    this.array = new LinkedNode[this.capacity]; // initialize array of LinkedNodes
    this.rehashStatus = false; // false until need to be rehashed
  }

  /**
   * Add the key,value pair to the data structure and increase the number of keys. If key is null,
   * throw IllegalNullKeyException; If key is already in data structure, throw
   * DuplicateKeyException();
   */
  @Override
  public void insert(K key, V value) throws IllegalNullKeyException, DuplicateKeyException {
    // sanity check for key
    if (key == null)
      throw new IllegalNullKeyException();

    // rehash if load factor too high and not currently rehashing
    if (this.getLoadFactor() >= this.getLoadFactorThreshold() && !this.rehashStatus)
      rehash();

    int hashIndex = hashFunction(key); // hashIndex of key

    // if head of hashIndex is null
    if (this.array[hashIndex] == null) {
      this.array[hashIndex] = new LinkedNode<K, V>(key, value);
      this.numKeys++;
      return;
      // check for duplicate, won't run if currently rehashing
    } else if (!this.rehashStatus) {
      LinkedNode<K, V> curr = this.array[hashIndex]; // linked node to traverse linkedlist
      while (curr != null) {
        // duplicate key found
        if (curr.key.equals(key))
          throw new DuplicateKeyException();
        curr = curr.next; // move pointer
      }
    }

    // insert at head of list for efficiency
    LinkedNode<K, V> temp = this.array[hashIndex]; // store previous head reference
    this.array[hashIndex] = new LinkedNode<K, V>(key, value);
    this.array[hashIndex].next = temp;
    this.numKeys++;
  }

  /**
   * Private helper method to resize and rehash entire HashTable.
   * 
   * @throws IllegalNullKeyException
   * @throws DuplicateKeyException
   */
  @SuppressWarnings("unchecked")
  private void rehash() throws IllegalNullKeyException, DuplicateKeyException {
    this.capacity = 2 * this.capacity + 1; // new capacity
    this.numKeys = 0; // reset numKey
    LinkedNode<K, V> curr; // pointer to traverse each element in previous HashTable
    LinkedNode<K, V>[] temp = this.array; // store entire previous HashTable
    this.array = new LinkedNode[this.capacity]; // new resized HashTable
    this.rehashStatus = true; // HashTable now rehashing

    // traverse previous HashTable
    for (int i = 0; i < temp.length; i++) {
      curr = temp[i]; // curr now set head of linkedlist to traverses
      while (curr != null) {
        this.insert(curr.key, curr.value); // insert key into new HashTable
        curr = curr.next;
      }
    }
    this.rehashStatus = false; // rehash finished
  }

  /**
   * Private helper method computing the hashIndex of a key.
   * 
   * @param key
   * @return the HashIndex
   */
  private int hashFunction(K key) {
    return Math.abs(key.hashCode() % this.capacity); // account for negative numbers
  }

  /**
   * If key is found, remove the key,value pair from the data structure decrease number of keys.
   * return true If key is null, throw IllegalNullKeyException If key is not found, return false
   * 
   * @param key
   * @return true if key was successfully removed
   */
  @Override
  public boolean remove(K key) throws IllegalNullKeyException {
    // sanity check for key
    if (key == null)
      throw new IllegalNullKeyException();

    int hashIndex = hashFunction(key); // hashIndex of key

    // if key at hashIndex doesn't exist
    if (this.array[hashIndex] == null)
      return false;

    // case if key is at head of hashIndex
    if (this.array[hashIndex].key.equals(key)) {
      this.array[hashIndex] = this.array[hashIndex].next; // move head reference
      this.numKeys--;
      return true;
      // need to traverse hashIndex
    } else if (!this.array[hashIndex].key.equals(key)) {
      LinkedNode<K, V> curr = this.array[hashIndex]; // pointer node to traverse
      while (curr != null) {
        // found key to remove
        if (curr.key.equals(key)) {
          this.numKeys--;
          return true;
        }
        curr = curr.next;
      }
    }
    return false;
  }

  /**
   * Returns the value associated with the specified key Does not remove key or decrease number of
   * keys
   * 
   * If key is null, throw IllegalNullKeyException If key is not found, throw
   * KeyNotFoundException().
   * 
   * @param key
   * @return the value of the key
   */
  @Override
  public V get(K key) throws IllegalNullKeyException, KeyNotFoundException {
    // sanity check for key
    if (key == null)
      throw new IllegalNullKeyException();

    int hashIndex = hashFunction(key); // hashIndex of key

    // key not in HashTable
    if (this.array[hashIndex] == null)
      throw new KeyNotFoundException();

    LinkedNode<K, V> curr = this.array[hashIndex]; // pointer node to traverse
    while (curr != null) {
      // key found
      if (curr.key.equals(key))
        return curr.value;
      curr = curr.next;
    }

    // key not found in HashTable
    throw new KeyNotFoundException();
  }

  /**
   * Returns the number of key,value pairs in the data structure.
   * 
   * @return the number of keys currently in the HashTable
   */
  @Override
  public int numKeys() {
    return this.numKeys;
  }

  /**
   * Returns the load factor threshold that was passed into the constructor when creating the
   * instance of the HashTable. When the current load factor is greater than or equal to the
   * specified load factor threshold, the table is resized and elements are rehashed.
   * 
   * @return the loadFactorThreshold
   */
  @Override
  public double getLoadFactorThreshold() {
    return this.loadFactorThreshold;
  }

  /**
   * Returns the current load factor for this hash table load factor = number of items / current
   * table size
   * 
   * @return the current load factor
   */
  @Override
  public double getLoadFactor() {
    return (double) this.numKeys / this.capacity; // double cast to get decimal value
  }

  /**
   * Return the current Capacity (table size) of the hash table array.
   * 
   * The initial capacity must be a positive integer, 1 or greater and is specified in the
   * constructor.
   * 
   * REQUIRED: When the load factor threshold is reached, the capacity must increase to: 2 *
   * capacity + 1
   * 
   * Once increased, the capacity never decreases.
   * 
   * @return the current capacity of the HashTable
   */
  @Override
  public int getCapacity() {
    return this.capacity;
  }

  /**
   * Returns the collision resolution scheme used for this hash table. Implement with one of the
   * following collision resolution strategies. Define this method to return an integer to indicate
   * which strategy.
   * 
   * @return the collision resolution scheme of this HashTable
   * 
   */
  @Override
  public int getCollisionResolution() {
    return 5;
  }



}
