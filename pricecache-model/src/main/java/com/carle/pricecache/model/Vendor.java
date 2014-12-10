package com.carle.pricecache.model;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Sets;

@Entity
@XmlRootElement(name = "vendor")
public class Vendor implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer vendorId;
	private String name;
	private Set<Price> prices = Sets.newHashSet();

	public Vendor() {
	}

	public Vendor(final String name) {
		this.name = name;
	}

	public Vendor(final Integer vendorId, final String name) {
		this.vendorId = vendorId;
		this.name = name;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_VENDOR")
	@SequenceGenerator(name = "SEQ_VENDOR", sequenceName = "SEQ_VENDOR")
	@Column(name = "vendor_id")
	public Integer getVendorId() {
		return vendorId;
	}

	public void setVendorId(final Integer vendorId) {
		this.vendorId = vendorId;
	}

	@Column(name = "name", unique = true)
	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	// TODO: check the association works correctly
	/**
	 * return all prices for this vendor, both latest and historical, for all versions
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "vendor")
	public Set<Price> getPrices() {
		return prices;
	}

	// TODO: verify behaviour for existing prices
	// TODO: each price should have vendor id set, i.e. price.getVendor() = this.
	public void setPrices(final Set<Price> prices) {
		this.prices = prices;
	}

	@Override
	public String toString() {
		return String.format("Vendor: id=%d,  name=%s", vendorId, name);
	}

	/**
	 * hashCode() based on name field only - ensures that Vendor objects that have not been persisted (and therefore do
	 * not have a database id) can be safely used in collections
	 */
	@Override
	public int hashCode() {
		return getName() == null ? super.hashCode() : getName().hashCode();
	}

	/**
	 * equals() based on name field to match hashCode() method
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
		Vendor other = (Vendor) obj;
		if (getName() == null) {
			if (other.getName() != null) {
				return false;
			}
		} else if (!getName().equals(other.getName())) {
			return false;
		}

		return true;
	}
}
