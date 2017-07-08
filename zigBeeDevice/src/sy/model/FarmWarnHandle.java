package sy.model;
/**
 * FarmWarnHandle entity. @author MyEclipse Persistence Tools
 */

import java.util.Date;

import javax.print.attribute.standard.DateTimeAtCompleted;

@Entity
@Table(name = "farmwarnhandle")
public class FarmWarnHandle implements java.io.Serializable {
        
	//fields
	private int id;
	private String houseieee;
	private Date handletime;
	private String note;
	
	// Constructors
	
	/** default constructor */
	public FarmWarnHandle() {
	}
	
	/** full constructor */
	public FarmWarnHandle(String houseieee, Date handletime, String note) {
		super();
		this.houseieee = houseieee;
		this.handletime = handletime;
		this.note = note;
	}

	// Property accessors
	@GenericGenerator(name = "generator", strategy = "identity")
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "houseieee", length = 50)
	public String getHouseieee() {
		return houseieee;
	}

	public void setHouseieee(String houseieee) {
		this.houseieee = houseieee;
	}

	@Column(name = "handletime", length = 19)
	public Date getHandletime() {
		return handletime;
	}

	public void setHandletime(Date handletime) {
		this.handletime = handletime;
	}

	@Column(name = "handletime", length = 500)
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
}
