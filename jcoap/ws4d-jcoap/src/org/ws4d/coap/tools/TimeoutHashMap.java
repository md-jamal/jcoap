package org.ws4d.coap.tools;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

//extends here means extending the functionality of the HashMap Class

//class HashMap<K,V>,K(key)-->type of keys maintained by this map  V(value)---->the type of mapped values

//Object Class : It is the root of the class hierarchy.. Every Class has Object as a super class . All objects , including arrays,implement the methods of this class. The object class is used when the specific class of a variable is not known.Object class is a parent to many classes(Integer,String...etc)
public class TimeoutHashMap<K, V> extends HashMap<Object, Object>{

	private static final long serialVersionUID = 4987370276778256858L;

	/* chronological list to remove expired elements when update() is called */ 
	LinkedList<TimoutType<K>> timeoutQueue = new LinkedList<TimoutType<K>>();

	//LinkedList<E> is a Doubly Linked list implementation of the List. Here we are having linkedlist of the keys
	
	/* Default Timeout is one minute */
	long timeout = 60000;		//timeout will be filled whenever an object of the class is created
	
	public TimeoutHashMap(long timeout){
		this.timeout = timeout;
	}
	
	//Put Method: Associates the specified value with the specified key in the map
	@Override
	public Object put(Object key, Object value) {
		
		//TimoutType is an class which mainly stores the value or key and the time at which it is expiring
		long expires = System.currentTimeMillis() + timeout;
		TimoutType<V> timeoutValue = new TimoutType<V>((V) value, expires);
		TimoutType<K> timeoutKey = new TimoutType<K>((K) key, expires);
		timeoutQueue.add(timeoutKey);		//Adding the key to the timeoutQueue LinkedList
		timeoutValue = (TimoutType<V>) super.put((K) key, timeoutValue);//Putting the Key and timeoutvalue into the Main HashMap Class
		if (timeoutValue != null){
			return timeoutValue.object;
		}
		return null;
	}




	//get Method : Returns the value to which the specified key is mapped in this identity hash map,or null if the map contains no mapping for this key
	@Override
	public Object get(Object key) {
		//getting the value from the Main Hash Map Class by specifying the key
		TimoutType<V> timeoutValue = (TimoutType<V>) super.get(key);	
		if (timeoutValueIsValid(timeoutValue)){	//check the validness of the time 
			return timeoutValue.object;
		} 
		return null;
	}	
	
	//Remove method :removes the mapping for this key from this map if present

	@Override
	public Object remove(Object key) {
		TimoutType<V> timeoutValue = (TimoutType<V>) super.remove(key);//remove it from the Main HashMap class
		if (timeoutValueIsValid(timeoutValue)){	//checking its validness
			return timeoutValue.object;
		} 		
		return null;
	}
	
	@Override
	public void clear() {		//Removes all mappings from this map
		super.clear();		//clearing the Main HashMap
		timeoutQueue.clear();	//clearing the LinkedList
	}
	
	/* remove expired elements */
	public void update(){
        while(true) {
		//the peek method returns the head of the list,or null if the list is empty
        	TimoutType<K> timeoutKey = timeoutQueue.peek();
        	if (timeoutKey == null){
        		/* if the timeoutKey queue is empty, there must be no more elements in the hashmap 
        		 * otherwise there is a bug in the implementation */
        		if (!super.isEmpty()){
        			throw new IllegalStateException("Error in TimeoutHashMap. Timeout queue is empty but hashmap not!");
        		}
        		return;
        	}
        	
        	long now = System.currentTimeMillis();
        	if (now > timeoutKey.expires){  //suppose if the key is expired
        		timeoutQueue.poll();	//the poll method is used to remove the top element from the list
        		TimoutType<V> timeoutValue = (TimoutType<V>) super.remove(timeoutKey.object);//also returning from the HashMap Class		
        		if (timeoutValueIsValid(timeoutValue)){//but the value present is more or someone has updated
        			/* This is a very special case which happens if an entry is overridden:
        			 * - put V with K 
        			 * - put V2 with K
        			 * - K is expired but V2 not 
        			 * because this is expected to be happened very seldom, we "reput" V2 to the hashmap 
        			 * wich is better than every time to making a get and than a remove */
        			super.put(timeoutKey.object, timeoutValue);
        		}
        	} else {
        		/* Key is not expired -> break the loop */
        		break;
        	}
        }
	}
	
	@Override
	public Object clone() {
		// TODO implement function
		throw new IllegalStateException(); 
//		return super.clone();
	}

	@Override
	public boolean containsKey(Object arg0) {
		// TODO implement function
		throw new IllegalStateException(); 
//		return super.containsKey(arg0);
	}

	@Override
	public boolean containsValue(Object arg0) {
		// TODO implement function
		throw new IllegalStateException(); 
//		return super.containsValue(arg0);
	}

	@Override
	public Set<Entry<Object, Object>> entrySet() {
		// TODO implement function
		throw new IllegalStateException(); 
//		return super.entrySet();
	}

	@Override
	public boolean isEmpty() {
		// TODO implement function
		throw new IllegalStateException(); 
//		return super.isEmpty();
	}

	@Override
	public Set<Object> keySet() {
		// TODO implement function
		throw new IllegalStateException(); 
//		return super.keySet();
	}

	@Override
	public void putAll(Map<? extends Object, ? extends Object> arg0) {
		// TODO implement function
		throw new IllegalStateException(); 
//		super.putAll(arg0);
	}

	@Override
	public int size() {
		// TODO implement function
		throw new IllegalStateException(); 
//		return super.size();
	}

	@Override
	public Collection<Object> values() {
		// TODO implement function
		throw new IllegalStateException(); 
//		return super.values();
	}

	
	/* private classes and methods */
	
	//checks whether the timeoutvalue is not null and current time should be less than the expired time

	private boolean timeoutValueIsValid(TimoutType<V> timeoutValue){
		return timeoutValue != null && System.currentTimeMillis() < timeoutValue.expires;		
	}

	private class TimoutType<T>{
		public T object;
		public long expires;
		
		public TimoutType(T object, long expires) {
			super();
			this.object = object;
			this.expires = expires;
		}
	}
}
