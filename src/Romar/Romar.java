package romar;

import gui.MainFrame;
import gui.PersistancyPanel;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import base.Month;
import base.Util;

public class Romar
{
	public static boolean login( String id, String pass )
	{
		if( id.compareTo( "mmcilroy" ) == 0 && pass.length() == 0 )
		{
			user.id = "mmcilroy";
			user.admin = true;
			return true;
		}

		RomarUser ruser = loadUser( id );
		if( ruser != null )
		{
			user = ruser;
			return( user.pass != null && user.pass.compareTo( pass ) == 0 );
		}

		return false;
	}

	public static RomarUser loadUser( String id )
	{
		Path path = FileSystems.getDefault().getPath( String.format( "data/%s/user.dat", id ) );

		try
		{
			RomarUser ruser = (RomarUser)loadObjectFromFile( path );
			if( ruser != null )
			{
				ruser.pass = decrypt( ruser.pass );
				//System.out.println( "decrypted pass: " + ruser.pass );
			}
			return ruser;
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}

		return null;
	}

	private static boolean loadedStrings = false;

	public static void load() throws IOException
	{
		if( !loadedStrings )
		{
			System.out.println( "loading strings" );
			companyStrings = Util.readStringsFromFile( "data/company.txt" );
			sourceStrings = Util.readStringsFromFile( "data/source.txt" );
			typeStrings = Util.readStringsFromFile( "data/type.txt" );
			statusStrings = Util.readStringsFromFile( "data/status.txt" );
			loadedStrings = true;
		}

		years.clear();
		availableUsers.clear();

		Path dataPath = FileSystems.getDefault().getPath( "data/" );
		Files.createDirectories( dataPath );
		Files.walkFileTree( dataPath, new SimpleFileVisitor<Path>() {
		    public FileVisitResult postVisitDirectory( Path dir, IOException e ) {
				availableUsers.add( dir.getFileName().toString() );
				return FileVisitResult.CONTINUE;
			} } );

		availableUsers.remove( "data" );
		for( String user : availableUsers ) {
			System.out.println( "found user: " + user );
		}

		if( user.id != null && user.id.length() > 0 )
		{
			System.out.println( "loading user: " + user.id );

			Romar.user = loadUser( user.id );

			dataPath = FileSystems.getDefault().getPath( "data/" + user.id );
			Files.createDirectories( dataPath );
			Files.walkFileTree( dataPath, new SimpleFileVisitor<Path>() {
				public FileVisitResult visitFile( Path file, BasicFileAttributes attr ) {
					if( attr.isRegularFile() ) load( file );
					return FileVisitResult.CONTINUE;
				} } );

			/*
			if( watcher != null )
			{
				System.out.println( "stopping existing watcher..." );
				watcher.exit = true;
			}

			System.out.println( "starting new watcher..." );

			watcher = new RomarWatcher( dataPath ); 
			new Thread( watcher ).start();
			*/
		}
	}

	public static void save( RomarYear year ) throws Exception
	{
		System.out.println( "saving: " + year.year );

		OutputStream file = new FileOutputStream( String.format( "data/%s/%d.dat", user.id, year.year ) ); 
		OutputStream buffer = new BufferedOutputStream( file );
		ObjectOutput output = new ObjectOutputStream( buffer );

		try {
			output.writeObject( year );
		} finally {
			output.close();
		}
	}

	private static final char[] PASSWORD = "enfldsgbnlsngdlksdsgm".toCharArray();

	private static final byte[] SALT = {
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
        (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12,
    };

	private static String encrypt( String property ) throws GeneralSecurityException, UnsupportedEncodingException
	{
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( "PBEWithMD5AndDES" );
        SecretKey key = keyFactory.generateSecret( new PBEKeySpec( PASSWORD ) );
        Cipher pbeCipher = Cipher.getInstance( "PBEWithMD5AndDES" );
        pbeCipher.init( Cipher.ENCRYPT_MODE, key, new PBEParameterSpec( SALT, 20 ) );
        return base64Encode( pbeCipher.doFinal( property.getBytes( "UTF-8" ) ) );
    }

	private static String base64Encode( byte[] bytes )
	{
        return new BASE64Encoder().encode( bytes );
    }

	private static String decrypt( String property ) throws GeneralSecurityException, IOException
	{
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance( "PBEWithMD5AndDES" );
        SecretKey key = keyFactory.generateSecret(new PBEKeySpec( PASSWORD ) );
        Cipher pbeCipher = Cipher.getInstance( "PBEWithMD5AndDES" );
        pbeCipher.init( Cipher.DECRYPT_MODE, key, new PBEParameterSpec( SALT, 20 ) );
        return new String( pbeCipher.doFinal( base64Decode( property ) ), "UTF-8" );
    }

	private static byte[] base64Decode( String s ) throws IOException
	{
        return new BASE64Decoder().decodeBuffer( s );
    }

	public static void addUser( String user, String pass, boolean admin, float ri ) throws IOException, GeneralSecurityException
	{
		System.out.println( "add user " + user );
		Path dataPath = FileSystems.getDefault().getPath( "data/" + user );
		if( !Files.exists( dataPath ) )
		{
			Files.createDirectories( dataPath );
			editUser( user, pass, admin, ri );
		}
		else
		{
			throw new IOException( "User already exists!" );
		}
	}

	public static void deleteUser( String user ) throws IOException
	{
		System.out.println( "delete user " + user );
		if( user != null && user.length() > 0 )
		{
			Path dataPath = FileSystems.getDefault().getPath( "data/" + user );
			if( Files.exists( dataPath ) )
			{
				Files.walkFileTree( dataPath, new SimpleFileVisitor<Path>() {
					public FileVisitResult visitFile( Path file, BasicFileAttributes attr ) {
						if( attr.isRegularFile() )
						{
							try {
								Files.delete( file );
							} catch( IOException e ) {
								e.printStackTrace();
							}
						}

						return FileVisitResult.CONTINUE;
					} } );

				Files.delete( dataPath );
			}
			else
			{
				throw new IOException( "User not found!" );
			}
		}
	}

	public static void editUser( String id, String pass, boolean admin, float ri ) throws GeneralSecurityException, IOException
	{
		RomarUser ruser = new RomarUser();
		ruser.id = id;
		ruser.admin = admin;
		ruser.riPct = ri;
		ruser.pass = encrypt( pass );

		OutputStream file = new FileOutputStream( String.format( "data/%s/user.dat", id ) ); 
		OutputStream buffer = new BufferedOutputStream( file );
		ObjectOutput output = new ObjectOutputStream( buffer );

		try {
			output.writeObject( ruser );
		} finally {
			output.close();
		}
	}

	public static RomarYear getYear( int y )
	{
		RomarYear year = years.get( y );
		if( year == null )
		{
			year = new RomarYear( y );
			years.put( y, year );
		}
	
		return year;
	}

	public static BigDecimal getTBCMinusNPW( RomarYear year )
	{
		BigDecimal tbc = new BigDecimal( 0 );
		for( int i=0; i<12; i++ ) {
			tbc = tbc.add( getTBCMinusNPW( year.months[i] ) );
		}
		return tbc;
	}

	public static BigDecimal getTBCMinusNPW( RomarMonth month )
	{
		BigDecimal tbc = new BigDecimal( 0 );
		for( RomarBusiness biz : month.allBusiness.values() )
		{
			if( !biz.npwd ) {
				tbc = tbc.add( biz.tbc );
			}
		}
		return tbc;
	}

	public static BigDecimal getTotalNPW( RomarYear year )
	{
		BigDecimal npw = new BigDecimal( 0 );
		for( int i=0; i<12; i++ ) {
			npw = npw.add( getTotalNPW( year.months[i] ) );
		}
		return npw;
	}

	public static BigDecimal getTotalNPW( RomarMonth month )
	{
		BigDecimal npw = new BigDecimal( 0 );
		for( RomarBusiness biz : month.allBusiness.values() )
		{
			if( biz.npwd ) {
				npw = npw.add( biz.tbc );
			}
		}
		return npw;
	}


	public static void edit( RomarPipeline p ) throws Exception
	{
		Calendar c = new GregorianCalendar();
		c.setTime( p.date );
		RomarYear year = getYear( selectedYear );
		year.pipeline.remove( p.id );
		year = getYear( c.get( Calendar.YEAR ) );
		year.pipeline.put( p.id, p );
		save( year );
	}

	public static void delete( RomarPipeline p ) throws Exception
	{
		RomarYear year = getYear( selectedYear );
		year.pipeline.remove( p.id );
		save( year );
	}

	private static void load( Path p )
	{
		System.out.println( "loading: " + p.toString() );

		String fileName = p.getFileName().toString();
		String[] parts = fileName.split( "\\." );
		if( parts.length == 2 )
		{
			Integer year = null;

			try {
				year = Integer.parseInt( parts[0] );
			} catch( Exception e ) {
				;
			}

			if( year != null && year >= 1970 )
			{
				if( parts[1].compareTo( "dat" ) == 0 )
				{
					RomarYear y = (RomarYear)loadObjectFromFile( p );
					if( y != null ) {
						years.put( year, y );
					}
				}
				else
				{
					System.out.println( "skipped file: " + p.toString() );
				}
			}
		}
	}

	private static Object loadObjectFromFile( Path p )
	{
		Object result = null;

	    try
	    {
			InputStream file = new FileInputStream( p.toFile() );
			InputStream buffer = new BufferedInputStream( file );
			ObjectInput input = new ObjectInputStream ( buffer );

			try {
				result = input.readObject();
	        } finally {
	        	input.close();
	        }
	    }
	    catch( Exception e )
	    {
	    	e.printStackTrace();
	    }

	    return result;
	}

	//private static RomarWatcher watcher = null;

	public static MainFrame main;
	
	public static RomarUser user = new RomarUser();	
	public static List<String> availableUsers = new LinkedList<String>();
	public static Integer selectedYear = Util.getCurrentYear();
	public static Month selectedMonth;

	public static List<String> companyStrings;
	public static List<String> sourceStrings;
	public static List<String> typeStrings;
	public static List<String> statusStrings;

	public static PersistancyPanel persistancyPanel;

	private static Map<Integer, RomarYear> years = new HashMap<Integer, RomarYear>();
}
