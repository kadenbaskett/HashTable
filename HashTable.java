package assign09;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class HashTable<K, V> implements Map<K, V> {
	private ArrayList<LinkedList<MapEntry<K, V>>> table;

	private int numOfItems;
	private int numOfCollisions;
	private int capacity;

	public HashTable() {
		capacity = 100;
		table = new ArrayList<LinkedList<MapEntry<K, V>>>();

		for (int i = 0; i < capacity; i++)
			table.add(new LinkedList<MapEntry<K, V>>());

		numOfItems = 0;
		numOfCollisions = 0;
	}

	public int loadFactor() {
		return numOfItems / table.size();
	}

	/**
	 * Resets the number-of-collisions statistic to 0.
	 */
	public void resetCollisions() {
		numOfCollisions = 0;
	}

	/**
	 * @return the number of collisions incurred (so far)
	 */
	public int collisions() {
		return numOfCollisions;
	}

	@Override
	public void clear() {
		table.clear();
		resetCollisions();
		numOfItems = 0;
		
		for(int i = 0; i < capacity; i ++) {
			table.add(new LinkedList<MapEntry<K, V>>());
		}
	}

	@Override
	public boolean containsKey(K key) {
		int currentPos = Math.abs(key.hashCode()) % table.size();

		for (int i = 0; i < table.get(currentPos).size(); i++) {

			if (table.get(currentPos).get(i).getKey().equals(key)) {
				return true;
			}

		}

		return false;
	}

	@Override
	public boolean containsValue(V value) {
		for (int i = 0; i < table.size(); i++) {

			if (table.get(i) != null) {

				for (int j = 0; j < table.get(i).size(); j++) {

					if (table.get(i).get(j).getValue().equals(value)) {
						return true;
					}

				}

			}
			// TODO Auto-generated method stub
		}

		return false;
	}

	@Override
	public List<MapEntry<K, V>> entries() {
		ArrayList<MapEntry<K, V>> entries = new ArrayList<MapEntry<K, V>>();

		for (int i = 0; i < table.size(); i++) {

			for (int j = 0; j < table.get(i).size(); j++) {
				entries.add(table.get(i).get(j));
			}

		}

		return entries;
	}

	@Override
	public V get(K key) {
		int currentPos = Math.abs(key.hashCode()) % table.size();

		for (int i = 0; i < table.get(currentPos).size(); i++) {

			if (table.get(currentPos).get(i).getKey().equals(key)) {
				return table.get(currentPos).get(i).getValue();
			}

		}

		return null;
	}

	@Override
	public boolean isEmpty() {
		return numOfItems == 0;
	}

	@Override
	public V put(K key, V value) {
		V temp = null;

		if (loadFactor() >= 9) {
			rehash();
		}

		int currentPos = Math.abs(key.hashCode()) % table.size();

		for (int i = 0; i < table.get(currentPos).size(); i++) {

			if (table.get(currentPos).get(i).getKey().equals(key)) {
				temp = table.get(currentPos).get(i).getValue();
				table.get(currentPos).get(i).setValue(value);
				return temp;
			}
			numOfCollisions ++;

		}

		table.get(currentPos).add(new MapEntry<K, V>(key, value));
		numOfItems++;
		return temp;
	}

	@Override
	public V remove(K key) {
		V temp = null;
		int currentPos = Math.abs(key.hashCode() % table.size());

		for (int i = 0; i < table.get(currentPos).size(); i++) {

			if (table.get(currentPos).get(i).getKey().equals(key)) {
				temp = table.get(currentPos).get(i).getValue();
				table.get(currentPos).remove(i);
				numOfItems --;
				return temp;
			}

		}

		return temp;
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return numOfItems;
	}

	private void addAll(List<MapEntry<K, V>> temp) {

		for (int i = 0; i < temp.size(); i++) {
			K key = temp.get(i).getKey();
			int currentPos = Math.abs(key.hashCode() % table.size());
			table.get(currentPos).add(temp.get(i));
		}

	}

	private void rehash() {
		List<MapEntry<K, V>> temp = entries();
		capacity = capacity * 2;
		table.clear();
		
		for (int i = 0; i < capacity; i++) {
			table.add(new LinkedList<MapEntry<K, V>>());
		}
		
		addAll(temp);
	}

}
