package romar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import base.ObservableData;
import base.Util;

public class RomarMonth implements Serializable
{
	private static final long serialVersionUID = 5245462896181554113L;

	public RomarMonth( RomarYear year )
	{
		this.year = year;
	}

	public Collection<RomarBusiness> getBusiness()
	{
		return allBusiness.values();
	}

	public void add( RomarBusiness biz ) throws Exception
	{
		if( biz == null ) {
			throw new Exception( "NULL item" );
		}

		if( biz.id == null ) {
			throw new Exception( "NULL item.id" );
		}

		if( allBusiness.containsKey( biz.id ) ) {
			throw new Exception( "BusinessItem already exists: " + biz.id );
		}

		allBusiness.put( biz.id, biz );

		calc();
		year.calc();
	}

	public void edit( RomarBusiness biz ) throws Exception
	{
		if( biz == null ) {
			throw new Exception( "NULL item" );
		}

		if( biz.id == null ) {
			throw new Exception( "NULL item.id" );
		}

		if( !allBusiness.containsKey( biz.id ) ) {
			throw new Exception( "BusinessItem not found: " + biz.id );
		}

		allBusiness.put( biz.id, biz );

		calc();
		year.calc();
	}

	public void delete( RomarBusiness biz ) throws Exception
	{
		if( biz == null ) {
			throw new Exception( "NULL item" );
		}

		if( biz.id == null ) {
			throw new Exception( "NULL item.id" );
		}

		if( !allBusiness.containsKey( biz.id ) ) {
			throw new Exception( "BusinessItem not found: " + biz.id );
		}

		allBusiness.remove( biz.id );

		calc();
		year.calc();
	}

	public void calc()
	{
		System.out.println( "RomarMonth.calc in" );

		BigDecimal newTBC = new BigDecimal( 0 );
		BigDecimal newTotalNPW = new BigDecimal( 0 );
		BigDecimal newRI = new BigDecimal( 0 );

		for( RomarBusiness biz : allBusiness.values() )
		{
			biz.ri1 = biz.tbc.multiply( new BigDecimal( Romar.user.riPct / 100.0f ) );

			newTBC = newTBC.add( biz.tbc );
			newRI = newRI.add( biz.ri1 );

			if( biz.npwd ) {
				newTotalNPW = newTotalNPW.add( biz.tbc );
			}
		}

		totalTBC.set( newTBC );
		totalRI.set( newRI );

		System.out.printf( "RomarMonth.calc out - Total TBC %s, Total RI %s\n", Util.toMoneyStr( newTBC ), Util.toMoneyStr( newRI ) );
	}

	public void clearObservers()
	{
		totalTBC.clear();
		totalRI.clear();
	}

	public RomarYear year;

	public Integer letters = 0;
	public Integer hours = 0;
	public Integer appt = 0;
	public Integer meeting = 0;
	public Integer sale = 0;

	public Map<Long, RomarBusiness> allBusiness;

	public ObservableData<BigDecimal> totalTBC;
	public ObservableData<BigDecimal> totalRI;

	{
		allBusiness = new HashMap<Long, RomarBusiness>();

		totalTBC = new ObservableData<BigDecimal>();
		totalRI = new ObservableData<BigDecimal>();

		totalTBC.set( new BigDecimal( 0 ) );
		totalRI.set( new BigDecimal( 0 ) );
	}
}
