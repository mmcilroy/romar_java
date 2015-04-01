package gui;

import gui.common.Gbc;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JPanel;

import org.jdesktop.swingx.JXTitledPanel;

import romar.RomarMonth;

@SuppressWarnings("serial")
public class MonthPanel extends JPanel
{
	public MonthPanel()
	{
		setLayout( new GridBagLayout() );

		businessPanel = new BusinessMonthPanel( this );
		workPanel = new WorkMonthPanel();

		JXTitledPanel topPanel = new JXTitledPanel( "Business" );
		topPanel.getContentContainer().setLayout( new BorderLayout() );
		topPanel.getContentContainer().add( businessPanel, BorderLayout.CENTER );

		JXTitledPanel bottomPanel = new JXTitledPanel( "Work" );
		bottomPanel.getContentContainer().setLayout( new GridLayout( 1, 0 ) );
		bottomPanel.getContentContainer().add( workPanel );

		add( topPanel, new Gbc( Gbc.BOTH, 0, 0 ).i( 5 ) );
		add( bottomPanel, new Gbc( Gbc.HORIZONTAL, 0, 1 ).XY( 1, 0 ).i( 5 ) );
	}

	public void display( RomarMonth month )
	{
		businessPanel.display( month );
		workPanel.display( month );
	}

	public void print()
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
				paint( g2 );
				return Printable.PAGE_EXISTS;
			} } );

		if ( pj.printDialog() == false ) {
			return;
		}

		try {
			pj.print();
		} catch( PrinterException ex ) {
	        ;
		}
	}

	private BusinessMonthPanel businessPanel;
	private WorkMonthPanel workPanel;
}
