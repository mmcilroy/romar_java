package gui;

import gui.common.Gbc;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jdesktop.swingx.JXTitledSeparator;

import romar.Romar;
import romar.RomarYear;


@SuppressWarnings("serial")
public class SummaryPanel extends JPanel
{
	public SummaryPanel()
	{
		printButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onPrint();
			} } );

		setLayout( new BorderLayout() );

		JPanel topPanel = new JPanel();
		topPanel.setLayout( new GridBagLayout() );
		topPanel.add( printButton, new Gbc( Gbc.NONE, 1, 0 ).XY( 0, 0 ).i( 5, 0, 0, 0 ) );

		mainPanel.setLayout( new GridBagLayout() );

		sep1 = new JXTitledSeparator( "<html><h3 color='blue'>Business " + Romar.user.id + " " + Romar.selectedYear.toString() );
		sep2 = new JXTitledSeparator( "<html><h3 color='blue'>Work" );
		mainPanel.add( sep1, new Gbc( Gbc.HORIZONTAL, 0, 0 ).i( 0, 10, 0, 10 ) );
		mainPanel.add( businessPanel, new Gbc( Gbc.BOTH, 0, 1 ).i( 0, 10, 0, 10 ) );
		mainPanel.add( sep2, new Gbc( Gbc.HORIZONTAL, 0, 2 ).i( 5, 10, 0, 10 ) );
		mainPanel.add( workPanel, new Gbc( Gbc.BOTH, 0, 3 ).i( 0, 10, 0, 10 ) );

		add( topPanel, BorderLayout.NORTH );
		add( mainPanel, BorderLayout.CENTER );
	}

	public void display( RomarYear year )
	{
		sep1.setTitle( "<html><h3 color='blue'>Business (" + Romar.user.id + " " + Romar.selectedYear.toString() + ")" );
		businessPanel.display( Romar.selectedYear );
		workPanel.display( Romar.selectedYear );
	}

	private void onPrint()
	{
		PrinterJob pj = PrinterJob.getPrinterJob();
		pj.setJobName( "Print Component" );

		PageFormat format = pj.defaultPage();
		format.setOrientation( PageFormat.LANDSCAPE );

		pj.setPrintable ( new Printable()
		{
			public int print( Graphics pg, PageFormat pf, int pageNum )
			{
				if( pageNum > 0 ) {
					return Printable.NO_SUCH_PAGE;
				}

				Graphics2D g2 = (Graphics2D)pg;
				g2.translate( pf.getImageableX(), pf.getImageableY() );
				g2.scale( 0.6, 0.6 );
				mainPanel.paint( g2 );
				return Printable.PAGE_EXISTS;
			} } );

		if ( pj.printDialog() == false ) {
			return;
		}

		try
		{
			pj.print();
		}
		catch( PrinterException ex )
		{
	        ;
		}
	}

	private BusinessSummaryPanel businessPanel = new BusinessSummaryPanel();
	private WorkSummaryPanel workPanel = new WorkSummaryPanel();

	private JPanel mainPanel = new JPanel();
	private JButton printButton = new JButton( "Print" );

	private JXTitledSeparator sep1;
	private JXTitledSeparator sep2;
}
