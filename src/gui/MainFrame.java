package gui;

import gui.common.ErrorDialog;
import gui.common.Gbc;

import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import romar.Romar;
import romar.RomarYear;
import base.Month;
import base.Util;

@SuppressWarnings("serial")
public class MainFrame extends JFrame
{
	public static void main( String[] args )
	{
		try {
			UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
		} catch( Exception e ) {
			;
		}
	
		new MainFrame();
	}

	public MainFrame() 
	{
		super();

		Romar.main = this;

		LoginDialog dialog = new LoginDialog();
		if( dialog.isOk() )
		{
			try
			{
				initControls();
				initLayout();

				Romar.persistancyPanel = persistancyPanel;
				Romar.load();
				display();
			}
			catch( IOException e )
			{
				ErrorDialog.show( this, "Failed to start!", e );
			}
		}
	}

	private void display()
	{
		refreshing = true;

		DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>)usersCombo.getModel();
		model.removeAllElements();

		for( String user : Romar.availableUsers ) {
			usersCombo.addItem( user );
		}

		RomarYear year = Romar.getYear( Romar.selectedYear );
		year.clearObservers();

		usersCombo.setSelectedItem( Romar.user.id );
		yearField.setValue( Romar.selectedYear );

		summaryPanel.display( year );
		persistancyPanel.display( year );
		pipelinePanel.display( year );

		for( int i=0; i<monthOrder.length; i++ ) {
			monthPanels[i].display( year.getMonth( monthOrder[i] ) );
		}

		label.setText( "<html><h3>" + Romar.user.id + " " + Romar.selectedYear );

		refreshing = false;
	}

	public synchronized void loadUser( )
	{
		String user = (String)usersCombo.getSelectedItem();
		if( user != null ) {
			Romar.user.id = (String)usersCombo.getSelectedItem();
		}

		Romar.selectedYear = (Integer)yearField.getValue();

		try
		{
			Romar.load();
			display();
		}
		catch( IOException e )
		{
			e.printStackTrace();
		}
	}

	private void initControls()
	{
		int cy = Util.getCurrentYear();

		SpinnerModel model = new SpinnerNumberModel( cy, cy-100, cy+100, 1 );
		yearField = new JSpinner( model );
		yearField.setEditor( new JSpinner.NumberEditor( yearField, "#" ) );
		yearField.addChangeListener( new ChangeListener() {
			public void stateChanged( ChangeEvent e ) {
				loadUser();
			} } );

		loadButton = new JButton( "Load" );
		loadButton.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				loadUser();
			} } );

		usersCombo = new JComboBox<String>();
		usersCombo.addActionListener( new ActionListener() {
			public void actionPerformed( ActionEvent e ) {
				if( !refreshing ) loadUser();
			} } );

		for( int i=0; i<monthOrder.length; i++ ) {
			monthPanels[i] = new MonthPanel();
		}
	}

	private void initLayout()
	{
		label = new JLabel();
		label.setHorizontalAlignment( SwingConstants.CENTER );

		BufferedImage img = null;
		try {
			img = ImageIO.read( new File( "logo.jpg" ) );
		} catch( IOException e ) {
			;
		}

		JLabel picLabel;
		if( img != null ) {
			picLabel = new JLabel( new ImageIcon( img ) );
		} else {
			picLabel = new JLabel( "err" );
		}

		JPanel bannerPanel = new JPanel();
		bannerPanel.setLayout( new GridBagLayout() );
		bannerPanel.setBackground( new Color( 0,42,92 ) );
		bannerPanel.add( picLabel, new Gbc( Gbc.NONE, 0, 0 ).a( Gbc.EAST ) );

		int x=0;
		JPanel topPanel = new JPanel();
		topPanel.setLayout( new GridBagLayout() );
		topPanel.add( label, new Gbc( Gbc.HORIZONTAL, x++, 0 ) );
		if( Romar.user.admin ) {
			topPanel.add( usersCombo, new Gbc( Gbc.NONE, x++, 0 ).XY( 0, 0 ) );
		}
		topPanel.add( yearField, new Gbc( Gbc.NONE, x++, 0 ).XY( 0, 0 ) );
		topPanel.add( loadButton, new Gbc( Gbc.NONE, x++, 0 ).XY( 0, 0 ) );

		final JTabbedPane tabPanel = new JTabbedPane();
		tabPanel.add( "Summary", summaryPanel );

		for( int i=0; i<monthOrder.length; i++ ) {
			tabPanel.add( monthOrder[i].toString(), monthPanels[i] );
		}

		tabPanel.add( "Persistancy", persistancyPanel );
		tabPanel.add( "Pipeline", pipelinePanel );

		tabPanel.addChangeListener( new ChangeListener() {
			public void stateChanged( ChangeEvent e ) {
				try {
					Romar.selectedMonth = Month.valueOf( tabPanel.getTitleAt( tabPanel.getSelectedIndex() ) );
				} catch( Exception x ) {
					Romar.selectedMonth = null;
				} } } );

		Container cp = getContentPane();
		cp.setLayout( new GridBagLayout() );
		cp.add( bannerPanel, new Gbc( Gbc.HORIZONTAL, 0, 0 ).XY( 1, 0 ).a( Gbc.EAST ) );
		cp.add( topPanel, new Gbc( Gbc.HORIZONTAL, 0, 1 ).XY( 1, 0 ) );
		cp.add( tabPanel, new Gbc( Gbc.BOTH, 0, 2 ).i( 5, 10, 10, 10 ) );

		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setTitle( "ROMAR" );
		setSize( 900, 700 );
		setLocationRelativeTo( null );
		setVisible( true );
	}

	private SummaryPanel summaryPanel = new SummaryPanel();
	private MonthPanel[] monthPanels = new MonthPanel[Month.values().length];
	private PersistancyPanel persistancyPanel = new PersistancyPanel();
	private PipelinePanel pipelinePanel = new PipelinePanel();

	private JComboBox<String> usersCombo;
	private JSpinner yearField;
	private JButton loadButton;
	private JLabel label;

	private Month[] monthOrder =
	{
		Month.Jan, Month.Feb, Month.Mar, Month.Apr, Month.May, Month.Jun,
		Month.Jul, Month.Aug, Month.Sep, Month.Oct, Month.Nov, Month.Dec 
	};

	private boolean refreshing = false;
}
