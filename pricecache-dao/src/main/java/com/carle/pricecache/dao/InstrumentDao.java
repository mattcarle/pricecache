package com.carle.pricecache.dao;

import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

import com.carle.pricecache.model.IdType;
import com.carle.pricecache.model.Instrument;

@Transactional
public interface InstrumentDao {

	Instrument findByAnyId(Map<IdType, String> instrumentIds);

	Instrument create(Map<IdType, String> instrumentIds);

}
