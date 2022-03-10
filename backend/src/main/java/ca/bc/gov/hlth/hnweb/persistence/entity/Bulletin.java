package ca.bc.gov.hlth.hnweb.persistence.entity;

import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Bulletin.
 */
@Entity
@Table(name = "bulletin")
public class Bulletin {

	/**
	 * primary key
	 */
	@Id
	@Column(name = "bulletin_id", columnDefinition = "bigserial")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bulletinId;

	/**
	 * The start date of the bulletin.
	 */
	@Basic
	@Column(name = "start_date", columnDefinition = "date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date startDate;

	/**
	 * The end date of the bulletin.
	 */
	@Basic
	@Column(name = "end_date", columnDefinition = "date", nullable = false)
	@Temporal(TemporalType.DATE)
	private Date endDate;

	/**
	 * The content of the bulletin.
	 */
	@Basic
	@Column(name = "content", nullable = false)
	private String content;

	public Long getBulletinId() {
		return bulletinId;
	}

	public void setBulletinId(Long bulletinId) {
		this.bulletinId = bulletinId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}