package com.carle.pricecache.dao;

import org.springframework.transaction.annotation.Transactional;

import com.carle.pricecache.model.Vendor;

@Transactional
public interface VendorDao {

	Vendor findByName(String vendorName);

	Vendor create(String vendorName);

}
