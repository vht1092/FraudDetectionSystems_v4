package com.fds.services;

import java.util.List;

import com.fds.entities.FdsPosDescription;

public interface DescriptionService {
	List<FdsPosDescription> findAllByTypeByOrderBySequencenoAsc(String type);
	Iterable<FdsPosDescription> findAllByType(String type);
}
