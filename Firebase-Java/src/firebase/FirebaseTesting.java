package firebase;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

class User{
	private String key;
	private String name;
	private String gender;
	private Long score;
	
	public User(){}
	
	public User(String name, String gender, Long score){
		this.name = name;
		this.gender = gender;
		this.score = score;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public double getScore() {
		return score;
	}
	public void setScore(Long score) {
		this.score = score;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	@Override
	public String toString() {
		return "User [key=" + key + ", name=" + name + ", gender=" + gender + ", score=" + score + "]";
	}
}
class UserFirebase{
	
	public UserFirebase() throws FileNotFoundException{
		
		System.out.println("Firebase Starting...");
		
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setServiceAccount(new FileInputStream("ServiceAccount/firebase-java-64d18e0a6ef6.json"))
				.setDatabaseUrl("https://fir-java.firebaseio.com/").build();
		FirebaseApp.initializeApp(options);
	}
	
	public List<User> loadUsers(){
		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference ref = database.getReference("user");
		
		ref.addValueEventListener(new ValueEventListener() {
			@SuppressWarnings("unchecked")
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				
				System.out.println(dataSnapshot.getValue());
				
				Map<String, Object> mapUsers = (Map<String, Object>) dataSnapshot.getValue();
				System.out.println("Object Set : " + mapUsers.values());
				System.out.println("Key Set : " + mapUsers.keySet());
				System.out.println("List to Array: " + mapUsers.keySet().toArray());				
				System.out.println("List Object To Array: " + mapUsers.values().toArray());
				
				ArrayList<User> users = new ArrayList<>();
				
				for(Entry<String, Object> map: mapUsers.entrySet()){
					HashMap<String, Object> mapUser = (HashMap<String, Object>) map.getValue();
					User user = new User();
					user.setName((String) mapUser.get("name"));
					user.setGender((String) mapUser.get("gender"));
					user.setScore((Long) mapUser.get("score"));
					users.add(user);
				}
				System.out.println(users);
				
				//TODO: LOOP THROUGH MAP COLLECTION
				/*Iterator<Entry<String, User>> entries = mapUsers.entrySet().iterator();
				while (entries.hasNext()) {
				  Entry entry = (Entry) entries.next();
				  Object key = entry.getKey();
				  Object value = entry.getValue();
				  System.out.println(key +" : "+ value);
				}*/
				
				//TODO: GET SINGLE USER OBJECT
				System.out.println(dataSnapshot.child("-KU6de53huhPyZ4uZihQ").getValue(User.class));
				
				ArrayList<User> usersV1 = new ArrayList<>();	
				//TODO: GET USER OBJECT BY KEY
				for(String key: mapUsers.keySet()){
					User user = dataSnapshot.child(key).getValue(User.class);
					user.setKey(key);
					
					usersV1.add(user);
				}
				System.out.println(usersV1);
			}
			@Override
			public void onCancelled(DatabaseError databaseError) {
				System.err.println(databaseError);
			}
		});
		return null;
	}
}
public class FirebaseTesting {
	
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		
		UserFirebase userFirebase = new UserFirebase();
		userFirebase.loadUsers();
		Thread.sleep(10000);
	}
	
	/*public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		
		System.out.println("=>> Starting Firebase App...");
		
		FirebaseOptions options = new FirebaseOptions.Builder()
				.setServiceAccount(new FileInputStream("ServiceAccount/firebase-java-64d18e0a6ef6.json"))
				.setDatabaseUrl("https://fir-java.firebaseio.com/").build();
		FirebaseApp.initializeApp(options);
		
		final FirebaseDatabase database = FirebaseDatabase.getInstance();
		DatabaseReference ref = database.getReference("user");

		Map<String, Object> users = new HashMap<String, Object>();
		users.put("name", "Phearun");
		users.put("gender", "Male");
		users.put("score", 95.0);
		
		//ref.setValue(users);
		ref.push().setValue(users);
		
		Map<String, User> users = new HashMap<>();
		users.put("user", new User("TEST", "TEST", 100));
		ref.push().setValue(users);
		
		ref.push().setValue(new User("new1", "new1", 100));

		for(int i=0; i<10; i++){
			User user = new User();
			user.setName("name" + i);
			user.setGender("male" + i);
			user.setScore(90 + i);
			ref.push().setValue(user);
		}
		
		//GET ALL DATA FROM SPECIFIC PATH
		ref.addValueEventListener(new ValueEventListener() {
		    @Override
		    public void onDataChange(DataSnapshot dataSnapshot) {
		    	
		    	System.out.println("KEY: " + dataSnapshot.getKey());
		    	System.out.println("VALUE: " + dataSnapshot.getValue());
		    	System.out.println("CHILDREN: " + dataSnapshot.getChildren());
		    	System.out.println("REF: " + dataSnapshot.getRef());
		    			    	
		        User user = dataSnapshot.getValue(User.class);
		        System.out.println(user);
		    }

		    @Override
		    public void onCancelled(DatabaseError databaseError) {
		        System.out.println("The read failed: " + databaseError.getCode());
		    }
		    
		});

		ref.addChildEventListener(new ChildEventListener() {
			
			@Override
			public void onChildRemoved(DataSnapshot arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChildMoved(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChildChanged(DataSnapshot arg0, String arg1) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
				System.out.println("New Record Added: " + dataSnapshot.getValue());
				System.out.println("Prev Child Key: " + prevChildKey);
			}
			
			@Override
			public void onCancelled(DatabaseError arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		Thread.sleep(10000);
	}*/	
}
