package romar;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@SuppressWarnings("serial")
public class RomarBusiness implements Serializable
{
	public RomarBusiness( RomarMonth month )
	{
		this.id = nextId++;
		this.month = month;
	}

	public RomarMonth month;
	public Long id;
	public Date date = new Date();
	public Integer clientNum = new Integer( 0 );
	public String surname = new String();
	public String initial = new String();
	public String company = new String();
	public String type = new String();
	public BigDecimal amount = new BigDecimal( 0 );
	public Integer term = new Integer( 0 );
	public String source = new String();
	public BigDecimal tbc = new BigDecimal( 0 );
	public String ri2 = new String();
	public Date posted = null;
	public BigDecimal paid = new BigDecimal( 0 );
	public Boolean locked = false;
	public Boolean npwd = false;

	public transient BigDecimal ri1 = new BigDecimal( 0 );

	public static Long nextId = System.currentTimeMillis();
}
