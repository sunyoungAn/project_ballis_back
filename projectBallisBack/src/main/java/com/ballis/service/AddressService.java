package com.ballis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ballis.model.Address;
import com.ballis.model.Member;
import com.ballis.model.DTO.AddressDTO;
import com.ballis.repository.AddressRepository;
import com.ballis.repository.MemberRepository;

import jakarta.transaction.Transactional;


@Service
public class AddressService {
	
	@Autowired
	private AddressRepository addressRepository;
	
	public List<Address> getAddress(Long member) {
		return addressRepository.findByMemberMemberNumber(member);
	}
	
	public Address save(Address newAddress) {
		return addressRepository.save(newAddress);
	}

	public List<Address> findByMemberMemberNumber(Long memberNumber) {
		return addressRepository.findByMemberMemberNumber(memberNumber);
	}

	public int countByMemberNumber(Long memberNumber) {
		return addressRepository.countByMember_MemberNumber(memberNumber);
	}

	public void delete(Long id) {
		addressRepository.deleteById(id);
		
	}

	@Transactional
	public void update(Long id, Integer defaultAddress) {
		
		
		Address address1 = addressRepository.findByMemberMemberNumberAndDefaultAddress(id, 1);
	    Address address2 = addressRepository.findByMemberMemberNumberAndDefaultAddress(id, 2);

	    if (address1 != null && address2 != null) {
	        address1.setDefaultAddress(2);
	        address2.setDefaultAddress(1);
	        
	        addressRepository.save(address1);
	        addressRepository.save(address2);
	    }
	}

	public Address findById(Long id) {
		return addressRepository.findById(id).get();
	}

	public Address findByMemberMemberNumberAndDefaultAddress(Long memberNumber, int i) {
		return addressRepository.findByMemberMemberNumberAndDefaultAddress(memberNumber, i);
	}

}
