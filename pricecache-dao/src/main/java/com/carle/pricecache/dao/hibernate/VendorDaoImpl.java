package com.carle.pricecache.dao.hibernate;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.carle.pricecache.dao.VendorDao;
import com.carle.pricecache.model.Vendor;

@Repository
public class VendorDaoImpl extends AbstractHibernateDaoImpl implements VendorDao {

	@Autowired
	public VendorDaoImpl(final SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public Vendor findByName(final String vendorName) {
		Criteria criteria = currentSession().createCriteria(Vendor.class);
		Vendor exampleVendor = new Vendor();
		exampleVendor.setName(vendorName);
		criteria.add(Example.create(exampleVendor));
		return (Vendor) criteria.uniqueResult();
	}

	@Override
	public Vendor create(final String vendorName) {
		Vendor vendor = new Vendor();
		vendor.setName(vendorName);
		currentSession().save(vendor);
		return vendor;
	}
}
