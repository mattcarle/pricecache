package com.carle.pricecache.model;

import static com.carle.pricecache.model.IdType.BBGID;
import static com.carle.pricecache.model.IdType.CUSIP;
import static com.carle.pricecache.model.IdType.ISIN;
import static com.carle.pricecache.model.IdType.RIC;
import static com.carle.pricecache.model.IdType.SEDOL;
import static com.carle.pricecache.model.IdType.TICKER;
import static org.apache.commons.lang3.StringUtils.isBlank;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import com.google.common.collect.Sets;

@Entity
@XmlRootElement(name = "instrument")
public class Instrument implements Serializable {
	private static final long serialVersionUID = 1L;

	private Integer instrumentId;
	private String isin;
	private String sedol;
	private String cusip;
	private String ticker;
	private String ric;
	private String bbgid;
	private Set<Price> prices = Sets.newHashSet();

	/**
	 * Default constructor required by Hibernate
	 */
	public Instrument() {
	}

	public Instrument(final Map<IdType, String> instrumentIds) {
		populateInstrumentIdsFromMap(instrumentIds);
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_INSTRUMENT")
	@SequenceGenerator(name = "SEQ_INSTRUMENT", sequenceName = "SEQ_INSTRUMENT")
	@Column(name = "instrument_id")
	public Integer getInstrumentId() {
		return instrumentId;
	}

	public void setInstrumentId(final Integer instrumentId) {
		this.instrumentId = instrumentId;
	}

	@Column
	public String getIsin() {
		return isin;
	}

	public void setIsin(final String isin) {
		this.isin = isin;
	}

	@Column
	public String getSedol() {
		return sedol;
	}

	public void setSedol(final String sedol) {
		this.sedol = sedol;
	}

	@Column
	public String getCusip() {
		return cusip;
	}

	public void setCusip(final String cusip) {
		this.cusip = cusip;
	}

	@Column
	public String getTicker() {
		return ticker;
	}

	public void setTicker(final String ticker) {
		this.ticker = ticker;
	}

	@Column
	public String getRic() {
		return ric;
	}

	public void setRic(final String ric) {
		this.ric = ric;
	}

	@Column
	public String getBbgid() {
		return bbgid;
	}

	public void setBbgid(final String bbgid) {
		this.bbgid = bbgid;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "instrument")
	public Set<Price> getPrices() {
		return prices;
	}

	public void setPrices(final Set<Price> prices) {
		this.prices = prices;
	}

	@Override
	public String toString() {
		return String.format("Instrument: id=%d, isin=%s, sedol=%s, cusip=%s, ticker=%s, ric=%s, bbgid=%s",
				instrumentId, isin, sedol, cusip, ticker, ric, bbgid);
	}

	/**
	 * All the identifiers on an instrument are nullable and two instruments are considered equal if they have the same
	 * database id, or if any one of their identifiers match (see equals method below). This logic, however, makes it
	 * impossbile to generate a hash code for instruments that do not have the database identifier set. This is because
	 * Instrument A with ids [RIC='IBM.N', TICKER='IBM'] and Instrument B with ids [TICKER='IBM', BBGID='IBM UN'] are
	 * equal (because we have a match on TICKER), but would generate different hash codes (using a conventional hash
	 * code algorithm). Therefore, for Instrument objects without a database id, we always return the same value (1) for
	 * the hash code. For this reason, avoid creating hash maps containing a large number of transient Instruments!
	 */
	@Override
	public int hashCode() {
		return instrumentId == null ? 1 : instrumentId.hashCode();
	}

	/**
	 * Two instrument objects are equal if they have the same database id, or if _any_ one of their identifiers match
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

		Instrument other = (Instrument) obj;

		return ((instrumentId != null && other.instrumentId != null && instrumentId.equals(other.instrumentId))
				|| idsMatch(bbgid, other.bbgid)
				|| idsMatch(cusip, other.cusip)
				|| idsMatch(isin, other.isin)
				|| idsMatch(ric, other.ric)
				|| idsMatch(sedol, other.sedol)
				|| idsMatch(ticker, other.ticker));
	}

	private boolean idsMatch(final String thisId, final String otherId) {
		return !isBlank(thisId) && !isBlank(otherId) && thisId.equals(otherId);
	}

	/**
	 * Extract a map of identifiers with IdType as the key.
	 */
	@Transient
	public Map<IdType, String> getInstrumentIdsAsMap() {
		Map<IdType, String> idMap = new HashMap<>();
		addIdToMap(idMap, BBGID, getBbgid());
		addIdToMap(idMap, CUSIP, getCusip());
		addIdToMap(idMap, ISIN, getIsin());
		addIdToMap(idMap, RIC, getRic());
		addIdToMap(idMap, SEDOL, getSedol());
		addIdToMap(idMap, TICKER, getTicker());

		return idMap;
	}

	private void addIdToMap(final Map<IdType, String> idMap, final IdType idType, final String idValue) {
		if (!isBlank(idValue)) {
			idMap.put(idType, idValue);
		}
	}

	/**
	 * Merge the identifiers from the passed-in instrument into this instrument. For each non-null identifier in the
	 * passed-in instrument, populate the same in this instrument unless we already have an identifier of that type in
	 * this instrument.
	 * 
	 * @param instrument
	 */
	public void mergeIdentifiers(final Instrument instrumentToMergeIn) {
		Map<IdType, String> thisIdMap = this.getInstrumentIdsAsMap();
		Map<IdType, String> mergeInIdMap = instrumentToMergeIn.getInstrumentIdsAsMap();

		for (Map.Entry<IdType, String> entry : mergeInIdMap.entrySet()) {
			// if we don't have an identifier of this type in this instrument...
			if (thisIdMap.get(entry.getKey()) == null) {
				// and we do actually have a non-null in the instrument to merge in...
				if (entry.getValue() != null) {
					// then add this identifier to this instrument
					thisIdMap.put(entry.getKey(), entry.getValue());
				}
			}
		}

		// now update the identifiers in this instrument to reflect the ones in the map
		this.populateInstrumentIdsFromMap(thisIdMap);
	}

	/**
	 * Populate the instrument identifier fields from the values in the map. Note that fields for values not in the map
	 * will be set to null
	 */
	public void populateInstrumentIdsFromMap(final Map<IdType, String> idMap) {
		for (IdType idType : IdType.values()) {
			String idValue = idMap.get(idType);
			populateIdFieldByType(idType, idValue);
		}
	}

	private void populateIdFieldByType(final IdType idType, final String idValue) {
		switch (idType) {
		case BBGID:
			setBbgid(idValue);
			break;
		case CUSIP:
			setCusip(idValue);
			break;
		case ISIN:
			setIsin(idValue);
			break;
		case RIC:
			setRic(idValue);
			break;
		case SEDOL:
			setSedol(idValue);
			break;
		case TICKER:
			setTicker(idValue);
			break;
		default:
			throw new IllegalArgumentException(String.format("ID Type %s is not supported", idType));
		}
	}
}
