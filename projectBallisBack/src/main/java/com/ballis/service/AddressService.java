package com.ballis.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ballis.model.Address;
import com.ballis.model.Member;
import com.ballis.repository.AddressRepository;

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
}
