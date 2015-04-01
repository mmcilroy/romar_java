package romar;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;

public class RomarWatcher implements Runnable
{
	public RomarWatcher( Path dir )
	{
		try
		{
			this.watcher = FileSystems.getDefault().newWatchService();
			this.key = dir.register( watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY );
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void run()
	{
		while( !exit ) {
			processEvents();
		}
	}

	void processEvents()
	{
		WatchKey key;
		try {
			key = watcher.poll( 1, TimeUnit.SECONDS );
		} catch( InterruptedException x ) {
			return;
		}

		if( key != null && key.equals( this.key ) ) {
			System.out.println( "Change..." );
			Romar.main.loadUser();
        } else {
        	System.out.println( "???... " + this.toString() );
        }
    }

    private WatchService watcher = null;
    private WatchKey key = null;

    public boolean exit = false;
}
