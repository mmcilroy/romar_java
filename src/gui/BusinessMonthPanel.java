package gui;

import gui.common.Gbc;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import romar.Romar;
import romar.RomarBusiness;
import romar.RomarMonth;
import base.DataObserver;

@SuppressWarnings("serial")
public class BusinessMonthPanel extends JPanel
{
	public BusinessMonthPanel( MonthPanel parent )
	{
		this.parent = parent;

		initControls();
		initLayout();
	}

	public void display( RomarMonth month )
	{
		this.month = month;

		month.totalTBC.add( new DataObserver<BigDecimal>() {
			public void onData( BigDecimal data ) {
				totalLabel.setText( getTotalStr() );
			} } );

		month.totalRI.add( new DataObserver<BigDecimal>() {
			public void onData( BigDecimal data ) {
				totalLabel.setText( getTotalStr() );
			} } );

		monthTable.display( month.getBusiness() );
		month.calc();
	}

	private String getTotalStr()
	{
		float tbc = 0.0f;
		float ri = 0.0f;

		if( month.totalTBC.get() != null ) {
			tbc = month.totalTBC.get().floatValue();
		}

		if( month.totalTBC.get() != null ) {
			ri = month.totalRI.get().floatValue();
		}

		return String.format( "<html><h3>" + Romar.user.id + " " + Romar.selectedYear + " - Total TBC \u00A3%.02f, Total RI \u00A3 %.02f", tbc, ri );
	}

	private void initControls()
	{
		totalLabel.setHorizontalAlignment( JLabel.CENTER );

		addButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onAdd();
			} } );

		editButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onEdit();
			} } );

		deleteButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onDelete();
			} } );

		printButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onPrint();
			} } );

		lockButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onLock();
			} } );

		npwButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				onNPW();
			} } );
	}

	private void initLayout()
	{
		setLayout( new BorderLayout() );

		JPanel buttonPanel = new JPanel();
		buttonPanel.add( addButton );
		buttonPanel.add( editButton );
		buttonPanel.add( deleteButton );
		buttonPanel.add( printButton );

		if( Romar.user.admin )
		{
			buttonPanel.add( lockButton );
			buttonPanel.add( npwButton );
		}

		JPanel topPanel = new JPanel();
		topPanel.setLayout( new GridBagLayout() );
		topPanel.add( totalLabel, new Gbc( Gbc.HORIZONTAL, 0, 0 ) );
		topPanel.add( buttonPanel, new Gbc( Gbc.NONE, 1, 0 ).XY( 0, 0 ) );

		add( topPanel, BorderLayout.NORTH );
		add( new JScrollPane( monthTable ), BorderLayout.CENTER );
	}

	private void onAdd()
	{
		EditBusinessDialog dialog = new EditBusinessDialog( month );
		if( dialog.isOk() )
		{
			Romar.persistancyPanel.display( month.year );
			monthTable.display( month.getBusiness() );
		}
	}

	private void onEdit()
	{
		RomarBusiness biz = monthTable.getSelectedItem();
		if( biz != null )
		{
			EditBusinessDialog dialog = new EditBusinessDialog( biz );
			if( dialog.isOk() )
			{
				Romar.persistancyPanel.display( month.year );
				monthTable.display( month.getBusiness() );
			}
		}
	}

	private void onDelete()
	{
		RomarBusiness biz = monthTable.getSelectedItem();
		if( biz != null && !biz.locked && !biz.npwd )
		{
			try
			{
				month.delete( biz );
				Romar.save( month.year );
				Romar.persistancyPanel.display( month.year );
				monthTable.display( month.getBusiness() );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}

	private void onPrint()
	{
		parent.print();
	}

	private void onLock()
	{
		RomarBusiness biz = monthTable.getSelectedItem();
		if( biz != null && !biz.npwd )
		{
			try
			{
				biz.locked = !biz.locked;

				if( biz.locked ) {
					biz.posted = new Date();
				} else {
					biz.posted = null;
				}

				Romar.save( month.year );
				monthTable.display( month.getBusiness() );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}

	private void onNPW()
	{
		RomarBusiness biz = monthTable.getSelectedItem();
		if( biz != null && !biz.locked )
		{
			try
			{
				biz.npwd = !biz.npwd;
				month.calc();
				month.year.calc();
				Romar.save( month.year );
				Romar.persistancyPanel.display( month.year );
				monthTable.display( month.getBusiness() );
			}
			catch( Exception e )
			{
				e.printStackTrace();
			}
		}
	}

	private MonthPanel parent;

	private RomarMonth month;

	private JLabel totalLabel = new JLabel();
	private JButton addButton = new JButton( "Add" );
	private JButton editButton = new JButton( "Edit" );
	private JButton deleteButton = new JButton( "Delete" );
	private JButton printButton = new JButton( "Print" );
	private JButton lockButton = new JButton( "Lock" );
	private JButton npwButton = new JButton( "Lapse/NPW" );

	private BusinessTable monthTable = new BusinessTable( true );
}