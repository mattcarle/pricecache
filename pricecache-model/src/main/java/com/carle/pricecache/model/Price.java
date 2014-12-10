package com.carle.pricecache.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Type;
import org.joda.time.DateTime;

@Entity
@XmlRootElement(name = "price")
public class Price implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer priceId;
	private DateTime createTime;
	private BigDecimal price;
	private Vendor vendor;
	private Instrument instrument;
	private Boolean isLatest;

	public Price() {
		this.isLatest = true;
		this.createTime = new DateTime();
	}

	public Price(final Integer priceId, final BigDecimal price, final DateTime createTime, final Vendor vendor,
			final Instrument instrument) {
		this.priceId = priceId;
		this.createTime = createTime;
		this.price = price;
		this.vendor = vendor;
		this.instrument = instrument;
		this.isLatest = true;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRICE")
	@SequenceGenerator(name = "SEQ_PRICE", sequenceName = "SEQ_PRICE")
	@Column(name = "price_id")
	public Integer getPriceId() {
		return priceId;
	}

	public void setPriceId(final Integer priceId) {
		this.priceId = priceId;
	}

	@Column(name = "create_time")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	public DateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(final DateTime createTime) {
		this.createTime = createTime;
	}

	@Column(name = "price")
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(final BigDecimal price) {
		this.price = price;
	}

	@ManyToOne
	@JoinColumn(name = "vendor_id", nullable = false)
	@Cascade(CascadeType.MERGE)
	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(final Vendor vendor) {
		this.vendor = vendor;
	}

	@ManyToOne
	@JoinColumn(name = "instrument_id", nullable = false)
	@Cascade(CascadeType.MERGE)
	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(final Instrument instrument) {
		this.instrument = instrument;
	}

	@Override
	public String toString() {
		return String.format("Price: priceId=%d, createTime=%s, price=%f, instrument=[%s], vendor=[%s]", priceId,
				createTime, price, instrument, vendor);
	}

	@Column(name = "is_latest")
	@Type(type = "yes_no")
	public Boolean getIsLatest() {
		return isLatest;
	}

	public void setIsLatest(final boolean isLatest) {
		this.isLatest = isLatest;
	}

	/**
	 * hashCode() of Price is based on a natural key of price, instrument, vendor and createTime. Note that key does not
	 * include priceId - this is to ensure that Price objects can be used in hashed collections even if they are
	 * transient (i.e. have not yet been persisted and therefore they have not been assigned a database id)
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((price == null) ? 0 : price.hashCode());
		result = prime * result + ((instrument == null) ? 0 : instrument.hashCode());
		result = prime * result + ((vendor == null) ? 0 : vendor.hashCode());
		result = prime * result + ((createTime == null) ? 0 : createTime.hashCode());
		return result;
	}

	/**
	 * equals() implementation to match hashCode()
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Price other = (Price) obj;
		if (price == null) {
			if (other.price != null) {
				return false;
			}
		} else if (!price.equals(other.price)) {
			return false;
		}
		if (instrument == null) {
			if (other.instrument != null) {
				return false;
			}
		} else if (!instrument.equals(other.instrument)) {
			return false;
		}
		if (vendor == null) {
			if (other.vendor != null) {
				return false;
			}
		} else if (!vendor.equals(other.vendor)) {
			return false;
		}
		if (createTime == null) {
			if (other.createTime != null) {
				return false;
			}
		} else if (!createTime.equals(other.createTime)) {
			return false;
		}
		return true;
	}
}
