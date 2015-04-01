package romar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

import base.Month;
import base.ObservableData;
import base.Util;

@SuppressWarnings("serial")
public class RomarYear implements Serializable
{
	public RomarYear( int year )
	{
		this.year = year;
	}

	public RomarMonth getMonth( Month m )
	{
		return months[m.ordinal()];
	}

	public void calc()
	{
		System.out.println( "RomarYear.calc in" );

		BigDecimal newSubTotal1 = new BigDecimal( 0 );

		for( int i=0; i<6; i++ ) {
			newSubTotal1 = newSubTotal1.add( months[i].totalTBC.get() );
		}
		subTotal1.set( newSubTotal1 );

		BigDecimal newSubTotal2 = new BigDecimal( 0 );
		for( int i=6; i<12; i++ ) {
			newSubTotal2 = newSubTotal2.add( months[i].totalTBC.get() );
		}
		subTotal2.set( newSubTotal2 );

		totalTBC.set( subTotal1.get().add( subTotal2.get() ) );

		Integer newTotalAppt = 0;
		for( int i=0; i<12; i++ ) {
			newTotalAppt += months[i].appt;
		}
		totalAppt.set( newTotalAppt );

		Integer newTotalMeeting = 0;
		for( int i=0; i<12; i++ ) {
			newTotalMeeting += months[i].meeting;
		}
		totalMeeting.set( newTotalMeeting );

		Integer newTotalSale = 0;
		for( int i=0; i<12; i++ ) {
			newTotalSale += months[i].sale;
		}
		totalSale.set( newTotalSale );

		if( totalAppt.get() == 0 || totalMeeting.get() == 0 ) {
			apptPerMeet.set( new BigDecimal( 0 ) );
		} else {
			apptPerMeet.set( new BigDecimal( (float)totalAppt.get() / (float)totalMeeting.get() ) );
		}

		if( totalMeeting.get() == 0 || totalSale.get() == 0 ) {
			meetPerSale.set( new BigDecimal( 0 ) );
		} else {
			meetPerSale.set( new BigDecimal( (float)totalMeeting.get() / (float)totalSale.get() ) );
		}

		if( totalAppt.get() == 0 ) {
			tbcPerAppt.set( new BigDecimal( 0 ) );
		} else {
			tbcPerAppt.set( Romar.getTBCMinusNPW( this ).divide( new BigDecimal( totalAppt.get() ), 2, RoundingMode.HALF_UP ) );
		}

		if( totalMeeting.get() == 0 ) {
			tbcPerMeet.set( new BigDecimal( 0 ) );
		} else {
			tbcPerMeet.set( Romar.getTBCMinusNPW( this ).divide( new BigDecimal( totalMeeting.get() ), 2, RoundingMode.HALF_UP ) );
		}

		System.out.printf( "RomarYear.calc out - Total TBC %s\n", Util.toMoneyStr( totalTBC.get() ) );
	}

	public void clearObservers()
	{
		totalAppt.clear();
		totalMeeting.clear();
		totalSale.clear();
		
		subTotal1.clear();
		subTotal2.clear();
		totalTBC.clear();

		apptPerMeet.clear();
		meetPerSale.clear();
		tbcPerAppt.clear();
		tbcPerMeet.clear();

		for( RomarMonth m : months ) {
			m.clearObservers();
		}
	}

	public int year;

	public RomarMonth months[];
	public Map<Long, RomarPipeline> pipeline;

	public ObservableData<Integer> totalAppt;
	public ObservableData<Integer> totalMeeting;
	public ObservableData<Integer> totalSale;

	public ObservableData<BigDecimal> subTotal1;
	public ObservableData<BigDecimal> subTotal2;
	public ObservableData<BigDecimal> totalTBC;

	public ObservableData<BigDecimal> apptPerMeet;	
	public ObservableData<BigDecimal> meetPerSale;	
	public ObservableData<BigDecimal> tbcPerAppt;
	public ObservableData<BigDecimal> tbcPerMeet;

	{
		months = new RomarMonth[Month.values().length];
		for( Month i : Month.values() ) {
			months[ i.ordinal() ] = new RomarMonth( this );
		}

		pipeline = new HashMap<Long, RomarPipeline>();

		totalAppt = new ObservableData<Integer>();
		totalMeeting = new ObservableData<Integer>();
		totalSale = new ObservableData<Integer>();

		subTotal1 = new ObservableData<BigDecimal>();
		subTotal2 = new ObservableData<BigDecimal>();
		totalTBC = new ObservableData<BigDecimal>();

		apptPerMeet = new ObservableData<BigDecimal>();
		meetPerSale = new ObservableData<BigDecimal>();
		tbcPerAppt = new ObservableData<BigDecimal>();
		tbcPerMeet = new ObservableData<BigDecimal>();

		totalAppt.set( 0 );
		totalMeeting.set( 0 );
		totalSale.set( 0 );

		subTotal1.set( new BigDecimal( 0 ) );
		subTotal2.set( new BigDecimal( 0 ) );
		totalTBC.set( new BigDecimal( 0 ) );

		apptPerMeet.set( new BigDecimal( 0 ) );
		meetPerSale.set( new BigDecimal( 0 ) );
		tbcPerAppt.set( new BigDecimal( 0 ) );
		tbcPerMeet.set( new BigDecimal( 0 ) );
	}
}
