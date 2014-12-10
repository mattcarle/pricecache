package com.carle.pricecache.dao.hibernate;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.carle.pricecache.dao.InstrumentDao;
import com.carle.pricecache.model.IdType;
import com.carle.pricecache.model.Instrument;

@Repository
public class InstrumentDaoImpl extends AbstractHibernateDaoImpl implements InstrumentDao {
	private static final Logger logger = LoggerFactory.getLogger(InstrumentDaoImpl.class);

	private static final String FIND_BY_ANY_ID = "from Instrument inst where %s";

	@Autowired
	public InstrumentDaoImpl(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public Instrument findByAnyId(final Map<IdType, String> instrumentIds) {
		Instrument instrument = null;
		if (instrumentIds != null && !instrumentIds.isEmpty()) {
			StringBuilder whereClause = new StringBuilder();
			Map<String, String> params = new HashMap<>();
			for (Map.Entry<IdType, String> idMapEntry : instrumentIds.entrySet()) {
				if (whereClause.length() != 0) {
					whereClause.append(" or ");
				}
				String columnName = idMapEntry.getKey()
						.name()
						.toLowerCase();
				whereClause.append(columnName)
						.append(" = :")
						.append(columnName);

				params.put(columnName, idMapEntry.getValue());
			}

			String queryString = String.format(FIND_BY_ANY_ID, whereClause);

			if (logger.isDebugEnabled()) {
				logger.debug("Formatted query for findByAnyId(): " + queryString);
			}

			instrument = (Instrument) currentSession().createQuery(queryString)
					.setProperties(params)
					.uniqueResult();
		}
		return instrument;
	}

	@Override
	public Instrument create(final Map<IdType, String> instrumentIds) {
		Instrument instrument = new Instrument();
		instrument.populateInstrumentIdsFromMap(instrumentIds);
		currentSession().save(instrument);
		return instrument;
	}

}
