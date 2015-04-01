package gui;

import gui.common.ErrorDialog;
import gui.common.Gbc;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable.PrintMode;

import romar.Romar;
import romar.RomarBusiness;
import romar.RomarMonth;
import romar.RomarYear;
import base.Month;

@SuppressWarnings("serial")
public class PersistancyPanel extends JPanel
{
	public PersistancyPanel()
	{
		initLayout();
	}

	public void display( RomarYear year )
	{
		this.year = year;

		Collection<RomarBusiness> npwd = new Vector<RomarBusiness>();

		for( Month m : Month.values() )
		{
			RomarMonth month = year.getMonth( m );
			for( RomarBusiness biz : month.getBusiness() )
			{
				if( biz.npwd )
				{
					npwd.add( biz );
				}
			}
		}

		totalLabel.setText( getTotalStr() );
		monthTable.display( npwd );
	}

	private void initLayout()
	{
		totalLabel.setHorizontalAlignment( JLabel.CENTER );

		printButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onPrint();
			} } );

		setLayout( new BorderLayout() );

		JPanel buttonPanel = new JPanel();
		buttonPanel.add( printButton );

		JPanel topPanel = new JPanel();
		topPanel.setLayout( new GridBagLayout() );
		topPanel.add( totalLabel, new Gbc( Gbc.HORIZONTAL, 0, 0 ) );
		topPanel.add( buttonPanel, new Gbc( Gbc.NONE, 1, 0 ).XY( 0, 0 ) );

		add( topPanel, BorderLayout.NORTH );
		add( new JScrollPane( monthTable ), BorderLayout.CENTER );
	}

	private void onPrint()
	{
		try {
			monthTable.print( PrintMode.FIT_WIDTH, new MessageFormat( Romar.user.id + " - Persistancy" ), null );
		} catch( PrinterException e ) {
			ErrorDialog.show( this, "Error", e );
		}
	}

	private String getTotalStr()
	{
		return String.format( "<html><h3>Total TBC \u00A3%.02f", Romar.getTotalNPW( year ).floatValue() );
	}

	private RomarYear year;

	private JLabel totalLabel = new JLabel();

	private JButton printButton = new JButton( "Print" );

	private BusinessTable monthTable = new BusinessTable( false );
}