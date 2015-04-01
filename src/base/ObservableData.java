package base;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class ObservableData<T> implements Serializable
{
	public void add( DataObserver<T> o )
	{
		if( observers == null ) {
			observers = new LinkedList<DataObserver<T>>();
		}

		observers.add( o );
	}

	public void set( T newData )
	{
		this.data = newData;
		
		if( observers == null ) {
			observers = new LinkedList<DataObserver<T>>();
		}

		//System.out.println( "publishing to " + observers.size() + " observers" );

		for( DataObserver<T> o : observers ) {
			o.onData( data );
		}
	}

	public T get()
	{
		return data;
	}

	public void clear()
	{
		if( observers == null ) {
			observers = new LinkedList<DataObserver<T>>();
		}

		observers.clear();
	}

	public T data;

	private transient List<DataObserver<T>> observers;
}
