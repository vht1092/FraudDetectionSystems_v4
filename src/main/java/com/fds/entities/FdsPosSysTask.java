package com.fds.entities;

import java.io.Serializable;
import javax.persistence.*;
import java.math.BigDecimal;


/**
 * The persistent class for the FDS_SYS_TASK database table.
 * 
 */

/*IDTASK      NOT NULL NUMBER        
FROMDATE    NOT NULL NUMBER(17)    
TODATE      NOT NULL NUMBER(17)    
OBJECTTASK  NOT NULL VARCHAR2(50)  
TYPETASK    NOT NULL VARCHAR2(10)  
PRIORITY             NUMBER        
USERID      NOT NULL VARCHAR2(12)  
CONTENTTASK          VARCHAR2(700) */

@Entity
@Table(name="FDS_POS_SYS_TASK")
@NamedQuery(name="FdsPosSysTask.findAll", query="SELECT f FROM FdsPosSysTask f")
public class FdsPosSysTask implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name="FDS_POS_SYS_TASK_IDTASK_GENERATOR", sequenceName="SQ_FDS_POS_SYS_TASK")
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FDS_POS_SYS_TASK_IDTASK_GENERATOR")
	@Column(unique=true, nullable=false)
	private long idtask;

	@Column(nullable=false, length=255)
	private String contenttask;

	@Column(precision=17)
	private BigDecimal fromdate;

	@Column(length=20)
	private String objecttask;

	private BigDecimal priority;

	@Column(precision=17)
	private BigDecimal todate;

	@Column(nullable=false, length=10)
	private String typetask;

	@Column(nullable=false, length=12)
	private String userid;
	
	@Column(name = "CREATEDATE", precision=17)
	private BigDecimal createdate;
	
	@Column(name = "TYPE_OF_BUSINESS", nullable=true,length=22)
	private String typeOfBusiness;
	
	@Column(nullable=true,length=50)
	private String mid;
	
	@Column(nullable=true,length=50)
	private String tid;


	public FdsPosSysTask() {
	}

	public long getIdtask() {
		return this.idtask;
	}

	public void setIdtask(long idtask) {
		this.idtask = idtask;
	}

	public String getContenttask() {
		return this.contenttask;
	}

	public void setContenttask(String contenttask) {
		this.contenttask = contenttask;
	}

	public BigDecimal getFromdate() {
		return this.fromdate;
	}

	public void setFromdate(BigDecimal fromdate) {
		this.fromdate = fromdate;
	}

	public String getObjecttask() {
		return this.objecttask;
	}

	public void setObjecttask(String objecttask) {
		this.objecttask = objecttask;
	}

	public BigDecimal getPriority() {
		return this.priority;
	}

	public void setPriority(BigDecimal priority) {
		this.priority = priority;
	}

	public BigDecimal getTodate() {
		return this.todate;
	}

	public void setTodate(BigDecimal todate) {
		this.todate = todate;
	}

	public String getTypetask() {
		return this.typetask;
	}

	public void setTypetask(String typetask) {
		this.typetask = typetask;
	}

	public String getUserid() {
		return this.userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	/**
	 * @return the createdate
	 */
	public BigDecimal getCreatedate() {
		return createdate;
	}

	/**
	 * @param createdate the createdate to set
	 */
	public void setCreatedate(BigDecimal createdate) {
		this.createdate = createdate;
	}

	/**
	 * @return the typeOfBusiness
	 */
	public String getTypeOfBusiness() {
		return typeOfBusiness;
	}

	/**
	 * @param typeOfBusiness the typeOfBusiness to set
	 */
	public void setTypeOfBusiness(String typeOfBusiness) {
		this.typeOfBusiness = typeOfBusiness;
	}

	/**
	 * @return the mid
	 */
	public String getMid() {
		return mid;
	}

	/**
	 * @param mid the mid to set
	 */
	public void setMid(String mid) {
		this.mid = mid;
	}

	/**
	 * @return the tid
	 */
	public String getTid() {
		return tid;
	}

	/**
	 * @param tid the tid to set
	 */
	public void setTid(String tid) {
		this.tid = tid;
	}

}