package com.fds.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fds.entities.FdsPosDescription;
import com.fds.repositories.DescriptionRepo;

@Service("descriptionService")
public class DescriptionServiceImpl implements DescriptionService {

	@Autowired
	private DescriptionRepo descriptionRepo;

	@Override
	public List<FdsPosDescription> findAllByTypeByOrderBySequencenoAsc(String type) {
		return descriptionRepo.findAllByTypeByOrderBySequencenoAsc(type);
	}
	
	@Override
	public Iterable<FdsPosDescription> findAllByType(String type) {
		return descriptionRepo.findAllByType(type);
	}

}
