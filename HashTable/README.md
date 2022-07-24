# HashTable

A HashTable implemented in Java.

My Collision Resolution Scheme is an array of Linked Nodes
which is chained buckets scheme. Whenever there is a collision, my HashTable will add it to the
head of the LinkedList to increase efficiency.

Hashing Algorithm: My hash algorithm is the absolute value of the hashCode() of the K object
modulus with the capacity (size) of the table. This will calculate a positive integer value
between 0 and the size of the HashTable to serve as the HashIndex.
