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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bulletinId == null) ? 0 : bulletinId.hashCode());
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Bulletin other = (Bulletin) obj;
		if (bulletinId == null) {
			if (other.bulletinId != null)
				return false;
		} else if (!bulletinId.equals(other.bulletinId))
			return false;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		return true;
	}

}