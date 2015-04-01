package gui;

import gui.common.Gbc;

import java.awt.GridBagLayout;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;

import romar.Romar;
import romar.RomarMonth;
import romar.RomarYear;
import base.DataObserver;
import base.Month;
import base.Util;

@SuppressWarnings("serial")
public class BusinessSummaryPanel extends JPanel
{
	public BusinessSummaryPanel()
	{
		dataLabels = new JLabel[NUM_YEARS][NUM_MONTHS];
		for( int i=0; i<NUM_YEARS; i++ ) {
			for( int j=0; j<NUM_MONTHS; j++ ) {
				dataLabels[i][j] = new JLabel();
			}
		}

		yearLabels = new JLabel[NUM_YEARS];
		for( int i=0; i<NUM_YEARS; i++ ) {
			yearLabels[i] = new JLabel();
		}

		totalLabels = new JLabel[NUM_YEARS];
		for( int i=0; i<NUM_YEARS; i++ ) {
			totalLabels[i] = new JLabel();
		}

		initLayout();
	}

	public void display( final int year )
	{
		for( int i=0; i<NUM_YEARS; i++ ) {
			yearLabels[i].setText( "<html><b>" + new Integer( year-i ).toString() );
		}

		for( int i=0; i<NUM_YEARS; i++ )
		{
			final JLabel totalLabel = totalLabels[i];

			RomarYear ryear = Romar.getYear( year-i );
			ryear.calc();

			ryear.totalTBC.add( new DataObserver<BigDecimal>() {
				public void onData( BigDecimal data ) {
					System.out.println( "totalTBC changed to " + Util.toMoneyStr( data ) );
					totalLabel.setText( Util.toMoneyStr( data ) );
				} } );

			totalLabel.setText( Util.toMoneyStr( ryear.totalTBC.get() ) );

			for( int j=0; j<NUM_MONTHS; j++ )
			{
				final JLabel dataLabel = dataLabels[i][j];

				RomarMonth rmonth = ryear.getMonth( Month.values()[j] );
				rmonth.totalTBC.add( new DataObserver<BigDecimal>() {
					public void onData( BigDecimal data ) {
						dataLabel.setText( Util.toMoneyStr( data ) );
					} } );

				dataLabel.setText( Util.toMoneyStr( rmonth.totalTBC.get() ) );
			}
		}
	}

	private void initLayout()
	{
		setLayout( new GridBagLayout() );

		// create the first row of the table
		add( new JLabel( "<html><b>Month" ), new Gbc( Gbc.NONE, 0, 0 ) );
		for( int i=0; i<NUM_YEARS; i++ ) {
			add( yearLabels[i], new Gbc( Gbc.NONE, i+1, 0 ) );
		}

		// create the month columns
		Month[] months = Month.values();
		for( int i=0; i<months.length; i++ ) {
			add( new JLabel( "<html><b>" + months[i].toString() ), new Gbc( Gbc.NONE, 0, i+1 ) );
		}

		// create the totals
		add( new JLabel( "<html><b>Total" ), new Gbc( Gbc.NONE, 0, NUM_MONTHS+1 ) );
		for( int i=0; i<NUM_YEARS; i++ ) {
			add( totalLabels[i], new Gbc( Gbc.NONE, i+1, NUM_MONTHS+1 ) );
		}

		// now add the data labels
		for( int i=0; i<NUM_YEARS; i++ ) {
			for( int j=0; j<NUM_MONTHS; j++ ) {
				add( dataLabels[i][j], new Gbc( Gbc.NONE, i+1, j+1 ) );
			}
		}
	}

	private JLabel yearLabels[];
	private JLabel dataLabels[][];
	private JLabel totalLabels[];

	private final int NUM_YEARS = 6;
	private final int NUM_MONTHS = 12;
}
