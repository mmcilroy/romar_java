package gui;

import gui.common.Gbc;

import java.awt.GridBagLayout;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;

import romar.Romar;
import romar.RomarYear;
import base.DataObserver;
import base.Util;

@SuppressWarnings("serial")
public class WorkSummaryPanel extends JPanel
{
	public WorkSummaryPanel()
	{
		yearLabels = new JLabel[NUM_YEARS];
		for( int i=0; i<NUM_YEARS; i++ ) {
			yearLabels[i] = new JLabel();
		}

		dataLabels = new JLabel[cols.length-1][NUM_YEARS];
		for( int i=0; i<cols.length-1; i++ ) {
			for( int j=0; j<NUM_YEARS; j++ ) {
				dataLabels[i][j] = new JLabel();
			}
		}

		initLayout();
	}

	public void display( int year )
	{
		for( int i=0; i<NUM_YEARS; i++ ) {
			yearLabels[i].setText( "<html><b>" + new Integer( year-i ).toString() );
		}

		for( int i=0; i<NUM_YEARS; i++ )
		{
			final JLabel apptLabel = dataLabels[0][i];
			final JLabel tbcLabel = dataLabels[1][i];
			final JLabel meetingLabel = dataLabels[2][i];
			final JLabel saleLabel = dataLabels[3][i];
			final JLabel apptMeetLabel = dataLabels[4][i];
			final JLabel meetSaleLabel = dataLabels[5][i];
			final JLabel tbcApptLabel = dataLabels[6][i];
			final JLabel tbcMeetLabel = dataLabels[7][i];
			
			final RomarYear ryear = Romar.getYear( year-i );
			ryear.calc();

			ryear.totalAppt.add( new DataObserver<Integer>() {
				public void onData( Integer data ) {
					apptLabel.setText( data.toString() );
				} } );

			ryear.totalMeeting.add( new DataObserver<Integer>() {
				public void onData( Integer data ) {
					meetingLabel.setText( data.toString() );
				} } );

			ryear.totalSale.add( new DataObserver<Integer>() {
				public void onData( Integer data ) {
					saleLabel.setText( data.toString() );
				} } );

			ryear.apptPerMeet.add( new DataObserver<BigDecimal>() {
				public void onData( BigDecimal data ) {
					apptMeetLabel.setText( Util.toMoneyStr( data ) );
				} } );

			ryear.meetPerSale.add( new DataObserver<BigDecimal>() {
				public void onData( BigDecimal data ) {
					meetSaleLabel.setText( Util.toMoneyStr( data ) );
				} } );

			ryear.tbcPerAppt.add( new DataObserver<BigDecimal>() {
				public void onData( BigDecimal data ) {
					tbcApptLabel.setText( Util.toMoneyStr( data ) );
				} } );

			ryear.tbcPerMeet.add( new DataObserver<BigDecimal>() {
				public void onData( BigDecimal data ) {
					tbcMeetLabel.setText( Util.toMoneyStr( data ) );
				} } );
			
			ryear.totalTBC.add( new DataObserver<BigDecimal>() {
				public void onData( BigDecimal data ) {
					tbcLabel.setText( Util.toMoneyStr( Romar.getTBCMinusNPW( ryear ) ) );
				} } );

			apptLabel.setText( ryear.totalAppt.get().toString() );
			meetingLabel.setText( ryear.totalMeeting.get().toString() );
			saleLabel.setText( ryear.totalSale.get().toString() );
			apptMeetLabel.setText( Util.toMoneyStr( ryear.apptPerMeet.get() ) );
			meetSaleLabel.setText( Util.toMoneyStr( ryear.meetPerSale.get() ) );
			tbcApptLabel.setText( Util.toMoneyStr( ryear.tbcPerAppt.get() ) );
			tbcMeetLabel.setText( Util.toMoneyStr( ryear.tbcPerMeet.get() ) );
			tbcLabel.setText( Util.toMoneyStr( Romar.getTBCMinusNPW( ryear ) ) );
		}
	}

	private void initLayout()
	{
		setLayout( new GridBagLayout() );

		// create the first row of the table
		for( int i=0; i<cols.length; i++ ) {
			add( new JLabel( "<html><b>" + cols[i] ), new Gbc( Gbc.NONE, i, 0 ) );
		}

		// add the years
		for( int i=0; i<NUM_YEARS; i++ ) {
			add( yearLabels[i], new Gbc( Gbc.NONE, 0, i+1 ) );
		}

		// add data labels
		int i=0,j=0;
		for( i=0; i<cols.length-1; i++ ) {
			for( j=0; j<NUM_YEARS; j++ ) {
				add( dataLabels[i][j], new Gbc( Gbc.NONE, i+1, j+1 ) );
			}
		}

		add( new JLabel( "" ), new Gbc( Gbc.NONE, 0, j+1 ).i( 10 ) );
	}

	private JLabel yearLabels[];
	private JLabel dataLabels[][];

	private String[] cols = { "", "Appt", "TBC", "Meeting", "Sale", "Appt/Meet", "Meet/Sale", "£/Appt", "£/Meet" };

	private final int NUM_YEARS = 6;
}
