import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that implements the channel used by headquarters and space explorers to
 * communicate.
 */
public class CommunicationChannel {

	/**
	 * Creates a {@code CommunicationChannel} object.
	 */
	
	ArrayList<Message>[] headQuarterChannel;
	ArrayList<Message> headQuarterOutputChannel;
	ArrayList<Message> spaceExplorerChannel;
	HashMap<String, Integer> hashMap;
	public CommunicationChannel() 
	{
		spaceExplorerChannel = new ArrayList<>();
		headQuarterChannel = new ArrayList[8];
		for(int i = 0; i < 8; i++) 
			headQuarterChannel[i] = new ArrayList<Message>(); 
		headQuarterOutputChannel = new ArrayList<>();
		hashMap = new HashMap<>();
		for(int i = 0; i < 8; i++)
			hashMap.put("Thread-" + i, i);
	}

	/**
	 * Puts a message on the space explorer channel (i.e., where space explorers write to and 
	 * headquarters read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageSpaceExplorerChannel(Message message) {
		synchronized(spaceExplorerChannel)
		{
			spaceExplorerChannel.add(message);
		}
	}

	/**
	 * Gets a message from the space explorer channel (i.e., where space explorers write to and
	 * headquarters read from).
	 * 
	 * @return message from the space explorer channel
	 */
	public Message getMessageSpaceExplorerChannel() 
	{
		Message message = null;
		synchronized(spaceExplorerChannel){
			if(!spaceExplorerChannel.isEmpty())
			{
				message = spaceExplorerChannel.get(0);
				spaceExplorerChannel.remove(0);
			}
		} 
		return message; 
	}
	/**
	 * Puts a message on the headquarters channel (i.e., where headquarters write to and 
	 * space explorers read from).
	 * 
	 * @param message
	 *            message to be put on the channel
	 */
	public void putMessageHeadQuarterChannel(Message message) 
	{
		if(message.getData().equals(HeadQuarter.END))
		{
			headQuarterOutputChannel.addAll(headQuarterChannel[hashMap.get(HeadQuarter.currentThread().getName())]);
			headQuarterChannel[hashMap.get(HeadQuarter.currentThread().getName())].clear();		
		}
		else
		{
			headQuarterChannel[hashMap.get(HeadQuarter.currentThread().getName())].add(message);
		}
	}

	/**
	 * Gets a message from the headquarters channel (i.e., where headquarters write to and
	 * space explorer read from).
	 * 
	 * @return message from the header quarter channel
	 */
	public Message getMessageHeadQuarterChannel() {
		
		synchronized(headQuarterOutputChannel)
		{
			Message message = null;
			if(!headQuarterOutputChannel.isEmpty())
			{
				message = headQuarterOutputChannel.get(0);
				headQuarterOutputChannel.remove(0);
			}
			return message;
		}
	}
}
