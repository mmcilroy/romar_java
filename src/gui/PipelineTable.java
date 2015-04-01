package gui;

import gui.common.DateCellRenderer;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import romar.RomarPipeline;
import romar.RomarYear;
import base.Util;

@SuppressWarnings("serial")
public class PipelineTable extends JXTable
{
	public PipelineTable()
	{
		setHighlighters( HighlighterFactory.createAlternateStriping() );
        setShowGrid( false );

        addMouseListener( new MouseAdapter()
        {
             public void mouseClicked( MouseEvent e )
             {
                 if( e.getClickCount() == 2 )
                 {
                	 RomarPipeline p = getSelectedItem();
	                 if( p != null ) {
	                	 onDoubleClick( p );
                     }
                 }
             }
        } );
	}

	public RomarPipeline getSelectedItem()
	{
        return getItemAtRow( getSelectedRow() );
	}

	public RomarPipeline getItemAtRow( int row )
	{
        if( row > -1 && model != null ) {
            return model.getItemAtRow( convertRowIndexToModel( row ) );
        }

        return null;
	}

	public void display( RomarYear year, Collection<RomarPipeline> pipeline )
	{
		this.year = year;
		this.model = new MainTableModel( pipeline );

        setModel( model );

        TableColumnExt col = getColumnExt( 0 );
    	col.setCellRenderer( new DateCellRenderer() );
    	col.setComparator( new Comparator<Date>() {
			public int compare( Date a, Date b ) {
				return a.compareTo( b );
			} } );

        getRowSorter().toggleSortOrder( 0 );
        doLayout();
	}

	protected void onDoubleClick( RomarPipeline pipeline )
	{
		if( pipeline != null )
		{
			EditPipelineDialog dialog = new EditPipelineDialog( pipeline );
			if( dialog.isOk() ) {
				display( year, year.pipeline.values() );
			}
		}
	}

	private MainTableModel model;
	private RomarYear year;

	class MainTableModel extends AbstractTableModel
	{
		MainTableModel( Collection<RomarPipeline> pipeline )
		{
	        rowToItem = new RomarPipeline[pipeline.size()];

	        int i=0;
	        for( RomarPipeline p : pipeline )
	        {
				itemToRow.put( p.id, i );
	            rowToItem[i++] = p;
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
	    	RomarPipeline p = getItemAtRow( row );
			if( p != null )
			{
				if( col == 0 ) {
					return p.date;
				} else if( col == 1 ) {
					return String.format( "#%d %s %s", p.clientNum, p.surname, p.initial );
				} else if( col == 2 ) {
					return p.type;
				} else if( col == 3 ) {
					return Util.toMoneyStr( p.premium );
				} else if( col == 4 ) {
					return Util.toMoneyStr( p.tbc );
				} else if( col == 5 ) {
					return p.status;
				}
			}

			return "";
	    }

	    private RomarPipeline getItemAtRow( int row )
	    {
	        return rowToItem[row];
	    }

		private RomarPipeline[] rowToItem;

		private Map<Long, Integer> itemToRow = new HashMap<Long, Integer>();

	    private String[] columnNames = new String[] { 
	    	"Date",
	    	"Client",
	    	"Product",
	    	"Premium/Mortgage",
	    	"TBC",
	    	"Status" };
	}
}
