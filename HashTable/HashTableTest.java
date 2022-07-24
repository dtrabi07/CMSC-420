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
import static org.junit.jupiter.api.Assertions.*; // org.junit.Assert.*;
import org.junit.jupiter.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.Random;

/**
 * Various JUnit tests testing implementation of HashTable.java.
 * 
 * @author AlexDo
 *
 */
public class HashTableTest {

  HashTable<Integer, String> ht; // Integer, String HashTable used for tests
  HashTable<String, String> ht2; // String, String HashTable used for tests

  /**
   * Method run before every test.
   * 
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    ht = new HashTable<Integer, String>();
    ht2 = new HashTable<String, String>();
  }

  /**
   * Method run after every test.
   * 
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
    ht = null;
    ht2 = null;
  }

  /**
   * Tests that a HashTable returns an integer code indicating which collision resolution strategy
   * is used. REFER TO HashTableADT for valid collision scheme codes.
   */
  @Test
  public void test000_collision_scheme() {
    HashTableADT htIntegerKey = new HashTable<Integer, String>();
    int scheme = htIntegerKey.getCollisionResolution();
    if (scheme < 1 || scheme > 9)
      fail("collision resolution must be indicated with 1-9");
  }

  /**
   * IMPLEMENTED AS EXAMPLE FOR YOU Tests that insert(null,null) throws IllegalNullKeyException
   */
  @Test
  public void test001_IllegalNullKey() {
    try {
      HashTableADT htIntegerKey = new HashTable<Integer, String>();
      htIntegerKey.insert(null, null);
      fail("should not be able to insert null key");
    } catch (IllegalNullKeyException e) {
      /* expected */ } catch (Exception e) {
      fail("insert null key should not throw exception " + e.getClass().getName());
    }
  }

  /**
   * Tests that inserting one key, size should be one.
   */
  @Test
  public void test002_insert_one_size_one() {
    try {
      ht.insert(5, null); // test insert
    } catch (Exception e) {
      fail("Unexpected exception test002");
    }

    if (ht.numKeys() != 1)
      fail("numKeys should be one but is " + ht.numKeys());
  }

  /**
   * Tests that inserting 20 keys, removing 10, size should be 10.
   */
  @Test
  public void test003_insert20_remove10_size_10() {

    try {
      for (int i = 1; i < 21; i++) {
        ht.insert(i, null); // unique test inserts
      }
      for (int i = 1; i < 11; i++) {
        ht.remove(i); // remove unique keys
      }
    } catch (Exception e) {
      fail("Unexpected exception test003");
    }

    if (ht.numKeys() != 10)
      fail("numKeys should be 10 but is " + ht.numKeys());
  }

  /**
   * Tests if DuplicateKeyException is thrown if a duplicate key is inserted.
   */
  @Test
  public void test004_insert_duplicate_key() {
    try {
      ht2.insert("Test", null);
      ht2.insert("Test", "Non"); // duplicate key inserted
    } catch (DuplicateKeyException e) {
      // do nothing, expected
    } catch (Exception e) {
      fail("Unexpected exception test004");
    }
  }

  /**
   * Tests if HashTable is rehashed properly by checking new capacity.
   */
  @Test
  public void test005_good_rehash() {
    // ht is a HashTable with specified initialCapcity and LoadFactorThreshold
    HashTable<Integer, String> ht = new HashTable<Integer, String>(20, 0.6);

    try {
      for (int i = 0; i < 12; i++) {
        ht.insert(i, null); // insert 12 keys
      }
      // loadFactor should now be 0.6 after inserting 12 keys (12/20)
      if (ht.getLoadFactor() != 0.6) {
        fail("Load factor should be 0.6 but is " + ht.getLoadFactor());
      }
      ht.insert(13, null); // trigger rehashing
    } catch (Exception e) {
      fail("Unexpected exception test005");
    }

    // capacity should now be 41 after most recent insert
    if (ht.getCapacity() != 41) {
      fail("Capacity should double after rehashing but is instead " + ht.getCapacity());
    }


  }

  /**
   * Tests if removing a key from the head of the linkedList causes an exception.
   */
  @Test
  public void test006_remove_from_head() {
    try {
      ht.insert(89, null);
      ht.insert(78, null); // new head of linkedlist
      ht.remove(78); // remove head
    } catch (Exception e) {
      fail("Unexpected exception test006");
    }
  }

  /**
   * Tests if getting an invalid key causes a KeyNotFoundException.
   */
  @Test
  public void test007_get_invalid_key() {

    try {
      ht2.get("HelloWorld"); // no key in table
      fail("KeyNotFoundException should have been thrown");
    } catch (KeyNotFoundException e) {
      // do nothing, expected
    } catch (Exception e) {
      fail("Unexpected exception test007");
    }
  }

  /**
   * Tests if removing an invalid key causes 0 exceptions and returns false.
   */
  @Test
  public void test008_remove_invalid_key() {
    boolean key = true; // should not be true after method end
    try {
      key = ht2.remove("Invalid");
    } catch (Exception e) {
      fail("Unexpected exception test008");
    }
    if (key)
      fail("key should be false but is " + key); // boolean key should now be false
  }

  /**
   * Tests if removing a null key throws IllegalNullKeyException.
   */
  @Test
  public void test009_remove_null_key() {
    try {
      ht.insert(1, null);
      ht.remove(null);
    } catch (IllegalNullKeyException e) {
      // do nothing, expected
    } catch (Exception e) {
      fail("Unexpected exception test009");
    }
  }

  /**
   * Tests if getting a key with a null value returns it properly.
   */
  @Test
  public void test010_get_null_key() {
    try {
      ht2.insert("None", null);
      if (ht2.get("None") != null)
        fail("get should have returned null but is " + ht2.get("None"));
    } catch (Exception e) {
      fail("Unexpected exception test010");
    }
  }

  /**
   * Tests if inserting, getting and removing 500,000 keys throws any unexpected exceptions.
   */
  @Test
  public void test011_insert_get_remove_ridiculous_number() {
    try {
      // many inserts
      for (int i = 0; i < 500000; i++) {
        ht.insert(i, null);
      }
      // many gets
      for (int i = 0; i < 500000; i++) {
        ht.get(i);
      }
      // many removes
      for (int i = 0; i < 500000; i++) {
        ht.remove(i);
      }
    } catch (Exception e) {
      fail("Unexpected exception test011");
    }
  }

}


