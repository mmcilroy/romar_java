package base;

import java.awt.BorderLayout;
import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import romar.RomarBusiness;
import romar.RomarMonth;
import romar.RomarPipeline;

public class Util
{
	public static void showPanel( JPanel panel )
	{
		JFrame frame = new JFrame();
		frame.setTitle( "TEST" );
		frame.setLayout( new BorderLayout() );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.add( panel, BorderLayout.CENTER );
		frame.setSize( 800, 600 );
		frame.setLocationRelativeTo( null );
		frame.setVisible( true );
	}

	public static String isValid( RomarBusiness item )
	{
		return null;
	}

	public static String isValid( RomarMonth month )
	{
		return null;
	}

	public static String isValid( RomarPipeline pipeline )
	{
		return null;
	}

	public static String nns( String s ) {
		return s == null ? "" : s; // non null string
	}

	public static String nne( Enum<?> e ) {
		return e == null ? "" : e.toString(); // non null enum
	}

	public static Float nnf( Float f ) {
		return f == null ? 0.0f : f; // non null float
	}

	public static boolean isNullOrEmpty( String s ) {
		return s == null || ( s != null && s.length() == 0 );
	}

	public static boolean isValidDecimalString( String s )
	{
		try {
			Float.parseFloat( s );
		} catch( Exception e ) {
			return false;
		}

		return true;
	}

	public static int getCurrentYear()
	{
		Calendar c = new GregorianCalendar();
		c.setTime( new Date() );
		return c.get( Calendar.YEAR );
		
	}

	public static String toMoneyStr( BigDecimal m )
	{
		if( m == null ) {
			return "0.00";
		} else {
			return String.format( "%.02f", m.floatValue() );
		}
	}

	public static List<String> readStringsFromFile( String file )
	{
		try
		{
			BufferedReader br = new BufferedReader( new FileReader( file ) );
			List<String> l = new LinkedList<String>();
			String s;
			while( ( s = br.readLine() ) != null ) {
				l.add( s );
			}
			br.close();
			return l;
		}
		catch( Exception e )
		{
			return null;
		}
	}
}
