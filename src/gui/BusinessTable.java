package gui;

import gui.common.DateCellRenderer;
import gui.common.Gbc;

import java.awt.Component;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import romar.RomarBusiness;
import base.Util;

@SuppressWarnings("serial")
public class BusinessTable extends JXTable
{
	public BusinessTable( boolean canEdit )
	{
		this.canEdit = canEdit;

		setHighlighters( HighlighterFactory.createAlternateStriping() );
        setShowGrid( false );

        addMouseListener( new MouseAdapter()
        {
             public void mouseClicked( MouseEvent e )
             {
                 if( e.getClickCount() == 2 )
                 {
                	 RomarBusiness b = getSelectedItem();
	                 if( b != null ) {
	                	 onDoubleClick( b );
                     }
                 }
             }
        } );
	}

	public RomarBusiness getSelectedItem()
	{
        return getItemAtRow( getSelectedRow() );
	}

	public RomarBusiness getItemAtRow( int row )
	{
        if( row > -1 && model != null ) {
            return model.getItemAtRow( convertRowIndexToModel( row ) );
        }
        
        return null;
	}

	public void display( Collection<RomarBusiness> business )
	{
		this.model = new MainTableModel( business );

        setModel( model );

        TableColumnExt dateCols[] = { getColumnExt( 0 ), getColumnExt( 10 ) };
        for( TableColumnExt col : dateCols )
        {
        	col.setCellRenderer( new DateCellRenderer() );
        	col.setComparator( new Comparator<Date>() {
				public int compare( Date a, Date b ) {
					return a.compareTo( b );
				} } );
        }

        getColumnExt( 1 ).setPreferredWidth( 140 );
        getColumnExt( 2 ).setPreferredWidth( 140 );
        getColumnExt( 3 ).setPreferredWidth( 70 );
        getColumnExt( 5 ).setPreferredWidth( 50 );
        getColumnExt( 6 ).setPreferredWidth( 60 );
        getColumnExt( 7 ).setPreferredWidth( 60 );
        getColumnExt( 8 ).setPreferredWidth( 60 );
        getColumnExt( 9 ).setPreferredWidth( 80 );
        getRowSorter().toggleSortOrder( 0 );
        getColumnModel().getColumn( 12 ).setCellRenderer( new ImageRenderer() );
        doLayout();
	}

	protected void onDoubleClick( RomarBusiness business )
	{
		if( canEdit && business != null ) {
			new EditBusinessDialog( business );
		}
	}

	static ImageIcon lockIcon = new ImageIcon( "lock.png" );
	static ImageIcon cancelIcon = new ImageIcon( "cancel.png" );

	class ImageRenderer extends DefaultTableCellRenderer
	{
		private JLabel l1, l2;

		{
			l1 = new JLabel();
			l2 = new JLabel();
			l1.setIcon( lockIcon );
			l2.setIcon( cancelIcon );
		}

		public Component getTableCellRendererComponent( JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column )
		{
			RomarBusiness biz = (RomarBusiness)value;

			JPanel panel = new JPanel();
			panel.setLayout( new GridBagLayout() );
			if( biz.locked ) {
				panel.add( l1, new Gbc( Gbc.NONE, 0, 0 ).i( 0 ).XY( 0, 0 ) );
			}
			if( biz.npwd ) {
				panel.add( l2, new Gbc( Gbc.NONE, 1, 0 ).i( 0 ).XY( 0, 0 ) );
			}
			return panel;
		}
	}

	private boolean canEdit = true;
	private MainTableModel model;

	class MainTableModel extends AbstractTableModel
	{
		MainTableModel( Collection<RomarBusiness> allbusiness )
		{
	        rowToItem = new RomarBusiness[allbusiness.size()];

	        int i=0;
	        for( RomarBusiness b : allbusiness )
	        {
				itemToRow.put( b.id, i );
	            rowToItem[i++] = b;
	        }
		}

	    public int getColumnCount()
	    {
	        return columnNames.length;
	    }

	    public int getRowCount()
	    {
	        return rowToItem.length;
	    }

		public String getColumnName( int col )
		{
			return columnNames[col];
		}

	    public Object getValueAt( int row, int col )
	    {
	    	RomarBusiness b = getItemAtRow( row );
			if( b != null )
			{
				if( col == 0 ) {
					return b.date;
				} else if( col == 1 ) {
					return String.format( "#%d %s %s", b.clientNum, b.surname, b.initial );
				} else if( col == 2 ) {
					return b.company;
				} else if( col == 3 ) {
					return b.type;
				} else if( col == 4 ) {
					return Util.toMoneyStr( b.amount );
				} else if( col == 5 ) {
					return b.term;
				} else if( col == 6 ) {
					return b.source;
				} else if( col == 7 ) {
					return Util.toMoneyStr( b.tbc );
				} else if( col == 8 ) {
					return Util.toMoneyStr( b.ri1 );
				} else if( col == 9 ) {
					return b.ri2;
				} else if( col == 10 ) {
					return b.posted;
				} else if( col == 11 ) {
					return Util.toMoneyStr( b.paid );
				} else if( col == 12 ) {
					return b;
				}
			}

			return "";
	    }

	    private RomarBusiness getItemAtRow( int row )
	    {
	        return rowToItem[row];
	    }

		private RomarBusiness[] rowToItem;

		private Map<Long, Integer> itemToRow = new HashMap<Long, Integer>();

	    private String[] columnNames = new String[] { 
	    	"Date",
	    	"Client",
	    	"Company",
	    	"Type",
	    	"Prem/Loan",
	    	"Term",
	    	"Source",
	    	"TBC",
	    	"RI",
	    	"RI2",
	    	"Posted",
	    	"Paid",
	    	"" };
	}
}
