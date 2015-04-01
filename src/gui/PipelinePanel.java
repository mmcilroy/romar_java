package gui;

import gui.common.ErrorDialog;
import gui.common.Gbc;

import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.text.MessageFormat;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable.PrintMode;

import romar.Romar;
import romar.RomarPipeline;
import romar.RomarYear;

@SuppressWarnings("serial")
public class PipelinePanel extends JPanel
{
	public PipelinePanel()
	{
		initControls();
		initLayout();
	}

	public void display( RomarYear year )
	{
		this.year = year;
		pipelineTable.display( year, year.pipeline.values() );
	}

	private void initControls()
	{
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
	}

	private void initLayout()
	{
		setLayout( new BorderLayout() );

		JPanel buttonPanel = new JPanel();
		buttonPanel.add( addButton );
		buttonPanel.add( editButton );
		buttonPanel.add( deleteButton );
		buttonPanel.add( printButton );

		JPanel topPanel = new JPanel();
		topPanel.setLayout( new GridBagLayout() );
		topPanel.add( buttonPanel, new Gbc( Gbc.NONE, 1, 0 ).XY( 0, 0 ) );

		add( topPanel, BorderLayout.NORTH );
		add( new JScrollPane( pipelineTable ), BorderLayout.CENTER );
	}

	private void onAdd()
	{
		EditPipelineDialog dialog = new EditPipelineDialog();
		if( dialog.isOk() ) {
			pipelineTable.display( year, year.pipeline.values() );
		}
	}

	private void onEdit()
	{
		RomarPipeline pipeline = pipelineTable.getSelectedItem();
		if( pipeline != null )
		{
			EditPipelineDialog dialog = new EditPipelineDialog( pipeline );
			if( dialog.isOk() ) {
				pipelineTable.display( year, year.pipeline.values() );
			}
		}
	}

	private void onDelete()
	{
		RomarPipeline pipeline = pipelineTable.getSelectedItem();
		if( pipeline != null )
		{
			try {
				Romar.delete( pipeline );
			} catch( Exception e ) {
				ErrorDialog.show( this, "Error", e );
			}
			pipelineTable.display( year, year.pipeline.values() );
		}
	}

	private void onPrint()
	{
		try {
			pipelineTable.print( PrintMode.FIT_WIDTH, new MessageFormat( Romar.user.id + " - Pipeline" ), null );
		} catch( PrinterException e ) {
			ErrorDialog.show( this, "Error", e );
		}
	}

	private RomarYear year;
	
	private JButton addButton = new JButton( "Add" );
	private JButton editButton = new JButton( "Edit" );
	private JButton deleteButton = new JButton( "Delete" );
	private JButton printButton = new JButton( "Print" );

	private PipelineTable pipelineTable = new PipelineTable();
}